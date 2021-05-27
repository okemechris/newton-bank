package com.djbabs.demobank.newtonbank.services;

import com.djbabs.demobank.newtonbank.entities.Transaction;
import com.djbabs.demobank.newtonbank.enums.PaymentChannel;
import com.djbabs.demobank.newtonbank.enums.TransactionDirection;
import com.djbabs.demobank.newtonbank.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Page<Transaction> findAllByAccountNumber(String accountNumber, Pageable pageable){
        return transactionRepository.findAllByAccountNumber(accountNumber,pageable);
    }

    public Transaction save(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public Page<Transaction> findAllByAccountNumberAndDirection(String accountNumber, TransactionDirection direction, Pageable pageable){
        return transactionRepository.findAllByAccountNumberAndDirection(accountNumber,direction,pageable);
    }

    public Page<Transaction> findAllByAccountNumberAndPaymentChannel(String accountNumber, PaymentChannel channel, Pageable pageable){
        return transactionRepository.findAllByAccountNumberAndPaymentChannel(accountNumber,channel,pageable);
    }


}
