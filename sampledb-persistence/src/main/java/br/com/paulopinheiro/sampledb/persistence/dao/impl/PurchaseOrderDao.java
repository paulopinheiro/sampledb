package br.com.paulopinheiro.sampledb.persistence.dao.impl;

import br.com.paulopinheiro.sampledb.persistence.dao.AbstractDao;
import br.com.paulopinheiro.sampledb.persistence.entity.Customer;
import br.com.paulopinheiro.sampledb.persistence.entity.MicroMarket;
import br.com.paulopinheiro.sampledb.persistence.entity.Product;
import br.com.paulopinheiro.sampledb.persistence.entity.ProductCode;
import br.com.paulopinheiro.sampledb.persistence.entity.PurchaseOrder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

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

    public List<PurchaseOrder> findOrdersByCustomer(Integer customerId) {
        return super.findEntitiesByEqualAttribute("customerId", customerId);
    }

    public List<PurchaseOrder> findOrdersByProduct(Integer productId) {
        return super.findEntitiesByEqualAttribute("productId", productId);
    }

    public List<PurchaseOrder> findOrdersByManufacturer(Integer manufacturerId) {
        return super.findEntitiesByEqualAttribute("manufacturerId", manufacturerId);
    }

    public List<PurchaseOrder> findOrdersBySalesPeriod(LocalDate fromDate, LocalDate toDate) {
        return super.findEntitiesByDateRange("salesDate", fromDate, toDate);
    }

    /**
     * Finds all purchase orders matching a specific customer and product combination.
     * Uses Criteria API path navigation to evaluate foreign key constraints safely.
     */
    public List<PurchaseOrder> findOrdersByCustomerProduct(Integer customerId, Integer productId) {
        // Fail-fast boundary check: if either ID is missing, return an empty list immediately
        if (customerId == null || productId == null) {
            return List.of();
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PurchaseOrder> cq = cb.createQuery(PurchaseOrder.class);
        Root<PurchaseOrder> root = cq.from(PurchaseOrder.class);

        cq.select(root);

        // Path Navigation: root.get("customer") points to the Customer relationship, 
        // and .get("customerId") reaches the primary key inside that entity.
        var customerExpression = root.get("customer").get("customerId");
        var productExpression = root.get("product").get("productId");

        // Combining multiple predicates natively using an AND condition
        cq.where(cb.and(
            cb.equal(customerExpression, customerId),
            cb.equal(productExpression, productId)
        ));

        return em.createQuery(cq).getResultList();
    }

    /**
     * Finds all purchase orders associated with a specific product code string.
     * Performs a cascading INNER JOIN: PurchaseOrder -> Product -> ProductCode.
     */
    public List<PurchaseOrder> findOrdersByProductCode(String productCode) {
        // Fail-fast condition using modern safe null/empty strings checking
        if (productCode == null || productCode.isBlank()) {
            return List.of(); // Safe immutable list from modern Java
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PurchaseOrder> cq = cb.createQuery(PurchaseOrder.class);
        Root<PurchaseOrder> orderRoot = cq.from(PurchaseOrder.class);

        cq.select(orderRoot);

        // 1. First JOIN: From PurchaseOrder to Product
        // orderRoot.join("product") uses the "product" property defined in PurchaseOrder entity
        Join<PurchaseOrder, Product> productJoin = orderRoot.join("product");

        // 2. Second JOIN: From Product to ProductCode
        // productJoin.join("productCode") navigates to the "productCode" property defined inside Product
        Join<Product, ProductCode> codeJoin = productJoin.join("productCode");

        // 3. Restriction: WHERE codeJoin.prodCode = productCode
        // Maps directly to the primary key property ("prodCode") inside the ProductCode entity
        cq.where(cb.equal(codeJoin.get("prodCode"), productCode.trim()));

        return em.createQuery(cq).getResultList();
    }

    /**
     * Finds all purchase orders belonging to a specific MicroMarket ZIP code.
     * Performs a cascading INNER JOIN: PurchaseOrder -> Customer -> MicroMarket.
     */
    public List<PurchaseOrder> findOrdersByMicroMarket(String microMarketZipCode) {
        // Fail-fast boundary condition for modern safe string checking
        if (microMarketZipCode == null || microMarketZipCode.isBlank()) {
            return List.of();
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PurchaseOrder> cq = cb.createQuery(PurchaseOrder.class);
        Root<PurchaseOrder> orderRoot = cq.from(PurchaseOrder.class);

        cq.select(orderRoot);

        // 1. First JOIN: From PurchaseOrder to Customer
        // Uses the "customer" property mapped inside the PurchaseOrder entity
        Join<PurchaseOrder, Customer> customerJoin = orderRoot.join("customer");

        // 2. Second JOIN: From Customer to MicroMarket
        // Uses the "microMarket" property mapped inside the Customer entity
        Join<Customer, MicroMarket> marketJoin = customerJoin.join("microMarket");

        // 3. Restriction: WHERE marketJoin.zipCode = microMarketZipCode
        // Points to the primary key property ("zipCode") inside the MicroMarket entity
        cq.where(cb.equal(marketJoin.get("zipCode"), microMarketZipCode.trim()));

        return em.createQuery(cq).getResultList();
    }
}
