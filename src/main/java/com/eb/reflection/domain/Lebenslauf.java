package com.eb.reflection.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@JsonIdentityInfo(
        generator = ObjectIdGenerators.IntSequenceGenerator.class,
        property = "@id"
)

@Getter
@Setter
@AllArgsConstructor
public class Lebenslauf {

    public Lebenslauf() {}
    private List<Station> stationen;

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Lebenslauf [stationen=[");
        if (stationen != null) {
            for (Station station : stationen) {
                builder.append(station);
                builder.append("\r\n ");
            }
        }
        builder.append("]");
        return builder.toString();
    }


    public void addStation(final Station station) {
        stationen.add(station);
    }
}
