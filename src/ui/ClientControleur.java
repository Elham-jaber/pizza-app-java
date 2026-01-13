package ui;

import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import pizzas.*;

/**
 * Contrôleur JavaFX de la fenêtre client.
 * <p>
 * Cette classe permet au client :
 * <ul>
 *   <li>de s'inscrire et de se connecter</li>
 *   <li>de consulter les pizzas disponibles</li>
 *   <li>de filtrer les pizzas</li>
 *   <li>de créer et gérer ses commandes</li>
 *   <li>d'évaluer les pizzas</li>
 * </ul>
 * </p>
 */
public class ClientControleur {

    /**
     * Gestionnaire métier côté client.
     */
    private GestionClient gestionClient;

    /**
     * Gestionnaire métier côté pizzaiolo.
     */
    private GestionPizzaiolo gestionPizzaiolo;

    /**
     * Commande actuellement en cours.
     */
    private Commande commandeEnCours;

    /**
     * Pizza actuellement sélectionnée.
     */
    private Pizza pizzaSelectionnee;

    // =====================
    // FXML
    // =====================

    /** Liste des pizzas affichées */
    @FXML private ListView<String> listePizzas;

    /** Liste des commandes */
    @FXML private ListView<String> listeCommandes;

    /** Liste des évaluations */
    @FXML private ListView<String> listeEvaluations;

    /** Choix du type de pizza pour le filtre */
    @FXML private ChoiceBox<String> choiceBoxFiltreType;

    /** Choix de la note pour l'évaluation */
    @FXML private ChoiceBox<Integer> choiceBoxNoteEvaluation;

    /** Champ email du client */
    @FXML private TextField entreeEmailClient;

    /** Champ mot de passe du client */
    @FXML private TextField entreeMotDePasseClient;

    /** Champ nom du client */
    @FXML private TextField entreeNomClient;

    /** Champ prénom du client */
    @FXML private TextField entreePrenomClient;

    /** Champ âge du client */
    @FXML private TextField entreeAgeClient;

    /** Champ adresse du client */
    @FXML private TextField entreeAdresseClient;

    /** Champ filtre prix maximum */
    @FXML private TextField entreeFiltrePrixMax;

    /** Champ filtre ingrédient */
    @FXML private TextField entreeFiltreContientIngredient;

    /** Zone de texte du commentaire d'évaluation */
    @FXML private TextArea texteCommentaireEvaluation;

    /** Champ affichant le nom de la pizza sélectionnée */
    @FXML private TextField entreeNomPizza;

    /** Champ affichant le prix de la pizza sélectionnée */
    @FXML private TextField entreePrixPizza;

    /** Champ affichant le type de la pizza sélectionnée */
    @FXML private TextField entreeTypePizza;

    /** Champ affichant la note moyenne de la pizza */
    @FXML private TextField entreeNoteMoyennePizza;

    /** Liste des ingrédients de la pizza sélectionnée */
    @FXML private ListView<String> listeIngredients;

    // =====================
    // INITIALISATION
    // =====================

    /**
     * Méthode appelée automatiquement lors de l'initialisation
     * du contrôleur JavaFX.
     */
    @FXML
    void initialize() {
        choiceBoxFiltreType.getItems().addAll("VIANDE", "VEGETARIENNE", "REGIONALE");
        choiceBoxNoteEvaluation.getItems().addAll(0,1,2,3,4,5);
    }

    // =====================
    // AUTHENTIFICATION
    // =====================

    /**
     * Injecte le gestionnaire pizzaiolo.
     *
     * @param gp gestionnaire pizzaiolo
     */
    public void setGestionPizzaiolo(GestionPizzaiolo gp) {
        this.gestionPizzaiolo = gp;
    }

    /**
     * Gère l'action de connexion du client.
     *
     * @param event événement JavaFX
     */
    @FXML
    void actionBoutonConnexion(ActionEvent event) {
        if (gestionClient.connexion(
                entreeEmailClient.getText(),
                entreeMotDePasseClient.getText())) {
            show("Connexion réussie");
        } else {
            show("Email ou mot de passe incorrect");
        }
    }

    /**
     * Gère la déconnexion du client.
     *
     * @param event événement JavaFX
     */
    @FXML
    void actionBoutonDeconnexion(ActionEvent event) {
        try {
            gestionClient.deconnexion();
            show("Déconnexion réussie");
        } catch (NonConnecteException e) {
            show("Aucun client connecté");
        }
    }

    /**
     * Gère la sélection d'une pizza dans la liste.
     *
     * @param event événement souris
     */
    @FXML
    void actionSelectionPizza(MouseEvent event) {
        int index = listePizzas.getSelectionModel().getSelectedIndex();
        if (index < 0) return;

        pizzaSelectionnee = gestionClient
                .getPizzas()
                .stream()
                .toList()
                .get(index);

        afficherPizzaSelectionnee();
    }

