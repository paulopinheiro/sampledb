package br.com.paulopinheiro.sampledb.core.dto;

import java.math.BigDecimal;

public record ProductInput(
    Integer productId, // Null for new entries
    BigDecimal purchaseCost,
    Integer quantityOnHand,
    BigDecimal markup,
    Boolean available,
    String description,
    Integer manufacturerId,
    String prodCode
) {}