package com.jessie.SHMarket.service.impl;

import com.jessie.SHMarket.dao.ContactInfoDAO;
import com.jessie.SHMarket.entity.ContactInfo;
import com.jessie.SHMarket.service.ContactInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("contactInfoService")
public class ContactInfoServiceImpl implements ContactInfoService
{
    @Autowired
    ContactInfoDAO contactInfoDAO;
    @Override
    public void newContactInfo(ContactInfo contactInfo)
    {
        contactInfoDAO.newContactInfo(contactInfo);
    }

    @Override
    public List<ContactInfo> queryUserContactInfo(int uid)
    {
        return contactInfoDAO.queryUserContactInfo(uid);
    }

    @Override
    public void deleteContactInfo(int cid)
    {
        contactInfoDAO.deleteContactInfo(cid);
    }

    @Override
    public ContactInfo getContactInfo(int cid)
    {
        return contactInfoDAO.getContactInfo(cid);
    }
}
