package br.com.paulopinheiro.sampledb.persistence.dao.impl;

import br.com.paulopinheiro.sampledb.persistence.dao.AbstractDao;
import br.com.paulopinheiro.sampledb.persistence.entity.PurchaseOrder;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class PurchaseOrderDao extends AbstractDao<PurchaseOrder> {
    @PersistenceContext(name="sampledb-PU") private EntityManager em;

    public PurchaseOrderDao() {super(PurchaseOrder.class);}

    @Override
    protected EntityManager getEntityManager() {return this.em;}
}
