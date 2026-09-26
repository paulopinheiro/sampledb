package br.com.paulopinheiro.sampledb.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "micro_market")
public class MicroMarket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "zip_code", length = 10, nullable = false)
    private String zipCode;

    @NotNull
    @Min(0)
    @Column(name = "radius", nullable = false)
    private Double radius = 0.0;

    @NotNull
    @Min(0)
    @Column(name = "area_length", nullable = false)
    private Double areaLength = 0.0;

    @NotNull
    @Min(0)
    @Column(name = "area_width", nullable = false)
    private Double areaWidth = 0.0;

    // Removed REMOVE cascade to prevent accidental deletion of customers
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "microMarket")
    private List<Customer> customers = new ArrayList<>(); // Defensive initialization

    public MicroMarket() {}

    public MicroMarket(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius != null ? radius : 0.0;
    }

    public Double getAreaLength() {
        return areaLength;
    }

    public void setAreaLength(Double areaLength) {
        this.areaLength = areaLength != null ? areaLength : 0.0;
    }

    public Double getAreaWidth() {
        return areaWidth;
    }

    public void setAreaWidth(Double areaWidth) {
        this.areaWidth = areaWidth != null ? areaWidth : 0.0;
    }

    public List<Customer> getCustomers() {
        // Return defensive copy or unmodifiable list if you want strict encapsulate
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers != null ? customers : new ArrayList<>();
    }

    /**
     * Bi-directional helper method to sync relationships safely.
     */
    public void addCustomer(Customer customer) {
        if (customer != null) {
            this.customers.add(customer);
            customer.setMicroMarket(this);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(zipCode);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;

        if (object instanceof MicroMarket other) {
            return Objects.equals(this.zipCode, other.getZipCode());
        }
        return false;
    }

    @Override
    public String toString() {
        return "MicroMarket{zipCode='" + zipCode + "'}";
    }
}
