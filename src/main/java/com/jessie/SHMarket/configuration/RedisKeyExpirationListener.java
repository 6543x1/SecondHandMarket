package com.jessie.SHMarket.configuration;

import com.jessie.SHMarket.service.OrderService;
import com.jessie.SHMarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 主要作用就是:接收过期的redis消息,获取到key,key就是订单号,然后去更新订单号的状态(说明一下:用户5分钟不支付的话取消用户的订单)
 */
@Transactional
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener
{


    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer)
    {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern)
    {

        String theInfo = message.toString();
        String[] theExpiredKey = theInfo.split("\\|");//以|分割开信息
        String type = theExpiredKey[0];
        String argus = theExpiredKey[1];
        if ("TASK_TEST".equals(type))
        {
            System.out.println("接受到了一个过期测试任务，参数是" + argus);
        } else if ("Jwt_TOKEN".equals(type))
        {
            System.out.println("有一个Token过期了，Token所属用户是" + argus);
        } else if ("orderGenerated".equals(type))
        {
            int oid = Integer.parseInt(argus);
            orderService.expireOrder(oid);
        } else if ("ClearExtraStatus".equals(type))
        {
            int uid = Integer.parseInt(argus);
            int expiredScore = (int) (userService.getAdditionalScore(uid) * 0.8 * (-1));
            userService.plusStatus(uid, expiredScore);
            userService.updateAdditionalScore(uid, userService.getAdditionalScore(uid) + expiredScore);
            //可能会有一点四舍五入的问题。。？不管了
        } else
        {
            System.out.println(theInfo);
        }


    }
}