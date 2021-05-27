package com.djbabs.demobank.newtonbank.repositories;

import com.djbabs.demobank.newtonbank.entities.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Optional<Customer> findByBvn(String bvn);
}
