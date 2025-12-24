package br.com.paulopinheiro.sampledb.core.service;

import br.com.paulopinheiro.sampledb.persistence.entity.Customer;
import br.com.paulopinheiro.sampledb.persistence.entity.Product;
import br.com.paulopinheiro.sampledb.persistence.entity.PurchaseOrder;
import java.math.BigDecimal;

public interface PurchaseStatisticsService {
    BigDecimal getTotalBilling();
    BigDecimal getAverageValue();
    PurchaseOrder getHighestValuePurchaseOrder();
    PurchaseOrder getLowestValuePurchaseOrder();
    Integer getPurchaseOrdersCount();
    Product getBestSeller();
    Customer getBestBuyer();
}
