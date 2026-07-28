package com.eb.base.extensions;

public class NumericExtensions {

    public static void main(String[] args) {
        System.out.println(ebRound(0.123456,3));
        System.out.println(ebRound(0.101,3));
    }

    /**
     * Rundet eine Double auf bestimmte Dezimalstellen
     */
    public static double ebRound(double self, int nr) {
        if (nr < 0) return self;
        double n = Math.pow(10, nr);
        int m = (int) (self * n + 0.9);
        return (double) m / n;
    }

    /**
     * Konvertiert Double zu String mit bestimmter Dezimalstellen
     */
    public static String ebToRoundedString(double zahl, int n) {
        if (n < 0) return String.valueOf(zahl);
        return String.format("%."+n+"f", zahl);
    }

    /**
     * Füllt Integer mit einem Zeichen auf bestimmte Länge
     */
    public static String ebFilledString(int n, int m, String str) {
        String res = String.valueOf(n);
        while (res.length() < m) {
            res = str.charAt(0) + res;
        }
        return res;
    }

    /**
     * Extrahiert Integer aus String
     */
    public static int ebIntValue(String self) {
        if (self == null) return -1;
        try {
            return Integer.parseInt(self);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Extrahiert Quadratwurzel
     */
    public static double sqrt(double p) {
        return Math.sqrt(p);
    }

    /**
     * Normal-Inverse Funktion (für statistische Berechnungen)
     */
    public static double normInv(double p) {
        return realNormInv(p / 100.0);
    }

    /**
     * Real-NormInv für statistische Berechnungen
     */
    private static double realNormInv(double p) {
        double[] a = {-3.969683028665376e+01, 2.209460984245205e+02,
                -2.759285104469687e+02, 1.383577518672690e+02,
                -3.066479806614716e+01, 2.506628277459239e+00};

        double[] b = {-5.447609879822406e+01, 1.615858368580409e+02,
                -1.556989798598866e+02, 6.680131188771972e+01,
                -1.328068155288572e+01};

        double[] c = {-7.784894002430293e-03, -3.223964580411365e-01,
                -2.400758277161838e+00, -2.549732539343734e+00,
                4.374664141464968e+00, 2.938163982698783e+00};

        double[] d = {7.784695709041462e-03, 3.224671290700398e-01,
                2.445134137142996e+00, 3.754408661907416e+00};

        double plow = 0.02425;
        double phigh = 1 - plow;

        if (p < plow) {
            double q = Math.sqrt(-2 * Math.log(p));
            return (((((c[0] * q + c[1]) * q + c[2]) * q + c[3]) * q + c[4]) * q + c[5]) /
                    ((((d[0] * q + d[1]) * q + d[2]) * q + d[3]) * q + 1);
        }

        if (phigh < p) {
            double q = Math.sqrt(-2 * Math.log(1 - p));
            return -(((((c[0] * q + c[1]) * q + c[2]) * q + c[3]) * q + c[4]) * q + c[5]) /
                    ((((d[0] * q + d[1]) * q + d[2]) * q + d[3]) * q + 1);
        }

        double q = p - 0.5;
        double r = q * q;
        return (((((a[0] * r + a[1]) * r + a[2]) * r + a[3]) * r + a[4]) * r + a[5]) * q /
               (((((b[0] * r + b[1]) * r + b[2]) * r + b[3]) * r + b[4]) * r + 1);
    }

}
