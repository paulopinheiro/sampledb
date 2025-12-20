package br.com.paulopinheiro.sampledb.persistence.dao.impl;

import br.com.paulopinheiro.sampledb.persistence.dao.AbstractDao;
import br.com.paulopinheiro.sampledb.persistence.entities.Product;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class ProductDao extends AbstractDao<Product> {
    @PersistenceContext(name="sampledb-PU") private EntityManager em;

    public ProductDao() {super(Product.class);}

    @Override
    public EntityManager getEntityManager() {return em;}
}
