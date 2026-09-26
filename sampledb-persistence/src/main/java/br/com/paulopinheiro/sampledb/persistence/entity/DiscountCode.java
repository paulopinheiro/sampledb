package br.com.paulopinheiro.sampledb.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "discount_code")
public class DiscountCode implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "discount_code", length = 1, nullable = false)
    private String discountCode;

    @NotNull
    @Min(0)
    @Digits(integer = 2, fraction = 2) // Prevents database truncation issues
    @Column(name = "rate", nullable = false, precision = 4, scale = 2)
    private BigDecimal rate = BigDecimal.ZERO; // Safe default initialization (SRP-friendly)

    // Public no-arg constructor required by Jakarta Persistence
    public DiscountCode() {}

    // Convenience constructor for tests/seeders
    public DiscountCode(String discountCode, BigDecimal rate) {
        this.discountCode = discountCode;
        this.rate = rate != null ? rate : BigDecimal.ZERO;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public BigDecimal getRate() {
        return rate; // Clean getter, zero side-effects
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate != null ? rate : BigDecimal.ZERO;
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountCode);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false; // High-performance native null check

        if (object instanceof DiscountCode other) {
            return Objects.equals(this.discountCode, other.getDiscountCode());
        }
        return false;
    }

    @Override
    public String toString() {
        return "DiscountCode{code='" + discountCode + "', rate=" + rate + "}";
    }
}
