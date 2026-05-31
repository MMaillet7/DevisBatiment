package com.devis.batiment.model;

import com.devis.batiment.model.enums.TypeBatiment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Immeuble : bâtiment multi-niveaux.
 * Chaque niveau porte sa propre hauteur de mur.
 */
public class Immeuble extends Batiment {

    private List<Niveau> niveaux = new ArrayList<>();

    public Immeuble() {}

    public Immeuble(String idBatiment, String nom) {
        super(idBatiment, nom);
    }

    @Override
    public TypeBatiment getTypeBatiment() { return TypeBatiment.IMMEUBLE; }

    @Override
    public int getNombreNiveaux() { return niveaux.size(); }

    @Override
    public double devisBatiment(Function<Integer, Double> prixParId) {
        return niveaux.stream()
            .mapToDouble(n -> n.devisNiveau(prixParId))
            .sum();
    }

    // ── Gestion des niveaux ──────────────────────────────────────────────────────

    public void ajouterNiveau(Niveau n)    { niveaux.add(n); }
    public void supprimerNiveau(Niveau n)  { niveaux.remove(n); }

    public List<Niveau> getNiveaux()                  { return niveaux; }
    public void setNiveaux(List<Niveau> niveaux)      { this.niveaux = niveaux; }
}
