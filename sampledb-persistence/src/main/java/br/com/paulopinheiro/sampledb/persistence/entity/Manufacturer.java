package br.com.paulopinheiro.sampledb.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "manufacturer")
public class Manufacturer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Column(name = "manufacturer_id", nullable = false)
    private Integer manufacturerId;

    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "addressline1", length = 30, nullable = false)
    private String addressLine1;

    @Size(min = 1, max = 30)
    @Column(name = "addressline2", length = 30)
    private String addressLine2;

    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "city", length = 25, nullable = false)
    private String city;

    @NotNull
    @Size(min = 2, max = 2)
    @Column(name = "state", length = 2, nullable = false)
    private String state;

    @Size(max = 10)
    @Column(name = "zip", length = 10)
    private String zip;

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

    @Size(max = 30)
    @Column(name = "rep", length = 30)
    private String rep;

    public Manufacturer() {}

    public Manufacturer(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public Manufacturer(Integer manufacturerId, String name) {
        this.manufacturerId = manufacturerId;
        this.name = name;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
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

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }

    @Override
    public int hashCode() {
        return Objects.hash(manufacturerId);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;

        if (object instanceof Manufacturer other) {
            return Objects.equals(this.manufacturerId, other.getManufacturerId());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Manufacturer{id=" + manufacturerId + ", name='" + name + "', rep='" + rep + "'}";
    }
}
