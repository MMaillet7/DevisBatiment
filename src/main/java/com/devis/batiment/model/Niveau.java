package com.devis.batiment.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Niveau (étage) d'un immeuble.
 * Contient plusieurs appartements et définit la hauteur sous plafond des murs.
 */
public class Niveau {

    private int idNiveau;
    private String nom;

    /** Hauteur sous plafond en mètres (ex : 2.5). */
    private double hauteur;

    private List<Appartement> appartements = new ArrayList<>();

    public Niveau() {}

    public Niveau(int idNiveau, String nom, double hauteur) {
        this.idNiveau = idNiveau;
        this.nom      = nom;
        this.hauteur  = hauteur;
    }

    // ── Calcul du devis ─────────────────────────────────────────────────────────

    /**
     * Coût total du niveau = somme des coûts de chaque appartement.
     * Les prix unitaires sont résolus par le service à partir du catalogue.
     */
    public double devisNiveau(java.util.function.Function<Integer, Double> prixParId) {
        return appartements.stream()
            .mapToDouble(a -> a.devisAppartement(hauteur, prixParId))
            .sum();
    }

    // ── Gestion des appartements ─────────────────────────────────────────────────

    public void ajouterAppartement(Appartement a)    { appartements.add(a); }
    public void supprimerAppartement(Appartement a)  { appartements.remove(a); }

    // ── Getters / Setters ───────────────────────────────────────────────────────

    public int getIdNiveau()               { return idNiveau; }
    public void setIdNiveau(int idNiveau)  { this.idNiveau = idNiveau; }

    public String getNom()                 { return nom; }
    public void setNom(String nom)         { this.nom = nom; }

    public double getHauteur()             { return hauteur; }
    public void setHauteur(double hauteur) { this.hauteur = hauteur; }

    public List<Appartement> getAppartements()                      { return appartements; }
    public void setAppartements(List<Appartement> appartements)     { this.appartements = appartements; }

    @Override
    public String toString() { return "Niveau[" + idNiveau + "] " + nom + " (h=" + hauteur + "m)"; }
}
