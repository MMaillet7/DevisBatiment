package com.devis.batiment.controller;

import com.devis.batiment.model.EntreeRevetement;
import com.devis.batiment.service.CatalogueService;
import com.devis.batiment.view.CatalogueView;
import javafx.collections.FXCollections;

/**
 * Contrôleur de l'onglet Catalogue.
 * Peuple le tableau depuis le CatalogueService.
 */
public class CatalogueController {

    private final CatalogueView vue;
    private final CatalogueService catalogueService;

    public CatalogueController(CatalogueView vue, CatalogueService catalogueService) {
        this.vue              = vue;
        this.catalogueService = catalogueService;
    }

    /** Rafraîchit le tableau à partir du catalogue chargé. */
    public void rafraichir() {
        if (!catalogueService.estCharge()) {
            vue.getLblInfo().setText("Aucun catalogue chargé. Utilisez l'onglet Accueil pour en charger un.");
            vue.getTable().setItems(FXCollections.emptyObservableList());
            return;
        }
        vue.getLblInfo().setText(
            catalogueService.getTous().size() + " revêtements dans le catalogue.");
        vue.getTable().setItems(
            FXCollections.observableArrayList(catalogueService.getTous()));
    }
}
