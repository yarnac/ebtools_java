package com.eb.reflection.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Address {

    public Address() {}
    private String city;
    private String street;

    @Override
    public String toString() {
        return "" + street + " " + city;
    }

    // Konstruktoren, Getter, Setter
}
