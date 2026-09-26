package br.com.paulopinheiro.sampledb.persistence.dao.impl;

import br.com.paulopinheiro.sampledb.persistence.dao.AbstractDao;
import br.com.paulopinheiro.sampledb.persistence.entity.ProductCode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Data Access Object for ProductCode entity using pure Jakarta CDI and Persistence.
 */
@ApplicationScoped // Migrating from legacy EJB @Stateless to lightweight CDI context
@Transactional // Guarantees native ACID transaction control via Jakarta Transactions
public class ProductCodeDao extends AbstractDao<ProductCode> {
    @PersistenceContext(unitName="sampledb-PU") private EntityManager em;

    public ProductCodeDao() {super(ProductCode.class);}

    @Override
    protected EntityManager getEntityManager() {return this.em;}

    public ProductCode findProductCodeByCode(String code) {
        return super.getUniqueEqualStringAttribute("prodCode", code);
    }
}
