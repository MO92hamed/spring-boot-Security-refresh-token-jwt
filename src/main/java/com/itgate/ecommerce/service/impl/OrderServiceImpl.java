package com.itgate.ecommerce.service.impl;


import com.itgate.ecommerce.models.Order;
import com.itgate.ecommerce.repository.OrderRepository;
import com.itgate.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    @Override
    public Order findOrderById(Long id) {
        return orderRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException(String.format("Order with id [%s] is not found in our databade", id)));
    }

    @Override
    public List<Order> findOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOerder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }
}
