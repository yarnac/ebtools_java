package com.eb.reflection.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.IntSequenceGenerator.class,
        property = "@id"
)

@Getter
@Setter
@AllArgsConstructor
public class Unternehmen {
    private String name;
    private Address sitz;
    private List<Filiale> filialen;
    private List<Person> mitarbeiter;

    public Unternehmen() {}

    public void addMitarbeiter(Person person) {
        mitarbeiter.add(person);
    }

}


