package com.banking.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a bank branch
 */
@Entity
@Table(name = "branches", indexes = {
        @Index(name = "idx_branch_ifsc", columnList = "ifsc", unique = true)
})
public class Branch extends BaseEntity {

    @NotBlank(message = "Branch name is required")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "IFSC code is required")
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Invalid IFSC code format")
    @Column(name = "ifsc", unique = true, nullable = false, length = 11)
    private String ifsc;

    @Embedded
    private Address address;

    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "contact", length = 15)
    private String contact;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "manager_name", length = 100)
    private String managerName;

    // One-to-many relationship with accounts
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();

    // Constructors
    public Branch() {}

    public Branch(String name, String ifsc, Address address) {
        this.name = name;
        this.ifsc = ifsc;
        this.address = address;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", ifsc='" + ifsc + '\'' +
                ", address=" + address +
                '}';
    }
}
