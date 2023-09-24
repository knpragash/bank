package com.learn.accounts.service;

import com.learn.accounts.dto.CustomerDto;

public interface IAccountsService {
    
    public void createAccount(CustomerDto customerDto);

    public CustomerDto fetchAccount(String mobileNumber);

    public boolean updateAccount(CustomerDto customerDto);

    public boolean deleteAccount(String mobileNumber);
}
