package com.learn.accounts.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.accounts.constants.AccountsConstants;
import com.learn.accounts.dto.CustomerDto;
import com.learn.accounts.entity.Accounts;
import com.learn.accounts.entity.Customer;
import com.learn.accounts.exception.CustomerAlreadyExistsException;
import com.learn.accounts.mapper.CustomerMapper;
import com.learn.accounts.repository.AccountRepository;
import com.learn.accounts.repository.CustomerRepository;
import com.learn.accounts.service.IAccountsService;

@Service
public class AccountsServiceImpl implements IAccountsService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());

        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customer.getMobileNumber());
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer exists with the same mobile number "+ customer.getMobileNumber());
        }

        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Pragash");
        Customer savedCustomer = customerRepository.save(customer);

        accountRepository.save(createNewAccount(savedCustomer));
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("Pragash");

        return newAccount;
    }
    
}
