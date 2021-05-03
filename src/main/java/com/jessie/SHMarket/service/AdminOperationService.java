package com.jessie.SHMarket.service;

import com.jessie.SHMarket.entity.AdminOperation;

import java.util.List;

public interface AdminOperationService
{
    void newOperation(AdminOperation adminOperation);

    List<AdminOperation> getAllOperations(int targetUser);

    List<AdminOperation> getAnAdminOperations(int operator);

    List<AdminOperation> getOperationsByType(String operation);
}
