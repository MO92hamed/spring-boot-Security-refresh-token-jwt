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
@Table(name = "_provider")
public class Provider extends User{
    private String matricule;
    private String service;
    private String company;
    @OneToMany(mappedBy = "provider", cascade = CascadeType.REMOVE)
    private Set<Product> products = new HashSet<>();

    @JsonIgnore
    public Set<Product> getProducts() {
        return products;
    }

    public Provider(){

    }

    public Provider(String username, String email, String password, String matricule, String service, String company) {
        super(username, email, password);
        this.matricule = matricule;
        this.service = service;
        this.company = company;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
