/*
 * Copyright parcIT GmbH
 *
 * Dieser Source-Code steht unter dem alleinigen Urheberschutz der parcIT GmbH.
 * Die Nutzung und Weitergabe ist nur mit ausdrücklicher Erlaubnis der parcIT GmbH gestattet.
 *
 */

package com.eb.inspector.domain.attributes;

/*
 * Basisklasse für ein Tupel Created on 15.02.2006 @author GPA
 */

/**
 *
 * @author ek
 * @see
 */
public class Tupel<A, B> {

    public static Tupel<Boolean, Boolean> of(boolean first, boolean second) {

        return new Tupel<>(Boolean.valueOf(first), Boolean.valueOf(second));
    }

    public static Tupel<Character, Character> of(char first, char second) {

        return new Tupel<>(Character.valueOf(first), Character.valueOf(second));
    }

    public static Tupel<Boolean, Boolean> of(boolean both) {

        return new Tupel<>(Boolean.valueOf(both), Boolean.valueOf(both));
    }

    public static Tupel<Character, Character> of(char both) {

        return new Tupel<>(Character.valueOf(both), Character.valueOf(both));
    }

    public static <F, S> Tupel<F, S> of(F first, S second) {

        return new Tupel<>(first, second);
    }

    public static Tupel<Integer, Integer> of(int both) {

        return new Tupel<>(Integer.valueOf(both), Integer.valueOf(both));
    }

    public static Tupel<Integer, Integer> of(int first, int second) {

        return new Tupel<>(Integer.valueOf(first), Integer.valueOf(second));
    }

    public static Tupel<Long, Long> of(long both) {

        return new Tupel<>(Long.valueOf(both), Long.valueOf(both));
    }

    public static Tupel<Long, Long> of(long first, long second) {

        return new Tupel<>(Long.valueOf(first), Long.valueOf(second));
    }

    private A first;

    private B second;

    public Tupel() {

        super();
    }

    /**
     * Bitte eine der passenden of-Methoden verwenden
     *
     * @param aFirst
     * @param aSecond
     */
    public Tupel(
            A aFirst,
            B aSecond) {

        super();

        first = aFirst;
        second = aSecond;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        Tupel other = (Tupel) obj;

        if (first == null) {
            if (other.first != null)
                return false;
        } else if (!first.equals(other.first))
            return false;

        if (second == null) {
            if (other.second != null)
                return false;
        } else if (!second.equals(other.second))
            return false;

        return true;
    }

    public A getFirst() {

        return first;
    }

    public B getSecond() {

        return second;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + (first == null ? 0 : first.hashCode());
        result = prime * result + (second == null ? 0 : second.hashCode());
        return result;
    }

    public void setFirst(A newFirst) {

        first = newFirst;
    }

    public void setSecond(B newSecond) {

        second = newSecond;
    }

    @Override
    public String toString() {

        return "Tupel [first=" + first + ", second=" + second + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
