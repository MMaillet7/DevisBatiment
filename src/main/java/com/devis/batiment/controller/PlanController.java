package com.devis.batiment.controller;

import com.devis.batiment.dessin.Groupe;
import com.devis.batiment.model.*;
import com.devis.batiment.service.CatalogueService;
import com.devis.batiment.service.PlanService;
import com.devis.batiment.service.ProjetService;
import com.devis.batiment.util.AlertUtil;
import com.devis.batiment.util.FormatUtil;
import com.devis.batiment.view.PlanView;

import java.util.ArrayList;
import java.util.List;

/**
 * Controleur de l'onglet Plan.
 * Immeuble uniquement — pas de Maison.
 * Une piece est composee de 4 murs fixes.
 */
public class PlanController {

    private static final int NB_MURS = 4;

    private final PlanView vue;
    private final ProjetService projetService;
    private final CatalogueService catalogueService;
    private final PlanService planService;

    private int idxEtageCourant  = -1;
    private int idxPieceCourante = -1;

    private int prochainIdCoin = 1;
    private int prochainIdMur  = 1;
    private int prochainIdOuv  = 1;

    private final List<List<Ouverture>> ouverturesParMur = new ArrayList<>();

    public PlanController(PlanView vue, ProjetService projetService, PlanService planService) {
        this.vue              = vue;
        this.projetService    = projetService;
        this.catalogueService = projetService.getCatalogueService();
        this.planService      = planService;
        for (int i = 0; i < NB_MURS; i++) ouverturesParMur.add(new ArrayList<>());
        connecterEvenements();
    }

