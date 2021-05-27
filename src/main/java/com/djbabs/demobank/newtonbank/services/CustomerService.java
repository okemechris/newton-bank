package com.djbabs.demobank.newtonbank.services;

import com.djbabs.demobank.newtonbank.entities.Customer;
import com.djbabs.demobank.newtonbank.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Optional<Customer> findById(Long id){
        return customerRepository.findById(id);
    }

    public Customer save(Customer customer){
        return customerRepository.save(customer);
    }

    public Optional<Customer> findByBvn(String bvn){
        return customerRepository.findByBvn(bvn);
    }
}
