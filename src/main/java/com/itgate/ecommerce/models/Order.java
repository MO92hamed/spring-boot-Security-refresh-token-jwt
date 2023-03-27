package com.itgate.ecommerce.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "_order")
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    private String reference;
    private String price;
    private String date;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany
    @JoinTable(name = "prod_ord", joinColumns = @JoinColumn(name = "prod_id"), inverseJoinColumns = @JoinColumn(name = "ord_id"))
    private Collection<Product> products;

    public void addproduct(Product product){
        if (products == null)
            products = new ArrayList<>();
        products.add(product);
    }

}
