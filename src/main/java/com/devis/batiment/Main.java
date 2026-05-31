package com.devis.batiment;

import com.devis.batiment.controller.MainController;
import com.devis.batiment.model.enums.TypeBatiment;
import com.devis.batiment.service.CatalogueService;
import com.devis.batiment.service.PlanService;
import com.devis.batiment.service.ProjetService;
import com.devis.batiment.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Point d'entree de l'application Devis Batiment.
 * Un projet Immeuble est cree automatiquement au demarrage.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        CatalogueService catalogueService = new CatalogueService();
        ProjetService    projetService    = new ProjetService(catalogueService);
        PlanService      planService      = new PlanService();

        // Immeuble uniquement — plus de Maison
        projetService.nouveauProjet("Mon projet", TypeBatiment.IMMEUBLE);

        MainView mainView = new MainView();
        new MainController(mainView, projetService, planService, primaryStage);

        Scene scene = new Scene(mainView, 1200, 750);
        primaryStage.setTitle("Devis Batiment");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
