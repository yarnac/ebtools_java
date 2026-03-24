package com.eb.histsample;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

// Tabelle LEBENSLAUFHE
public class LebenslaufHistEle {
    Long idLebenslaufHistEle;       //  DbSpalte ID_LEBENSLAUF_HE INTEGER (primary key)
    Long idLebenslauf;              //  DbSpalte ID_LEBENSLAUF INTEGER (foreign key auf
    TaetigkeitsArt taetigkeitsArt;  //  DbSpalte SL_TAETIGKEIT_ART (SMALLINT) Typ: Enum TaetigkeitsArt
    String taetigkeitsBezeichnung;  //  DbSpalte BEZ_ZAEZIGKEIT varchar(40)
    Date startDatum;                // DbSoalte DAT_START DATE
}

