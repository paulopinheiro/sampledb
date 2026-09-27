package br.com.paulopinheiro.sampledb.core.dto;

public record CustomerInput (
    Integer customerId,
    String name,
    String addressLine1,
    String addressLine2,
    String city,
    String state,
    String phone,
    String fax,
    String email,
    Integer creditLimit,
    String discountCode,
    String zipCode
) {}
