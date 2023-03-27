package com.itgate.ecommerce.service;



import com.itgate.ecommerce.models.Order;

import java.util.List;

public interface OrderService {
    public Order findOrderById(Long id);
    public List<Order> findOrders();
    public void deleteOerder(Long id);
    public Order createOrder(Order order);
    public Order updateOrder(Order order);
}
