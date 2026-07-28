package com.eb.base.extensions;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateExtensions {

    private static final DateTimeFormatter GERMAN_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter REVERSED_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * Gibt ein nur Datum zurück (ohne Zeit)
     */
    public static LocalDateTime ebPureDatetime(LocalDateTime self) {
        if (self == null) return null;
        return self.toLocalDate().atStartOfDay();
    }

    /**
     * Gibt ein nur Datum als LocalDate zurück (ohne Zeit)
     */
    public static LocalDate ebPureDate(LocalDateTime self) {
        if (self == null) return null;
        return self.toLocalDate();
    }

    /**
     * Gibt das Jahresultimo zurück (31.12. des Jahres)
     */
    public static LocalDate ebJahresultimo(LocalDateTime self) {
        if (self == null) return null;
        return LocalDate.of(self.getYear(), 12, 31);
    }

    /**
     * Prüft ob ein Datum Jahresultimo ist
     */
    public static boolean ebIsJahresultimo(LocalDateTime self) {
        if (self == null) return false;
        return ebPureDate(self).equals(ebJahresultimo(self));
    }

    /**
     * Gibt das Vorjahresultimo zurück
     */
    public static LocalDate ebVorjahresultimo(LocalDateTime self) {
        if (self == null) return null;
        return LocalDate.of(self.getYear() - 1, 12, 31);
    }

    /**
     * Gibt den Jahresanfang zurück (01.01. des Jahres)
     */
    public static LocalDate ebJahresanfang(LocalDateTime self) {
        if (self == null) return null;
        return LocalDate.of(self.getYear(), 1, 1);
    }

    /**
     * Gibt das Datum als reversed String zurück (yyyyMMddHHmmss)
     */
    public static String ebDateTimeStringRev(LocalDateTime self) {
        if (self == null) return "";
        return self.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * Gibt das Datum als reversed String zurück (yyyy_MM_dd)
     */
    public static String ebDateStringRev(LocalDateTime self) {
        if (self == null) return "";
        return self.format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
    }

    /**
     * Gibt die Differenz in Tagen zurück
     */
    public static int ebDiffInTagen(LocalDateTime self, LocalDateTime other) {
        if (self == null || other == null) return 0;
        return (int) Math.abs(ChronoUnit.DAYS.between(ebPureDate(self), ebPureDate(other)));
    }

    /**
     * Gibt die Differenz in Tagen zurück (absolut)
     */
    public static int ebDiffInTagenAbs(LocalDateTime self, LocalDateTime other) {
        return ebDiffInTagen(self, other);
    }

    /**
     * Gibt die Differenz in Sekunden zurück
     */
    public static long ebDiffInSekunden(LocalDateTime start, LocalDateTime stop) {
        if (start == null || stop == null) return 0;
        return ChronoUnit.SECONDS.between(start, stop);
    }

    /**
     * Gibt die Differenz in Monaten zurück
     */
    public static int ebDiffInMonaten(LocalDateTime self, LocalDateTime other) {
        if (self == null || other == null) return 0;
        return (int) ChronoUnit.MONTHS.between(ebPureDate(self), ebPureDate(other));
    }

    /**
     * Gibt die Differenz in Jahren zurück (double)
     */
    public static double ebDiffInJahren(LocalDateTime self, LocalDateTime other) {
        if (self == null || other == null) return 0;
        long days = ChronoUnit.DAYS.between(ebPureDate(self), ebPureDate(other));
        return Math.abs(days) / 365.25;
    }

    /**
     * Prüft ob Datumsangaben im selben Jahr liegen
     */
    public static boolean ebIsSameYear(LocalDateTime self, LocalDateTime other) {
        if (self == null || other == null) return false;
        return self.getYear() == other.getYear();
    }

    /**
     * Gibt Wochentag als deutsches Kürzel zurück
     */
    public static String ebWochentag(LocalDateTime self) {
        if (self == null) return "";
        return switch (self.getDayOfWeek()) {
            case MONDAY -> "Mo";
            case TUESDAY -> "Di";
            case WEDNESDAY -> "Mi";
            case THURSDAY -> "Do";
            case FRIDAY -> "Fr";
            case SATURDAY -> "Sa";
            case SUNDAY -> "So";
        };
    }

}
