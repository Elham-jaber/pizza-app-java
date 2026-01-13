package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import pizzas.Ingredient;

/**
 * Tests JUnit de la classe {@link pizzas.Ingredient Ingredient}.
 */
class TestIngredient {

    private Ingredient ing;

    @BeforeEach
    void setUp() throws Exception {
        ing = new Ingredient("Fromage", 2.5);
    }

    @AfterEach
    void tearDown() throws Exception {}

    /**
     * Vérifie le nom de l'ingrédient.
     */
    @Test
    void testGetNom() {
        assertEquals("Fromage", ing.getNom());
    }

    /**
     * Vérifie le prix de l'ingrédient.
     */
    @Test
    void testGetPrix() {
        assertEquals(2.5, ing.getPrix());
    }

    /**
     * Vérifie qu'on peut modifier le prix.
     */
    @Test
    void testSetPrix() {
        ing.setPrix(3.0);
        assertEquals(3.0, ing.getPrix());
    }

    /**
     * Vérifie qu'on ne peut pas mettre un prix négatif.
     */
    @Test
    void testSetPrixNegatif() {
        ing.setPrix(-10);
        assertTrue(ing.getPrix() >= 0);
    }
}
