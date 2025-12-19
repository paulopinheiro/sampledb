package br.com.paulopinheiro.sampledb.core.services.impl;

import br.com.paulopinheiro.sampledb.core.services.MicroMarketService;
import br.com.paulopinheiro.sampledb.persistence.dao.impl.MicroMarketDao;
import br.com.paulopinheiro.sampledb.persistence.entities.MicroMarket;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;
import java.util.Optional;

@Stateless
public class DefaultMicroMarketService implements MicroMarketService {
    @EJB private MicroMarketDao dao;

    @Override
    public List<MicroMarket> getAllMicroMarkets() {
        return dao.findAll();
    }

    @Override
    public void saveMicroMarket(MicroMarket microMarket) {
        if (Optional.ofNullable(microMarket).isEmpty()) throw new IllegalArgumentException("Micro market can't be null");

        if (Optional.ofNullable(microMarket.getZipCode()).isEmpty()) {
            dao.create(microMarket);
        } else {
            dao.edit(microMarket);
        }
    }

    @Override
    public MicroMarket getMicroMarketByZipCode(String zipCode) {
        if (Optional.ofNullable(zipCode).isEmpty()) throw new IllegalArgumentException("Zip code can't be null");
        return dao.findMicroMarketByZipCode(zipCode);
    }

    @Override
    public void removeMicroMarket(MicroMarket microMarket) {
        if (Optional.ofNullable(microMarket).isEmpty()) throw new IllegalArgumentException("Micro market can't be null");

        dao.remove(microMarket);
    }
    
}
