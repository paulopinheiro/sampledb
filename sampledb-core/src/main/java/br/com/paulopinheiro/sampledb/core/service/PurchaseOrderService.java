package br.com.paulopinheiro.sampledb.core.service;

import br.com.paulopinheiro.sampledb.core.dto.PurchaseOrderInput;
import br.com.paulopinheiro.sampledb.persistence.entity.PurchaseOrder;
import java.time.LocalDate;
import java.util.List;

public interface PurchaseOrderService {
    List<PurchaseOrder> getAllOrders();
    List<PurchaseOrder> getOrdersByCustomer(Integer customerId);
    List<PurchaseOrder> getOrdersByProduct(Integer productId);
    List<PurchaseOrder> getOrdersByManufacturer(Integer manufacturerId);
    List<PurchaseOrder> getOrdersByCustomerProduct(Integer customerId, Integer productId);
    List<PurchaseOrder> getOrdersByProductCode(String productCode);
    List<PurchaseOrder> getOrdersByMicroMarket(String microMarketZipCode);
    List<PurchaseOrder> getOrdersByPeriod(LocalDate fromDate, LocalDate toDate);
    
    void saveOrder(PurchaseOrderInput purchaseOrder);
    PurchaseOrder getOrderByNum(Integer orderNum);
    void removeOrder(Integer orderNum);
}