    private void connecterEvenements() {
        vue.getBtnAjouterEtage().setOnAction(e   -> ajouterEtage());
        vue.getBtnModifierEtage().setOnAction(e  -> modifierEtage());
        vue.getBtnSupprimerEtage().setOnAction(e -> supprimerEtage());
        vue.getListEtages().getSelectionModel().selectedIndexProperty()
            .addListener((obs, old, idx) -> selectionnerEtage(idx.intValue()));
        vue.getCbEtageAffiche().setOnAction(e -> rafraichirCanvas());

        vue.getBtnAjouterOuv().setOnAction(e   -> ajouterOuverture());
        vue.getBtnSupprimerOuv().setOnAction(e -> supprimerOuverture());

        vue.getBtnValiderPiece().setOnAction(e   -> validerPiece());
        vue.getBtnSupprimerPiece().setOnAction(e -> supprimerPiece());
        vue.getListPieces().getSelectionModel().selectedIndexProperty()
            .addListener((obs, old, idx) -> idxPieceCourante = idx.intValue());

        vue.getBtnZoomIn().setOnAction(e  -> vue.getPlanCanvas().zoomIn());
        vue.getBtnZoomOut().setOnAction(e -> vue.getPlanCanvas().zoomOut());
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // ETAGES
    // ══════════════════════════════════════════════════════════════════════════════

    private void ajouterEtage() {
        if (!projetService.projetOuvert()) return;
        String nom = vue.getTfNomEtage().getText().trim();
        double h   = FormatUtil.parseDouble(vue.getTfHauteurEtage().getText(), 2.5);
        if (nom.isEmpty()) { AlertUtil.erreur("Erreur", "Donnez un nom a l'etage."); return; }

        Immeuble immeuble = getImmeuble();
        if (immeuble == null) return;
        immeuble.ajouterNiveau(new Niveau(immeuble.getNiveaux().size() + 1, nom, h));
        rafraichirListeEtages();
        vue.getTfNomEtage().clear();
        vue.getTfHauteurEtage().clear();
    }

    private void modifierEtage() {
        if (!projetService.projetOuvert()) return;
        String nom  = vue.getTfNomEtage().getText().trim();
        String hStr = vue.getTfHauteurEtage().getText().trim();
        Immeuble immeuble = getImmeuble();
        if (immeuble == null) return;
        if (idxEtageCourant < 0 || idxEtageCourant >= immeuble.getNiveaux().size()) {
            AlertUtil.erreur("Erreur", "Selectionnez un etage a modifier."); return;
        }
        Niveau n = immeuble.getNiveaux().get(idxEtageCourant);
        if (!nom.isEmpty())  n.setNom(nom);
        if (!hStr.isEmpty()) n.setHauteur(FormatUtil.parseDouble(hStr, n.getHauteur()));
        rafraichirListeEtages();
        vue.getListEtages().getSelectionModel().select(idxEtageCourant);
    }

    private void supprimerEtage() {
        if (idxEtageCourant < 0) return;
        Immeuble immeuble = getImmeuble();
        if (immeuble == null) return;
        List<Niveau> niveaux = immeuble.getNiveaux();
        if (idxEtageCourant >= niveaux.size()) return;
        if (!AlertUtil.confirmer("Supprimer", "Supprimer cet etage et toutes ses pieces ?")) return;
        immeuble.supprimerNiveau(niveaux.get(idxEtageCourant));
        idxEtageCourant = -1;
        rafraichirListeEtages();
        rafraichirCanvas();
    }

    private void selectionnerEtage(int idx) {
        idxEtageCourant  = idx;
        idxPieceCourante = -1;
        if (idx >= 0) {
            Immeuble immeuble = getImmeuble();
            if (immeuble != null) {
                List<Niveau> niveaux = immeuble.getNiveaux();
                if (idx < niveaux.size()) {
                    vue.getTfNomEtage().setText(niveaux.get(idx).getNom());
                    vue.getTfHauteurEtage().setText(String.valueOf(niveaux.get(idx).getHauteur()));
                }
            }
        }
        rafraichirListePieces();
        rafraichirCanvas();
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // OUVERTURES
    // ══════════════════════════════════════════════════════════════════════════════

    private void ajouterOuverture() {
        int idxMur = vue.getIdxMurOuvertures();
        String type = vue.getCbTypeOuv().getValue();
        if (type == null) { AlertUtil.erreur("Erreur", "Choisissez un type d'ouverture."); return; }
        double pos  = FormatUtil.parseDouble(vue.getTfPosOuv().getText(), 0);
        double larg = FormatUtil.parseDouble(vue.getTfLargOuv().getText(), 0.9);
        double haut = FormatUtil.parseDouble(vue.getTfHautOuv().getText(), 2.0);
        Ouverture ouv = type.equals("Porte")
            ? new Porte(prochainIdOuv++, pos, larg, haut)
            : new Fenetre(prochainIdOuv++, pos, larg, haut);
        ouverturesParMur.get(idxMur).add(ouv);
        rafraichirListeOuv(idxMur);
        vue.getTfPosOuv().clear();
        vue.getTfLargOuv().clear();
        vue.getTfHautOuv().clear();
    }

    private void supprimerOuverture() {
        int idxMur = vue.getIdxMurOuvertures();
        int sel    = vue.getListOuv(idxMur).getSelectionModel().getSelectedIndex();
        if (sel < 0 || sel >= ouverturesParMur.get(idxMur).size()) return;
        ouverturesParMur.get(idxMur).remove(sel);
        rafraichirListeOuv(idxMur);
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // VALIDATION / SUPPRESSION DE PIECE
    // ══════════════════════════════════════════════════════════════════════════════

    private void validerPiece() {
        String nom = vue.getTfNomPiece().getText().trim();
        if (nom.isEmpty()) { AlertUtil.erreur("Erreur", "Donnez un nom a la piece."); return; }
        List<Piece> pieces = piecesEtageCourant();
        if (pieces == null) { AlertUtil.erreur("Erreur", "Selectionnez un etage d'abord."); return; }

        for (int i = 0; i < NB_MURS; i++) {
            double x1 = FormatUtil.parseDouble(vue.getTfX1(i).getText(), Double.NaN);
            double y1 = FormatUtil.parseDouble(vue.getTfY1(i).getText(), Double.NaN);
            double x2 = FormatUtil.parseDouble(vue.getTfX2(i).getText(), Double.NaN);
            double y2 = FormatUtil.parseDouble(vue.getTfY2(i).getText(), Double.NaN);
            if (Double.isNaN(x1) || Double.isNaN(y1) || Double.isNaN(x2) || Double.isNaN(y2)) {
                AlertUtil.erreur("Erreur",
                    "Mur " + (i + 1) + " : entrez des coordonnees valides.");
                return;
            }
        }

        Piece piece = new Piece(pieces.size() + 1, nom);
        piece.setIdRevetementSol(idRevSelectionne(vue.getCbRevSol()));
        piece.setIdRevetementPlafond(idRevSelectionne(vue.getCbRevPlafond()));

        for (int i = 0; i < NB_MURS; i++) {
            double x1 = FormatUtil.parseDouble(vue.getTfX1(i).getText(), 0);
            double y1 = FormatUtil.parseDouble(vue.getTfY1(i).getText(), 0);
            double x2 = FormatUtil.parseDouble(vue.getTfX2(i).getText(), 0);
            double y2 = FormatUtil.parseDouble(vue.getTfY2(i).getText(), 0);
            Coin debut = new Coin(prochainIdCoin++, x1, y1);
            Coin fin   = new Coin(prochainIdCoin++, x2, y2);
            Mur mur    = new Mur(prochainIdMur++, debut, fin);
            mur.setIdRevetementInterieur(idRevSelectionne(vue.getCbMurInt(i)));
            mur.setIdRevetementExterieur(idRevSelectionne(vue.getCbMurExt(i)));
            for (Ouverture o : ouverturesParMur.get(i)) mur.ajouterOuverture(o);
            piece.ajouterMur(mur);
        }

        pieces.add(piece);
        rafraichirListePieces();
        rafraichirCanvas();
        reinitialiserFormulairePiece();
    }

    private void supprimerPiece() {
        List<Piece> pieces = piecesEtageCourant();
        if (pieces == null || idxPieceCourante < 0 || idxPieceCourante >= pieces.size()) return;
        if (!AlertUtil.confirmer("Supprimer", "Supprimer cette piece et ses murs ?")) return;
        pieces.remove(idxPieceCourante);
        idxPieceCourante = -1;
        rafraichirListePieces();
        rafraichirCanvas();
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // RAFRAICHISSEMENT
    // ══════════════════════════════════════════════════════════════════════════════

    /** Initialisation complete a l'ouverture du projet. */
    public void rafraichirDepuisProjet() {
        if (!projetService.projetOuvert()) return;
        remplirCombosRevetementsMurs();
        remplirComboRevetementsSurface();
        rafraichirListeEtages();
        rafraichirCanvas();
        reinitialiserFormulairePiece();
    }

    /** Retour sur l'onglet Plan sans toucher au formulaire. */
    public void rafraichirSansReset() {
        if (!projetService.projetOuvert()) return;
        remplirCombosRevetementsMurs();
        remplirComboRevetementsSurface();
        rafraichirCanvas();
        rafraichirListePieces();
    }

    private void rafraichirListeEtages() {
        vue.getListEtages().getItems().clear();
        vue.getCbEtageAffiche().getItems().clear();
        Immeuble immeuble = getImmeuble();
        if (immeuble != null) {
            for (Niveau n : immeuble.getNiveaux()) {
                vue.getListEtages().getItems().add(n.getNom() + " (h=" + n.getHauteur() + "m)");
                vue.getCbEtageAffiche().getItems().add(n.getNom());
            }
        }
        vue.getCbEtageAffiche().getSelectionModel().selectFirst();
        rafraichirListePieces();
    }

    private void rafraichirListePieces() {
        vue.getListPieces().getItems().clear();
        List<Piece> pieces = piecesEtageCourant();
        if (pieces != null)
            pieces.forEach(p -> vue.getListPieces().getItems().add(
                p.getNom() + " (" + p.getMurs().size() + " murs)"));
    }

    private void rafraichirListeOuv(int idxMur) {
        vue.getListOuv(idxMur).getItems().clear();
        for (Ouverture o : ouverturesParMur.get(idxMur))
            vue.getListOuv(idxMur).getItems().add(
                o.getTypeOuverture().getLibelle() + "  pos=" + o.getPositionSurMur() + "m");
    }

    private void rafraichirCanvas() {
        int idx = Math.max(0, vue.getCbEtageAffiche().getSelectionModel().getSelectedIndex());
        Groupe groupe = planService.construireGroupe(projetService.getPiecesNiveau(idx));
        vue.getPlanCanvas().setModel(groupe);
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // REINITIALISATION
    // ══════════════════════════════════════════════════════════════════════════════

    private void reinitialiserFormulairePiece() {
        vue.getTfNomPiece().clear();
        vue.getCbRevSol().setValue(null);
        vue.getCbRevPlafond().setValue(null);
        for (int i = 0; i < NB_MURS; i++) {
            vue.getTfX1(i).clear(); vue.getTfY1(i).clear();
            vue.getTfX2(i).clear(); vue.getTfY2(i).clear();
            vue.getCbMurInt(i).setValue(null);
            vue.getCbMurExt(i).setValue(null);
            ouverturesParMur.get(i).clear();
            vue.getListOuv(i).getItems().clear();
        }
        vue.getCbTypeOuv().setValue(null);
        vue.getTfPosOuv().clear();
        vue.getTfLargOuv().clear();
        vue.getTfHautOuv().clear();
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // COMBOS REVETEMENTS
    // ══════════════════════════════════════════════════════════════════════════════

    private void remplirCombosRevetementsMurs() {
        List<String> opts = new ArrayList<>();
        opts.add("-- aucun --");
        catalogueService.getPourMur().forEach(e -> opts.add(e.getId() + " - " + e.getDesignation()));
        for (int i = 0; i < NB_MURS; i++) {
            vue.getCbMurInt(i).getItems().setAll(opts);
            vue.getCbMurExt(i).getItems().setAll(opts);
        }
    }

    private void remplirComboRevetementsSurface() {
        List<String> sol = new ArrayList<>();
        sol.add("-- aucun --");
        catalogueService.getPourSol().forEach(e -> sol.add(e.getId() + " - " + e.getDesignation()));
        vue.getCbRevSol().getItems().setAll(sol);

        List<String> pla = new ArrayList<>();
        pla.add("-- aucun --");
        catalogueService.getPourPlafond().forEach(e -> pla.add(e.getId() + " - " + e.getDesignation()));
        vue.getCbRevPlafond().getItems().setAll(pla);
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ══════════════════════════════════════════════════════════════════════════════

    private Immeuble getImmeuble() {
        if (!projetService.projetOuvert()) return null;
        Batiment bat = projetService.getProjetCourant().getBatiment();
        return (bat instanceof Immeuble immeuble) ? immeuble : null;
    }

    private List<Piece> piecesEtageCourant() {
        Immeuble immeuble = getImmeuble();
        if (immeuble == null) return null;
        List<Niveau> niveaux = immeuble.getNiveaux();
        if (idxEtageCourant < 0 || idxEtageCourant >= niveaux.size()) return null;
        Niveau niveau = niveaux.get(idxEtageCourant);
        if (niveau.getAppartements().isEmpty())
            niveau.ajouterAppartement(new Appartement(1, "Appartement 1"));
        return niveau.getAppartements().get(0).getPieces();
    }

    private Integer idRevSelectionne(javafx.scene.control.ComboBox<String> cb) {
        String val = cb.getValue();
        if (val == null || val.startsWith("--")) return null;
        try { return Integer.parseInt(val.split(" - ")[0].trim()); }
        catch (NumberFormatException e) { return null; }
    }
}
