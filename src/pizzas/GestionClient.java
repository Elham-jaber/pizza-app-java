package pizzas;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implémentation des services client.
 * <p>
 * Cette classe permet :
 * <ul>
 *   <li>l'inscription et la connexion des clients</li>
 *   <li>la gestion des commandes</li>
 *   <li>la consultation et le filtrage des pizzas</li>
 *   <li>la gestion des évaluations</li>
 * </ul>
 * </p>
 */
public class GestionClient implements InterClient {

    /**
     * Client actuellement connecté.
     */
    private Client clientConnecte;

    /**
     * Liste des clients inscrits.
     */
    private final List<Client> clientsInscrits;

    /**
     * Ensemble des pizzas disponibles.
     */
    private final Set<Pizza> pizzas;

    /**
     * Référence vers la gestion du pizzaiolo.
     */
    private GestionPizzaiolo gestionPizzaiolo;

    /* Filtres */

    /**
     * Filtre sur le type de pizza.
     */
    private TypePizza filtreType;

    /**
     * Filtre sur les ingrédients.
     */
    private final Set<String> filtreIngredients;

    /**
     * Filtre sur le prix maximum.
     */
    private Double filtrePrixMax;

    /**
     * Construit un gestionnaire client.
     *
     * @param pizzas ensemble des pizzas disponibles
     */
    public GestionClient(Set<Pizza> pizzas) {
        this.clientsInscrits = new ArrayList<>();
        this.pizzas = pizzas;
        this.filtreIngredients = new HashSet<>();
    }

    /**
     * Associe un gestionnaire pizzaiolo.
     *
     * @param gp gestionnaire pizzaiolo
     */
    public void setGestionPizzaiolo(GestionPizzaiolo gp) {
        this.gestionPizzaiolo = gp;
    }

    // =========================
    // INSCRIPTION / CONNEXION
    // =========================

    /**
     * Inscrit un nouveau client.
     *
     * @param email adresse email
     * @param mdp mot de passe
     * @param info informations personnelles
     * @return code de retour :
     * <ul>
     *   <li>0 : succès</li>
     *   <li>-1 : champs manquants</li>
     *   <li>-2 : mot de passe trop court</li>
     *   <li>-3 : email invalide</li>
     *   <li>-4 : email déjà utilisé</li>
     * </ul>
     */
    @Override
    public int inscription(String email, String mdp, InformationPersonnelle info) {

        //  champs obligatoires
        if (info == null
                || info.getNom() == null || info.getNom().isBlank()
                || info.getPrenom() == null || info.getPrenom().isBlank()
                || info.getAge() <= 0
                || email == null || email.isBlank()
                || mdp == null || mdp.isBlank()) {
            return -1; // champs manquants
        }

        //  mot de passe
        if (mdp.length() < 8) {
            return -2; // mot de passe trop court
        }

        // email valide
        if (!email.matches(".+@.+\\..+")) {
            return -3; // email invalide
        }

        //  email déjà utilisé
        for (Client c : clientsInscrits) {
            if (c.getEmail().equalsIgnoreCase(email)) {
                return -4; // email déjà utilisé
            }
        }

        //  création du client
        Client c = new Client(email, mdp, info);
        clientsInscrits.add(c);

        return 0;
    }

    /**
     * Connecte un client.
     *
     * @param email email du client
     * @param mdp mot de passe
     * @return {@code true} si la connexion réussit, {@code false} sinon
     */
    @Override
    public boolean connexion(String email, String mdp) {
        if (email == null || mdp == null) return false;

        for (Client c : clientsInscrits) {

            if (c.getEmail().equalsIgnoreCase(email)
                    && c.getMdp().equals(mdp)) {
                clientConnecte = c;
                return true;
            }
        }
        return false;
    }

    /**
     * Déconnecte le client actuellement connecté.
     *
     * @throws NonConnecteException si aucun client n'est connecté
     */
    @Override
    public void deconnexion() throws NonConnecteException {
        if (clientConnecte == null) {
            throw new NonConnecteException();
        }
        clientConnecte = null;
    }


