package br.com.paulopinheiro.sampledb.persistence.dao.impl;

import br.com.paulopinheiro.sampledb.persistence.dao.AbstractDao;
import br.com.paulopinheiro.sampledb.persistence.entity.Manufacturer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Data Access Object for Manufacturer entity using pure Jakarta CDI and Persistence.
 * Generation rules are handled at the core service level due to business smart keys.
 */
@ApplicationScoped 
@Transactional 
public class ManufacturerDao extends AbstractDao<Manufacturer> {

    @PersistenceContext(unitName = "sampledb-PU") 
    private EntityManager em;

    public ManufacturerDao() {
        super(Manufacturer.class);
    }

    /**
     * Encapsulates the persistence logic to find the max ID prefixing a specific year.
     * Keeps persistence code isolated from the core service.
     */
    public Integer findMaxIdByYearPrefix(int idPrefix) {
        var cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(Integer.class);
        var root = cq.from(Manufacturer.class);

        cq.select(cb.max(root.get("manufacturerId")));
        cq.where(cb.between(root.get("manufacturerId"), idPrefix, idPrefix + 9999));

        return em.createQuery(cq).getSingleResult();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
