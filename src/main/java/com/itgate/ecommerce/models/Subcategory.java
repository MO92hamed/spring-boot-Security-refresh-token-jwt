package com.itgate.ecommerce.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_subcategory")
public class Subcategory {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;

    @ManyToOne
   @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "subcategory", cascade = CascadeType.REMOVE)
    private Set<Product> products = new HashSet<>();

    @JsonIgnore
    public Set<Product> getProducts() {
        return products;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


}