    // COMMANDES
    
    /**
     * Démarre une nouvelle commande pour le client connecté.
     *
     * @return la commande créée
     * @throws NonConnecteException si aucun client n'est connecté
     */
    @Override
    public Commande debuterCommande() throws NonConnecteException {
        if (clientConnecte == null) {
            throw new NonConnecteException();
        }
        return clientConnecte.creerCommande();
    }

    /**
     * Ajoute une ou plusieurs pizzas à une commande.
     *
     * @param pizza pizza à ajouter
     * @param nombre quantité
     * @param cmd commande concernée
     * @throws NonConnecteException si aucun client n'est connecté
     * @throws CommandeException en cas d'erreur de commande
     */
    @Override
    public void ajouterPizza(Pizza pizza, int nombre, Commande cmd)
            throws NonConnecteException, CommandeException {

        if (clientConnecte == null) {
            throw new NonConnecteException();
        }
        if (pizza == null || cmd == null || nombre <= 0) {
            throw new CommandeException("Paramètres invalides");
        }
        if (!clientConnecte.getCommandes().contains(cmd)) {
            throw new CommandeException("Commande inconnue");
        }

        for (int i = 0; i < nombre; i++) {
            cmd.ajouterPizza(pizza);
        }
    }

    /**
     * Valide une commande.
     *
     * @param cmd commande à valider
     * @throws NonConnecteException si aucun client n'est connecté
     * @throws CommandeException si la commande est invalide
     */
    @Override
    public void validerCommande(Commande cmd)
            throws NonConnecteException, CommandeException {

        if (clientConnecte == null) {
            throw new NonConnecteException();
        }
        if (cmd == null || !clientConnecte.getCommandes().contains(cmd)) {
            throw new CommandeException("Commande inconnue");
        }
        cmd.valider();
    }

    /**
     * Annule une commande.
     *
     * @param cmd commande à annuler
     * @throws NonConnecteException si aucun client n'est connecté
     * @throws CommandeException si la commande est invalide
     */
    @Override
    public void annulerCommande(Commande cmd)
            throws NonConnecteException, CommandeException {

        if (clientConnecte == null) {
            throw new NonConnecteException();
        }
        if (cmd == null || !clientConnecte.getCommandes().contains(cmd)) {
            throw new CommandeException("Commande inconnue");
        }
        clientConnecte.annulerCommande(cmd);
    }

    /**
     * Retourne les commandes en cours du client connecté.
     *
     * @return liste des commandes en cours
     * @throws NonConnecteException si aucun client n'est connecté
     */
    @Override
    public List<Commande> getCommandesEncours() throws NonConnecteException {
        if (clientConnecte == null) {
            throw new NonConnecteException();
        }
        return clientConnecte.getCommandes().stream()
                .filter(c -> c.getEtat() == EtatCommande.CREE)
                .sorted((c1, c2) -> c1.getDateCreation().compareTo(c2.getDateCreation()))
                .collect(Collectors.toList());
    }

    /**
     * Retourne les commandes passées du client connecté.
     *
     * @return liste des commandes passées
     * @throws NonConnecteException si aucun client n'est connecté
     */
    @Override
    public List<Commande> getCommandePassees() throws NonConnecteException {
        if (clientConnecte == null) {
            throw new NonConnecteException();
        }
        return clientConnecte.getCommandes().stream()
                .filter(c -> c.getEtat() != EtatCommande.CREE)
                .sorted(Comparator.comparing(Commande::getDateCreation))
                .collect(Collectors.toList());
    }

   
    // PIZZAS & FILTRES
  

    /**
     * Retourne l'ensemble des pizzas disponibles.
     *
     * @return ensemble non modifiable de pizzas
     */
    @Override
    public Set<Pizza> getPizzas() {
        return Collections.unmodifiableSet(pizzas);
    }

