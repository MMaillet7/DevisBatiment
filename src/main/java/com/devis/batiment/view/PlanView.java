package com.devis.batiment.view;

import com.devis.batiment.dessin.PlanCanvas;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Vue de l'onglet "Plan".
 * Gauche : etages / formulaire piece (4 murs fixes) — sans section couts.
 * Centre : PlanCanvas + sélecteur d'etage + zoom.
 */
public class PlanView extends BorderPane {

    private static final int NB_MURS = 4;

    // ── Etages ───────────────────────────────────────────────────────────────────
    private final ListView<String> listEtages  = new ListView<>();
    private final TextField tfNomEtage         = new TextField();
    private final TextField tfHauteurEtage     = new TextField();
    private final Button btnAjouterEtage       = new Button("+ Ajouter");
    private final Button btnModifierEtage      = new Button("Modifier");
    private final Button btnSupprimerEtage     = new Button("- Supprimer");

    // ── Piece — identite ─────────────────────────────────────────────────────────
    private final TextField tfNomPiece          = new TextField();
    private final ComboBox<String> cbRevSol     = new ComboBox<>();
    private final ComboBox<String> cbRevPlafond = new ComboBox<>();

    // ── 4 murs — coordonnees + revetements ───────────────────────────────────────
    private final TextField[] tfX1   = new TextField[NB_MURS];
    private final TextField[] tfY1   = new TextField[NB_MURS];
    private final TextField[] tfX2   = new TextField[NB_MURS];
    private final TextField[] tfY2   = new TextField[NB_MURS];
    private final ComboBox<String>[] cbMurInt = new ComboBox[NB_MURS];
    private final ComboBox<String>[] cbMurExt = new ComboBox[NB_MURS];

    // ── Ouvertures ────────────────────────────────────────────────────────────────
    private final ListView<String>[] listOuv   = new ListView[NB_MURS];
    private final ComboBox<String> cbTypeOuv   = new ComboBox<>();
    private final TextField tfPosOuv           = new TextField();
    private final TextField tfLargOuv          = new TextField();
    private final TextField tfHautOuv          = new TextField();
    private final Button btnAjouterOuv         = new Button("+ Ouverture");
    private final Button btnSupprimerOuv       = new Button("- Retirer");
    private int idxMurOuvertures               = 0;

    // ── Validation + liste pieces ─────────────────────────────────────────────────
    private final Button btnValiderPiece       = new Button("Ajouter la piece");
    private final Button btnSupprimerPiece     = new Button("- Supprimer piece selectionnee");
    private final ListView<String> listPieces  = new ListView<>();

    // ── Canvas ───────────────────────────────────────────────────────────────────
    private final PlanCanvas planCanvas           = new PlanCanvas();
    private final ComboBox<String> cbEtageAffiche = new ComboBox<>();
    private final Button btnZoomIn                = new Button("+");
    private final Button btnZoomOut               = new Button("-");

    public PlanView() {
        for (int i = 0; i < NB_MURS; i++) {
            tfX1[i] = new TextField(); tfY1[i] = new TextField();
            tfX2[i] = new TextField(); tfY2[i] = new TextField();
            cbMurInt[i] = new ComboBox<>();
            cbMurExt[i] = new ComboBox<>();
            listOuv[i]  = new ListView<>();
        }
        construire();
    }

