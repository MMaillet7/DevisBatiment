package com.devis.batiment.model.enums;

/**
 * Distingue les deux types de bâtiment supportés.
 */
public enum TypeBatiment {
    MAISON("Maison"),
    IMMEUBLE("Immeuble");

    private final String libelle;

    TypeBatiment(String libelle) { this.libelle = libelle; }

    public String getLibelle() { return libelle; }

    @Override
    public String toString() { return libelle; }
}
