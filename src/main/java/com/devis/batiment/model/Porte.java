package com.devis.batiment.model;

import com.devis.batiment.model.enums.TypeOuverture;

/**
 * Porte : ouverture rectangulaire (largeur × hauteur).
 * Dessinée en VERT sur le plan.
 */
public class Porte extends Ouverture {

    private double largeur;
    private double hauteur;

    public Porte() {}

    public Porte(int idOuverture, double positionSurMur, double largeur, double hauteur) {
        super(idOuverture, positionSurMur);
        this.largeur = largeur;
        this.hauteur = hauteur;
    }

    @Override
    public double surface() { return largeur * hauteur; }

    @Override
    public TypeOuverture getTypeOuverture() { return TypeOuverture.PORTE; }

    /**
     * Retourne les coordonnées [x1,y1, x2,y2] de la porte sur le plan,
     * calculées depuis le coin début du mur selon la position et la largeur.
     */
    @Override
    public double[] getCoordonneesDessin(Mur mur) {
        Coin debut = mur.getDebut();
        Coin fin   = mur.getFin();
        double longueurMur = mur.longueur();
        if (longueurMur == 0) return new double[]{debut.getCx(), debut.getCy(),
                                                   debut.getCx(), debut.getCy()};
        double dx = (fin.getCx() - debut.getCx()) / longueurMur;
        double dy = (fin.getCy() - debut.getCy()) / longueurMur;

        double x1 = debut.getCx() + dx * getPositionSurMur();
        double y1 = debut.getCy() + dy * getPositionSurMur();
        double x2 = x1 + dx * largeur;
        double y2 = y1 + dy * largeur;
        return new double[]{x1, y1, x2, y2};
    }

    public double getLargeur()             { return largeur; }
    public void setLargeur(double largeur) { this.largeur = largeur; }

    public double getHauteur()             { return hauteur; }
    public void setHauteur(double hauteur) { this.hauteur = hauteur; }
}
