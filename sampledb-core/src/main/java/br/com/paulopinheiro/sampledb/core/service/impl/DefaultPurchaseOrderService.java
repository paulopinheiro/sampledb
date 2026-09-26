package br.com.paulopinheiro.sampledb.core.service.impl;

import br.com.paulopinheiro.sampledb.core.service.ProductService;
import br.com.paulopinheiro.sampledb.core.service.PurchaseOrderService;
import br.com.paulopinheiro.sampledb.persistence.dao.impl.PurchaseOrderDao;
import br.com.paulopinheiro.sampledb.persistence.entity.Customer;
import br.com.paulopinheiro.sampledb.persistence.entity.Manufacturer;
import br.com.paulopinheiro.sampledb.persistence.entity.MicroMarket;
import br.com.paulopinheiro.sampledb.persistence.entity.Product;
import br.com.paulopinheiro.sampledb.persistence.entity.ProductCode;
import br.com.paulopinheiro.sampledb.persistence.entity.PurchaseOrder;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Stateless
public class DefaultPurchaseOrderService implements PurchaseOrderService {
    @EJB private PurchaseOrderDao dao;
    @EJB private ProductService productService;

    @Override
    public List<PurchaseOrder> getAllOrders() {
        return dao.findAll();
    }

    @Override
    public List<PurchaseOrder> getOrdersByCustomer(Customer customer) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<PurchaseOrder> getOrdersByProduct(Product product) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<PurchaseOrder> getOrdersByManufacturer(Manufacturer manufacturer) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<PurchaseOrder> getOrdersByCustomerProduct(Customer customer, Product product) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<PurchaseOrder> getOrdersByProductCode(ProductCode productCode) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<PurchaseOrder> getOrdersByMicroMarket(MicroMarket microMarket) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<PurchaseOrder> getOrdersByPeriod(LocalDate fromDate, LocalDate toDate) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    @Transactional
    public void saveOrder(PurchaseOrder purchaseOrder) {
        if (Optional.ofNullable(purchaseOrder).isEmpty()) throw new IllegalArgumentException("Purchase order can't be null.");
        if (Optional.ofNullable(purchaseOrder.getOrderNum()).isEmpty()) throw new IllegalArgumentException("You have to inform the purchase order number");

        PurchaseOrder oldOrder = getOrderByNum(purchaseOrder.getOrderNum());

        Integer quantityTaken;

        if (Optional.ofNullable(oldOrder).isEmpty()) {
            dao.create(purchaseOrder);
            quantityTaken = Integer.valueOf(purchaseOrder.getQuantity());
        } else {
            dao.edit(purchaseOrder);
            quantityTaken = Integer.valueOf(purchaseOrder.getQuantity()) - Integer.valueOf(oldOrder.getQuantity());
        }

        if (quantityTaken != 0) productService.subtractFromProductQuantity(purchaseOrder.getProduct(), quantityTaken);
    }

    @Override
    public PurchaseOrder getOrderByNum(Integer orderNum) {
        return dao.find(orderNum);
    }

    @Override
    public void removeOrder(PurchaseOrder purchaseOrder) {
        if (Optional.ofNullable(purchaseOrder).isEmpty()) throw new IllegalArgumentException("Purchase order can't be null.");

        dao.remove(purchaseOrder);
    }
}
