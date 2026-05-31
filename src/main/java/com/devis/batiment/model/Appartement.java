package com.devis.batiment.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Appartement appartenant à un Niveau.
 * Regroupe plusieurs pièces.
 */
public class Appartement {

    private int idAppart;
    private String nom;
    private List<Piece> pieces = new ArrayList<>();

    public Appartement() {}

    public Appartement(int idAppart, String nom) {
        this.idAppart = idAppart;
        this.nom      = nom;
    }

    // ── Calculs ─────────────────────────────────────────────────────────────────

    /** Surface totale de l'appartement (somme des surfaces des pièces). */
    public double surface() {
        return pieces.stream().mapToDouble(Piece::surface).sum();
    }

    /**
     * Coût total de l'appartement.
     * @param hauteur     hauteur des murs (fournie par le Niveau parent).
     * @param prixParId   fonction : id revêtement catalogue → prix unitaire (€/m²).
     */
    public double devisAppartement(double hauteur, Function<Integer, Double> prixParId) {
        double cout = 0;
        for (Piece p : pieces) {
            // Revêtements de chaque mur (intérieur + extérieur)
            for (Mur m : p.getMurs()) {
                double pMurInt = m.getIdRevetementInterieur() != null ? prixParId.apply(m.getIdRevetementInterieur()) : 0;
                double pMurExt = m.getIdRevetementExterieur() != null ? prixParId.apply(m.getIdRevetementExterieur()) : 0;
                cout += m.coutInterieur(hauteur, pMurInt);
                cout += m.coutExterieur(hauteur, pMurExt);
            }
            // Revêtements sol et plafond
            double prixSol     = p.getIdRevetementSol()     != null ? prixParId.apply(p.getIdRevetementSol())     : 0;
            double prixPlafond = p.getIdRevetementPlafond() != null ? prixParId.apply(p.getIdRevetementPlafond()) : 0;
            cout += p.surface() * prixSol;
            cout += p.surface() * prixPlafond;
        }
        return cout;
    }

    // ── Gestion des pièces ───────────────────────────────────────────────────────

    public void ajouterPiece(Piece p)    { pieces.add(p); }
    public void supprimerPiece(Piece p)  { pieces.remove(p); }

    // ── Getters / Setters ───────────────────────────────────────────────────────

    public int getIdAppart()               { return idAppart; }
    public void setIdAppart(int idAppart)  { this.idAppart = idAppart; }

    public String getNom()                 { return nom; }
    public void setNom(String nom)         { this.nom = nom; }

    public List<Piece> getPieces()                { return pieces; }
    public void setPieces(List<Piece> pieces)     { this.pieces = pieces; }

    @Override
    public String toString() { return "Appartement[" + idAppart + "] " + nom; }
}
