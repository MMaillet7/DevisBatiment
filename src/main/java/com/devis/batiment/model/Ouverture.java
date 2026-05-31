package com.devis.batiment.model;

import com.devis.batiment.model.enums.TypeOuverture;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Ouverture pratiquée dans un mur (porte, fenêtre, trémie).
 * La surface de l'ouverture est soustraite à la surface nette du mur.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "typeOuverture")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Porte.class,   name = "PORTE"),
    @JsonSubTypes.Type(value = Fenetre.class, name = "FENETRE"),
    @JsonSubTypes.Type(value = Tremie.class,  name = "TREMIE")
})
public abstract class Ouverture {

    private int idOuverture;

    /** Position sur le mur : distance depuis le Coin début (en mètres). */
    private double positionSurMur;

    public Ouverture() {}

    public Ouverture(int idOuverture, double positionSurMur) {
        this.idOuverture    = idOuverture;
        this.positionSurMur = positionSurMur;
    }

    /** Surface de l'ouverture (m²), à soustraire de la surface brute du mur. */
    public abstract double surface();

    /** Type de l'ouverture (pour JSON et affichage). */
    public abstract TypeOuverture getTypeOuverture();

    /** Coordonnées début de l'ouverture sur le plan (calculées depuis le mur). */
    public abstract double[] getCoordonneesDessin(Mur mur);

    public int getIdOuverture()               { return idOuverture; }
    public void setIdOuverture(int id)        { this.idOuverture = id; }

    public double getPositionSurMur()                   { return positionSurMur; }
    public void setPositionSurMur(double positionSurMur){ this.positionSurMur = positionSurMur; }
}
