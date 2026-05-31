package com.devis.batiment.repository;

import com.devis.batiment.model.EntreeRevetement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lecture du catalogue de revêtements depuis un fichier .txt (CSV point-virgule).
 * Format attendu :
 *   idRevetement;designation;pourMur(0/1);pourSol(0/1);pourPlafond(0/1);prixUnitaire
 * La première ligne (en-tête) est ignorée.
 */
public class CatalogueRepository {

    /**
     * Charge toutes les entrées du catalogue depuis le fichier.
     * @param fichier fichier .txt au format CSV (séparateur ';').
     * @return liste des entrées, jamais null.
     * @throws IOException si le fichier est introuvable ou mal formé.
     */
    public List<EntreeRevetement> charger(File fichier) throws IOException {
        List<EntreeRevetement> catalogue = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            boolean premiereIgnoree = false;
            while ((ligne = reader.readLine()) != null) {
                ligne = ligne.trim();
                if (ligne.isEmpty()) continue;
                if (!premiereIgnoree) {
                    premiereIgnoree = true; // on saute l'en-tête
                    continue;
                }
                EntreeRevetement entree = parseLigne(ligne);
                if (entree != null) {
                    catalogue.add(entree);
                }
            }
        }
        return catalogue;
    }

    private EntreeRevetement parseLigne(String ligne) {
        String[] parts = ligne.split(";");
        if (parts.length < 6) return null;
        try {
            int    id          = Integer.parseInt(parts[0].trim());
            String designation = parts[1].trim();
            boolean pourMur     = parts[2].trim().equals("1");
            boolean pourSol     = parts[3].trim().equals("1");
            // gère la colonne pourPlafond qui peut avoir un espace avant le ';'
            boolean pourPlafond = parts[4].trim().replaceAll("\\s", "").equals("1");
            double  prix        = Double.parseDouble(parts[5].trim());
            return new EntreeRevetement(id, designation, pourMur, pourSol, pourPlafond, prix);
        } catch (NumberFormatException e) {
            System.err.println("[CatalogueRepository] Ligne ignorée (format invalide) : " + ligne);
            return null;
        }
    }
}
