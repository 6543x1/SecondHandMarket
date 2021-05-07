package com.jessie.SHMarket.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderWithGoods implements Serializable
{
    private Order order;
    private Goods goods;

}
