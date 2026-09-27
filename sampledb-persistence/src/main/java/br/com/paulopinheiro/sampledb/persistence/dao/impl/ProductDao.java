package br.com.paulopinheiro.sampledb.persistence.dao.impl;

import br.com.paulopinheiro.sampledb.persistence.dao.AbstractDao;
import br.com.paulopinheiro.sampledb.persistence.entity.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Data Access Object for Product entity using pure Jakarta CDI and Persistence.
 * Generation rules are handled at the core service level due to business smart
 * keys.
 */
@ApplicationScoped
@Transactional
public class ProductDao extends AbstractDao<Product> {

    @PersistenceContext(unitName = "sampledb-PU")
    private EntityManager em;

    public ProductDao() {
        super(Product.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public void editAndRefresh(Product product) {
        Product merged = getEntityManager().merge(product);
        getEntityManager().flush(); // Forces JDBC communication and fires the DB Trigger
        getEntityManager().refresh(merged); // Refreshes the instance with the trigger's modifications
    }
}
