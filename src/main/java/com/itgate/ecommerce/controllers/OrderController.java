package com.itgate.ecommerce.controllers;



import com.itgate.ecommerce.models.Customer;
import com.itgate.ecommerce.models.Order;
import com.itgate.ecommerce.models.Product;
import com.itgate.ecommerce.service.impl.CustomerServiceImpl;
import com.itgate.ecommerce.service.impl.OrderServiceImpl;
import com.itgate.ecommerce.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OrderController {

    private final OrderServiceImpl orderService;
    private final CustomerServiceImpl customerServiceImpl;
    private final ProductServiceImpl productServiceImpl;

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id){
        return orderService.findOrderById(id);
    }

    @GetMapping
    public List<Order> getAllOrders(){
        return orderService.findOrders();
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteOrder(@PathVariable Long id){
        Map<String, Boolean> response = new HashMap<>();
        orderService.findOrderById(id);
        try{
            response.put("Deleted", Boolean.TRUE);
            return response;
        }catch(Exception e){
            response.put("failed to delete", Boolean.FALSE);
            return response;
        }
    }

    @PostMapping("/{idcustomer}")
    public Order addOrder(@RequestBody Order order, @PathVariable Long idcustomer, @RequestParam List<Long> ids){
        for (int i = 0; i < ids.size(); i++){
            Product product = productServiceImpl.findById(ids.get(i));
            order.addproduct(product);
        }
        Customer customer = customerServiceImpl.findCustomerById(idcustomer);
        order.setCustomer(customer);
        return orderService.createOrder(order);
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order order){
        Order order1 = orderService.findOrderById(id);
        if (order1 != null){
            order.setCustomer(order1.getCustomer());
            return orderService.updateOrder(order);
        }
        else
            throw new RuntimeException("Failed to update ");
    }
}
