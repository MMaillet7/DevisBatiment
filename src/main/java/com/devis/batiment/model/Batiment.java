package com.devis.batiment.model;

import com.devis.batiment.model.enums.TypeBatiment;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.function.Function;

/**
 * Classe mère abstraite pour les bâtiments.
 * Deux sous-types : Maison (pièces directement) et Immeuble (niveaux → appartements → pièces).
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "typeBatiment")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Maison.class,   name = "MAISON"),
    @JsonSubTypes.Type(value = Immeuble.class, name = "IMMEUBLE")
})
public abstract class Batiment {

    private String idBatiment;
    private String nom;

    public Batiment() {}

    public Batiment(String idBatiment, String nom) {
        this.idBatiment = idBatiment;
        this.nom        = nom;
    }

    /** Type du bâtiment (pour JSON et affichage). */
    public abstract TypeBatiment getTypeBatiment();

    /**
     * Coût total du bâtiment.
     * @param prixParId fonction : id revêtement catalogue → prix unitaire (€/m²).
     */
    public abstract double devisBatiment(Function<Integer, Double> prixParId);

    /** Nombre total de niveaux (1 pour une maison). */
    public abstract int getNombreNiveaux();

    public String getIdBatiment()                   { return idBatiment; }
    public void setIdBatiment(String idBatiment)    { this.idBatiment = idBatiment; }

    public String getNom()                          { return nom; }
    public void setNom(String nom)                  { this.nom = nom; }

    @Override
    public String toString() { return getTypeBatiment().getLibelle() + " [" + idBatiment + "] " + nom; }
}
