package com.devis.batiment.dessin;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Groupe de figures dessinables. Délègue le dessin à chaque élément.
 * Adaptée du projet dessinVectoriel (F. de Bertrand de Beuvron, INSA Strasbourg).
 */
public class Groupe extends Figure {

    private final List<Figure> contient = new ArrayList<>();

    public void add(Figure f) {
        contient.add(f);
    }

    public void remove(Figure f) {
        contient.remove(f);
    }

    public void clear() {
        contient.clear();
    }

    public List<Figure> getContient() {
        return contient;
    }

    public int size() {
        return contient.size();
    }

    public boolean isEmpty() {
        return contient.isEmpty();
    }

    @Override
    public double maxX() {
        return contient.stream().mapToDouble(Figure::maxX).max().orElse(0);
    }

    @Override
    public double minX() {
        return contient.stream().mapToDouble(Figure::minX).min().orElse(0);
    }

    @Override
    public double maxY() {
        return contient.stream().mapToDouble(Figure::maxY).max().orElse(0);
    }

    @Override
    public double minY() {
        return contient.stream().mapToDouble(Figure::minY).min().orElse(0);
    }

    @Override
    public void dessine(GraphicsContext ctx) {
        for (Figure f : contient) {
            f.dessine(ctx);
        }
    }

    @Override
    public void deplace(double dx, double dy) {
        for (Figure f : contient) {
            f.deplace(dx, dy);
        }
    }

    @Override
    public Groupe copie() {
        Groupe res = new Groupe();
        for (Figure f : contient) {
            res.add(f.copie());
        }
        return res;
    }
}
