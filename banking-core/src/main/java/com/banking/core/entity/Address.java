package com.banking.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Embeddable address entity
 */
@Embeddable
public class Address {

    @NotBlank(message = "Street is required")
    @Column(name = "street", length = 100)
    private String street;

    @NotBlank(message = "City is required")
    @Column(name = "city", length = 50)
    private String city;

    @NotBlank(message = "State is required")
    @Column(name = "state", length = 50)
    private String state;

    @NotBlank(message = "Postal code is required")
    @Size(min = 5, max = 10, message = "Postal code must be between 5 and 10 characters")
    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @NotBlank(message = "Country is required")
    @Column(name = "country", length = 50)
    private String country;

    // Constructors
    public Address() {}

    public Address(String street, String city, String state, String postalCode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }

    // Getters and Setters
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return street + ", " + city + ", " + state + " " + postalCode + ", " + country;
    }
}
