package com.djbabs.demobank.newtonbank.repositories;

import com.djbabs.demobank.newtonbank.entities.Transaction;
import com.djbabs.demobank.newtonbank.enums.PaymentChannel;
import com.djbabs.demobank.newtonbank.enums.TransactionDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findAllByAccountNumber(String accountNumber, Pageable pageable);
    Page<Transaction> findAllByAccountNumberAndDirection(String accountNumber, TransactionDirection direction,Pageable pageable);
    Page<Transaction> findAllByAccountNumberAndPaymentChannel(String accountNumber, PaymentChannel channel, Pageable pageable);
}
