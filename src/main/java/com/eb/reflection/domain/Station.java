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
public class Station{
    private String von;
    private String bis;
    private Unternehmen unternehmen;
    private String position;

    public Station() {}

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(" von ");
        builder.append(von);
        builder.append(" bis ");
        builder.append(bis);
        builder.append(": ");
        builder.append(position);
        builder.append(" / ");
        builder.append(unternehmen.getName());
        return builder.toString();
    }

}