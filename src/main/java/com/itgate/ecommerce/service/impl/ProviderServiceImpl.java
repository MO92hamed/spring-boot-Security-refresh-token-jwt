package com.itgate.ecommerce.service.impl;


import com.itgate.ecommerce.models.Provider;
import com.itgate.ecommerce.repository.ProviderRepository;
import com.itgate.ecommerce.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {
    private final ProviderRepository providerRepository;
    @Override
    public Provider findProvierById(Long id) {
        return providerRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException(String.format("Provider with id [%s] is not found in our database", id)));
    }

    @Override
    public List<Provider> findProviders() {
        return providerRepository.findAll();
    }

    @Override
    public void deleteProvider(Long id) {
        providerRepository.deleteById(id);
    }

    @Override
    public Provider createProvider(Provider provider) {
        return providerRepository.save(provider);
    }

    @Override
    public Provider updateProvider(Provider provider) {
        return providerRepository.save(provider);
    }
}
