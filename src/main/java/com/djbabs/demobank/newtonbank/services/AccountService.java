package com.djbabs.demobank.newtonbank.services;

import com.djbabs.demobank.newtonbank.entities.Account;
import com.djbabs.demobank.newtonbank.entities.Customer;
import com.djbabs.demobank.newtonbank.pojos.NameInquiryResponse;
import com.djbabs.demobank.newtonbank.pojos.NewAccountRequest;
import com.djbabs.demobank.newtonbank.repositories.AccountRepository;
import com.djbabs.demobank.newtonbank.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public Optional<Account> findById(Long id){
        return accountRepository.findById(id);
    }

    public Account create(NewAccountRequest newAccountRequest){

        String accountNumber = Long.toString(new Date().getTime());
        accountNumber = accountNumber.substring(3);

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountType(newAccountRequest.getAccountType());
        account.setCustomerId(newAccountRequest.getCustomerId());
        account.setBalance(BigDecimal.ZERO);
        return accountRepository.save(account);
    }

    public Optional<Account>findByAccountNumber(String accountNumber){
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public List<Account> findAllCustomerId(Long customerId){
        return accountRepository.findAllByCustomerId(customerId);
    }

    public NameInquiryResponse performNameInquiry(String accountNumber){

        Account account = findByAccountNumber(accountNumber).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Account not found"));
        Customer customer = customerRepository.findById(account.getCustomerId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Account customer not found"));
        NameInquiryResponse response = new NameInquiryResponse();
        response.setAccount(account.getAccountNumber());
        response.setAccountName(customer.toString());
        response.setBvn(customer.getBvn());

        return response;
    }

}
