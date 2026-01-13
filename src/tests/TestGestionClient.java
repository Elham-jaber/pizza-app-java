package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import pizzas.*;

/**
 * Tests JUnit de la classe {@link pizzas.GestionClient GestionClient}.
 */
class TestGestionClient {

    private GestionClient gestion;
    private GestionPizzaiolo gp;
    private Pizza pizza;

    @BeforeEach
    void setUp() throws Exception {
        gp = new GestionPizzaiolo();
        gp.creerIngredient("Fromage", 2.0);

        pizza = gp.creerPizza("Test", TypePizza.VIANDE);
        gp.ajouterIngredientPizza(pizza, "Fromage");
        gp.setPrixPizza(pizza, pizza.getPrixMinimal() + 1);

        gestion = new GestionClient(gp.getPizzas());
    }

    @AfterEach
    void tearDown() throws Exception {}

    /**
     * Vérifie l'inscription et la connexion.
     */
    @Test
    void testInscriptionConnexion() {
        InformationPersonnelle info =
            new InformationPersonnelle("A","B","C",20);

        int res = gestion.inscription("a@a.com", "123", info);
        assertEquals(0, res);

        assertTrue(gestion.connexion("a@a.com", "123"));
    }

    /**
     * Vérifie la création et validation d'une commande.
     */
    @Test
    void testCommandeComplete() throws Exception {
        InformationPersonnelle info =
            new InformationPersonnelle("A","B","C",20);

        gestion.inscription("a@a.com", "123", info);
        gestion.connexion("a@a.com", "123");

        Commande cmd = gestion.debuterCommande();
        gestion.ajouterPizza(pizza, 1, cmd);
        gestion.validerCommande(cmd);

        assertEquals(EtatCommande.VALIDEE, cmd.getEtat());
    }

    /**
     * Vérifie les filtres.
     */
    @Test
    void testFiltres() {
        Set<Pizza> pizzas = gestion.getPizzas();
        assertEquals(1, pizzas.size());

        gestion.ajouterFiltre(TypePizza.VIANDE);
        Set<Pizza> res = gestion.selectionPizzaFiltres();
        assertEquals(1, res.size());
    }
}
