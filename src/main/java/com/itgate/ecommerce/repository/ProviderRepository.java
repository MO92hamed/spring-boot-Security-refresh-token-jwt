package com.itgate.ecommerce.repository;


import com.itgate.ecommerce.models.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
}