    /**
     * Ajoute un filtre sur le type de pizza.
     *
     * @param type type de pizza
     */
    @Override
    public void ajouterFiltre(TypePizza type) {
        this.filtreType = type;
    }

    /**
     * Ajoute un filtre sur les ingrédients.
     *
     * @param ingredients ingrédients à filtrer
     */
    @Override
    public void ajouterFiltre(String... ingredients) {
        for (String i : ingredients) {
            if (i != null && !i.isBlank()) {
                filtreIngredients.add(i.toLowerCase());
            }
        }
    }

    /**
     * Ajoute un filtre sur le prix maximum.
     *
     * @param prixMaximum prix maximum
     */
    @Override
    public void ajouterFiltre(double prixMaximum) {
        if (prixMaximum > 0) {
            this.filtrePrixMax = prixMaximum;
        }
    }

    /**
     * Sélectionne les pizzas correspondant aux filtres actifs.
     *
     * @return ensemble des pizzas filtrées
     */
    @Override
    public Set<Pizza> selectionPizzaFiltres() {
        return pizzas.stream()
                .filter(p -> filtreType == null || p.getType() == filtreType)
                .filter(p -> filtrePrixMax == null || p.getPrixVente() <= filtrePrixMax)
                .filter(p -> filtreIngredients.isEmpty() ||
                        p.getIngredients().stream()
                                .map(i -> i.getNom().toLowerCase())
                                .collect(Collectors.toSet())
                                .containsAll(filtreIngredients))
                .collect(Collectors.toSet());
    }

    /**
     * Supprime tous les filtres actifs.
     */
    @Override
    public void supprimerFiltres() {
        filtreType = null;
        filtreIngredients.clear();
        filtrePrixMax = null;
    }

   
    // ÉVALUATIONS


    /**
     * Retourne les évaluations d'une pizza.
     *
     * @param pizza pizza concernée
     * @return ensemble des évaluations ou {@code null} si la pizza est nulle
     */
    @Override
    public Set<Evaluation> getEvaluationsPizza(Pizza pizza) {
        if (pizza == null) return null;
        return new HashSet<>(pizza.getEvaluations());
    }

    /**
     * Calcule la note moyenne d'une pizza.
     *
     * @param pizza pizza concernée
     * @return la note moyenne, -1 si aucune évaluation, -2 si pizza nulle
     */
    @Override
    public double getNoteMoyenne(Pizza pizza) {
        if (pizza == null) return -2;
        if (pizza.getEvaluations().isEmpty()) return -1;

        return pizza.getEvaluations().stream()
                .mapToInt(Evaluation::getNote)
                .average()
                .orElse(-1);
    }

    /**
     * Ajoute une évaluation à une pizza.
     *
     * @param pizza pizza évaluée
     * @param note note attribuée
     * @param commentaire commentaire optionnel
     * @return {@code true} si l'évaluation est ajoutée, {@code false} sinon
     * @throws NonConnecteException si aucun client n'est connecté
     * @throws CommandeException si la pizza n'a jamais été commandée
     */
    @Override
    public boolean ajouterEvaluation(Pizza pizza, int note, String commentaire)
            throws NonConnecteException, CommandeException {

        if (clientConnecte == null) {
            throw new NonConnecteException();
        }
        if (pizza == null || note < 0 || note > 5) {
            return false;
        }

        boolean aCommande = clientConnecte.getCommandes().stream()
                .filter(c -> c.getEtat() == EtatCommande.TRAITEE)
                .flatMap(c -> c.getPizzas().stream())
                .anyMatch(p -> p.equals(pizza));

        if (!aCommande) {
            throw new CommandeException("Pizza jamais commandée");
        }

        boolean dejaEvaluee = pizza.getEvaluations().stream()
                .anyMatch(e -> e.getClient().equals(clientConnecte));

        if (dejaEvaluee) {
            return false;
        }

        pizza.ajouterEvaluation(
                new Evaluation(clientConnecte, pizza, note, commentaire)
        );
        return true;
    }
}

