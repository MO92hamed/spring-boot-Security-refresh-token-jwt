package com.itgate.ecommerce.service;



import com.itgate.ecommerce.models.Provider;

import java.util.List;

public interface ProviderService {
    public Provider findProvierById(Long id);
    public List<Provider> findProviders();
    public void deleteProvider(Long id);
    public Provider createProvider(Provider provider);
    public Provider updateProvider(Provider provider);
}