    /**
     * Gère l'inscription d'un nouveau client.
     *
     * @param event événement JavaFX
     */
    @FXML
    void actionBoutonInscription(ActionEvent event) {
        int age;
        try {
            age = Integer.parseInt(entreeAgeClient.getText());
        } catch (Exception e) {
            show("Âge invalide");
            return;
        }

        InformationPersonnelle info = new InformationPersonnelle(
                entreeNomClient.getText(),
                entreePrenomClient.getText(),
                entreeAdresseClient.getText(),
                age
        );

        int res = gestionClient.inscription(
                entreeEmailClient.getText(),
                entreeMotDePasseClient.getText(),
                info
        );

        switch (res) {
            case 0 -> show("Inscription réussie");
            case -1 -> show("Tous les champs sont obligatoires et l’âge doit être > 0");
            case -2 -> show("Mot de passe trop court (minimum 8 caractères)");
            case -3 -> show("Email invalide");
            case -4 -> show("Email déjà utilisé");
            default -> show("Erreur inconnue");
        }
    }

    // =====================
    // PIZZAS
    // =====================

    /**
     * Affiche toutes les pizzas disponibles.
     *
     * @param event événement JavaFX
     */
    @FXML
    void actionBoutonAfficherToutesPizzas(ActionEvent event) {
        listePizzas.getItems().clear();
        for (Pizza p : gestionClient.getPizzas()) {
            listePizzas.getItems().add(
                p.getNom() + " (" + p.getType() + ") - " + p.getPrixVente() + " €"
            );
        }
    }

    /**
     * Affiche les informations détaillées de la pizza sélectionnée.
     */
    private void afficherPizzaSelectionnee() {
        if (pizzaSelectionnee == null) return;

        entreeNomPizza.setText(pizzaSelectionnee.getNom());
        entreePrixPizza.setText(
                String.format("%.2f €", pizzaSelectionnee.getPrixVente())
        );
        entreeTypePizza.setText(pizzaSelectionnee.getType().toString());

        listeIngredients.getItems().clear();
        pizzaSelectionnee.getIngredients().forEach(i ->
                listeIngredients.getItems().add(i.getNom())
        );

        double note = gestionClient.getNoteMoyenne(pizzaSelectionnee);
        entreeNoteMoyennePizza.setText(
                note >= 0 ? String.format("%.1f / 5", note) : "-"
        );
    }

    // =====================
    // COMMANDES
    // =====================

    /**
     * Crée une nouvelle commande pour le client connecté.
     *
     * @param event événement JavaFX
     */
    @FXML
    void actionBoutonCreerNouvelleCommande(ActionEvent event) {
        try {
            commandeEnCours = gestionClient.debuterCommande();
            show("Nouvelle commande créée");
        } catch (NonConnecteException e) {
            show("Connectez-vous d'abord");
        }
    }

    /**
     * Ajoute la pizza sélectionnée à la commande en cours.
     *
     * @param event événement JavaFX
     */
    @FXML
    void actionBoutonAjouterPizzaSelectionneeCommande(ActionEvent event) {
        if (pizzaSelectionnee == null || commandeEnCours == null) {
            show("Sélectionnez une pizza et une commande");
            return;
        }
        try {
            gestionClient.ajouterPizza(pizzaSelectionnee, 1, commandeEnCours);
            show("Pizza ajoutée");
        } catch (Exception e) {
            show(e.getMessage());
        }
    }

    /**
     * Valide la commande en cours.
     *
     * @param event événement JavaFX
     */
    @FXML
    void actionBoutonValiderCommandeEnCours(ActionEvent event) {
        try {
            gestionClient.validerCommande(commandeEnCours);
            gestionPizzaiolo.enregistrerCommande(commandeEnCours);
            show("Commande validée");
            commandeEnCours = null;
        } catch (Exception e) {
            show(e.getMessage());
        }
    }

    // =====================
    // SETTERS & UTILS
    // =====================

    /**
     * Injecte le gestionnaire client.
     *
     * @param gestionClient gestionnaire client
     */
    public void setGestionClient(GestionClient gestionClient) {
        this.gestionClient = gestionClient;
    }

    /**
     * Affiche une boîte de dialogue d'information.
     *
     * @param msg message à afficher
     */
    private void show(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    /**
     * Méthode requise par le FXML (sélection commande).
     *
     * @param event événement souris
     */
    @FXML
    void actionSelectionCommande(MouseEvent event) {
        // méthode requise par le FXML
    }

    /**
     * Méthode requise par le FXML (sélection évaluation).
     *
     * @param event événement souris
     */
    @FXML
    void actionSelectionEvaluation(MouseEvent event) {
        // méthode requise par le FXML
    }
}
