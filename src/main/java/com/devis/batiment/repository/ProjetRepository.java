package com.devis.batiment.repository;

import com.devis.batiment.model.Projet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

/**
 * Persistance JSON d'un Projet.
 * Utilise Jackson avec indentation pour lisibilité du fichier sauvegardé.
 */
public class ProjetRepository {

    private final ObjectMapper mapper;

    public ProjetRepository() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Sauvegarde le projet dans un fichier JSON.
     * @param projet  le projet à sauvegarder.
     * @param fichier le fichier de destination (.json).
     * @throws IOException en cas d'erreur d'écriture.
     */
    public void sauvegarder(Projet projet, File fichier) throws IOException {
        mapper.writeValue(fichier, projet);
    }

    /**
     * Charge un projet depuis un fichier JSON.
     * @param fichier le fichier source (.json).
     * @return le projet désérialisé.
     * @throws IOException en cas d'erreur de lecture ou de format invalide.
     */
    public Projet charger(File fichier) throws IOException {
        return mapper.readValue(fichier, Projet.class);
    }
}
