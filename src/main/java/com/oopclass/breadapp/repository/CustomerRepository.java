package com.oopclass.breadapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oopclass.breadapp.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	//User findByEmail(String email);
}
