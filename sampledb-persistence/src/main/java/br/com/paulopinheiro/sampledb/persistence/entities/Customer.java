package br.com.paulopinheiro.sampledb.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;


@Entity
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="customer_id")
    private Integer customerId;
    @NotNull @Size(min=1, max=30)
    private String name;
    @NotNull @Size(min=1, max=30)
    private String addressLine1;
    @Size(max=30)
    private String addressLine2;
    @Size(max=25)
    private String city;
    @Size(min=2, max=2)
    private String state;
    @Size(max=12)
    private String phone;
    @Size(max=12)
    private String fax;
    @Size(max=40)
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")
    private String email;
    @Min(0)
    @Column(name="credit_limit")
    private Integer creditLimit;
    @JoinColumn(name = "discount_code", referencedColumnName = "discount_code")
    @ManyToOne(optional = false)
    private DiscountCode discountCode;
    @JoinColumn(name = "zip", referencedColumnName = "zip_code")
    @ManyToOne(optional = false)
    private MicroMarket microMarket;

    public Customer() {}

    public Integer getCustomerId() {return customerId;}
    public void setCustomerId(Integer customerId) {this.customerId = customerId;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getAddressLine1() {return addressLine1;}
    public void setAddressLine1(String addressLine1) {this.addressLine1 = addressLine1;}

    public String getAddressLine2() {return addressLine2;}
    public void setAddressLine2(String addressLine2) {this.addressLine2 = addressLine2;}

    public String getCity() {return city;}
    public void setCity(String city) {this.city = city;}

    public String getState() {return state;}
    public void setState(String state) {this.state = state;}

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}

    public String getFax() {return fax;}
    public void setFax(String fax) {this.fax = fax;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public Integer getCreditLimit() {return creditLimit;}
    public void setCreditLimit(Integer creditLimit) {this.creditLimit = creditLimit;}

    public DiscountCode getDiscountCode() {return discountCode;}
    public void setDiscountCode(DiscountCode discountCode) {this.discountCode = discountCode;}

    public MicroMarket getMicroMarket() {return microMarket;}
    public void setMicroMarket(MicroMarket microMarket) {this.microMarket = microMarket;}

    @Override
    public int hashCode() {return Objects.hash(customerId);}

    @Override
    public boolean equals(Object object) {
        if (this==object) return true;
        if (Optional.ofNullable(object).isEmpty()) return false;

        if (object instanceof Customer other)
            return Objects.equals(this.getCustomerId(), other.getCustomerId());

        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
