package com.devis.batiment.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utilitaires de formatage numérique pour l'affichage.
 */
public final class FormatUtil {

    private static final NumberFormat CURRENCY =
        NumberFormat.getCurrencyInstance(Locale.FRANCE);

    private FormatUtil() {}

    /** Formate un montant en euros (ex: "1 234,56 €"). */
    public static String euros(double montant) {
        return CURRENCY.format(montant);
    }

    /** Formate un double en mètres avec 2 décimales. */
    public static String metres(double valeur) {
        return String.format("%.2f m", valeur);
    }

    /** Formate une surface en m². */
    public static String surface(double valeur) {
        return String.format("%.2f m²", valeur);
    }

    /**
     * Parse un double depuis une chaîne, retourne defaultVal si invalide.
     */
    public static double parseDouble(String s, double defaultVal) {
        if (s == null || s.isBlank()) return defaultVal;
        try {
            return Double.parseDouble(s.replace(",", ".").trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    /**
     * Parse un int depuis une chaîne, retourne defaultVal si invalide.
     */
    public static int parseInt(String s, int defaultVal) {
        if (s == null || s.isBlank()) return defaultVal;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
}
