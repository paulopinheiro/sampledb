package br.com.paulopinheiro.sampledb.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import br.com.paulopinheiro.sampledb.persistence.validator.SalesBeforeShipping;
import java.util.Objects;

@Entity
@Table(name = "purchase_order")
// Custom validation annotation applied directly to the type level
@SalesBeforeShipping(salesDateField = "salesDate", shippingDateField = "shippingDate")
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Column(name = "order_num", nullable = false)
    private Integer orderNum;

    @NotNull
    @Min(0)
    @Column(name = "quantity", nullable = false)
    private Short quantity = 0;

    @NotNull
    @Min(0)
    @Column(name = "shipping_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal shippingCost = BigDecimal.ZERO;

    @NotNull
    @Column(name = "sales_date", nullable = false)
    private LocalDate salesDate;

    @Column(name = "shipping_date")
    private LocalDate shippingDate;

    @Column(name = "freight_company")
    private String freightCompany;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", nullable = false)
    private Customer customer;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private Product product;

    public PurchaseOrder() {}

    public PurchaseOrder(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public PurchaseOrder(LocalDate salesDate, Short quantity) {
        this.salesDate = salesDate;
        this.quantity = quantity != null ? quantity : 0;
    }

    public PurchaseOrder(Short quantity, BigDecimal shippingCost, LocalDate salesDate, LocalDate shippingDate, 
                         String freightCompany, Customer customer, Product product) {
        this.quantity = quantity != null ? quantity : 0;
        this.shippingCost = shippingCost != null ? shippingCost : BigDecimal.ZERO;
        this.salesDate = salesDate;
        this.shippingDate = shippingDate;
        this.freightCompany = freightCompany;
        this.customer = customer;
        this.product = product;
    }

    // Transient Domain Computations

    @Transient
    public BigDecimal getSubTotalCost() {
        if (product == null) return BigDecimal.ZERO;
        return product.getSellingPriceWithDiscount().multiply(new BigDecimal(getQuantity()));
    }

    @Transient
    public BigDecimal getCustomerDiscount() {
        if (customer == null || customer.getDiscountCode() == null || customer.getDiscountCode().getRate() == null) {
            return BigDecimal.ZERO;
        }
        return getSubTotalCost().multiply(
                customer.getDiscountCode().getRate().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)
        );
    }

    @Transient
    public BigDecimal getSubTotalCostWithDiscount() {
        return getSubTotalCost().subtract(getCustomerDiscount());
    }

    @Transient
    public BigDecimal getTotalCost() {
        return getSubTotalCostWithDiscount().add(getShippingCost());
    }

    // Getters and Setters

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Short getQuantity() {
        return quantity;
    }

    public void setQuantity(Short quantity) {
        this.quantity = quantity != null ? quantity : 0;
    }

    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost != null ? shippingCost : BigDecimal.ZERO;
    }

    public LocalDate getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(LocalDate salesDate) {
        this.salesDate = salesDate;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(LocalDate shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getFreightCompany() {
        return freightCompany;
    }

    public void setFreightCompany(String freightCompany) {
        this.freightCompany = freightCompany;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNum);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;

        if (object instanceof PurchaseOrder other) {
            return Objects.equals(this.orderNum, other.getOrderNum());
        }
        return false;
    }

    @Override
    public String toString() {
        return "PurchaseOrder{orderNum=" + orderNum + ", salesDate=" + salesDate + "}";
    }
}
