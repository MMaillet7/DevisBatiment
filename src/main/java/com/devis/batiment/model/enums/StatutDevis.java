package com.devis.batiment.model.enums;

/**
 * Statut du cycle de vie d'un devis.
 */
public enum StatutDevis {

    BROUILLON("Brouillon"),
    ENVOYE("Envoyé"),
    ACCEPTE("Accepté"),
    REFUSE("Refusé"),
    EXPIRE("Expiré");

    private final String libelle;

    StatutDevis(String libelle) { this.libelle = libelle; }

    public String getLibelle() { return libelle; }

    @Override
    public String toString() { return libelle; }
}
