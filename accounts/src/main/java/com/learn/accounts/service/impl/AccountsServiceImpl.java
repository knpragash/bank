package com.learn.accounts.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.accounts.constants.AccountsConstants;
import com.learn.accounts.dto.AccountsDto;
import com.learn.accounts.dto.CustomerDto;
import com.learn.accounts.entity.Accounts;
import com.learn.accounts.entity.Customer;
import com.learn.accounts.exception.CustomerAlreadyExistsException;
import com.learn.accounts.exception.ResourceNotFoundException;
import com.learn.accounts.exception.WrongNameException;
import com.learn.accounts.mapper.AccountsMapper;
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

        // if (customer.getName().equalsIgnoreCase("abc")) {
        //     throw new WrongNameException("Wrong Name. Hahaha!");
        // }

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

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        Accounts accounts = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Accounts", "Customer Id", 
                                                    customer.getCustomerId().toString())
        );

        // Customer optionalCustomer = customerRepository.findByMobileNumber(mobileNumber);
        // CustomerDto customerDto = CustomerMapper.mapToCustomerDto(optionalCustomer, new CustomerDto());

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        AccountsDto accountsDto = AccountsMapper.mapToAccountsDto(accounts, new AccountsDto());

        customerDto.setAccountsDto(accountsDto);


        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {

        AccountsDto accountsDto = customerDto.getAccountsDto();
        
        if(accountsDto !=null ){
            Accounts accounts = accountRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );

            Customer customer = customerRepository.findById(accounts.getCustomerId()).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "mobileNumber", 
                                                        customerDto.getMobileNumber().toString())
            );

            Accounts newAccount = AccountsMapper.mapToAccounts(accountsDto, accounts);
            Customer newCustomer = CustomerMapper.mapToCustomer(customerDto, customer);
            accountRepository.save(newAccount);
            customerRepository.save(newCustomer);
        }
        
        return true;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
		boolean isDeleted = false;
        
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "mobileNumber", 
                                                        mobileNumber)
            );
    
        Accounts accounts = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "customerId", 
                                                        customer.getCustomerId().toString())
            );
        
        // accountRepository.delete(accounts);
        // customerRepository.delete(customer);
        accountRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());

        isDeleted = true;
        
        return isDeleted;
    }
    
}
