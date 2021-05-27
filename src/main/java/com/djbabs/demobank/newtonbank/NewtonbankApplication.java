package com.djbabs.demobank.newtonbank;

import com.djbabs.demobank.newtonbank.entities.Customer;
import com.djbabs.demobank.newtonbank.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class NewtonbankApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewtonbankApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(CustomerRepository repository) {
		return (args) -> {

			// save a couple of customers
			Customer customer1 = new Customer();
			customer1.setBvn("222367087469");
			customer1.setDob(new Date());
			customer1.setFirstName("Christian");
			customer1.setLastName("Okeme");
			customer1 = repository.findByBvn("222367087469").orElse(repository.save(customer1));


			Customer customer2 = new Customer();
			customer2.setBvn("222377087469");
			customer2.setDob(new Date());
			customer2.setFirstName("Otobong");
			customer2.setLastName("Jerome");
			customer2 = repository.findByBvn("222367087469").orElse(repository.save(customer1));
			customer2 = repository.save(customer2);

			Customer customer3 = new Customer();
			customer3.setBvn("222367087969");
			customer3.setDob(new Date());
			customer3.setFirstName("John");
			customer3.setLastName("Doe");
			customer3 = repository.save(customer3);


		}
	}
}
