package br.com.paulopinheiro.sampledb.persistence.validator;

import br.com.paulopinheiro.sampledb.persistence.validator.impl.SalesBeforeShippingValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE }) // <-- Applied to the Class level
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SalesBeforeShippingValidator.class)
@Documented
public @interface SalesBeforeShipping {
    String message() default "Shipping date must be equal or after sale date";
    String salesDateField();
    String shippingDateField();

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
