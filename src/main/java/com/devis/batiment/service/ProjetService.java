package com.devis.batiment.service;

import com.devis.batiment.model.*;
import com.devis.batiment.model.enums.TypeBatiment;
import com.devis.batiment.repository.ProjetRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Service principal : gère le projet courant (création, chargement, sauvegarde)
 * et calcule le devis global en s'appuyant sur le CatalogueService.
 */
public class ProjetService {

    private final ProjetRepository projetRepository = new ProjetRepository();
    private final CatalogueService catalogueService;

    private Projet projetCourant;
    private File fichierCourant;

    public ProjetService(CatalogueService catalogueService) {
        this.catalogueService = catalogueService;
    }

    // ── Gestion du projet ───────────────────────────────────────────────────────

    /** Crée un nouveau projet vide. */
    public Projet nouveauProjet(String nom, TypeBatiment type) {
        projetCourant = new Projet(nom);
        if (type == TypeBatiment.MAISON) {
            projetCourant.setBatiment(new Maison("BAT-001", nom, 2.50));
        } else {
            projetCourant.setBatiment(new Immeuble("BAT-001", nom));
        }
        fichierCourant = null;
        return projetCourant;
    }

    /** Sauvegarde le projet courant dans le fichier donné. */
    public void sauvegarder(File fichier) throws IOException {
        if (projetCourant == null) throw new IllegalStateException("Aucun projet ouvert.");
        fichierCourant = fichier;
        projetRepository.sauvegarder(projetCourant, fichier);
    }

    /** Sauvegarde dans le fichier courant (s'il existe). */
    public void sauvegarder() throws IOException {
        if (fichierCourant == null) throw new IllegalStateException("Aucun fichier associé au projet.");
        sauvegarder(fichierCourant);
    }

    /** Charge un projet depuis un fichier JSON. */
    public Projet charger(File fichier) throws IOException {
        projetCourant = projetRepository.charger(fichier);
        fichierCourant = fichier;
        // Recharge le catalogue si le chemin est stocké
        if (projetCourant.getCheminCatalogue() != null) {
            File cat = new File(projetCourant.getCheminCatalogue());
            if (cat.exists()) {
                catalogueService.charger(cat);
            }
        }
        return projetCourant;
    }

    // ── Calcul du devis ─────────────────────────────────────────────────────────

    /**
     * Calcule le coût total du bâtiment courant.
     * Retourne 0 si le catalogue n'est pas chargé ou si le bâtiment est null.
     */
    public double calculerDevisTotal() {
        if (projetCourant == null || projetCourant.getBatiment() == null) return 0;
        return projetCourant.getBatiment().devisBatiment(catalogueService::prixParId);
    }

    /**
     * Calcule le coût d'une pièce isolée (utile pour affichage en temps réel).
     */
    public double calculerCoutPiece(Piece piece, double hauteur) {
        double cout = 0;
        for (Mur m : piece.getMurs()) {
            cout += m.coutInterieur(hauteur, catalogueService.prixParId(m.getIdRevetementInterieur()));
            cout += m.coutExterieur(hauteur, catalogueService.prixParId(m.getIdRevetementExterieur()));
        }
        cout += piece.surface() * catalogueService.prixParId(piece.getIdRevetementSol());
        cout += piece.surface() * catalogueService.prixParId(piece.getIdRevetementPlafond());
        return cout;
    }

    // ── Dessin ──────────────────────────────────────────────────────────────────

    /**
     * Retourne les pièces à dessiner pour un niveau donné.
     * Pour une Maison, niveauIndex est ignoré (retourne toutes les pièces).
     * Pour un Immeuble, retourne les pièces du niveau correspondant.
     */
    public List<Piece> getPiecesNiveau(int niveauIndex) {
        if (projetCourant == null || projetCourant.getBatiment() == null) return List.of();
        Batiment b = projetCourant.getBatiment();
        if (b instanceof Maison maison) {
            return maison.getPieces();
        } else if (b instanceof Immeuble immeuble) {
            List<Niveau> niveaux = immeuble.getNiveaux();
            if (niveauIndex < 0 || niveauIndex >= niveaux.size()) return List.of();
            Niveau n = niveaux.get(niveauIndex);
            return n.getAppartements().stream()
                .flatMap(a -> a.getPieces().stream())
                .toList();
        }
        return List.of();
    }

    /**
     * Retourne la hauteur de mur pour un niveau donné.
     */
    public double getHauteurNiveau(int niveauIndex) {
        if (projetCourant == null || projetCourant.getBatiment() == null) return 2.5;
        Batiment b = projetCourant.getBatiment();
        if (b instanceof Maison maison) return maison.getHauteur();
        if (b instanceof Immeuble immeuble) {
            List<Niveau> niveaux = immeuble.getNiveaux();
            if (niveauIndex >= 0 && niveauIndex < niveaux.size())
                return niveaux.get(niveauIndex).getHauteur();
        }
        return 2.5;
    }

    // ── Accesseurs ──────────────────────────────────────────────────────────────

    public Projet getProjetCourant()              { return projetCourant; }
    public File getFichierCourant()               { return fichierCourant; }
    public boolean projetOuvert()                 { return projetCourant != null; }
    public CatalogueService getCatalogueService() { return catalogueService; }
}
