package br.com.paulopinheiro.sampledb.core.dto;

import java.math.BigDecimal;

/**
 * Immutable payload for creating or updating a DiscountCode.
 */
public record DiscountCodeInput(
    String discountCode,
    BigDecimal rate
) {}
