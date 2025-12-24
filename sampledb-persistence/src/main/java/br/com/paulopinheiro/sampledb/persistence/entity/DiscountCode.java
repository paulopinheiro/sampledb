package br.com.paulopinheiro.sampledb.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "discount_code")
public class DiscountCode implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Size(min=1, max=1)
    @Column(name="discount_code", length=1)
    private String discountCode;
    @Min(0)
    private BigDecimal rate;

    public DiscountCode() {}

    public String getDiscountCode() {return discountCode;}
    public void setDiscountCode(String discountCode) {this.discountCode = discountCode;}

    public BigDecimal getRate() {return rate;}
    public void setRate(BigDecimal rate) {this.rate = rate;}

    @Override
    public int hashCode() {return Objects.hash(discountCode);}

    @Override
    public boolean equals(Object object) {
        if (this==object) return true;

        if (Optional.ofNullable(object).isEmpty()) return false;

        if (object instanceof DiscountCode other)
            return Objects.equals(this.getDiscountCode(), other.getDiscountCode());

        return false;
    }

    @Override
    public String toString() {
        return discountCode;
    }
}
