package com.devis.batiment.dessin;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Segment entre deux PointDessin, dessinable sur un Canvas JavaFX.
 * L'epaisseur est exprimee en unites modele et doit etre coherente avec
 * l'echelle utilisee dans PlanCanvas (ex. 0.05 m pour un mur).
 * Adaptee du projet dessinVectoriel (F. de Bertrand de Beuvron, INSA Strasbourg).
 */
public class Segment extends FigureSimple {

    /** Epaisseur du trait en unites modele (metres). */
    private double epaisseur;

    private PointDessin debut;
    private PointDessin fin;

    public Segment(PointDessin debut, PointDessin fin, Color couleur, double epaisseur) {
        super(couleur);
        this.debut     = debut;
        this.fin       = fin;
        this.epaisseur = epaisseur;
    }

    public Segment(PointDessin debut, PointDessin fin, Color couleur) {
        this(debut, fin, couleur, 0.08);
    }

    public Segment(PointDessin debut, PointDessin fin) {
        this(debut, fin, Color.BLACK, 0.08);
    }

    public PointDessin getDebut() { return debut; }
    public PointDessin getFin()   { return fin; }
    public void setDebut(PointDessin debut) { this.debut = debut; }
    public void setFin(PointDessin fin)     { this.fin = fin; }
    public double getEpaisseur()            { return epaisseur; }
    public void setEpaisseur(double e)      { this.epaisseur = e; }

    @Override public double maxX() { return Math.max(debut.getPx(), fin.getPx()); }
    @Override public double minX() { return Math.min(debut.getPx(), fin.getPx()); }
    @Override public double maxY() { return Math.max(debut.getPy(), fin.getPy()); }
    @Override public double minY() { return Math.min(debut.getPy(), fin.getPy()); }

    @Override
    public void dessine(GraphicsContext ctx) {
        ctx.setStroke(getCouleur());
        ctx.setLineWidth(epaisseur);
        ctx.strokeLine(debut.getPx(), debut.getPy(), fin.getPx(), fin.getPy());
    }

    @Override
    public void deplace(double dx, double dy) {
        debut.deplace(dx, dy);
        fin.deplace(dx, dy);
    }

    @Override
    public Segment copie() {
        return new Segment(debut.copie(), fin.copie(), getCouleur(), epaisseur);
    }

    public double longueur() { return debut.distance(fin); }

    @Override
    public String toString() { return "[" + debut + ";" + fin + "]"; }
}
