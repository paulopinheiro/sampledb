package br.com.paulopinheiro.sampledb.persistence.dao.impl;

import br.com.paulopinheiro.sampledb.persistence.dao.AbstractDao;
import br.com.paulopinheiro.sampledb.persistence.entities.MicroMarket;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class MicroMarketDao extends AbstractDao<MicroMarket> {
    @PersistenceContext(name="sampledb-PU") private EntityManager em;

    public MicroMarketDao() {super(MicroMarket.class);}

    @Override
    protected EntityManager getEntityManager() {return em;}

    public MicroMarket findMicroMarketByZipCode(String zipCode) {
        return super.getUniqueEqualStringAttribute("zipCode", zipCode);
    }
}
