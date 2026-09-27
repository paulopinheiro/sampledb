package br.com.paulopinheiro.sampledb.core.dto;

/**
 * Immutable payload for creating or updating a Manufacturer.
 */
public record ManufacturerInput(
        Integer manufacturerId, // Will be null for new records
        String name,
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String zip,
        String phone,
        String fax,
        String email,
        String rep
) {}
