package br.com.paulopinheiro.sampledb.persistence.dao.impl;

import br.com.paulopinheiro.sampledb.persistence.dao.AbstractDao;
import br.com.paulopinheiro.sampledb.persistence.entity.ProductCode;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class ProductCodeDao extends AbstractDao<ProductCode> {
    @PersistenceContext(name="sampledb-PU") private EntityManager em;

    public ProductCodeDao() {super(ProductCode.class);}

    @Override
    protected EntityManager getEntityManager() {return this.em;}

    public ProductCode findProductCodeByCode(String code) {
        return super.getUniqueEqualStringAttribute("prodCode", code);
    }
}
