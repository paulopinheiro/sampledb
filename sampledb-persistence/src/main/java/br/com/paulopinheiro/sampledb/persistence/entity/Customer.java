package br.com.paulopinheiro.sampledb.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Size(max = 30)
    @Column(name = "addressline1", length = 30, nullable = false)
    private String addressLine1;

    @Size(max = 30)
    @Column(name = "addressline2", length = 30)
    private String addressLine2;

    @Size(max = 25)
    @Column(name = "city", length = 25)
    private String city;

    @Size(max = 2)
    @Column(name = "state", length = 2)
    private String state;

    @Size(max = 12)
    @Column(name = "phone", length = 12)
    private String phone;

    @Size(max = 12)
    @Column(name = "fax", length = 12)
    private String fax;

    @Size(max = 40)
    @Pattern(
        regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
        message = "Invalid email format"
    )
    @Column(name = "email", length = 40)
    private String email;

    @NotNull
    @Min(0)
    @Column(name = "credit_limit", nullable = false)
    private Integer creditLimit = 0;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "discount_code", referencedColumnName = "discount_code", nullable = false)
    private DiscountCode discountCode;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "zip", referencedColumnName = "zip_code", nullable = false)
    private MicroMarket microMarket;

    public Customer() {}

    public Customer(Integer customerId) {this.customerId = customerId;}

    public Customer(Integer customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Integer creditLimit) {
        this.creditLimit = creditLimit != null ? creditLimit : 0;
    }

    public DiscountCode getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(DiscountCode discountCode) {
        this.discountCode = discountCode;
    }

    public MicroMarket getMicroMarket() {
        return microMarket;
    }

    public void setMicroMarket(MicroMarket microMarket) {
        this.microMarket = microMarket;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;

        if (object instanceof Customer other) {
            return Objects.equals(this.customerId, other.getCustomerId());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Customer{" + "customerId=" + customerId + ", name=" + name + ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2 + ", city=" + city + ", state=" + state + ", phone=" + phone + ", fax=" + fax + ", email=" + email + ", creditLimit=" + creditLimit + ", discountCode=" + discountCode + ", microMarket=" + microMarket + '}';
    }
}
