package br.com.paulopinheiro.sampledb.core.services;

import br.com.paulopinheiro.sampledb.persistence.entities.Customer;
import br.com.paulopinheiro.sampledb.persistence.entities.Manufacturer;
import br.com.paulopinheiro.sampledb.persistence.entities.MicroMarket;
import br.com.paulopinheiro.sampledb.persistence.entities.Product;
import br.com.paulopinheiro.sampledb.persistence.entities.ProductCode;
import br.com.paulopinheiro.sampledb.persistence.entities.PurchaseOrder;
import java.time.LocalDate;
import java.util.List;

public interface PurchaseOrderService {
    List<PurchaseOrder> getAllOrders();
    List<PurchaseOrder> getOrdersByCustomer(Customer customer);
    List<PurchaseOrder> getOrdersByProduct(Product product);
    List<PurchaseOrder> getOrdersByManufacturer(Manufacturer manufacturer);
    List<PurchaseOrder> getOrdersByCustomerProduct(Customer customer, Product product);
    List<PurchaseOrder> getOrdersByProductCode(ProductCode productCode);
    List<PurchaseOrder> getOrdersByMicroMarket(MicroMarket microMarket);
    List<PurchaseOrder> getOrdersByPeriod(LocalDate fromDate, LocalDate toDate);
    
    void saveOrder(PurchaseOrder purchaseOrder);
    PurchaseOrder getOrderByNum(Integer orderNum);
    void removeOrder(PurchaseOrder purchaseOrder);
}
