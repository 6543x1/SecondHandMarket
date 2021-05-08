package com.jessie.SHMarket.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jessie.SHMarket.entity.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class RedisUtil
{
    //把里面GSON的部分用FASTJSON实现了
    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1;
    /**
     * 是否开启redis缓存  true开启   false关闭
     */
    @Value("${spring.redis.open: #{false}}")
    private boolean open;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ValueOperations<String, String> valueOperations;
    @Autowired
    private HashOperations<String, String, Object> hashOperations;
    @Autowired
    private ListOperations<String, Object> listOperations;
    @Autowired
    private SetOperations<String, Object> setOperations;

    /**
     * 默认过期时长，单位：秒
     */
//  public final static long DEFAULT_EXPIRE = 10;
    @Autowired
    private ZSetOperations<String, Object> zSetOperations;

    public void batchInsert(List<Map<String, String>> saveList, TimeUnit unit, int timeout)
    {
        /* 插入多条数据 */
        redisTemplate.executePipelined(new SessionCallback<Object>()
        {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException
            {
                for (Map<String, String> needSave : saveList)
                {
                    redisTemplate.opsForValue().set(needSave.get("key"), needSave.get("value"), timeout, unit);
                }
                return null;
            }
        });
    }

    public List<String> batchGet(List<String> keyList)
    {
        /* 批量获取多条数据 */
        List<Object> objects = redisTemplate.executePipelined(new RedisCallback<String>()
        {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException
            {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) redisConnection;
                for (String key : keyList)
                {
                    stringRedisConnection.get(key);
                }
                return null;
            }
        });

        List<String> collect = objects.stream().map(val -> String.valueOf(val)).collect(Collectors.toList());

        return collect;
    }

    public void saveUserShopCart(int uid, String data)
    {
        //艹可以设置两个KEY 一个KEY过期了取出另外一个KEY的VALUE持久化到数据库里
        //然后把这两个KEY都干掉就行...太暴力了吧
        String expiredKey = "Shop_Cart|" + uid;
        String dataKey = "Shop_Cart_Data|" + uid;
        //md一样的key会覆盖啊，我还以为会抛Exception...算了已经写好的就懒得动了...
        set(expiredKey, dataKey, 10 * 24 * 60 * 60);//保存10天?
        set(dataKey, data);
    }

    public String getUserShopCart(int uid)
    {
        String dataKey = "Shop_Cart_Data|" + uid;
        if (exists(dataKey))
        {
            return get(dataKey);
        } else
        {
            return null;
        }
    }

    public void saveUserHistoryKey(int uid, String searchKey)
    {
        String key = "Search_Key|" + uid;
        LinkedHashMap<String, String> hashMap;
        if (exists(key))
        {
            hashMap = get(key, LinkedHashMap.class);
        } else
        {
            hashMap = new LinkedHashMap<String, String>()
            {
                private static final long serialVersionUID = 1L;

                @Override
                protected boolean removeEldestEntry(Map.Entry<String, String> eldest)
                {
                    return size() > 5;
                }
            };
        }
        if (!hashMap.containsKey(searchKey))
            hashMap.put(searchKey, searchKey);
        set(key, hashMap);
    }

    public LinkedHashMap<String, String> getUserHistoryKey(int uid)
    {
        String key = "Search_Key|" + uid;
        if (exists(key))
        {
            return get(key, LinkedHashMap.class);
        } else
        {
            return null;
        }
    }

    //|Push_Message|User,value=|DATA(JSON)|
    public void saveUserMessage(int uid, UserMessage data)
    {
        String Key = "Push_Message|" + uid;
        //UserMessage userMessage=new UserMessage("1","1");
        ArrayList<UserMessage> list;
        if (exists(Key))
        {
            list = get(Key, ArrayList.class);
        } else
        {
            list = new ArrayList<>();
        }
        list.add(data);
        set(Key, list);
    }

    public String getUserMessage(int uid)
    {
        String Key = "Push_Message|" + uid;
        if (exists(Key))
        {
            ArrayList<UserMessage> list = get(Key, ArrayList.class);
            delete(Key);//不知道删除不存在的键会不会异常，就这样了...
            return JSONArray.toJSONString(list);
        } else
        {
            return "[]";
        }
    }

    public boolean exists(String key)
    {
        if (!open)
        {
            return false;
        }

        return redisTemplate.hasKey(key);
    }

    public void set(String key, Object value, long expire)
    {
        if (!open)
        {
            return;
        }
        try
        {
            valueOperations.set(key, toJson(value));

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        if (expire != NOT_EXPIRE)
        {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void set(String key, Object value)
    {
        if (!open)
        {
            return;
        }
        try
        {

            valueOperations.set(key, toJson(value));

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public <T> T get(String key, Class<T> clazz, long expire)
    {
        if (!open)
        {
            return null;
        }

        String value = valueOperations.get(key);
        if (expire != NOT_EXPIRE)
        {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz)
    {
        if (!open)
        {
            return null;
        }

        return get(key, clazz, NOT_EXPIRE);
    }

    public String get(String key, long expire)
    {//重新设置过期时间吗？
        if (!open)
        {
            return null;
        }

        String value = valueOperations.get(key);
        if (expire != NOT_EXPIRE)
        {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public String get(String key)
    {
        if (!open)
        {
            return null;
        }

        return get(key, NOT_EXPIRE);
    }

    public void delete(String key)
    {
        if (!open)
        {
            return;
        }

        if (exists(key))
        {
            redisTemplate.delete(key);
        }
    }

    public void delete(String... keys)
    {
        if (!open)
        {
            return;
        }

        for (String key : keys)
        {
            redisTemplate.delete(key);
        }
    }

    public void deletePattern(String pattern)
    {
        if (!open)
        {
            return;
        }

        Set<String> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
        {
            redisTemplate.delete(keys);
        }
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object)
    {
        if (!open)
        {
            return null;
        }

        if (object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String)
        {
            return String.valueOf(object);
        }

        return JSON.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz)
    {
        if (!open)
        {
            return null;
        }

        return JSON.parseObject(json, clazz);
    }


}