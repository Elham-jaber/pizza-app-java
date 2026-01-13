package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import pizzas.*;

/**
 * Tests JUnit de la classe {@link pizzas.Client Client}.
 */
class TestClient {

    private Client client;
    private InformationPersonnelle info;

    @BeforeEach
    void setUp() throws Exception {
        info = new InformationPersonnelle("A","B","C",20);
        client = new Client("a@a.com", "123", info);
    }

    @AfterEach
    void tearDown() throws Exception {}

    /**
     * Vérifie l'email.
     */
    @Test
    void testGetEmail() {
        assertEquals("a@a.com", client.getEmail());
    }

    /**
     * Vérifie qu'on peut créer une commande.
     */
    @Test
    void testCreerCommande() {
        Commande c = client.creerCommande();
        assertNotNull(c);
        assertEquals(1, client.getCommandes().size());
    }

    /**
     * Vérifie qu'on peut annuler une commande.
     */
    @Test
    void testAnnulerCommande() throws Exception {
        Commande c = client.creerCommande();
        client.annulerCommande(c);
        assertEquals(0, client.getCommandes().size());
    }
}
