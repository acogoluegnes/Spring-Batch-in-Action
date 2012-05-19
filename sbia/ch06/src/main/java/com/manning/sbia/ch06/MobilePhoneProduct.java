package com.manning.sbia.ch06;

/**
 * @author bazoud
 */
public class MobilePhoneProduct extends Product {
    private String manufacturer;

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }
}
