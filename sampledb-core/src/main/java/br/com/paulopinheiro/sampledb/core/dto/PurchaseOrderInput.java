package br.com.paulopinheiro.sampledb.core.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PurchaseOrderInput(
        Integer orderNum,
        Short quantity,
        BigDecimal shippingCost,
        LocalDate salesDate,
        LocalDate shippingDate,
        String freightCompany,
        Integer customerId,
        Integer productId
        ) {}
