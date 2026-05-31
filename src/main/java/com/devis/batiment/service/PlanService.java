package com.devis.batiment.service;

import java.util.List;

import com.devis.batiment.dessin.Groupe;
import com.devis.batiment.dessin.PointDessin;
import com.devis.batiment.dessin.Segment;
import com.devis.batiment.model.Mur;
import com.devis.batiment.model.Ouverture;
import com.devis.batiment.model.Piece;

import javafx.scene.paint.Color;

/**
 * Convertit le modèle métier (pièces, murs, ouvertures) en un Groupe de figures
 * dessinables pour le PlanCanvas.
 *
 * Convention de couleurs :
 *   - Murs    : noir (Color.BLACK)
 *   - Fenêtres: bleu (Color.BLUE)
 *   - Portes  : vert (Color.GREEN)
 *   - Trémies : gris pointillé (représentées en GRAY)
 */
public class PlanService {

    /**
     * Construit le Groupe de figures pour un étage donné.
     * @param pieces  liste des pièces de l'étage à dessiner.
     * @return un Groupe prêt à être affiché dans un PlanCanvas.
     */
    public Groupe construireGroupe(List<Piece> pieces) {
        Groupe groupe = new Groupe();
        for (Piece piece : pieces) {
            Groupe groupePiece = construireGroupePiece(piece);
            groupe.add(groupePiece);
        }
        return groupe;
    }

    private Groupe construireGroupePiece(Piece piece) {
        Groupe g = new Groupe();
        for (Mur mur : piece.getMurs()) {
            // Segment principal du mur (noir)
            PointDessin pDebut = new PointDessin(mur.getDebut().getCx(), mur.getDebut().getCy(), Color.BLACK);
            PointDessin pFin   = new PointDessin(mur.getFin().getCx(),   mur.getFin().getCy(),   Color.BLACK);
            Segment segMur = new Segment(pDebut, pFin, Color.BLACK);
            g.add(segMur);

            // Ouvertures par-dessus le segment du mur
            for (Ouverture o : mur.getOuvertures()) {
                double[] coords = o.getCoordonneesDessin(mur);
                if (coords != null && coords.length == 4) {
                    PointDessin od = new PointDessin(coords[0], coords[1]);
                    PointDessin of = new PointDessin(coords[2], coords[3]);
                    Color couleur = switch (o.getTypeOuverture()) {
                        case PORTE   -> Color.web("#4bf035");
                        case FENETRE -> Color.web("#357cf0");
                        case TREMIE  -> Color.GRAY;
                    };
                    g.add(new Segment(od, of, couleur));
                }
            }
        }
        return g;
    }
}
