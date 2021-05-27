package com.djbabs.demobank.newtonbank.repositories;

import com.djbabs.demobank.newtonbank.entities.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findAllByCustomerId(Long customerId);
}
