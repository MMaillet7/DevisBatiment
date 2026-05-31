package com.devis.batiment.model;

/**
 * Représente une entrée du catalogue de revêtements (lue depuis le fichier .txt).
 * Format CSV : idRevetement;designation;pourMur;pourSol;pourPlafond;prixUnitaire
 */
public class EntreeRevetement {

    private int id;
    private String designation;
    private boolean pourMur;
    private boolean pourSol;
    private boolean pourPlafond;
    private double prixUnitaire;

    public EntreeRevetement() {}

    public EntreeRevetement(int id, String designation,
                            boolean pourMur, boolean pourSol, boolean pourPlafond,
                            double prixUnitaire) {
        this.id           = id;
        this.designation  = designation;
        this.pourMur      = pourMur;
        this.pourSol      = pourSol;
        this.pourPlafond  = pourPlafond;
        this.prixUnitaire = prixUnitaire;
    }

    // ── Getters / Setters ───────────────────────────────────────────────────────

    public int getId()               { return id; }
    public void setId(int id)        { this.id = id; }

    public String getDesignation()               { return designation; }
    public void setDesignation(String d)         { this.designation = d; }

    public boolean isPourMur()               { return pourMur; }
    public void setPourMur(boolean pourMur)  { this.pourMur = pourMur; }

    public boolean isPourSol()               { return pourSol; }
    public void setPourSol(boolean pourSol)  { this.pourSol = pourSol; }

    public boolean isPourPlafond()                   { return pourPlafond; }
    public void setPourPlafond(boolean pourPlafond)  { this.pourPlafond = pourPlafond; }

    public double getPrixUnitaire()                  { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    @Override
    public String toString() {
        return id + " – " + designation + " (" + prixUnitaire + " €/m²)";
    }
}
