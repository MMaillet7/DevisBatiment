package com.devis.batiment.dessin;

import javafx.scene.paint.Color;

/**
 * Figure simple avec une couleur de tracé.
 * Adaptée du projet dessinVectoriel (F. de Bertrand de Beuvron, INSA Strasbourg).
 */
public abstract class FigureSimple extends Figure {

    private Color couleur;

    public FigureSimple(Color couleur) {
        this.couleur = couleur;
    }

    public Color getCouleur() { return couleur; }
    public void setCouleur(Color couleur) { this.couleur = couleur; }
}
