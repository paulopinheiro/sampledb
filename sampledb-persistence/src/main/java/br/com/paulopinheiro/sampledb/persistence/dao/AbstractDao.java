package br.com.paulopinheiro.sampledb.persistence.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Objects;

public abstract class AbstractDao<T> {
    
    private final Class<T> entityClass;

    protected AbstractDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    protected T getUniqueEqualStringAttribute(String attributeName, String equalPattern) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);

        String safePattern = Objects.requireNonNullElse(equalPattern, "");

        cq.select(root);
        cq.where(cb.equal(root.get(attributeName), safePattern));
        
        List<T> list = getEntityManager().createQuery(cq).getResultList();
        return list.isEmpty() ? null : list.getFirst(); // Java Modern feature: getFirst() over get(0)
    }

    protected TypedQuery<T> getLikeTypedQuery(String attributeName, String likePattern) {
        CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);

        String safePattern = Objects.requireNonNullElse(likePattern, "").toUpperCase();

        cq.select(root);
        cq.where(cb.like(cb.upper(root.get(attributeName)), "%" + safePattern + "%"));

        return getEntityManager().createQuery(cq);
    }

    protected List<T> findEntitiesWithNotNullColumn(String attributeName) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);

        cq.select(root);
        cq.where(cb.isNotNull(root.get(attributeName)));

        return getEntityManager().createQuery(cq).getResultList();
    }

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        EntityManager em = getEntityManager();
        // Defensive check: if the entity is managed, remove it directly. 
        // If detached, merge it back into the persistent context before removing.
        if (em.contains(entity)) {
            em.remove(entity);
        } else {
            em.remove(em.merge(entity));
        }
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        cq.select(cq.from(entityClass));
        
        TypedQuery<T> query = getEntityManager().createQuery(cq);
        query.setMaxResults(range[1] - range[0] + 1);
        query.setFirstResult(range[0]);
        return query.getResultList();
    }

    public int count() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class); // Explicit type definition
        Root<T> root = cq.from(entityClass);
        
        cq.select(cb.count(root));
        Long result = getEntityManager().createQuery(cq).getSingleResult();
        return result != null ? result.intValue() : 0;
    }
}
