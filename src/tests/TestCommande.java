package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import pizzas.*;

/**
 * Tests JUnit de la classe {@link pizzas.Commande Commande}.
 */
class TestCommande {

    private Client client;
    private Commande commande;
    private Pizza pizza;

    @BeforeEach
    void setUp() throws Exception {
        client = new Client("a@a.com", "123",
            new InformationPersonnelle("A","B","C",20));
        commande = new Commande(client);
        pizza = new Pizza("Test", TypePizza.VIANDE);
    }

    @AfterEach
    void tearDown() throws Exception {}

    /**
     * Vérifie que l'état initial est CREE.
     */
    @Test
    void testEtatInitial() {
        assertEquals(EtatCommande.CREE, commande.getEtat());
    }

    /**
     * Vérifie qu'on ne peut pas valider une commande vide.
     */
    @Test
    void testValiderCommandeVide() {
        assertThrows(CommandeException.class, () -> {
            commande.valider();
        });
    }

    /**
     * Vérifie qu'on peut ajouter une pizza.
     */
    @Test
    void testAjouterPizza() throws Exception {
        commande.ajouterPizza(pizza);
        assertEquals(1, commande.getPizzas().size());
    }

    /**
     * Vérifie qu'on peut valider une commande non vide.
     */
    @Test
    void testValiderCommande() throws Exception {
        commande.ajouterPizza(pizza);
        commande.valider();
        assertEquals(EtatCommande.VALIDEE, commande.getEtat());
    }

    /**
     * Vérifie qu'on peut traiter une commande validée.
     */
    @Test
    void testTraiterCommande() throws Exception {
        commande.ajouterPizza(pizza);
        commande.valider();
        commande.traiter();
        assertEquals(EtatCommande.TRAITEE, commande.getEtat());
    }
}