    @SuppressWarnings("unchecked")
    private void construire() {
        setPadding(new Insets(10));

        VBox gauche = new VBox(8);
        gauche.setPadding(new Insets(10));
        gauche.setStyle("-fx-background-color: #f7f7f7; -fx-background-radius: 8;");

        // ── ETAGES ───────────────────────────────────────────────────────────────
        listEtages.setPrefHeight(80);
        tfNomEtage.setPromptText("ex : Rez-de-chaussee");
        tfHauteurEtage.setPromptText("ex : 2.5");
        btnAjouterEtage.setMaxWidth(Double.MAX_VALUE);
        btnModifierEtage.setMaxWidth(Double.MAX_VALUE);
        btnSupprimerEtage.setMaxWidth(Double.MAX_VALUE);
        HBox hEtage = new HBox(5, btnAjouterEtage, btnModifierEtage, btnSupprimerEtage);
        HBox.setHgrow(btnAjouterEtage,   Priority.ALWAYS);
        HBox.setHgrow(btnModifierEtage,  Priority.ALWAYS);
        HBox.setHgrow(btnSupprimerEtage, Priority.ALWAYS);
        gauche.getChildren().addAll(
            titre("Etages"), listEtages,
            champ("Nom :", tfNomEtage),
            champ("Hauteur (m) :", tfHauteurEtage),
            hEtage, new Separator()
        );

        // ── NOUVELLE PIECE ────────────────────────────────────────────────────────
        tfNomPiece.setPromptText("ex : Salon");
        cbRevSol.setPromptText("-- aucun --");     cbRevSol.setMaxWidth(Double.MAX_VALUE);
        cbRevPlafond.setPromptText("-- aucun --"); cbRevPlafond.setMaxWidth(Double.MAX_VALUE);
        gauche.getChildren().addAll(
            titre("Nouvelle piece"),
            champ("Nom piece :", tfNomPiece),
            champ("Sol :",       cbRevSol),
            champ("Plafond :",   cbRevPlafond)
        );

        // ── 4 MURS ────────────────────────────────────────────────────────────────
        for (int i = 0; i < NB_MURS; i++) {
            gauche.getChildren().add(sousTitre("Mur " + (i + 1)));
            gauche.getChildren().add(coordGrid(i));
            cbMurInt[i].setPromptText("-- aucun --"); cbMurInt[i].setMaxWidth(Double.MAX_VALUE);
            cbMurExt[i].setPromptText("-- aucun --"); cbMurExt[i].setMaxWidth(Double.MAX_VALUE);
            gauche.getChildren().addAll(
                champ("Rev. int. :", cbMurInt[i]),
                champ("Rev. ext. :", cbMurExt[i])
            );
        }

        // ── OUVERTURES ────────────────────────────────────────────────────────────
        gauche.getChildren().add(sousTitre("Ouvertures"));
        TabPane tabsMurs = new TabPane();
        tabsMurs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabsMurs.setStyle("-fx-tab-min-height: 24; -fx-font-size: 11;");
        for (int i = 0; i < NB_MURS; i++) {
            final int idx = i;
            listOuv[i].setPrefHeight(50);
            Tab t = new Tab("Mur " + (i + 1), listOuv[i]);
            t.setOnSelectionChanged(e -> { if (t.isSelected()) idxMurOuvertures = idx; });
            tabsMurs.getTabs().add(t);
        }
        gauche.getChildren().add(tabsMurs);

        cbTypeOuv.getItems().addAll("Porte", "Fenetre");
        cbTypeOuv.setPromptText("Type"); cbTypeOuv.setMaxWidth(Double.MAX_VALUE);
        tfPosOuv.setPromptText("Position depuis debut (m)");
        tfLargOuv.setPromptText("Largeur (m)");
        tfHautOuv.setPromptText("Hauteur (m)");
        btnAjouterOuv.setMaxWidth(Double.MAX_VALUE);
        btnSupprimerOuv.setMaxWidth(Double.MAX_VALUE);
        HBox hOuv = new HBox(5, btnAjouterOuv, btnSupprimerOuv);
        HBox.setHgrow(btnAjouterOuv,   Priority.ALWAYS);
        HBox.setHgrow(btnSupprimerOuv, Priority.ALWAYS);
        gauche.getChildren().addAll(
            cbTypeOuv,
            champ("Position :", tfPosOuv),
            champ("Largeur :",  tfLargOuv),
            champ("Hauteur :",  tfHautOuv),
            hOuv, new Separator()
        );

        // ── VALIDATION + LISTE PIECES ─────────────────────────────────────────────
        btnValiderPiece.setMaxWidth(Double.MAX_VALUE);
        btnValiderPiece.setPrefHeight(34);
        btnValiderPiece.setStyle(
            "-fx-background-color: #27ae60; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-background-radius: 5;");
        btnSupprimerPiece.setMaxWidth(Double.MAX_VALUE);
        btnSupprimerPiece.setStyle("-fx-text-fill: #c0392b;");
        listPieces.setPrefHeight(80);
        gauche.getChildren().addAll(
            btnValiderPiece,
            sousTitre("Pieces de l'etage"),
            listPieces,
            btnSupprimerPiece
        );

        ScrollPane scroll = new ScrollPane(gauche);
        scroll.setFitToWidth(true);
        scroll.setPrefWidth(330);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // ── CANVAS ────────────────────────────────────────────────────────────────
        planCanvas.setStyle("-fx-border-color: #bbb; -fx-border-width: 1;");
        VBox.setVgrow(planCanvas, Priority.ALWAYS);

        cbEtageAffiche.setPromptText("Etage affiche");
        cbEtageAffiche.setMaxWidth(180);
        btnZoomIn.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-min-width: 32;");
        btnZoomOut.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-min-width: 32;");

        HBox barreBas = new HBox(10, cbEtageAffiche, btnZoomIn, btnZoomOut);
        barreBas.setAlignment(Pos.CENTER_LEFT);
        barreBas.setPadding(new Insets(6, 0, 0, 0));

        VBox panneauCanvas = new VBox(planCanvas, barreBas);
        VBox.setVgrow(planCanvas, Priority.ALWAYS);
        panneauCanvas.setPadding(new Insets(0, 0, 0, 10));

        setLeft(scroll);
        setCenter(panneauCanvas);
    }

