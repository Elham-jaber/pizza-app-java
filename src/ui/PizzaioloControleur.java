package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pizzas.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur JavaFX de l'interface pizzaiolo.
 * <p>
 * Cette classe gère :
 * <ul>
 *   <li>la création et la modification des pizzas</li>
 *   <li>la gestion des ingrédients</li>
 *   <li>le traitement des commandes</li>
 *   <li>l'affichage des statistiques</li>
 *   <li>la sauvegarde et le chargement des données</li>
 * </ul>
 * </p>
 */
public class PizzaioloControleur {

    /**
     * Gestionnaire métier du pizzaiolo.
     */
    private GestionPizzaiolo gestionPizzaiolo;

    /**
     * Gestionnaire métier client.
     */
    private GestionClient gestionClient;

    // ======================
    // FXML
    // ======================

    /** Champ affichant le bénéfice généré par un client */
    @FXML private TextField entreeBeneficeClient;

    /** Champ affichant le bénéfice total d'une pizza */
    @FXML private TextField entreeBeneficeTotalPizza;

    /** Champ affichant le bénéfice unitaire d'une pizza */
    @FXML private TextField entreeBeneficeUnitairePizza;

    /** Champ de saisie du nom de la pizza */
    @FXML private TextField entreeNomPizza;

    /** Sélecteur du type de pizza */
    @FXML private ChoiceBox<TypePizza> choiceBoxTypePizza;

    /** Liste des pizzas */
    @FXML private ListView<String> listePizzas;

    /** Libellé de la liste des pizzas */
    @FXML private Label labelListePizzas;

    /** Champ affichant le prix minimal d'une pizza */
    @FXML private TextField entreePrixMinimalPizza;

    /** Champ de saisie du prix de vente */
    @FXML private TextField entreePrixVentePizza;

    /** Pizza actuellement sélectionnée */
    private Pizza pizzaSelectionnee;

    /** Libellé de la liste des ingrédients */
    @FXML private Label labelListeIngredients;

    /** Champ du chemin de la photo de la pizza */
    @FXML private TextField entreePhotoPizza;

    /** Liste déroulante des clients */
    @FXML private ComboBox<InformationPersonnelle> comboBoxClients;

    /** Libellé de la liste des commandes */
    @FXML private Label labelListeCommandes;

    /** Liste des commandes */
    @FXML private ListView<String> listeCommandes;

    /** Commande actuellement sélectionnée */
    @FXML private Commande commandeSelectionnee;

    /** Champ affichant le nombre de pizzas commandées par client */
    @FXML private TextField entreeNbPizzasClient;

    /** Champ affichant le bénéfice d'une commande */
    @FXML private TextField entreeBeneficeCommande;

    /** Liste interne des commandes affichées */
    private List<Commande> commandesAffichees = new ArrayList<>();

    // ======================
    // INITIALISATION
    // ======================

    /** Champ de saisie du nom de l'ingrédient */
    @FXML private TextField entreeNomIngredient;

    /** Champ de saisie du prix de l'ingrédient */
    @FXML private TextField entreePrixIngredient;

    /** Liste des ingrédients */
    @FXML private ListView<String> listeIngredients;

    /** Sélecteur de type de pizza pour les ingrédients */
    @FXML private ChoiceBox<TypePizza> choiceBoxTypeIngredient;

    /** Ingrédient actuellement sélectionné */
    private Ingredient ingredientSelectionne;

    /** Champ affichant le nombre de commandes d'une pizza */
    @FXML private TextField entreeNbCommandesPizza;

    /** Champ affichant le nombre total de commandes */
    @FXML private TextField entreeNombreTotalCommandes;

    /** Champ affichant le bénéfice total des commandes */
    @FXML private TextField entreeBeneficeTotalCommandes;

    /**
     * Méthode appelée automatiquement à l'initialisation du contrôleur.
     */
    @FXML
    void initialize() {
        choiceBoxTypeIngredient.getItems().setAll(TypePizza.values());
        choiceBoxTypePizza.getItems().setAll(TypePizza.values());
        pizzaSelectionnee = null;
        comboBoxClients.getItems().clear();
        labelListeCommandes.setText("...");
    }

    /**
     * Affiche une boîte de dialogue d'information.
     *
     * @param message message à afficher
     */
    private void show(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Gère la sélection d'un ingrédient dans la liste.
     *
     * @param event événement souris
     */
    @FXML
    void actionListeSelectionIngredient(MouseEvent event) {
        int index = listeIngredients.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            ingredientSelectionne = null;
            return;
        }

        String ligne = listeIngredients.getItems().get(index);
        String nomIngredient = ligne.split("\\(")[0].trim();

        for (Ingredient i : gestionPizzaiolo.getIngredients()) {
            if (i.getNom().equalsIgnoreCase(nomIngredient)) {
                ingredientSelectionne = i;
                return;
            }
        }
        ingredientSelectionne = null;
    }

    /**
     * Gère la sélection d'une pizza dans la liste.
     *
     * @param event événement souris
     */
    @FXML
    void actionListeSelectionPizza(MouseEvent event) {
        int index = listePizzas.getSelectionModel().getSelectedIndex();
        if (index < 0) return;

        pizzaSelectionnee =
                gestionPizzaiolo.getPizzas().stream().toList().get(index);

        entreeNomPizza.setText(pizzaSelectionnee.getNom());
        choiceBoxTypePizza.setValue(pizzaSelectionnee.getType());
        entreePrixVentePizza.setText(
                String.valueOf(pizzaSelectionnee.getPrixVente()));
    }

    /**
     * Rafraîchit la liste des clients affichés.
     */
    public void rafraichirClients() {
        comboBoxClients.getItems().clear();
        comboBoxClients.getItems().addAll(
                gestionPizzaiolo.ensembleClients()
        );
    }

    /**
     * Injecte le gestionnaire pizzaiolo.
     *
     * @param gp gestionnaire pizzaiolo
     */
    public void setGestionPizzaiolo(GestionPizzaiolo gp) {
        this.gestionPizzaiolo = gp;
        rafraichirClients();
    }

    /**
     * Injecte le gestionnaire client.
     *
     * @param gestionClient gestionnaire client
     */
    public void setGestionClient(GestionClient gestionClient) {
        this.gestionClient = gestionClient;
    }

    /**
     * Quitte l'application.
     *
     * @param event événement action
     */
    @FXML
    void actionMenuQuitter(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Charge les données depuis un fichier.
     *
     * @param event événement action
     */
    @FXML
    void actionMenuCharger(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Charger les données");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier données (.dat)", ".dat")
        );

        Stage stage = (Stage) listePizzas.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) return;

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {

            gestionPizzaiolo = (GestionPizzaiolo) ois.readObject();
            rafraichirClients();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sauvegarde les données dans un fichier.
     *
     * @param event événement action
     */
    @FXML
    void actionMenuSauvegarder(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder les données");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier données (.dat)", ".dat")
        );

        Stage stage = (Stage) listePizzas.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file == null) return;

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(file))) {

            oos.writeObject(gestionPizzaiolo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche la fenêtre "À propos".
     *
     * @param event événement action
     */
    @FXML
    void actionMenuApropos(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("À propos");
        alert.setHeaderText(null);
        alert.setContentText("Application 4BytesOfPizza\nProjet JavaFX");
        alert.showAndWait();
    }
}
