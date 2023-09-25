package com.learn.accounts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learn.accounts.constants.AccountsConstants;
import com.learn.accounts.dto.CustomerDto;
import com.learn.accounts.dto.ResponseDto;
import com.learn.accounts.service.IAccountsService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
// @AllArgsConstructor
@Validated
public class AccountsController {

	@Autowired
	private IAccountsService iAccountsService;

	@GetMapping("/sayHello")
	public String sayHello() {
		return "It Works...";
	}

	@PostMapping(path="/create")
	public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {

		iAccountsService.createAccount(customerDto);

		return ResponseEntity
		.status(HttpStatus.CREATED)
		.body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
	}

	@GetMapping(path="/fetch")
	public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam 
							@Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
							String mobileNumber) {

	 	CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);

		return ResponseEntity
		.status(HttpStatus.OK)
		.body(customerDto);
	}

	@PutMapping(path="/update")
	public ResponseEntity<ResponseDto> updateAccount(@Valid @RequestBody CustomerDto customerDto) {

		boolean isUpdated = false;
		
		isUpdated = iAccountsService.updateAccount(customerDto);

		if(isUpdated) {
			return ResponseEntity
			.status(HttpStatus.OK)
			.body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
		} else {
			return ResponseEntity
			.status(HttpStatus.EXPECTATION_FAILED)
			.body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
		}

	}

	@DeleteMapping(path="/delete")
	public ResponseEntity<ResponseDto> deleteAccount(@RequestParam 
							@Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
							String mobileNumber) {
								
		boolean isDeleted = false;

		isDeleted = iAccountsService.deleteAccount(mobileNumber);

		if(isDeleted) {
			return ResponseEntity
			.status(HttpStatus.OK)
			.body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
		} else {
			return ResponseEntity
			.status(HttpStatus.EXPECTATION_FAILED)
			.body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
		}

	}
}
