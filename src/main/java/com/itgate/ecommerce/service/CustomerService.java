package com.itgate.ecommerce.service;



import com.itgate.ecommerce.models.Customer;

import java.util.List;

public interface CustomerService {
    public Customer findCustomerById(Long id);
    public List<Customer> findCustomers();
    public void deleteCustomer(Long id);
    public Customer createCustomer(Customer customer);
    public Customer updateCustomer(Customer customer);
}
