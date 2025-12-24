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
import java.util.Optional;

@Entity
@Table(name = "product_code")
public class ProductCode implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="prod_code")
    @Size(min=2, max=2)
    private String prodCode;
    @NotNull
    @JoinColumn(name = "discount_code", referencedColumnName = "discount_code")
    @ManyToOne(optional = false)
    private DiscountCode discountCode;
    @Size(max=10)
    private String description;

    public ProductCode() {}

    public String getProdCode() {return prodCode;}
    public void setProdCode(String prodCode) {this.prodCode = prodCode;}

    public DiscountCode getDiscountCode() {return discountCode;}
    public void setDiscountCode(DiscountCode discountCode) {this.discountCode = discountCode;}
    
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    @Override
    public int hashCode() {return Objects.hash(prodCode);}

    @Override
    public boolean equals(Object object) {
        if (this==object) return true;
        if (Optional.ofNullable(object).isEmpty()) return false;

        if (object instanceof ProductCode other)
            return Objects.equals(this.getProdCode(), other.getProdCode());

        return false;
    }

    @Override
    public String toString() {
        return description;
    }
}
