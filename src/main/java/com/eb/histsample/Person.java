package com.eb.histsample;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Person {
    private Long id_person;     // DbSpalte ID_PERSON INTEGER (primary key)
    private String nachname;    // DbSpalte BEZ_NACHNAME VARCHAR(40)
    private String vorname;     // DbSpalte BEZ_VORNAME VARCHAR(40)
    Date geburtstag;            // DbSpalte DAT_GEBURTSTAG DATE
}
