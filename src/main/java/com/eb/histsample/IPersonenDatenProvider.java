package com.eb.histsample;

import java.util.Date;
import java.util.List;

public interface IPersonenDatenProvider {
    List<Person> getPersonen();
    List<Lebenslauf> getLebenslaeufe(Long idPerson);
    Lebenslauf getLebenslauf(Long idLebenslauf);
    Lebenslauf getLebenslaufHistEle(Long idLebenslauf, Date datum);
}
