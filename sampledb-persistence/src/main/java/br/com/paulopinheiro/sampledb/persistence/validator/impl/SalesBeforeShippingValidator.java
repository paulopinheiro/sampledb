package br.com.paulopinheiro.sampledb.persistence.validator.impl;

import br.com.paulopinheiro.sampledb.persistence.entity.PurchaseOrder;
import br.com.paulopinheiro.sampledb.persistence.validator.SalesBeforeShipping;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;

public class SalesBeforeShippingValidator implements ConstraintValidator<SalesBeforeShipping, Object> {
    
    private String salesDateField;
    private String shippingDateField;
    private String message;

    @Override
    public void initialize(SalesBeforeShipping constraintAnnotation) {
        this.salesDateField = constraintAnnotation.salesDateField();
        this.shippingDateField = constraintAnnotation.shippingDateField();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object valueObject, ConstraintValidatorContext context) {
        if (valueObject == null) return true; // High-performance native null check

        // Java 25 Pattern Matching for switch (Type Safety first, avoiding reflection when possible)
        LocalDate[] dates = switch (valueObject) {
            case PurchaseOrder order -> new LocalDate[]{ order.getSalesDate(), order.getShippingDate() };
            default -> extractDatesViaReflection(valueObject); // Fallback for other generic entities
        };

        LocalDate salesDate = dates[0];
        LocalDate shippingDate = dates[1];

        // If either date is missing, let @NotNull handles it, or it means it hasn't shipped yet (valid)
        if (salesDate == null || shippingDate == null) {
            return true;
        }

        boolean isValid = !shippingDate.isBefore(salesDate);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                   .addPropertyNode(shippingDateField) 
                   .addConstraintViolation();
        }

        return isValid;
    }

    /**
     * Fallback method using traditional reflection for generic reusability.
     */
    private LocalDate[] extractDatesViaReflection(Object target) {
        try {
            Field salesField = target.getClass().getDeclaredField(salesDateField);
            Field shippingField = target.getClass().getDeclaredField(shippingDateField);

            salesField.setAccessible(true);
            shippingField.setAccessible(true);

            return new LocalDate[]{
                (LocalDate) salesField.get(target),
                (LocalDate) shippingField.get(target)
            };
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error accessing date fields via reflection for verification", e);
        }
    }
}
