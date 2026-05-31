package com.devis.batiment.dessin;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

/**
 * Pane JavaFX contenant un Canvas qui dessine un Groupe de figures (plan d'un etage).
 * Se redimensionne automatiquement avec son parent.
 * Supporte zoom via RectangleHV.
 * Version sans grille — fond blanc, murs noirs.
 * Adaptee de DessinCanvas (F. de Bertrand de Beuvron, INSA Strasbourg).
 */
public class PlanCanvas extends Pane {

    private static final double MULT_FIT = 1.15;

    private final Canvas canvas;
    private Groupe model;
    private RectangleHV zoneVue;

    public PlanCanvas() { this(new Groupe()); }

    public PlanCanvas(Groupe model) {
        this.model  = model;
        this.canvas = new Canvas();
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        canvas.widthProperty().addListener(obs  -> redraw());
        canvas.heightProperty().addListener(obs -> redraw());
        getChildren().add(canvas);
        fitAll();
    }

    public void setModel(Groupe model) { this.model = model; fitAll(); }
    public Groupe getModel()           { return model; }

    public void fitAll() {
        if (model.isEmpty()) {
            zoneVue = new RectangleHV(-50, 550, -50, 550);
        } else {
            double mx = model.minX(), mX = model.maxX();
            double my = model.minY(), mY = model.maxY();
            double padX = (mX - mx) * (MULT_FIT - 1) / 2;
            double padY = (mY - my) * (MULT_FIT - 1) / 2;
            zoneVue = new RectangleHV(mx - padX, mX + padX, my - padY, mY + padY);
        }
        redraw();
    }

    public void zoomIn()  { zoneVue = zoneVue.scale(0.75); redraw(); }
    public void zoomOut() { zoneVue = zoneVue.scale(1.33); redraw(); }

    public void redraw() {
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        if (w <= 0 || h <= 0) return;

        // Fond blanc
        ctx.setTransform(new Affine());
        ctx.setFill(Color.WHITE);
        ctx.fillRect(0, 0, w, h);

        // Transformation modele -> pixel avec les 6 coefficients explicites
        RectangleHV canvasRect = new RectangleHV(0, w, 0, h);
        Transform t = zoneVue.fitTransform(canvasRect);
        ctx.setTransform(new Affine(
            t.getMxx(), t.getMxy(), t.getTx(),
            t.getMyx(), t.getMyy(), t.getTy()
        ));

        // Dessine toutes les figures
        model.dessine(ctx);

        // Remet repere pixel
        ctx.setTransform(new Affine());
    }

    public double[] screenToModel(double sx, double sy) {
        RectangleHV cr = new RectangleHV(0, canvas.getWidth(), 0, canvas.getHeight());
        Transform t = zoneVue.fitTransform(cr);
        try {
            javafx.geometry.Point2D p = t.inverseTransform(sx, sy);
            return new double[]{p.getX(), p.getY()};
        } catch (NonInvertibleTransformException e) {
            return new double[]{sx, sy};
        }
    }

    public RectangleHV getZoneVue()             { return zoneVue; }
    public void setZoneVue(RectangleHV zoneVue) { this.zoneVue = zoneVue; redraw(); }
}
