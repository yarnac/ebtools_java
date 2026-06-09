package com.eb.reflection;

import com.eb.histsample.IPersonenDatenProvider;
import com.eb.reflection.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;
import static java.lang.System.setOut;

public class Datenprovider {


    public static void main(String[] args)
    {
        new Datenprovider().initialize();
    }


    public List<Object> initialize()
    {
        List<Object> result = new ArrayList<>();

        Address erftstrasse = new Address("Köln", "Erftstrasse 15-17");
        Address regentenstrasse = new Address("Köln", "Regentenstrasse 53b");
        Unternehmen parcIT = new Unternehmen("parcIT", erftstrasse, new ArrayList<>(), new ArrayList<>());

        Address fehrsstrasse = new Address("Itzehoe", "Fehrsstraße 22");
        Address coriansberg = new Address("Itzehoe", "Voriansberg 3");
        Address kaiserstrasse = new Address("Kaiserstrasse", "Kaiserstrasse 1");


        Unternehmen fehrsschule = new Unternehmen("Fehrsschule", fehrsstrasse, new ArrayList<>(), new ArrayList<>());
        Unternehmen kks = new Unternehmen("Kaiser Karl Schule", coriansberg, new ArrayList<>(), new ArrayList<>());
        Unternehmen bundeswehr= new Unternehmen("parcIT", erftstrasse, new ArrayList<>(), new ArrayList<>());

        Unternehmen unikarlsruhe = new Unternehmen("Uni(TH) Karlsruhe", kaiserstrasse, new ArrayList<>(), new ArrayList<>());
        Person ekkart = new Person("Ekkart Bolten", 59, erftstrasse, parcIT, new Lebenslauf(new ArrayList<>()), new ArrayList<>());
        parcIT.addMitarbeiter(ekkart);

        Person markus = new Person("Markus Bolten", 59, erftstrasse, parcIT, new Lebenslauf(new ArrayList<>()), new ArrayList<>());
        parcIT.addMitarbeiter(markus);
        Person stefan = new Person("Stefan Bolten", 59, erftstrasse, parcIT, new Lebenslauf(new ArrayList<>()), new ArrayList<>());
        parcIT.addMitarbeiter(stefan);

        ekkart.addStation(new Station("August 1972", "Juli 1976", fehrsschule, "Grundschüler"));
        ekkart.addStation(new Station("August 1976", "Juli 1986", kks, "Gymnasiast"));
        ekkart.addStation(new Station("August 1986", "Juli 1998", unikarlsruhe, "Zeitsoldat"));
        ekkart.addStation(new Station("August 1998", "Juli 2026", parcIT, "Programmierer"));

        markus.addStation(new Station("August 1972", "Juli 1976", fehrsschule, "Grundschüler"));
        markus.addStation(new Station("August 1976", "Juli 1986", kks, "Gymnasiast"));
        markus.addStation(new Station("August 1986", "Juli 1998", unikarlsruhe, "Zeitsoldat"));
        markus.addStation(new Station("August 1998", "Juli 2026", parcIT, "Programmierer"));

        result.add(erftstrasse);
        result.add(regentenstrasse);
        result.add(fehrsstrasse);
        result.add(coriansberg);
        result.add(kaiserstrasse);
        result.add(fehrsstrasse);
        result.add(kks);
        result.add(bundeswehr);
        result.add(unikarlsruhe);
        result.add(ekkart);
        result.add(markus);
        result.add(stefan);
        result.add(parcIT);


        if (true)
            return result;







        ObjectMapper mapper = new ObjectMapper();

        try {
            String json = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(parcIT);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonNode tree = mapper.valueToTree(parcIT);

        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File("c:\\Data\\person.json"), ekkart);

            Person p2 = mapper.readValue(new File("c:\\Data\\person.json"), Person.class);

            System.out.println(p2.getName()); // Alice
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


// Beispiel: Zugriff auf Felder dynamisch
        tree.fieldNames().forEachRemaining(field -> {
            System.out.println(field + " -> " + tree.get(field));
        });

// Beispiel: Zugriff auf Felder dynamisch

        DiffNode diff = ObjectDifferBuilder.buildDefault()
                .compare(ekkart, markus);

        diff.visit((node, visit) -> {
            if (node.hasChanges()) {
                System.out.println(
                        node.getPath() + " changed: " +
                                node.canonicalGet(markus) + " -> " +
                                node.canonicalGet(ekkart)
                );
            }
        });

        tree.fieldNames().forEachRemaining(field -> {
            System.out.println(field + " -> " + tree.get(field));
        });

        return null;

    }
}
