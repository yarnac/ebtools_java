package com.eb.reflection.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@JsonIdentityInfo(
        generator = ObjectIdGenerators.IntSequenceGenerator.class,
        property = "@id"
)

@Getter
@Setter
@AllArgsConstructor
public class Filiale {
    private String name;
    private Unternehmen unternehmen;
    private Address address;

    public Filiale() {}
}
