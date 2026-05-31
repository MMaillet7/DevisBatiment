package com.devis.batiment.model;

import com.devis.batiment.model.enums.TypeBatiment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Maison : bâtiment à niveau unique.
 * Contient directement une liste de pièces et une hauteur de mur globale.
 */
public class Maison extends Batiment {

    /** Hauteur sous plafond en mètres. */
    private double hauteur;

    private List<Piece> pieces = new ArrayList<>();

    public Maison() {}

    public Maison(String idBatiment, String nom, double hauteur) {
        super(idBatiment, nom);
        this.hauteur = hauteur;
    }

    @Override
    public TypeBatiment getTypeBatiment() { return TypeBatiment.MAISON; }

    @Override
    public int getNombreNiveaux() { return 1; }

    @Override
    public double devisBatiment(Function<Integer, Double> prixParId) {
        double cout = 0;
        for (Piece p : pieces) {
            for (Mur m : p.getMurs()) {
                double pMurInt = m.getIdRevetementInterieur() != null ? prixParId.apply(m.getIdRevetementInterieur()) : 0;
                double pMurExt = m.getIdRevetementExterieur() != null ? prixParId.apply(m.getIdRevetementExterieur()) : 0;
                cout += m.coutInterieur(hauteur, pMurInt);
                cout += m.coutExterieur(hauteur, pMurExt);
            }
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

    public double getHauteur()             { return hauteur; }
    public void setHauteur(double hauteur) { this.hauteur = hauteur; }

    public List<Piece> getPieces()                { return pieces; }
    public void setPieces(List<Piece> pieces)     { this.pieces = pieces; }
}
