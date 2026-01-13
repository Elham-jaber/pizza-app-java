package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import pizzas.*;

/**
 * Tests JUnit de la classe {@link pizzas.GestionPizzaiolo GestionPizzaiolo}.
 */
class TestGestionPizzaiolo {

    private GestionPizzaiolo gestion;
    private Pizza pizza;

    @BeforeEach
    void setUp() throws Exception {
        gestion = new GestionPizzaiolo();
        gestion.creerIngredient("Fromage", 2.0);
        gestion.creerIngredient("Tomate", 1.0);

        pizza = gestion.creerPizza("Test", TypePizza.VIANDE);
        gestion.ajouterIngredientPizza(pizza, "Fromage");
        gestion.ajouterIngredientPizza(pizza, "Tomate");
    }

    @AfterEach
    void tearDown() throws Exception {}

    /**
     * Vérifie la création d'un ingrédient.
     */
    @Test
    void testCreerIngredient() {
        int res = gestion.creerIngredient("Jambon", 3.0);
        assertEquals(0, res);
    }

    /**
     * Vérifie l'interdiction d'un ingrédient.
     */
    @Test
    void testInterdireIngredient() {
        boolean ok = gestion.interdireIngredient("Tomate", TypePizza.VEGETARIENNE);
        assertTrue(ok);
    }

    /**
     * Vérifie l'ajout d'un ingrédient à une pizza.
     */
    @Test
    void testAjouterIngredientPizza() {
        Pizza p2 = gestion.creerPizza("Test2", TypePizza.VIANDE);
        int res = gestion.ajouterIngredientPizza(p2, "Fromage");
        assertEquals(0, res);
    }

    /**
     * Vérifie le calcul du bénéfice.
     */
    @Test
    void testBeneficePizza() {
        double min = pizza.getPrixMinimal();
        gestion.setPrixPizza(pizza, min + 5);

        double benef = gestion.beneficeParPizza().get(pizza);
        assertEquals(5.0, benef, 0.0001);
    }

    /**
     * Vérifie le classement des pizzas.
     */
    @Test
    void testClassementPizzas() throws Exception {
        // Créer un client et une commande
        InformationPersonnelle info =
            new InformationPersonnelle("A","B","C",20);
        Client c = new Client("a@a.com", "123", info);

        Commande cmd = new Commande(c);
        cmd.ajouterPizza(pizza);
        cmd.valider();

        gestion.enregistrerClient(c);
        gestion.enregistrerCommande(cmd);

        // Traiter la commande
        gestion.commandeNonTraitees();

        // Le classement doit contenir la pizza
        assertTrue(gestion.classementPizzasParNombreCommandes().contains(pizza));
    }
}
