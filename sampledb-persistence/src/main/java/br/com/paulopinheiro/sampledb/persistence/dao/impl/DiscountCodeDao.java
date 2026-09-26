package br.com.paulopinheiro.sampledb.persistence.dao.impl;

import br.com.paulopinheiro.sampledb.persistence.dao.AbstractDao;
import br.com.paulopinheiro.sampledb.persistence.entity.DiscountCode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Data Access Object for DiscountCode entity using pure Jakarta CDI and Persistence.
 */
@ApplicationScoped // Replaces legacy @Stateless with high-performance CDI context
@Transactional // Ensures transactional safety natively through Jakarta Transactions
public class DiscountCodeDao extends AbstractDao<DiscountCode> {

    @PersistenceContext(unitName = "sampledb-PU") // Correct property is unitName (name is for JNDI binding)
    private EntityManager em;

    public DiscountCodeDao() {
        super(DiscountCode.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Finds a single DiscountCode by its primary key string representation.
     */
    public DiscountCode findDiscountCodeByCode(String code) {
        return super.getUniqueEqualStringAttribute("discountCode", code);
    }
}
