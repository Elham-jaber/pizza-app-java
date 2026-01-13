package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pizzas.GestionClient;
import pizzas.GestionPizzaiolo;

/**
 * okokoko 
 * Classe exécutable qui lance l'interface graphique de l'application.
 */
public final class MainInterface extends Application {

    private GestionClient gestionClient;
    private GestionPizzaiolo gestionPizzaiolo;

    /** 
     * Fenêtre client
     */
    private void startFenetreClient() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("client.fxml"));
            VBox root = loader.load();

            ClientControleur controleur = loader.getController();
            controleur.setGestionClient(gestionClient);
            controleur.setGestionPizzaiolo(gestionPizzaiolo);

            Stage stage = new Stage();
            stage.setTitle("Client – Commandes de pizzas");
            stage.setScene(new Scene(root, 1210, 620));
            stage.setResizable(true);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace(); // IMPORTANT
        }
    }

    /**
     * Fenêtre pizzaiolo (fenêtre principale)
     */
    private void startFenetrePizzaiolo(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ui/pizzaiolo.fxml"));
            VBox root = loader.load();

            PizzaioloControleur controleur = loader.getController();
            controleur.setGestionPizzaiolo(gestionPizzaiolo);
            controleur.setGestionClient(gestionClient);

            primaryStage.setTitle("Pizzaiolo – Gestion");
            primaryStage.setScene(new Scene(root, 985, 630));
            primaryStage.setResizable(true);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    



  

    }

    /**
     * Point d'entrée JavaFX
     */
    @Override
    public void start(Stage primaryStage) {

   
        // BACKEND PARTAGÉ (UNE SEULE FOIS)
      
        gestionPizzaiolo = new GestionPizzaiolo();
        gestionClient = new GestionClient(gestionPizzaiolo.getPizzas());

      
        gestionClient.setGestionPizzaiolo(gestionPizzaiolo);

        // =========================
        // LANCEMENT DES FENÊTRES
        // =========================
        startFenetrePizzaiolo(primaryStage); // fenêtre principale
        startFenetreClient();                // fenêtre secondaire
    }


    /**
     * Méthode main
     */
    public static void main(String[] args) {
        launch(args);
    }
}

