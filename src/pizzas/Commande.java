package pizzas;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Représente une commande de pizzas effectuée par un client.
 * <p>
 * Une commande possède :
 * <ul>
 *   <li>un identifiant unique</li>
 *   <li>un client associé</li>
 *   <li>une liste de pizzas</li>
 *   <li>une date de création</li>
 *   <li>un état (créée, validée, traitée)</li>
 * </ul>
 * </p>
 * La commande est modifiable uniquement tant qu'elle est à l'état .
 */
public class Commande implements Serializable {

    /**
     * Compteur statique utilisé pour générer des identifiants uniques.
     */
    private static int compteur = 1;

    /**
     * Identifiant unique de la commande.
     */
    private final int id;

    /**
     * Client ayant passé la commande.
     */
    private final Client client;

    /**
     * Liste des pizzas de la commande.
     */
    private final List<Pizza> pizzas = new ArrayList<>();

    /**
     * Date et heure de création de la commande.
     */
    private final LocalDateTime dateCreation;

    /**
     * État actuel de la commande.
     */
    private EtatCommande etat;

    /**
     * Retourne la date de création de la commande.
     *
     * @return la date de création
     */
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    /**
     * Construit une nouvelle commande pour un client donné.
     *
     * @param client le client qui passe la commande
     * @throws IllegalArgumentException si le client est nul
     */
    public Commande(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client nul");
        }
        this.id = compteur++;
        this.client = client;
        this.dateCreation = LocalDateTime.now();
        this.etat = EtatCommande.CREE;
    }

    /**
     * Retourne l'identifiant de la commande.
     *
     * @return l'identifiant
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne le client associé à la commande.
     *
     * @return le client
     */
    public Client getClient() {
        return client;
    }

    /**
     * Retourne l'état actuel de la commande.
     *
     * @return l'état de la commande
     */
    public EtatCommande getEtat() {
        return etat;
    }

    /**
     * Retourne une vue non modifiable de la liste des pizzas.
     *
     * @return la liste des pizzas
     */
    public List<Pizza> getPizzas() {
        return Collections.unmodifiableList(pizzas);
    }

    /**
     * Indique si la commande est encore modifiable.
     *
     * @return {@code true} si la commande est à l'état CREE, sinon {@code false}
     */
    private boolean estModifiable() {
        return etat == EtatCommande.CREE;
    }

    /**
     * Ajoute une pizza à la commande.
     *
     * @param p la pizza à ajouter
     * @throws CommandeException si la commande n'est pas modifiable
     * @throws IllegalArgumentException si la pizza est nulle
     */
    public void ajouterPizza(Pizza p) throws CommandeException {
        if (!estModifiable()) {
            throw new CommandeException("Commande non modifiable");
        }
        if (p == null) {
            throw new IllegalArgumentException("Pizza nulle");
        }
        pizzas.add(p);
    }

    /**
     * Retire une pizza de la commande.
     *
     * @param p la pizza à retirer
     * @throws CommandeException si la commande n'est pas modifiable
     */
    public void retirerPizza(Pizza p) throws CommandeException {
        if (!estModifiable()) {
            throw new CommandeException("Commande non modifiable");
        }
        pizzas.remove(p);
    }

    /**
     * Valide la commande.
     *
     * @throws CommandeException si la commande est déjà validée
     *                          ou si elle ne contient aucune pizza
     */
    public void valider() throws CommandeException {
        if (!estModifiable()) {
            throw new CommandeException("Commande déjà validée");
        }
        if (pizzas.isEmpty()) {
            throw new CommandeException("Commande vide");
        }
        etat = EtatCommande.VALIDEE;
    }

    /**
     * Marque la commande comme traitée.
     *
     * @throws CommandeException si la commande n'a pas été validée
     */
    public void traiter() throws CommandeException {
        if (etat != EtatCommande.VALIDEE) {
            throw new CommandeException("Commande non validée");
        }
        etat = EtatCommande.TRAITEE;
    }

    /**
     * Calcule le prix total de la commande.
     *
     * @return le prix total des pizzas
     */
    public double getPrixTotal() {
        return pizzas.stream()
                .mapToDouble(Pizza::getPrixVente)
                .sum();
    }

    /**
     * Calcule le bénéfice total de la commande.
     *
     * @return le bénéfice total
     */
    public double getBenefice() {
        return pizzas.stream()
                .mapToDouble(p -> p.getPrixVente() - p.getPrixMinimal())
                .sum();
    }
}
