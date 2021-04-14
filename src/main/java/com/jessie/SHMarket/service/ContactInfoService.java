package com.jessie.SHMarket.service;

import com.jessie.SHMarket.entity.ContactInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ContactInfoService
{
    public void newContactInfo(ContactInfo contactInfo);
    public List<ContactInfo> queryUserContactInfo(int uid);
    public void deleteContactInfo(int cid);
    public ContactInfo getContactInfo(int cid);
}
