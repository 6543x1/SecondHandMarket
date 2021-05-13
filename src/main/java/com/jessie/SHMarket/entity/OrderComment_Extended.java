package com.jessie.SHMarket.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data()
public class OrderComment_Extended extends OrderComment
{
    private Goods goods;//评价顺便也返回商品信息

}
