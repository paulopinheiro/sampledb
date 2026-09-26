package br.com.paulopinheiro.sampledb.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "product_code")
public class ProductCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Size(min = 2, max = 2)
    @Column(name = "prod_code", length = 2, nullable = false)
    private String prodCode;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "discount_code", referencedColumnName = "discount_code", nullable = false)
    private DiscountCode discountCode;

    @Size(max = 10)
    @Column(name = "description", length = 10)
    private String description;

    public ProductCode() {}

    public ProductCode(String prodCode, DiscountCode discountCode) {
        this.prodCode = prodCode;
        this.discountCode = discountCode;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public DiscountCode getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(DiscountCode discountCode) {
        this.discountCode = discountCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(prodCode);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;

        if (object instanceof ProductCode other) {
            return Objects.equals(this.prodCode, other.getProdCode());
        }
        return false;
    }

    @Override
    public String toString() {
        return "ProductCode{code='" + prodCode + "', description='" + description + "'}";
    }
}
