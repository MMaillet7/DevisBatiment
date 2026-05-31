package com.devis.batiment.view;

import com.devis.batiment.model.EntreeRevetement;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Vue de l'onglet "Catalogue".
 * Affiche le catalogue de revêtements chargé sous forme de tableau.
 */
public class CatalogueView extends BorderPane {

    private final TableView<EntreeRevetement> table = new TableView<>();
    private final Label lblInfo = new Label("Aucun catalogue chargé. Utilisez l'onglet Accueil pour en charger un.");

    public CatalogueView() {
        construire();
    }

    private void construire() {
        setPadding(new Insets(16));

        Label titre = new Label("Catalogue de revêtements");
        titre.setFont(Font.font("System", FontWeight.BOLD, 16));

        lblInfo.setStyle("-fx-text-fill: #888; -fx-font-style: italic;");

        // Colonnes
        TableColumn<EntreeRevetement, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));
        colId.setPrefWidth(50);

        TableColumn<EntreeRevetement, String> colDesig = new TableColumn<>("Désignation");
        colDesig.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDesignation()));
        colDesig.setPrefWidth(180);

        TableColumn<EntreeRevetement, String> colMur = new TableColumn<>("Mur");
        colMur.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isPourMur() ? "✓" : ""));
        colMur.setPrefWidth(50);
        colMur.setStyle("-fx-alignment: CENTER;");

        TableColumn<EntreeRevetement, String> colSol = new TableColumn<>("Sol");
        colSol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isPourSol() ? "✓" : ""));
        colSol.setPrefWidth(50);
        colSol.setStyle("-fx-alignment: CENTER;");

        TableColumn<EntreeRevetement, String> colPlafond = new TableColumn<>("Plafond");
        colPlafond.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isPourPlafond() ? "✓" : ""));
        colPlafond.setPrefWidth(60);
        colPlafond.setStyle("-fx-alignment: CENTER;");

        TableColumn<EntreeRevetement, String> colPrix = new TableColumn<>("Prix (€/m²)");
        colPrix.setCellValueFactory(c -> new SimpleStringProperty(
            String.format("%.2f", c.getValue().getPrixUnitaire())));
        colPrix.setPrefWidth(100);
        colPrix.setStyle("-fx-alignment: CENTER_RIGHT;");

        table.getColumns().addAll(colId, colDesig, colMur, colSol, colPlafond, colPrix);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("Catalogue vide"));

        VBox header = new VBox(8, titre, lblInfo);
        setTop(header);
        BorderPane.setMargin(header, new Insets(0, 0, 12, 0));
        setCenter(table);
    }

    // ── Accesseurs ──────────────────────────────────────────────────────────────

    public TableView<EntreeRevetement> getTable() { return table; }
    public Label getLblInfo()                      { return lblInfo; }
}
