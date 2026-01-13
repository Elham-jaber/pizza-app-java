

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pizzas.GestionClient;
import pizzas.GestionPizzaiolo;
import ui.ClientControleur;
import ui.PizzaioloControleur;

public class MainPizzas extends Application {

    @Override
    public void start(Stage primaryStage) {

      
        // BACKEND PARTAGÉ

        GestionPizzaiolo gestionPizzaiolo = new GestionPizzaiolo();
        GestionClient gestionClient =
                new GestionClient(gestionPizzaiolo.getPizzas());

        try {
         
            // FENÊTRE PIZZAIOLO

            FXMLLoader loaderP = new FXMLLoader(
                    getClass().getResource("pizzaiolo.fxml"));
            VBox rootP = loaderP.load();

            PizzaioloControleur pizzaioloControleur =
                    loaderP.getController();
            pizzaioloControleur.setGestionPizzaiolo(gestionPizzaiolo);
            pizzaioloControleur.setGestionClient(gestionClient);

            Stage stagePizzaiolo = new Stage();
            stagePizzaiolo.setTitle("Pizzaiolo - Gestion");
            stagePizzaiolo.setScene(new Scene(rootP, 985, 630));
            stagePizzaiolo.show();

  
            // FENÊTRE CLIENT
      
            FXMLLoader loaderC = new FXMLLoader(
                    getClass().getResource("client.fxml"));
            VBox rootC = loaderC.load();

            ClientControleur clientControleur =
                    loaderC.getController();
            clientControleur.setGestionClient(gestionClient);
            clientControleur.setGestionPizzaiolo(gestionPizzaiolo);

            Stage stageClient = new Stage();
            stageClient.setTitle("Client - Commandes");
            stageClient.setScene(new Scene(rootC, 1210, 620));
            stageClient.show();

        } catch (Exception e) {
            e.printStackTrace(); // IMPORTANT pour debug
        }
    }

    public static void main(String[] args) {
        launch(args); // OBLIGATOIRE
    }
}
