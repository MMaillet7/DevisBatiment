package com.devis.batiment.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Mur délimité par deux Coin (début/fin), avec :
 * - une liste d'ouvertures (portes, fenêtres, trémies)
 * - un revêtement intérieur et un revêtement extérieur (référencés par id catalogue)
 * La hauteur du mur est portée par le Niveau (ou la Maison pour les maisons).
 */
public class Mur {

    private int idMur;
    private Coin debut;
    private Coin fin;
    private List<Ouverture> ouvertures = new ArrayList<>();

    /** Id du revêtement intérieur dans le catalogue (null = aucun). */
    private Integer idRevetementInterieur;

    /** Id du revêtement extérieur dans le catalogue (null = aucun). */
    private Integer idRevetementExterieur;

    public Mur() {}

    public Mur(int idMur, Coin debut, Coin fin) {
        this.idMur = idMur;
        this.debut = debut;
        this.fin   = fin;
    }

    // ── Calculs géométriques ────────────────────────────────────────────────────

    /** Longueur du mur en mètres (distance euclidienne entre les deux coins). */
    public double longueur() {
        return Math.sqrt(
            Math.pow(debut.getCx() - fin.getCx(), 2) +
            Math.pow(debut.getCy() - fin.getCy(), 2)
        );
    }

    /**
     * Surface brute d'un côté du mur (longueur × hauteur).
     * @param hauteur hauteur du mur en mètres (fournie par l'étage).
     */
    public double surfaceBrute(double hauteur) {
        return longueur() * hauteur;
    }

    /**
     * Surface nette d'un côté du mur = surface brute − somme des surfaces d'ouvertures.
     * @param hauteur hauteur du mur en mètres.
     */
    public double surfaceNette(double hauteur) {
        double totalOuvertures = ouvertures.stream()
            .mapToDouble(Ouverture::surface)
            .sum();
        return Math.max(0, surfaceBrute(hauteur) - totalOuvertures);
    }

    /**
     * Coût du revêtement intérieur (prix unitaire × surface nette).
     * @param hauteur   hauteur en mètres.
     * @param prixInterieur prix unitaire du revêtement intérieur (€/m²).
     */
    public double coutInterieur(double hauteur, double prixInterieur) {
        return surfaceNette(hauteur) * prixInterieur;
    }

    /**
     * Coût du revêtement extérieur (prix unitaire × surface nette).
     * @param hauteur   hauteur en mètres.
     * @param prixExterieur prix unitaire du revêtement extérieur (€/m²).
     */
    public double coutExterieur(double hauteur, double prixExterieur) {
        return surfaceNette(hauteur) * prixExterieur;
    }

    // ── Gestion des ouvertures ──────────────────────────────────────────────────

    public void ajouterOuverture(Ouverture o)    { ouvertures.add(o); }
    public void supprimerOuverture(Ouverture o)  { ouvertures.remove(o); }

    // ── Getters / Setters ───────────────────────────────────────────────────────

    public int getIdMur()              { return idMur; }
    public void setIdMur(int idMur)    { this.idMur = idMur; }

    public Coin getDebut()             { return debut; }
    public void setDebut(Coin debut)   { this.debut = debut; }

    public Coin getFin()               { return fin; }
    public void setFin(Coin fin)       { this.fin = fin; }

    public List<Ouverture> getOuvertures()                  { return ouvertures; }
    public void setOuvertures(List<Ouverture> ouvertures)   { this.ouvertures = ouvertures; }

    public Integer getIdRevetementInterieur()                         { return idRevetementInterieur; }
    public void setIdRevetementInterieur(Integer idRevetementInterieur){ this.idRevetementInterieur = idRevetementInterieur; }

    public Integer getIdRevetementExterieur()                         { return idRevetementExterieur; }
    public void setIdRevetementExterieur(Integer idRevetementExterieur){ this.idRevetementExterieur = idRevetementExterieur; }

    @Override
    public String toString() { return "Mur[" + debut + ";" + fin + "]"; }
}
