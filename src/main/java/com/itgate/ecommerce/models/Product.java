package com.itgate.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@Table(name = "_product")
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String price;
    private String description;
    private String image;
    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @ManyToMany(mappedBy = "products", cascade = CascadeType.REMOVE)
    private Collection<Order> orders;

    @JsonIgnore
    public Collection<Order> getOrders() {
        return orders;
    }
}
