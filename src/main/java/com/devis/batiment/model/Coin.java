package com.devis.batiment.model;

/**
 * Coin : point 2D représentant une extrémité de mur dans le plan du bâtiment.
 * Coordonnées en mètres (repère du plan).
 */
public class Coin {

    private int idCoin;
    private double cx;
    private double cy;

    public Coin() {}

    public Coin(int idCoin, double cx, double cy) {
        this.idCoin = idCoin;
        this.cx = cx;
        this.cy = cy;
    }

    public int getIdCoin()          { return idCoin; }
    public void setIdCoin(int id)   { this.idCoin = id; }

    public double getCx()           { return cx; }
    public void setCx(double cx)    { this.cx = cx; }

    public double getCy()           { return cy; }
    public void setCy(double cy)    { this.cy = cy; }

    @Override
    public String toString() { return "(" + cx + ";" + cy + ")"; }
}
