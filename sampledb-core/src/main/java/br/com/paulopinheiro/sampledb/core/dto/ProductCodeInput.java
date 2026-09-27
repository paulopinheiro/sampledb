package br.com.paulopinheiro.sampledb.core.dto;

/**
 * Immutable payload for creating or updating a ProductCode.
 */
public record ProductCodeInput(
    String prodCode,
    String discountCode,
    String description) {}
