package br.com.paulopinheiro.sampledb.core.dto;

/**
 * Immutable payload for creating or updating a MicroMarket.
 */
public record MicroMarketInput(
    String zipCode,
    Double radius,
    Double areaLength,
    Double areaWidth
) {}
