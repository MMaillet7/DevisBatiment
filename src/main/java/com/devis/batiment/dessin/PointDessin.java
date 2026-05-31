package com.devis.batiment.dessin;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Un point dessinable (petit disque) sur un Canvas JavaFX.
 * Adaptée du projet dessinVectoriel (F. de Bertrand de Beuvron, INSA Strasbourg).
 */
public class PointDessin extends FigureSimple {

    public static final double RAYON = 4.0;

    private double px;
    private double py;

    public PointDessin(double px, double py, Color couleur) {
        super(couleur);
        this.px = px;
        this.py = py;
    }

    public PointDessin(double px, double py) {
        this(px, py, Color.BLACK);
    }

    public double getPx() { return px; }
    public double getPy() { return py; }
    public void setPx(double px) { this.px = px; }
    public void setPy(double py) { this.py = py; }

    @Override public double maxX() { return px; }
    @Override public double minX() { return px; }
    @Override public double maxY() { return py; }
    @Override public double minY() { return py; }

    @Override
    public void dessine(GraphicsContext ctx) {
        ctx.setFill(getCouleur());
        ctx.fillOval(px - RAYON, py - RAYON, 2 * RAYON, 2 * RAYON);
    }

    @Override
    public void deplace(double dx, double dy) {
        px += dx;
        py += dy;
    }

    @Override
    public PointDessin copie() {
        return new PointDessin(px, py, getCouleur());
    }

    public double distance(PointDessin autre) {
        double dx = this.px - autre.px;
        double dy = this.py - autre.py;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return "(" + px + "," + py + ")";
    }
}
