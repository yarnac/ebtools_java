package com.eb.inspector.domain.attributes;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Generischer Provider, der automatisch ein InspectMapping für beliebige Klassen erzeugt.
 * Nutzt Reflection, um alle Instanzfelder zu erkennen und entsprechende Getter-Funktionen bereitzustellen.
 */
public final class GenericsInspectAttributeProvider {

    /** Thread-sicherer Cache für schnelle Wiederholungsevaluierungen desselben Types */
    private static final ConcurrentHashMap<Class<?>, InspectMapping<Object>> CACHE = new ConcurrentHashMap<>();

    private GenericsInspectAttributeProvider() {
        // Utility-Klasse: keine Instanzen erstellen
    }

    /**
     * Erstellt ein InspectMapping für die gegebene Klasse.
     * Kann als Drop-in-Ersatz für {@link InspectAttributeProvider#getInspectMapping(Class)} verwendet werden.
     */
    public static InspectMapping<Object> getMapping(Class<?> clazz) {
        if (clazz == null || clazz.isInterface() || clazz.isArray()) {
            return null;
        }

        return CACHE.computeIfAbsent(clazz, GenericsInspectAttributeProvider::buildMapping);
    }

    @SuppressWarnings("unchecked")
    private static InspectMapping<Object> buildMapping(Class<?> clazz) {
        InspectMapping<Object> mapping = new InspectMapping<>(clazz);
        List<InspectMappingAttribute> attributes = new ArrayList<>();

        // Rekursiv durch die Klassenhierarchie laufen, um auch geerbte Felder zu erfassen
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();

            // Reflection-Felder freischalten (Java 8+ empfohlen über setAccessible(Array))
            try {
                AccessibleObject.setAccessible(fields, true);
            }
            catch (Exception e) {
                return new InspectMapping<>(clazz);
            }

            for (Field field : fields) {

                if (field.getType().getName().startsWith("java.lang")) {
                    continue;
                }
                // Nur Instanzfelder: Keine static, synthetic oder bridging-Kompiliererzeugnisse
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                    continue;
                }

                String fieldName = field.getName();

                // Sicherer Getter per Reflection mit graceful Error-Handling für UI-Rendering
                Function<Object, Object> getter = obj -> {
                    try {
                        return field.get(obj);
                    } catch (IllegalAccessException e) {
                        // Sollte bei erfolgreichem setAccessible(false) nicht passieren.
                        // Fallback: Feld als nicht verfügbar markieren.
                        return null;
                    }
                };

                attributes.add(new InspectMappingAttribute<>(fieldName, getter));
            }

            currentClass = currentClass.getSuperclass();
        }

        mapping.setAttributes(attributes);
        mapping.setToStringFunction(Object::toString);
        mapping.setGetSubObjectsFunction(x -> getComplexObjectsNotNull(x)); // Default: keine tiefen Objekte
        return mapping;
    }

    static HashSet<Object> set = new HashSet<>();

    @SuppressWarnings("unchecked")
    public static List<Object> getComplexObjectsNotNull(Object obj) {
        if (obj == null) return new ArrayList<>();



        List<Object> result = new ArrayList<>();
        Class<?> currentClass = obj.getClass();
        int i=0;
        // Traverse die Klassenhierarchie, um auch geerbte Felder zu erfassen
        while (currentClass != null && currentClass != Object.class && i++ < 3) {
            for (Field field : currentClass.getDeclaredFields()) {
                // Ignoriere statische Felder
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) continue;

                try {
                    // Java 21: setAccessible bypass Module-Protection für interne UI-Inspektionen
                    field.setAccessible(true);
                    Object value = field.get(obj);

                    // Nur nicht-null Werte extrahieren & ggf. automatisch aufsplitten
                    if (value != null && !set.contains(value)) {
                        set.add(value);
                        addNonNullSafe(value, result);
                    }

                } catch (IllegalAccessException ignored) {
                    // Fängt Sicherheitsrestrictions ab und verhindert UI-Crashes
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        return result;
    }

    /**
     * Hilfsfunktion für null-sicheres Hinzufügen & automatisches Flattend von Container-Typen.
     */
    @SuppressWarnings("unchecked")
    private static void addNonNullSafe(Object value, List<Object> target) {
        if (value == null) return;

        // Automatische Extraktion je nach Typ
        if (value instanceof Collection<?> coll) {
            for (Object item : coll) if (item != null) target.add(item);
        } else if (value instanceof Map<?, ?> map) {
            for (Object item : map.values()) if (item != null) target.add(item);
        } else if (value.getClass().isArray()) {
            int len = Array.getLength(value);
            for (int i = 0; i < len; i++) {
                Object item = Array.get(value, i);
                if (item != null) target.add(item);
            }
        } else {
            target.add(value);
        }
    }
}