package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.ContactInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ContactInfoDAO
{
    @Insert("insert into contactinfo (name,phoneNumber,location,uid) values(#{name},#{phoneNumber},#{location},#{uid}) ")
    public void newContactInfo(ContactInfo contactInfo);
    @Select("select * from contactinfo where uid=#{uid}")
    public List<ContactInfo> queryUserContactInfo(int uid);
    @Delete("delete from contactinfo where cid=#{cid}")
    public void deleteContactInfo(int cid);
    @Select("select * from contactinfo where cid=#{cid}")
    public ContactInfo getContactInfo(int cid);
}
