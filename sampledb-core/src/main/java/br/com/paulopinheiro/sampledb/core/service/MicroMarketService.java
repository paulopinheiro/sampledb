package br.com.paulopinheiro.sampledb.core.service;

import br.com.paulopinheiro.sampledb.core.dto.MicroMarketInput;
import br.com.paulopinheiro.sampledb.persistence.entity.MicroMarket;
import java.util.List;

public interface MicroMarketService {
    List<MicroMarket> getAllMicroMarkets();
    
    void saveMicroMarket(MicroMarketInput input);
    MicroMarket getMicroMarketByZipCode(String zipCode);
    void removeMicroMarket(String microMarketZipCode);
}
