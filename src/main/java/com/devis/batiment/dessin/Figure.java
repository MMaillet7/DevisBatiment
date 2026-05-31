package com.devis.batiment.dessin;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Classe abstraite de base pour toute figure dessinable sur un Canvas JavaFX.
 * Adaptée du projet dessinVectoriel (F. de Bertrand de Beuvron, INSA Strasbourg).
 */
public abstract class Figure {

    public static final Color COULEUR_SELECTION = Color.RED;

    public abstract double maxX();
    public abstract double minX();
    public abstract double maxY();
    public abstract double minY();

    public double centreX() { return (maxX() + minX()) / 2; }
    public double centreY() { return (maxY() + minY()) / 2; }

    public abstract void dessine(GraphicsContext context);
    public abstract void deplace(double dx, double dy);
    public abstract Figure copie();
}
