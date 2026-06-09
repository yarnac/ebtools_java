package com.eb.reflection.domain;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.IntSequenceGenerator.class,
        property = "@idPerson"
)


@Getter
@Setter
@AllArgsConstructor
public class Person {
    private String name;
    private int age;
    private Address address;
    private Unternehmen unternehmen;
    private Lebenslauf lebenslauf;
    private List<String> hobbies;

    public Person() {}


    public void addStation(Station station) {
        getLebenslauf().addStation(station);
    }
    // Konstruktoren, Getter, Setter
}