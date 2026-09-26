package br.com.paulopinheiro.sampledb.persistence.dao.impl;

import br.com.paulopinheiro.sampledb.persistence.dao.AbstractDao;
import br.com.paulopinheiro.sampledb.persistence.entity.MicroMarket;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Data Access Object for MicroMarket entity using pure Jakarta CDI and Persistence.
 */
@ApplicationScoped // Migrating from legacy EJB @Stateless to lightweight CDI context
@Transactional // Guarantees native ACID transaction control via Jakarta Transactions
public class MicroMarketDao extends AbstractDao<MicroMarket> {

    @PersistenceContext(unitName = "sampledb-PU") // Corrected descriptor attribute to unitName
    private EntityManager em;

    public MicroMarketDao() {
        super(MicroMarket.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Finds a single MicroMarket by its primary key ZIP code representation.
     */
    public MicroMarket findMicroMarketByZipCode(String zipCode) {
        return super.getUniqueEqualStringAttribute("zipCode", zipCode);
    }
}
