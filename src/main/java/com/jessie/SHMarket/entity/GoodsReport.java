package com.jessie.SHMarket.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReport implements Serializable
{
    int uid;
    int target;
    int targetUser;//标记卖家。方便扣分
    String reason;
    int status;
    int reportId;
    String result;
    LocalDateTime finishTime;
    String nickName;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getFinisTime()
    {
        return finishTime;
    }

    public int getUid()
    {
        return uid;
    }

    public void setUid(int uid)
    {
        this.uid = uid;//因为UID有异常排查到这里，结果生成后发现不是这个问题
    }
}