    private Label titre(String t) {
        Label l = new Label(t);
        l.setFont(Font.font("System", FontWeight.BOLD, 13));
        l.setStyle("-fx-text-fill: #2c3e50;");
        return l;
    }

    private Label sousTitre(String t) {
        Label l = new Label(t);
        l.setFont(Font.font("System", FontWeight.BOLD, 11));
        l.setStyle("-fx-text-fill: #555;");
        return l;
    }

    private HBox champ(String label, Control ctrl) {
        Label l = new Label(label);
        l.setMinWidth(85);
        HBox box = new HBox(6, l, ctrl);
        HBox.setHgrow(ctrl, Priority.ALWAYS);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private GridPane coordGrid(int i) {
        tfX1[i].setPromptText("X1"); tfY1[i].setPromptText("Y1");
        tfX2[i].setPromptText("X2"); tfY2[i].setPromptText("Y2");
        GridPane g = new GridPane();
        g.setHgap(4); g.setVgap(3);
        g.addRow(0, new Label("Debut :"), tfX1[i], new Label("Y :"), tfY1[i]);
        g.addRow(1, new Label("Fin   :"), tfX2[i], new Label("Y :"), tfY2[i]);
        return g;
    }

    // ── Accesseurs ───────────────────────────────────────────────────────────────
    public ListView<String> getListEtages()   { return listEtages; }
    public TextField getTfNomEtage()          { return tfNomEtage; }
    public TextField getTfHauteurEtage()      { return tfHauteurEtage; }
    public Button getBtnAjouterEtage()        { return btnAjouterEtage; }
    public Button getBtnModifierEtage()       { return btnModifierEtage; }
    public Button getBtnSupprimerEtage()      { return btnSupprimerEtage; }

    public TextField getTfNomPiece()          { return tfNomPiece; }
    public ComboBox<String> getCbRevSol()     { return cbRevSol; }
    public ComboBox<String> getCbRevPlafond() { return cbRevPlafond; }

    public TextField getTfX1(int i)           { return tfX1[i]; }
    public TextField getTfY1(int i)           { return tfY1[i]; }
    public TextField getTfX2(int i)           { return tfX2[i]; }
    public TextField getTfY2(int i)           { return tfY2[i]; }
    public ComboBox<String> getCbMurInt(int i){ return cbMurInt[i]; }
    public ComboBox<String> getCbMurExt(int i){ return cbMurExt[i]; }

    public ListView<String> getListOuv(int i) { return listOuv[i]; }
    public ComboBox<String> getCbTypeOuv()    { return cbTypeOuv; }
    public TextField getTfPosOuv()            { return tfPosOuv; }
    public TextField getTfLargOuv()           { return tfLargOuv; }
    public TextField getTfHautOuv()           { return tfHautOuv; }
    public Button getBtnAjouterOuv()          { return btnAjouterOuv; }
    public Button getBtnSupprimerOuv()        { return btnSupprimerOuv; }
    public int getIdxMurOuvertures()          { return idxMurOuvertures; }

    public Button getBtnValiderPiece()        { return btnValiderPiece; }
    public Button getBtnSupprimerPiece()      { return btnSupprimerPiece; }
    public ListView<String> getListPieces()   { return listPieces; }

    public PlanCanvas getPlanCanvas()               { return planCanvas; }
    public ComboBox<String> getCbEtageAffiche()     { return cbEtageAffiche; }
    public Button getBtnZoomIn()                    { return btnZoomIn; }
    public Button getBtnZoomOut()                   { return btnZoomOut; }
}
