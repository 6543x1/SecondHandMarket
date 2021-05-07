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
}
