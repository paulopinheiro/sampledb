package br.com.paulopinheiro.sampledb.persistence.dao.impl;

import br.com.paulopinheiro.sampledb.persistence.dao.AbstractDao;
import br.com.paulopinheiro.sampledb.persistence.entities.Manufacturer;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.Optional;

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

    public Integer getNextAvailableId() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
        Root<Manufacturer> root = cq.from(Manufacturer.class);

        cq.select(cb.max(root.get("manufacturerId")));

        Integer maxId = em.createQuery(cq).getSingleResult();

        return (Optional.ofNullable(maxId ).isEmpty()) ? 1 : maxId + 1;
    }
}
