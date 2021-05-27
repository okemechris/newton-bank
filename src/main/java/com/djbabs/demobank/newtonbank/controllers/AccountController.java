package com.djbabs.demobank.newtonbank.controllers;

import com.djbabs.demobank.newtonbank.entities.Account;
import com.djbabs.demobank.newtonbank.pojos.ApiResponse;
import com.djbabs.demobank.newtonbank.pojos.NameInquiryResponse;
import com.djbabs.demobank.newtonbank.pojos.NewAccountRequest;
import com.djbabs.demobank.newtonbank.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/v1/account/name_inquiry")
    public ApiResponse<NameInquiryResponse> nameInquiry(@RequestParam String accountNumber){
        NameInquiryResponse response = accountService.performNameInquiry(accountNumber);

        return new ApiResponse<>("Request successful",true, HttpStatus.OK.value(),response);
    }

    @GetMapping("/v1/account/get_by_account_number")
    public ApiResponse<Account> getAccountByAccountNumber(@RequestParam String accountNumber){
        Account account = accountService.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Account not found"));

        return new ApiResponse<>("Request successful",true, HttpStatus.OK.value(),account);
    }

    @PostMapping("/v1/account/create")
    public ApiResponse<Account> nameInquiry(@RequestBody NewAccountRequest request){
        Account response = accountService.create(request);

        return new ApiResponse<>("Request successful",true, HttpStatus.OK.value(),response);
    }
}
