package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.AdminOperation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AdminOperationDAO
{
    @Insert("insert into admin_operation (operator, operation, targetUser, targetData,operationTime,reason) VALUES (#{operator},#{operation},#{targetUser},#{targetData},#{operationTime},#{reason})")
    void newOperation(AdminOperation adminOperation);

    @Select("select * from admin_operation where targetUser=#{targetUser}")
    List<AdminOperation> getAllOperations(int targetUser);

    @Select("select * from admin_operation where operator=#{operator}")
    List<AdminOperation> getAnAdminOperations(int operator);

    @Select("select * from admin_operation where operation=#{operation};")
    List<AdminOperation> getOperationsByType(String operation);
}
