package br.com.paulopinheiro.sampledb.core.service.impl;

import br.com.paulopinheiro.sampledb.core.dto.PurchaseOrderInput;
import br.com.paulopinheiro.sampledb.core.service.CustomerService;
import br.com.paulopinheiro.sampledb.core.service.ProductService;
import br.com.paulopinheiro.sampledb.core.service.PurchaseOrderService;
import br.com.paulopinheiro.sampledb.persistence.dao.impl.PurchaseOrderDao;
import br.com.paulopinheiro.sampledb.persistence.entity.PurchaseOrder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class DefaultPurchaseOrderService implements PurchaseOrderService {
    @Inject private PurchaseOrderDao dao;
    @Inject private CustomerService customerService;
    @Inject private ProductService productService;

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<PurchaseOrder> getAllOrders() {
        return dao.findAll();
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public PurchaseOrder getOrderByNum(Integer orderNum) {
        return dao.find(orderNum);
    }

    @Override
    public List<PurchaseOrder> getOrdersByCustomer(Integer customerId) {
        return dao.findOrdersByCustomer(customerId);
    }

    @Override
    public List<PurchaseOrder> getOrdersByProduct(Integer productId) {
        return dao.findOrdersByProduct(productId);
    }

    @Override
    public List<PurchaseOrder> getOrdersByManufacturer(Integer manufacturerId) {
        return dao.findOrdersByManufacturer(manufacturerId);
    }

    @Override
    public List<PurchaseOrder> getOrdersByCustomerProduct(Integer customerId, Integer productId) {
        return dao.findOrdersByCustomerProduct(customerId, productId);
    }

    @Override
    public List<PurchaseOrder> getOrdersByProductCode(String productCode) {
        return dao.findOrdersByProductCode(productCode);
    }

    @Override
    public List<PurchaseOrder> getOrdersByMicroMarket(String microMarketZipCode) {
        return dao.findOrdersByMicroMarket(microMarketZipCode);
    }

    @Override
    public List<PurchaseOrder> getOrdersByPeriod(LocalDate fromDate, LocalDate toDate) {
        return dao.findOrdersBySalesPeriod(fromDate, toDate);
    }

    @Override
    @Transactional
    public void saveOrder(PurchaseOrderInput input) {
        if (input == null) throw new IllegalArgumentException("Purchase order input cannot be null.");
        if (input.orderNum() == null) throw new IllegalArgumentException("You must inform the purchase order number.");

        // Fetch the actual database state BEFORE any changes are applied
        PurchaseOrder existing = dao.find(input.orderNum());
        int quantityTaken;

        if (existing == null) { // It's a new Purchase Order
            quantityTaken = input.quantity();

            PurchaseOrder newOrder = new PurchaseOrder(input.orderNum());
            mapInputToEntity(input,newOrder);
            dao.create(newOrder);
        } else { //It's an existing order
            // The record 'input.quantity()' holds the new value screen, 
            // while 'existing.getQuantity()' holds the stable DB value.
            quantityTaken = input.quantity() - existing.getQuantity();

            mapInputToEntity(input,existing);
            dao.edit(existing);
        }

        // Adjust product inventory safely based on the frozen delta calculation
        if (quantityTaken != 0) {
            // Fetching target product from the input payload
            var targetProduct = productService.getProductById(input.productId());
            productService.subtractFromProductQuantity(targetProduct, quantityTaken);
        }
    }

    @Override
    @Transactional
    public void removeOrder(Integer orderNum) {
        if (orderNum == null) throw new IllegalArgumentException("Order number must be informed");

        PurchaseOrder purchaseOrder = this.getOrderByNum(orderNum);

        if (purchaseOrder != null) {
            int quantityToRestore = purchaseOrder.getQuantity();
            
            // Devolve o saldo para o estoque antes de apagar o pedido
            if (quantityToRestore > 0) {
                productService.subtractFromProductQuantity(purchaseOrder.getProduct(), -quantityToRestore);
            }
            
            dao.remove(purchaseOrder);
        }
    }

    /**
     * Helper method to map DTO data into the JPA Entity cleanly.
     */
    private void mapInputToEntity(PurchaseOrderInput input, PurchaseOrder entity) {
        entity.setQuantity(input.quantity());
        entity.setShippingCost(input.shippingCost());
        entity.setSalesDate(input.salesDate());
        entity.setShippingDate(input.shippingDate());
        entity.setFreightCompany(input.freightCompany());
        entity.setCustomer(customerService.getCustomerById(input.customerId()));
        entity.setProduct(productService.getProductById(input.productId()));
    }
}
