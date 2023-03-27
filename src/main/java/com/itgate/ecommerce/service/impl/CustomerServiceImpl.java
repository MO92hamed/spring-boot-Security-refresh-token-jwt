package com.itgate.ecommerce.service.impl;


import com.itgate.ecommerce.models.Customer;
import com.itgate.ecommerce.repository.CustomerRepository;
import com.itgate.ecommerce.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    @Override
    public Customer findCustomerById(Long id) {
        return customerRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Customer with id [%s] is not found in our database", id)));
    }

    @Override
    public List<Customer> findCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}
