package com.bankapp.customer_service.repository;

import com.bankapp.customer_service.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, String> {

    Customer findByCustomerId(String customerId);
}
