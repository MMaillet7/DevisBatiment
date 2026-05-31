package com.devis.batiment.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Pièce composée de murs, d'un sol et d'un plafond.
 * Les coins sont utilisés uniquement pour calculer la surface au sol (shoelace).
 */
public class Piece {

    private int idPiece;
    private String nom;
    private List<Mur> murs = new ArrayList<>();

    /** Id du revêtement de sol dans le catalogue (null = aucun). */
    private Integer idRevetementSol;

    /** Id du revêtement de plafond dans le catalogue (null = aucun). */
    private Integer idRevetementPlafond;

    public Piece() {}

    public Piece(int idPiece, String nom) {
        this.idPiece = idPiece;
        this.nom     = nom;
    }

    // ── Calculs géométriques ────────────────────────────────────────────────────

    /**
     * Surface au sol de la pièce calculée par la formule du lacet (Shoelace).
     * Les coins sont extraits dans l'ordre des murs.
     */
    public double surface() {
        List<Coin> coins = extraireCoins();
        int n = coins.size();
        if (n < 3) return 0;
        double somme = 0;
        for (int i = 0; i < n; i++) {
            Coin c1 = coins.get(i);
            Coin c2 = coins.get((i + 1) % n);
            somme += c1.getCx() * c2.getCy() - c2.getCx() * c1.getCy();
        }
        return Math.abs(somme) / 2.0;
    }

    /**
     * Extrait les coins dans l'ordre des murs (début du mur i = début du mur suivant).
     */
    private List<Coin> extraireCoins() {
        List<Coin> coins = new ArrayList<>();
        for (Mur m : murs) {
            coins.add(m.getDebut());
        }
        return coins;
    }

    /**
     * Coût total de la pièce = murs intérieurs + sol + plafond.
     *
     * @param hauteur           hauteur des murs en mètres.
     * @param prixMursInt       prix unitaire revêtement intérieur des murs (€/m²), 0 si aucun.
     * @param prixMursExt       prix unitaire revêtement extérieur des murs (€/m²), 0 si aucun.
     * @param prixSol           prix unitaire revêtement sol (€/m²), 0 si aucun.
     * @param prixPlafond       prix unitaire revêtement plafond (€/m²), 0 si aucun.
     */
    public double coutTotal(double hauteur,
                            double prixMursInt, double prixMursExt,
                            double prixSol, double prixPlafond) {
        double cout = 0;
        for (Mur m : murs) {
            cout += m.coutInterieur(hauteur, prixMursInt);
            cout += m.coutExterieur(hauteur, prixMursExt);
        }
        cout += surface() * prixSol;
        cout += surface() * prixPlafond;
        return cout;
    }

    // ── Gestion des murs ────────────────────────────────────────────────────────

    public void ajouterMur(Mur m)    { murs.add(m); }
    public void supprimerMur(Mur m)  { murs.remove(m); }

    // ── Getters / Setters ───────────────────────────────────────────────────────

    public int getIdPiece()              { return idPiece; }
    public void setIdPiece(int idPiece)  { this.idPiece = idPiece; }

    public String getNom()               { return nom; }
    public void setNom(String nom)       { this.nom = nom; }

    public List<Mur> getMurs()                   { return murs; }
    public void setMurs(List<Mur> murs)          { this.murs = murs; }

    public Integer getIdRevetementSol()                       { return idRevetementSol; }
    public void setIdRevetementSol(Integer idRevetementSol)   { this.idRevetementSol = idRevetementSol; }

    public Integer getIdRevetementPlafond()                       { return idRevetementPlafond; }
    public void setIdRevetementPlafond(Integer idRevetementPlafond){ this.idRevetementPlafond = idRevetementPlafond; }

    @Override
    public String toString() { return "Pièce[" + idPiece + "] " + nom; }
}
