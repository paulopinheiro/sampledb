package br.com.paulopinheiro.sampledb.core.services.impl;

import br.com.paulopinheiro.sampledb.core.services.PurchaseOrderService;
import br.com.paulopinheiro.sampledb.persistence.entities.Customer;
import br.com.paulopinheiro.sampledb.persistence.entities.Manufacturer;
import br.com.paulopinheiro.sampledb.persistence.entities.MicroMarket;
import br.com.paulopinheiro.sampledb.persistence.entities.Product;
import br.com.paulopinheiro.sampledb.persistence.entities.ProductCode;
import br.com.paulopinheiro.sampledb.persistence.entities.PurchaseOrder;
import java.time.LocalDate;
import java.util.List;

public class DefaultPurchaseOrderService implements PurchaseOrderService {

    @Override
    public List<PurchaseOrder> getAllOrders() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
    public void saveOrder(PurchaseOrder purchaseOrder) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public PurchaseOrder getOrderByNum(Integer orderNum) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void removeOrder(PurchaseOrder purchaseOrder) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
