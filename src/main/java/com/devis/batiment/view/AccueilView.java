package com.devis.batiment.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Vue de l'onglet "Accueil".
 * Gauche : selection du catalogue + bouton telecharger (TXT).
 * Droite : preview du devis (texte mis a jour a chaque visite de l'onglet).
 */
public class AccueilView extends BorderPane {

    // ── Gauche ───────────────────────────────────────────────────────────────────
    private final Button btnChoisirCatalogue = new Button("Choisir un catalogue...");
    private final Label  lblCatalogueActuel  = new Label("Aucun catalogue charge");
    private final Button btnTelecharger      = new Button("Telecharger le devis (TXT)");

    // ── Droite ───────────────────────────────────────────────────────────────────
    private final TextArea taPreviewDevis = new TextArea();

    public AccueilView() {
        construire();
    }

    private void construire() {
        setPadding(new Insets(20));

        // ── Panneau gauche ───────────────────────────────────────────────────────
        btnChoisirCatalogue.setMaxWidth(Double.MAX_VALUE);
        btnChoisirCatalogue.setPrefHeight(36);
        btnChoisirCatalogue.setStyle(
            "-fx-background-color: #3a7bd5; -fx-text-fill: white; -fx-font-weight: bold;");
        lblCatalogueActuel.setStyle("-fx-text-fill: #666; -fx-font-style: italic;");
        lblCatalogueActuel.setWrapText(true);

        btnTelecharger.setMaxWidth(Double.MAX_VALUE);
        btnTelecharger.setPrefHeight(40);
        btnTelecharger.setStyle(
            "-fx-background-color: #27ae60; -fx-text-fill: white; " +
            "-fx-font-size: 13; -fx-font-weight: bold; -fx-background-radius: 6;");
        btnTelecharger.setDisable(true);

        VBox panneauGauche = new VBox(14,
            titre("Catalogue de revetements"),
            btnChoisirCatalogue,
            lblCatalogueActuel,
            new Separator(),
            titre("Export du devis"),
            btnTelecharger
        );
        panneauGauche.setPadding(new Insets(16));
        panneauGauche.setPrefWidth(280);
        panneauGauche.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 8;");

        // ── Panneau droit ────────────────────────────────────────────────────────
        taPreviewDevis.setEditable(false);
        taPreviewDevis.setWrapText(false);
        taPreviewDevis.setFont(Font.font("Monospaced", 12));
        taPreviewDevis.setStyle("-fx-control-inner-background: #fafafa;");
        taPreviewDevis.setText("(Le devis apparaitra ici une fois les etages et pieces definis)");

        Label lblTitrePreview = titre("Apercu du devis");

        VBox panneauDroit = new VBox(10, lblTitrePreview, taPreviewDevis);
        VBox.setVgrow(taPreviewDevis, Priority.ALWAYS);
        panneauDroit.setPadding(new Insets(0, 0, 0, 16));

        setLeft(panneauGauche);
        setCenter(panneauDroit);
    }

    private Label titre(String texte) {
        Label l = new Label(texte);
        l.setFont(Font.font("System", FontWeight.BOLD, 13));
        return l;
    }

    // ── Accesseurs ───────────────────────────────────────────────────────────────
    public Button getBtnChoisirCatalogue() { return btnChoisirCatalogue; }
    public Label  getLblCatalogueActuel()  { return lblCatalogueActuel; }
    public Button getBtnTelecharger()      { return btnTelecharger; }
    public TextArea getTaPreviewDevis()    { return taPreviewDevis; }
}
