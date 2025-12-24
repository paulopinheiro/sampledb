package br.com.paulopinheiro.sampledb.persistence.validator.impl;

import br.com.paulopinheiro.sampledb.persistence.validator.SalesBeforeShipping;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;

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
        if (Optional.ofNullable(valueObject).isEmpty()) return true;

        try {
            Field salesField = valueObject.getClass().getDeclaredField(salesDateField);
            Field shippingField = valueObject.getClass().getDeclaredField(shippingDateField);

            salesField.setAccessible(true);
            shippingField.setAccessible(true);

            LocalDate salesDate = (LocalDate) salesField.get(valueObject);
            LocalDate shippingDate = (LocalDate) shippingField.get(valueObject);

            if (Optional.ofNullable(salesDate).isEmpty()|| Optional.ofNullable(shippingDate).isEmpty()) return true;

            boolean isValid = !shippingDate.isBefore(salesDate);

            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                       .addPropertyNode(shippingDateField) 
                       .addConstraintViolation();
            }

            return isValid;
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException ex) {
            throw new RuntimeException("Error accessing date fields for validation", ex);
        }
    }
}
