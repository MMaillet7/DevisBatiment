package com.devis.batiment.controller;

import com.devis.batiment.service.PlanService;
import com.devis.batiment.service.ProjetService;
import com.devis.batiment.view.MainView;
import javafx.stage.Stage;

/**
 * Controleur principal.
 * Cable les sous-controleurs et gere les changements d'onglet.
 */
public class MainController {

    private final MainView vue;
    private final AccueilController accueilCtrl;
    private final PlanController planCtrl;
    private final CatalogueController catalogueCtrl;

    public MainController(MainView vue, ProjetService projetService,
                          PlanService planService, Stage stage) {
        this.vue = vue;

        accueilCtrl   = new AccueilController(vue.getAccueilView(), projetService, stage);
        planCtrl      = new PlanController(vue.getPlanView(), projetService, planService);
        catalogueCtrl = new CatalogueController(vue.getCatalogueView(),
                                                projetService.getCatalogueService());

        // Initialisation immediate au demarrage
        accueilCtrl.rafraichir();
        planCtrl.rafraichirDepuisProjet();

        connecterOnglets();
    }

    private void connecterOnglets() {
        vue.getTabPane().getSelectionModel().selectedItemProperty()
            .addListener((obs, ancien, nouveau) -> {
                if (nouveau == vue.getTabCatalogue()) {
                    catalogueCtrl.rafraichir();
                } else if (nouveau == vue.getTabPlan()) {
                    planCtrl.rafraichirSansReset();
                } else if (nouveau == vue.getTabAccueil()) {
                    accueilCtrl.rafraichir();
                }
            });
    }
}
