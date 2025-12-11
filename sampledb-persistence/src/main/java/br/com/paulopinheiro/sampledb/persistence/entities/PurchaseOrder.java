package br.com.paulopinheiro.sampledb.persistence.entities;

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
import java.time.LocalDate;

@Entity
@Table(name = "purchase_order")
public class PurchaseOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="order_num")
    private Integer orderNum;
    @NotNull @Min(0)
    private Short quantity;
    @Min(0)
    @Column(name="shipping_cost")
    private BigDecimal shippingCost;
    @NotNull
    @Column(name="sales_date")
    private LocalDate salesDate;
    @Column(name="shipping_date")
    private LocalDate shippingDate;
    @Column(name="freight_company")
    private String freightCompany;
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    @ManyToOne(optional = false)
    private Customer customer;
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne(optional = false)
    private Product product;

    public PurchaseOrder() {}

    public PurchaseOrder(Short quantity, BigDecimal shippingCost, LocalDate salesDate, LocalDate shippingDate, String freightCompany, Customer customer, Product product) {
        this.quantity = quantity;
        this.shippingCost = shippingCost;
        this.salesDate = salesDate;
        this.shippingDate = shippingDate;
        this.freightCompany = freightCompany;
        this.customer = customer;
        this.product = product;
    }

    @Transient
    public BigDecimal getTotalSaleCost() {
        BigDecimal saleCost, customerDiscount;

        saleCost = this.getProduct().getSellingPriceWithDiscount().multiply(new BigDecimal(this.getQuantity()));

        customerDiscount = saleCost.multiply(this.getCustomer().getDiscountCode().getRate());

        return saleCost.subtract(customerDiscount);
    }

    @Transient
    public BigDecimal getTotalCost() {
        return this.getTotalSaleCost().add(this.getShippingCost());
    }

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
        this.quantity = quantity;
    }

    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
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
        int hash = 0;
        hash += (orderNum != null ? orderNum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PurchaseOrder)) {
            return false;
        }
        PurchaseOrder other = (PurchaseOrder) object;
        return !((this.orderNum == null && other.orderNum != null) || (this.orderNum != null && !this.orderNum.equals(other.orderNum)));
    }

    @Override
    public String toString() {
        return orderNum + " (" + customer + ")";
    }
}
