package com.devis.batiment.service;

import com.devis.batiment.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Genere l'export du devis en format TXT.
 * La methode genererTexteDevis() est aussi utilisee pour la preview dans l'onglet Accueil.
 */
public class ExportService {

    private final CatalogueService catalogueService;

    public ExportService(CatalogueService catalogueService) {
        this.catalogueService = catalogueService;
    }

    // ── Génération du texte (réutilisé pour preview et export fichier) ───────────

    /**
     * Genere le contenu complet du devis sous forme de String.
     * Utilise pour la preview dans l'onglet Accueil et pour l'export TXT.
     */
    public String genererTexteDevis(Projet projet, double montantTotal) {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(60)).append("\n");
        sb.append("  DEVIS DE BATIMENT\n");
        sb.append("=".repeat(60)).append("\n");
        sb.append("Projet    : ").append(projet.getNomProjet()).append("\n");
        sb.append("Date      : ")
          .append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
          .append("\n");
        sb.append("Batiment  : ").append(projet.getBatiment()).append("\n");
        if (projet.getCheminCatalogue() != null)
            sb.append("Catalogue : ").append(projet.getCheminCatalogue()).append("\n");
        sb.append("-".repeat(60)).append("\n");

        Batiment b = projet.getBatiment();
        if (b instanceof Immeuble immeuble) {
            if (immeuble.getNiveaux().isEmpty()) {
                sb.append("  (Aucun etage defini)\n");
            }
            for (Niveau n : immeuble.getNiveaux()) {
                sb.append("\nNiveau : ").append(n.getNom())
                  .append(" (hauteur ").append(n.getHauteur()).append(" m)\n");
                sb.append("~".repeat(40)).append("\n");
                for (Appartement a : n.getAppartements()) {
                    sb.append("  Appartement : ").append(a.getNom()).append("\n");
                    genererPieces(sb, a.getPieces(), n.getHauteur(), "    ");
                }
                if (n.getAppartements().isEmpty())
                    sb.append("    (Aucune piece definie)\n");
            }
        }

        sb.append("-".repeat(60)).append("\n");
        if (!catalogueService.estCharge()) {
            sb.append("TOTAL HT   : (catalogue non charge)\n");
        } else {
            sb.append(String.format("TOTAL HT   : %,.2f EUR%n", montantTotal));
        }
        sb.append("=".repeat(60)).append("\n");
        return sb.toString();
    }

    private void genererPieces(StringBuilder sb, List<Piece> pieces,
                               double hauteur, String indent) {
        for (Piece p : pieces) {
            sb.append(indent).append("Piece : ").append(p.getNom())
              .append("  (surface sol ~")
              .append(String.format("%.2f", p.surface())).append(" m2)\n");
            for (Mur m : p.getMurs()) {
                double surf = m.surfaceNette(hauteur);
                sb.append(String.format(indent + "  Mur %d  longueur=%.2fm  surface nette=%.2f m2%n",
                    m.getIdMur(), m.longueur(), surf));
                if (m.getIdRevetementInterieur() != null) {
                    double px = catalogueService.prixParId(m.getIdRevetementInterieur());
                    sb.append(String.format(
                        indent + "    Rev. int. (id%d) : %.2f EUR/m2 x %.2f m2 = %.2f EUR%n",
                        m.getIdRevetementInterieur(), px, surf, surf * px));
                }
                if (m.getIdRevetementExterieur() != null) {
                    double px = catalogueService.prixParId(m.getIdRevetementExterieur());
                    sb.append(String.format(
                        indent + "    Rev. ext. (id%d) : %.2f EUR/m2 x %.2f m2 = %.2f EUR%n",
                        m.getIdRevetementExterieur(), px, surf, surf * px));
                }
            }
            if (p.getIdRevetementSol() != null) {
                double surf = p.surface();
                double px   = catalogueService.prixParId(p.getIdRevetementSol());
                sb.append(String.format(
                    indent + "  Sol     (id%d) : %.2f EUR/m2 x %.2f m2 = %.2f EUR%n",
                    p.getIdRevetementSol(), px, surf, surf * px));
            }
            if (p.getIdRevetementPlafond() != null) {
                double surf = p.surface();
                double px   = catalogueService.prixParId(p.getIdRevetementPlafond());
                sb.append(String.format(
                    indent + "  Plafond (id%d) : %.2f EUR/m2 x %.2f m2 = %.2f EUR%n",
                    p.getIdRevetementPlafond(), px, surf, surf * px));
            }
        }
    }

    // ── Export vers fichier ───────────────────────────────────────────────────────

    public void exporterTxt(Projet projet, double montantTotal, File destination)
            throws IOException {
        String contenu = genererTexteDevis(projet, montantTotal);
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(destination),
                                       StandardCharsets.UTF_8))) {
            pw.print(contenu);
        }
    }
}
