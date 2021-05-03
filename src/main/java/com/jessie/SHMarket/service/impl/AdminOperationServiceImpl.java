package com.jessie.SHMarket.service.impl;

import com.jessie.SHMarket.dao.AdminOperationDAO;
import com.jessie.SHMarket.entity.AdminOperation;
import com.jessie.SHMarket.service.AdminOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("adminOperationService")
public class AdminOperationServiceImpl implements AdminOperationService
{
    @Autowired
    AdminOperationDAO adminOperationDAO;

    @Override
    public void newOperation(AdminOperation adminOperation)
    {
        adminOperationDAO.newOperation(adminOperation);
    }

    @Override
    public List<AdminOperation> getAllOperations(int targetUser)
    {
        return adminOperationDAO.getAllOperations(targetUser);
    }

    @Override
    public List<AdminOperation> getAnAdminOperations(int operator)
    {
        return adminOperationDAO.getAnAdminOperations(operator);
    }

    @Override
    public List<AdminOperation> getOperationsByType(String operation)
    {
        return adminOperationDAO.getOperationsByType(operation);
    }
}
