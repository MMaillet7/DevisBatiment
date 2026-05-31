package com.devis.batiment.service;

import com.devis.batiment.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires des calculs de devis.
 */
class DevisServiceTest {

    private Maison maison;
    private Piece piece;
    private Mur mur;

    @BeforeEach
    void setUp() {
        maison = new Maison("TEST-01", "Maison Test", 2.5);
        piece  = new Piece(1, "Salon");
        // Mur de 5m × 2.5m = 12.5 m²
        Coin c1 = new Coin(1, 0, 0);
        Coin c2 = new Coin(2, 5, 0);
        mur = new Mur(1, c1, c2);
        mur.setIdRevetementInterieur(1); // peinture à 10.95 €/m²
        piece.ajouterMur(mur);
        maison.ajouterPiece(piece);
    }

    @Test
    void testLongueurMur() {
        assertEquals(5.0, mur.longueur(), 0.001, "La longueur du mur doit être 5.0 m");
    }

    @Test
    void testSurfaceBruteMur() {
        assertEquals(12.5, mur.surfaceBrute(2.5), 0.001, "Surface brute = 5 × 2.5 = 12.5 m²");
    }

    @Test
    void testSurfaceNetteAvecOuverture() {
        // Ajout d'une porte 0.9 × 2.0 = 1.8 m²
        Porte porte = new Porte(1, 1.0, 0.9, 2.0);
        mur.ajouterOuverture(porte);
        double expected = 12.5 - 1.8;
        assertEquals(expected, mur.surfaceNette(2.5), 0.001);
    }

    @Test
    void testSurfacePieceRectangulaire() {
        // Pièce rectangulaire 5×4 m
        Piece p = new Piece(2, "Chambre");
        Coin a = new Coin(1, 0, 0), b = new Coin(2, 5, 0),
             c = new Coin(3, 5, 4), d = new Coin(4, 0, 4);
        p.ajouterMur(new Mur(10, a, b));
        p.ajouterMur(new Mur(11, b, c));
        p.ajouterMur(new Mur(12, c, d));
        p.ajouterMur(new Mur(13, d, a));
        assertEquals(20.0, p.surface(), 0.001, "Surface 5×4 = 20 m²");
    }

    @Test
    void testCoutInterieurMur() {
        // 12.5 m² × 10.95 €/m² = 136.875 €
        double cout = mur.coutInterieur(2.5, 10.95);
        assertEquals(136.875, cout, 0.001);
    }

    @Test
    void testSurfaceNetteJamaisNegative() {
        // Ouverture absurde plus grande que le mur
        Fenetre f = new Fenetre(1, 0, 100, 100);
        mur.ajouterOuverture(f);
        assertTrue(mur.surfaceNette(2.5) >= 0, "La surface nette ne peut pas être négative");
    }

    @Test
    void testDevisMaison() {
        // Prix artificiel de 10 €/m² pour le revêtement 1
        double devis = maison.devisBatiment(id -> id == 1 ? 10.0 : 0.0);
        // Surface nette mur int. = 12.5 m² × 10 = 125 €
        assertEquals(125.0, devis, 0.001);
    }
}
