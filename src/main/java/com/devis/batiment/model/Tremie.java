package com.devis.batiment.model;

import com.devis.batiment.model.enums.TypeOuverture;

/**
 * Trémie : ouverture circulaire (rayon).
 * Dessinée comme un segment noir pointillé sur le plan.
 */
public class Tremie extends Ouverture {

    private double rayon;

    public Tremie() {}

    public Tremie(int idOuverture, double positionSurMur, double rayon) {
        super(idOuverture, positionSurMur);
        this.rayon = rayon;
    }

    @Override
    public double surface() { return Math.PI * rayon * rayon; }

    @Override
    public TypeOuverture getTypeOuverture() { return TypeOuverture.TREMIE; }

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
        double x2 = x1 + dx * (2 * rayon);
        double y2 = y1 + dy * (2 * rayon);
        return new double[]{x1, y1, x2, y2};
    }

    public double getRayon()            { return rayon; }
    public void setRayon(double rayon)  { this.rayon = rayon; }
}
