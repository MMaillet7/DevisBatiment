package com.devis.batiment.view;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

/**
 * Vue principale — sans menu Fichier.
 * Les 3 onglets sont accessibles directement au demarrage.
 */
public class MainView extends BorderPane {

    private final TabPane tabPane       = new TabPane();
    private final Tab tabAccueil        = new Tab("Accueil");
    private final Tab tabPlan           = new Tab("Plan");
    private final Tab tabCatalogue      = new Tab("Catalogue");

    private final AccueilView   accueilView   = new AccueilView();
    private final PlanView      planView      = new PlanView();
    private final CatalogueView catalogueView = new CatalogueView();

    public MainView() {
        construire();
    }

    private void construire() {
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabAccueil.setContent(accueilView);
        tabPlan.setContent(planView);
        tabCatalogue.setContent(catalogueView);
        tabPane.getTabs().addAll(tabAccueil, tabPlan, tabCatalogue);
        setCenter(tabPane);
    }

    public TabPane getTabPane()             { return tabPane; }
    public Tab getTabAccueil()              { return tabAccueil; }
    public Tab getTabPlan()                 { return tabPlan; }
    public Tab getTabCatalogue()            { return tabCatalogue; }

    public AccueilView getAccueilView()     { return accueilView; }
    public PlanView getPlanView()           { return planView; }
    public CatalogueView getCatalogueView() { return catalogueView; }
}
