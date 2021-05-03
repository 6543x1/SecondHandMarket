package com.jessie.SHMarket.service;

public interface MailService
{
    public void sendResetPw(String dest, String theInfo);
    public void sendNewOrder(String dest, String theInfo);

    public void newMessage(String subject, String dest, String theInfo);
}
