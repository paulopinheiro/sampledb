package br.com.paulopinheiro.sampledb.persistence.entity;

import br.com.paulopinheiro.sampledb.persistence.converter.BooleanToStringConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
/**
 * 
 * @author paulopinheiro
 * Product entity.
 * 
 * Due to redundancy, create this trigger in the database
 * (example for Apache Derby)
 * 
 * CREATE TRIGGER update_product_availability
    AFTER UPDATE ON product
    REFERENCING NEW AS new
    FOR EACH ROW MODE DB2SQL
    UPDATE product SET available = 'FALSE'
    WHERE product_id = new.product_id
    AND   new.quantity_on_hand=0;
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @NotNull
    @Min(0)
    @Column(name = "purchase_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal purchaseCost = BigDecimal.ZERO;

    @NotNull
    @Min(0)
    @Column(name = "quantity_on_hand", nullable = false)
    private Integer quantityOnHand = 0;

    @NotNull
    @Min(0)
    @Column(name = "markup", nullable = false, precision = 4, scale = 2)
    private BigDecimal markup = BigDecimal.ZERO;

    @NotNull
    @Column(name = "available", nullable = false, length = 5)
    // Assuming your BooleanToStringConverter converts true to "TRUE" and false to "FALSE"
    @Convert(converter = BooleanToStringConverter.class)
    private Boolean available = Boolean.TRUE;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "description", length = 50, nullable = false)
    private String description;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "manufacturer_id", referencedColumnName = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_code", referencedColumnName = "prod_code", nullable = false)
    private ProductCode productCode;

    public Product() {}

    // Business Logic Rules (Transient Domain Computations)

    @Transient
    public BigDecimal getMarkupAmount() {
        return this.getPurchaseCost()
                .multiply(this.getMarkup().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
    }

    @Transient
    public BigDecimal getSellingPrice() {
        return this.getPurchaseCost().add(this.getMarkupAmount());
    }

    @Transient
    public BigDecimal getDiscountRate() {
        // Law of Demeter fix: Safe navigation without deeply nested Optionals
        if (productCode != null && productCode.getDiscountCode() != null && productCode.getDiscountCode().getRate() != null) {
            return productCode.getDiscountCode().getRate();
        }
        return BigDecimal.ZERO;
    }

    @Transient
    public BigDecimal getDiscountAmount() {
        return this.getSellingPrice()
                .multiply(this.getDiscountRate().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
    }

    @Transient
    public BigDecimal getSellingPriceWithDiscount() {
        return this.getSellingPrice().subtract(this.getDiscountAmount());
    }

    // Getters and Setters

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public BigDecimal getPurchaseCost() {
        return purchaseCost;
    }

    public void setPurchaseCost(BigDecimal purchaseCost) {
        this.purchaseCost = purchaseCost != null ? purchaseCost : BigDecimal.ZERO;
    }

    public Integer getQuantityOnHand() {
        return quantityOnHand;
    }

    /**
     * Replaces the Database Trigger rule natively inside the domain model (SRP/SOLID)
     */
    public void setQuantityOnHand(Integer quantityOnHand) {
        this.quantityOnHand = quantityOnHand != null ? quantityOnHand : 0;
        if (this.quantityOnHand == 0) {
            this.available = Boolean.FALSE; // Replaces DB Trigger safely in memory
        }
    }

    public BigDecimal getMarkup() {
        return markup;
    }

    public void setMarkup(BigDecimal markup) {
        this.markup = markup;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available != null ? available : Boolean.FALSE;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public ProductCode getProductCode() {
        return productCode;
    }

    public void setProductCode(ProductCode productCode) {
        this.productCode = productCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;

        if (object instanceof Product other) {
            return Objects.equals(this.productId, other.getProductId());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Product{id=" + productId + ", description='" + description + "', available=" + available + "}";
    }
}