package br.com.paulopinheiro.sampledb.persistence.dao.impl;

import br.com.paulopinheiro.sampledb.persistence.dao.AbstractDao;
import br.com.paulopinheiro.sampledb.persistence.entity.PurchaseOrder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Data Access Object for PurchaseOrder entity using pure Jakarta CDI and Persistence.
 * Generation rules are handled at the core service level due to business smart keys.
 */
@ApplicationScoped 
@Transactional 
public class PurchaseOrderDao extends AbstractDao<PurchaseOrder> {
    @PersistenceContext(unitName="sampledb-PU") private EntityManager em;

    public PurchaseOrderDao() {super(PurchaseOrder.class);}

    @Override
    protected EntityManager getEntityManager() {return this.em;}
}
