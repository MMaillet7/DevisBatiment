package com.devis.batiment.dessin;

import javafx.scene.transform.Transform;

/**
 * Rectangle à côtés horizontaux/verticaux représentant la portion de scène affichée.
 * Fournit la transformation modèle → vue (zoom + translation).
 * Adaptée du projet dessinVectoriel (F. de Bertrand de Beuvron, INSA Strasbourg).
 */
public class RectangleHV {

    private double xMin, xMax, yMin, yMax;

    public RectangleHV(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    // ── Transformations ────────────────────────────────────────────────────────

    /** Même centre, côtés multipliés par facteur. */
    public RectangleHV scale(double facteur) {
        double dx = xMax - xMin;
        double dy = yMax - yMin;
        double cx = (xMax + xMin) / 2;
        double cy = (yMax + yMin) / 2;
        return new RectangleHV(
            cx - (dx / 2) * facteur,
            cx + (dx / 2) * facteur,
            cy - (dy / 2) * facteur,
            cy + (dy / 2) * facteur
        );
    }

    public RectangleHV translateGauche(double portionConservee) {
        double dx = (xMax - xMin) * (1 - portionConservee);
        return new RectangleHV(xMin - dx, xMax - dx, yMin, yMax);
    }

    public RectangleHV translateDroite(double portionConservee) {
        double dx = (xMax - xMin) * (1 - portionConservee);
        return new RectangleHV(xMin + dx, xMax + dx, yMin, yMax);
    }

    public RectangleHV translateHaut(double portionConservee) {
        double dy = (yMax - yMin) * (1 - portionConservee);
        return new RectangleHV(xMin, xMax, yMin - dy, yMax - dy);
    }

    public RectangleHV translateBas(double portionConservee) {
        double dy = (yMax - yMin) * (1 - portionConservee);
        return new RectangleHV(xMin, xMax, yMin + dy, yMax + dy);
    }

    /**
     * Calcule la transformation (translation + scale uniforme) pour que ce rectangle
     * tienne entièrement dans le rectangle {@code vue} (typiquement le Canvas).
     */
    public Transform fitTransform(RectangleHV vue) {
        double dx1 = xMax - xMin, dy1 = yMax - yMin;
        double cx1 = (xMax + xMin) / 2, cy1 = (yMax + yMin) / 2;
        double dx2 = vue.xMax - vue.xMin, dy2 = vue.yMax - vue.yMin;
        double cx2 = (vue.xMax + vue.xMin) / 2, cy2 = (vue.yMax + vue.yMin) / 2;

        Transform t1 = Transform.translate(-cx1, -cy1);
        Transform ts = Transform.scale(1, 1);
        if (dx1 > 0 && dy1 > 0 && dx2 > 0 && dy2 > 0) {
            double scale = Math.min(dx2 / dx1, dy2 / dy1);
            ts = Transform.scale(scale, scale);
        }
        Transform t2 = Transform.translate(cx2, cy2);
        return t2.createConcatenation(ts).createConcatenation(t1);
    }

    // ── Getters / Setters ───────────────────────────────────────────────────────

    public double getxMin() { return xMin; }
    public double getxMax() { return xMax; }
    public double getyMin() { return yMin; }
    public double getyMax() { return yMax; }
    public void setxMin(double v) { xMin = v; }
    public void setxMax(double v) { xMax = v; }
    public void setyMin(double v) { yMin = v; }
    public void setyMax(double v) { yMax = v; }

    @Override
    public String toString() {
        return "RectHV{x=[" + xMin + "," + xMax + "], y=[" + yMin + "," + yMax + "]}";
    }
}
