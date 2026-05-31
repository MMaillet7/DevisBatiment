package com.devis.batiment.controller;

import com.devis.batiment.service.CatalogueService;
import com.devis.batiment.service.ExportService;
import com.devis.batiment.service.ProjetService;
import com.devis.batiment.util.AlertUtil;
import com.devis.batiment.view.AccueilView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Controleur de l'onglet Accueil.
 * Gere la selection du catalogue, la preview du devis et l'export TXT.
 * La preview se met a jour a chaque visite de l'onglet.
 */
public class AccueilController {

    private final AccueilView vue;
    private final ProjetService projetService;
    private final CatalogueService catalogueService;
    private final ExportService exportService;
    private final Stage stage;

    public AccueilController(AccueilView vue, ProjetService projetService, Stage stage) {
        this.vue              = vue;
        this.projetService    = projetService;
        this.catalogueService = projetService.getCatalogueService();
        this.exportService    = new ExportService(catalogueService);
        this.stage            = stage;
        connecterEvenements();
    }

    private void connecterEvenements() {
        vue.getBtnChoisirCatalogue().setOnAction(e -> choisirCatalogue());
        vue.getBtnTelecharger().setOnAction(e -> telecharger());
    }

    private void choisirCatalogue() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choisir le catalogue de revetements");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Catalogue (*.txt)", "*.txt"));
        File f = chooser.showOpenDialog(stage);
        if (f == null) return;
        try {
            catalogueService.charger(f);
            if (projetService.getProjetCourant() != null)
                projetService.getProjetCourant().setCheminCatalogue(f.getAbsolutePath());
            vue.getLblCatalogueActuel().setText(
                f.getName() + "  (" + catalogueService.getTous().size() + " revetements)");
            vue.getBtnTelecharger().setDisable(false);
            // Actualise la preview apres chargement du catalogue
            rafraichirPreview();
        } catch (IOException ex) {
            AlertUtil.erreur("Erreur catalogue",
                "Impossible de lire le fichier :\n" + ex.getMessage());
        }
    }

    private void telecharger() {
        if (!projetService.projetOuvert()) {
            AlertUtil.erreur("Aucun projet", "Aucun projet disponible.");
            return;
        }
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Enregistrer le devis");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Texte (*.txt)", "*.txt"));
        File dest = chooser.showSaveDialog(stage);
        if (dest == null) return;
        try {
            double total = projetService.calculerDevisTotal();
            exportService.exporterTxt(projetService.getProjetCourant(), total, dest);
            AlertUtil.info("Export reussi", "Devis exporte vers :\n" + dest.getAbsolutePath());
        } catch (IOException ex) {
            AlertUtil.erreur("Erreur export", ex.getMessage());
        }
    }

    /**
     * Appele a chaque visite de l'onglet Accueil.
     * Met a jour le label catalogue et regenere la preview du devis.
     */
    public void rafraichir() {
        if (catalogueService.estCharge()) {
            vue.getLblCatalogueActuel().setText(
                catalogueService.getTous().size() + " revetements charges");
            vue.getBtnTelecharger().setDisable(false);
        }
        rafraichirPreview();
    }

    /**
     * Regenere le texte de la preview a partir de l'etat courant du projet.
     */
    private void rafraichirPreview() {
        if (!projetService.projetOuvert()) {
            vue.getTaPreviewDevis().setText(
                "(Le devis apparaitra ici une fois les etages et pieces definis)");
            return;
        }
        double total = projetService.calculerDevisTotal();
        String texte = exportService.genererTexteDevis(
            projetService.getProjetCourant(), total);
        vue.getTaPreviewDevis().setText(texte);
        // Remonte en haut de la TextArea
        vue.getTaPreviewDevis().setScrollTop(0);
    }
}
