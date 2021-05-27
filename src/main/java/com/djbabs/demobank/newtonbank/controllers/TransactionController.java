package com.djbabs.demobank.newtonbank.controllers;

import com.djbabs.demobank.newtonbank.entities.Transaction;
import com.djbabs.demobank.newtonbank.enums.PaymentChannel;
import com.djbabs.demobank.newtonbank.enums.TransactionDirection;
import com.djbabs.demobank.newtonbank.pojos.ApiResponse;
import com.djbabs.demobank.newtonbank.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/v1/transaction/find_by_account")
    public ApiResponse<Page<Transaction>> getTransactionsByAccount(@RequestParam String accountNumber, @RequestParam int page, @RequestParam int limit){

        System.out.println("page is"+page+" size "+limit);
        Page<Transaction> transactionPage = transactionService.findAllByAccountNumber(accountNumber, PageRequest.of(page,limit, Sort.Direction.ASC,"dateAdded"));

        return new ApiResponse<>("Request successful",true, HttpStatus.OK.value(),transactionPage);
    }

    @GetMapping("/v1/transaction/find_by_account_and_direction")
    public ApiResponse<Page<Transaction>> getTransactionsByAccountAndDir(@RequestParam String accountNumber, @RequestParam
            TransactionDirection dir,@RequestParam int page,@RequestParam int limit){
        Page<Transaction> transactionPage = transactionService.findAllByAccountNumberAndDirection(
                accountNumber, dir,PageRequest.of(page,limit, Sort.Direction.ASC,"dateAdded"));

        return new ApiResponse<>("Request successful",true, HttpStatus.OK.value(),transactionPage);
    }

    @PostMapping("/v1/transaction/find_by_account_and_channel")
    public ApiResponse<Page<Transaction>>  nameInquiry(@RequestParam String accountNumber,@RequestParam
            PaymentChannel channel,@RequestParam int page,@RequestParam int limit){

        Page<Transaction> transactionPage = transactionService.findAllByAccountNumberAndPaymentChannel(
                accountNumber, channel,PageRequest.of(page,limit, Sort.Direction.ASC,"dateAdded"));

        return new ApiResponse<>("Request successful",true, HttpStatus.OK.value(),transactionPage);
    }
}
