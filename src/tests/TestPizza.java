package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import pizzas.*;

/**
 * Tests JUnit de la classe {@link pizzas.Pizza Pizza}.
 */
class TestPizza {

    private Pizza pizza;
    private Ingredient fromage;
    private Ingredient tomate;

    @BeforeEach
    void setUp() throws Exception {
        pizza = new Pizza("Test", TypePizza.VIANDE);
        fromage = new Ingredient("Fromage", 2.0);
        tomate = new Ingredient("Tomate", 1.0);
    }

    @AfterEach
    void tearDown() throws Exception {}

    /**
     * Vérifie qu'on peut ajouter un ingrédient.
     */
    @Test
    void testAjouterIngredient() {
        assertTrue(pizza.ajouterIngredient(fromage));
        assertEquals(1, pizza.getIngredients().size());
    }

    /**
     * Vérifie qu'on ne peut pas ajouter deux fois le même ingrédient.
     */
    @Test
    void testAjouterDeuxFoisMemeIngredient() {
        pizza.ajouterIngredient(fromage);
        assertFalse(pizza.ajouterIngredient(fromage));
    }

    /**
     * Vérifie le calcul du prix minimal.
     */
    @Test
    void testPrixMinimal() {
        pizza.ajouterIngredient(fromage); // 2
        pizza.ajouterIngredient(tomate);  // 1
        // (2 + 1) * 1.4 = 4.2
        assertEquals(4.2, pizza.getPrixMinimal());
    }

    /**
     * Vérifie qu'on ne peut pas fixer un prix inférieur au prix minimal.
     */
    @Test
    void testSetPrixVenteInferieur() {
        pizza.ajouterIngredient(fromage);
        assertThrows(IllegalArgumentException.class, () -> {
            pizza.setPrixVente(1.0);
        });
    }
}
