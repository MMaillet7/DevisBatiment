package com.devis.batiment.model.enums;

/**
 * Type d'ouverture dans un mur.
 */
public enum TypeOuverture {
    PORTE("Porte"),
    FENETRE("Fenêtre"),
    TREMIE("Trémie");

    private final String libelle;

    TypeOuverture(String libelle) { this.libelle = libelle; }

    public String getLibelle() { return libelle; }

    @Override
    public String toString() { return libelle; }
}
