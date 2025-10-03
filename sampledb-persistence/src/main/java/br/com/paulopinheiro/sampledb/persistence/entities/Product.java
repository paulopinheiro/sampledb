package br.com.paulopinheiro.sampledb.persistence.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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
@Table(name = "PRODUCT")
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p")})
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    private Integer productId;
    @Basic(optional=false)
    private BigDecimal purchaseCost;
    @Basic(optional=false)
    private Integer quantityOnHand;
    @Basic(optional=false)
    private BigDecimal markup;
    @Basic(optional=false)
    private String available;
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private List<PurchaseOrder> purchaseOrderList;
    @JoinColumn(name = "MANUFACTURER_ID", referencedColumnName = "MANUFACTURER_ID")
    @ManyToOne(optional = false)
    private Manufacturer manufacturer;
    @JoinColumn(name = "PRODUCT_CODE", referencedColumnName = "PROD_CODE")
    @ManyToOne(optional = false)
    private ProductCode productCode;

    public Product() {}

    public Product(BigDecimal purchaseCost, Integer quantityOnHand, BigDecimal markup, String available, String description, List<PurchaseOrder> purchaseOrderList, Manufacturer manufacturer, ProductCode productCode) {
        this.purchaseCost = purchaseCost;
        this.quantityOnHand = quantityOnHand;
        this.markup = markup;
        this.available = available;
        this.description = description;
        this.purchaseOrderList = purchaseOrderList;
        this.manufacturer = manufacturer;
        this.productCode = productCode;
    }

    @Transient
    public BigDecimal getMarkupAmount() {
        return this.getPurchaseCost().multiply(this.getMarkup());
    }

    @Transient
    public BigDecimal getSellingPrice() {
        return this.getPurchaseCost().add(this.getMarkupAmount());
    }

    @Transient
    public BigDecimal getDiscountRate() {
        BigDecimal discountRate = new BigDecimal(0);

        if ((Optional.ofNullable(this.getProductCode()).isPresent())
         && (Optional.ofNullable(this.getProductCode().getDiscountCode()).isPresent())
         && (Optional.ofNullable(this.getProductCode().getDiscountCode().getRate()).isPresent()))
            discountRate = this.getProductCode().getDiscountCode().getRate();

        return discountRate;
    }

    @Transient
    public BigDecimal getDiscountAmount() {
        return this.getSellingPrice().multiply(this.getDiscountRate());
    }

    @Transient
    public BigDecimal getSellingPriceWithDiscount() {
        return this.getSellingPrice().subtract(this.getDiscountAmount());
    }

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
        this.purchaseCost = purchaseCost;
    }

    public Integer getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(Integer quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public BigDecimal getMarkup() {
        return markup;
    }

    public void setMarkup(BigDecimal markup) {
        this.markup = markup;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PurchaseOrder> getPurchaseOrderList() {
        return purchaseOrderList;
    }

    public void setPurchaseOrderList(List<PurchaseOrder> purchaseOrderList) {
        this.purchaseOrderList = purchaseOrderList;
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
        int hash = 0;
        hash += (productId != null ? productId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        return !((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId)));
    }

    @Override
    public String toString() {
        return description;
    }
}
