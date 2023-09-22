package com.learn.accounts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.accounts.constants.AccountsConstants;
import com.learn.accounts.dto.CustomerDto;
import com.learn.accounts.dto.ResponseDto;
import com.learn.accounts.service.IAccountsService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
// @AllArgsConstructor
public class AccountsController {

	@Autowired
	private IAccountsService iAccountsService;

	@GetMapping("/sayHello")
	public String sayHello() {
		return "It Works...";
	}

	@PostMapping(path="/create")
	public ResponseEntity<ResponseDto> createAccount(@RequestBody CustomerDto customerDto) {
		
		iAccountsService.createAccount(customerDto);

		return ResponseEntity
		.status(HttpStatus.CREATED)
		.body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
	}
}
