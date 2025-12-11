package br.com.paulopinheiro.sampledb.persistence.dao.impl;

import br.com.paulopinheiro.sampledb.persistence.dao.AbstractDao;
import br.com.paulopinheiro.sampledb.persistence.entities.Manufacturer;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class ManufacturerDao extends AbstractDao<Manufacturer> {
    @PersistenceContext(name="sampledb-PU")
    private EntityManager em;

    public ManufacturerDao() {
        super(Manufacturer.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
