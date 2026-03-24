package com.eb.histsample;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
// DbTabelle LEBENSLAUF
public class Lebenslauf {
    Long idLebenslauf;          // DbSpalte ID_LEBENSLAUF INTEGER (primary key)
    String gesendetAn;          // DbSpalte ID_PERSON INTEGER  (forein key auf PERSON)
    String gesendetGrund;       // DbSpalte BEZ_GRUNG VARCHAR(100)
    Date gesendetDatum;         // DbSpalte DAT_GESENDET DATE
}
