package com.itgate.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@Table(name = "_customer")
public class Customer extends User{
    private String adderess;
    private String city;
    private String image;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    private Set<Order> orders = new HashSet<>();

    @JsonIgnore
    public Set<Order> getOrders() {
        return orders;
    }

    public Customer(){

    }

    public Customer(String username, String email, String password, String adderess, String city) {
        super(username, email, password);
        this.adderess = adderess;
        this.city = city;
    }


}
