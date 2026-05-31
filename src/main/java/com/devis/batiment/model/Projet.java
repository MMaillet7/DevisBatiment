package com.devis.batiment.model;

/**
 * Projet : racine du modèle de données.
 * Contient le bâtiment et le chemin vers le fichier catalogue de revêtements.
 * C'est cet objet qui est sérialisé/désérialisé en JSON.
 */
public class Projet {

    private String nomProjet;
    private Batiment batiment;

    /** Chemin absolu ou relatif vers le fichier catalogue .txt. */
    private String cheminCatalogue;

    public Projet() {}

    public Projet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public String getNomProjet()                  { return nomProjet; }
    public void setNomProjet(String nomProjet)    { this.nomProjet = nomProjet; }

    public Batiment getBatiment()                 { return batiment; }
    public void setBatiment(Batiment batiment)    { this.batiment = batiment; }

    public String getCheminCatalogue()                    { return cheminCatalogue; }
    public void setCheminCatalogue(String cheminCatalogue){ this.cheminCatalogue = cheminCatalogue; }

    @Override
    public String toString() { return "Projet[" + nomProjet + "]"; }
}
