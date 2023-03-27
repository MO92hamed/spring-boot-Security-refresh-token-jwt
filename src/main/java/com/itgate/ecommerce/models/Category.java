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
@Table(name = "_category")
public class Category {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private Set<Subcategory> subcategories = new HashSet<>();

    @JsonIgnore
    public Set<Subcategory> getSubcategories() {
        return subcategories;
    }
}
