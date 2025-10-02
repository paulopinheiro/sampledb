package br.com.paulopinheiro.sampledb.core.services;

import br.com.paulopinheiro.sampledb.persistence.entities.Customer;
import br.com.paulopinheiro.sampledb.persistence.entities.Product;
import br.com.paulopinheiro.sampledb.persistence.entities.PurchaseOrder;
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
