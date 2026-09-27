package br.com.paulopinheiro.sampledb.core.service.impl;

import br.com.paulopinheiro.sampledb.core.dto.MicroMarketInput;
import br.com.paulopinheiro.sampledb.core.service.MicroMarketService;
import br.com.paulopinheiro.sampledb.persistence.dao.impl.MicroMarketDao;
import br.com.paulopinheiro.sampledb.persistence.entity.MicroMarket;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

/**
 * Default implementation of the MicroMarket business service using pure Jakarta CDI.
 */
@ApplicationScoped 
public class DefaultMicroMarketService implements MicroMarketService {
    @Inject private MicroMarketDao dao;

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<MicroMarket> getAllMicroMarkets() {
        return dao.findAll();
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public MicroMarket getMicroMarketByZipCode(String zipCode) {
        if (zipCode==null || zipCode.isEmpty()) throw new IllegalArgumentException("Zip code can't be null");
        return dao.findMicroMarketByZipCode(zipCode);
    }

    @Override
    @Transactional
    public void saveMicroMarket(MicroMarketInput input) {
        if (input == null) throw new IllegalArgumentException("Micro Market input cannot be null");
        if (input.zipCode()== null || input.zipCode().isEmpty()) throw new IllegalArgumentException("Zip Code cannot be null");

        MicroMarket existing = getMicroMarketByZipCode(input.zipCode());
        
        if (existing == null) { //It's a new Micro Market
            MicroMarket microMarket = new MicroMarket(input.zipCode());
            this.mapInputToEntity(input, microMarket);
            dao.create(microMarket);
        } else { // It's an existing Micro Market
            this.mapInputToEntity(input, existing);
            dao.edit(existing);
        }
    }

    @Override
    @Transactional
    public void removeMicroMarket(String microMarketZipCode) {
        if (microMarketZipCode==null) throw new IllegalArgumentException("Zip Code must be informed");

        MicroMarket microMarket = this.getMicroMarketByZipCode(microMarketZipCode);

        if (microMarket!=null) dao.remove(microMarket);
    }

    /**
     * Helper method to map DTO data into the JPA Entity cleanly.
     */
    private void mapInputToEntity(MicroMarketInput input, MicroMarket existing) {
        existing.setRadius(input.radius());
        existing.setAreaLength(input.areaLength());
        existing.setAreaWidth(input.areaWidth());
    }
}
