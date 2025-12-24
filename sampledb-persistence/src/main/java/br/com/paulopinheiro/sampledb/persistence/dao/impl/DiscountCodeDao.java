package br.com.paulopinheiro.sampledb.persistence.dao.impl;

import br.com.paulopinheiro.sampledb.persistence.dao.AbstractDao;
import br.com.paulopinheiro.sampledb.persistence.entity.DiscountCode;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class DiscountCodeDao extends AbstractDao<DiscountCode> {
    @PersistenceContext(name="sampledb-PU") private EntityManager em;

    public DiscountCodeDao() {super(DiscountCode.class);}

    @Override
    protected EntityManager getEntityManager() {return em;}

    public DiscountCode findDiscountCodeByCode(String code) {
        return super.getUniqueEqualStringAttribute("discountCode", code);
    }
}
