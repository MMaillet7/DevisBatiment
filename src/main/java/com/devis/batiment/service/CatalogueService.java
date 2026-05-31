package com.devis.batiment.service;

import com.devis.batiment.model.EntreeRevetement;
import com.devis.batiment.repository.CatalogueRepository;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service d'accès au catalogue de revêtements.
 * Charge le fichier .txt une seule fois et expose des méthodes de filtrage.
 */
public class CatalogueService {

    private final CatalogueRepository repository = new CatalogueRepository();
    private List<EntreeRevetement> catalogue = Collections.emptyList();
    private Map<Integer, EntreeRevetement> index = Collections.emptyMap();

    // ── Chargement ──────────────────────────────────────────────────────────────

    /**
     * Charge (ou recharge) le catalogue depuis le fichier donné.
     * @throws IOException si le fichier est illisible.
     */
    public void charger(File fichier) throws IOException {
        catalogue = repository.charger(fichier);
        index = catalogue.stream()
            .collect(Collectors.toMap(EntreeRevetement::getId, e -> e));
    }

    public boolean estCharge() {
        return !catalogue.isEmpty();
    }

    // ── Accès ───────────────────────────────────────────────────────────────────

    public List<EntreeRevetement> getTous() {
        return Collections.unmodifiableList(catalogue);
    }

    public Optional<EntreeRevetement> getParId(int id) {
        return Optional.ofNullable(index.get(id));
    }

    public List<EntreeRevetement> getPourMur() {
        return catalogue.stream().filter(EntreeRevetement::isPourMur).collect(Collectors.toList());
    }

    public List<EntreeRevetement> getPourSol() {
        return catalogue.stream().filter(EntreeRevetement::isPourSol).collect(Collectors.toList());
    }

    public List<EntreeRevetement> getPourPlafond() {
        return catalogue.stream().filter(EntreeRevetement::isPourPlafond).collect(Collectors.toList());
    }

    /**
     * Retourne le prix unitaire d'un revêtement par son id.
     * Retourne 0.0 si l'id est null ou inconnu.
     */
    public double prixParId(Integer id) {
        if (id == null) return 0.0;
        return getParId(id).map(EntreeRevetement::getPrixUnitaire).orElse(0.0);
    }
}
