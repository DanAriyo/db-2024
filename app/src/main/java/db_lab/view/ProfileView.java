package db_lab.view;

// Import delle classi necessarie
import java.io.IOException;
import db_lab.controller.Controller;
import db_lab.data.dataentity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProfileView implements View {

    // Riferimenti al controller e all'utente attuale
    private Controller controller;
    private User currentUser;

    // Riferimento alla VBox definita nel file FXML
    @FXML
    VBox vBox;

    // Restituisce il controller associato alla vista
    @Override
    public Controller getController() {
        return this.controller;
    }

    // Imposta il controller e l'utente corrente
    public void setParameters(Controller controller, User user) {
        this.controller = controller;
        this.currentUser = user;
    }

    // Gestisce il click sul pulsante "Vendi"
    @FXML
    public void handleSellButton(final ActionEvent event) {
        try {
            // Carica il layout della vista di vendita
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/SellView.fxml"));
            Parent root = loader.load();

            // Imposta i parametri nel controller della vista di vendita
            SellView controller = loader.getController();
            controller.setParameter(getController(), currentUser);

            // Ottiene la finestra corrente e imposta la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Stampa l'errore in caso di problemi con il caricamento
        }
    }

    // Gestisce il click sul pulsante "Home"
    @FXML
    public void handleHomeButton(final ActionEvent event) {
        try {
            // Carica il layout della vista home
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/HomeView.fxml"));
            Parent root = loader.load();

            // Imposta i parametri nel controller della vista home
            HomeView controller = loader.getController();
            controller.setParameter(getController(), currentUser);

            // Mostra la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Gestisce il click sul pulsante "Saldo"
    @FXML
    public void handleBalanceButton(final ActionEvent event) {
        try {
            // Carica il layout della vista lista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ListView.fxml"));
            Parent root = loader.load();

            // Imposta i parametri specifici per la visualizzazione del saldo
            ListView controller = loader.getController();
            controller.setBalanceParameters(this.controller, this.currentUser,
                    this.controller.getBalanceById(this.currentUser.getIdSaldo()));

            // Mostra la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Gestisce il click sul pulsante "Conto Bancario"
    @FXML
    public void handleBankAccountButton(final ActionEvent event) {
        try {
            // Carica il layout della vista lista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ListView.fxml"));
            Parent root = loader.load();

            // Imposta i parametri specifici per il conto bancario
            ListView controller = loader.getController();
            controller.setBankAccountParameters(this.controller, this.currentUser,
                    this.controller.getBankAccountById(this.currentUser.getIban()));

            // Mostra la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Gestisce il click sul pulsante "Recensioni"
    @FXML
    public void handleReviewsButton(final ActionEvent event) {
        try {
            // Carica il layout della vista lista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ListView.fxml"));
            Parent root = loader.load();

            // Imposta i parametri specifici per la visualizzazione delle recensioni
            ListView controller = loader.getController();
            controller.setReviewsParameters(getController(), currentUser,
                    this.controller.getReviewsById(this.currentUser.getId()));

            // Mostra la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Gestisce il click sul pulsante "Transazioni"
    @FXML
    public void handleTransactionsButton(final ActionEvent event) {
        try {
            // Carica il layout della vista lista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ListView.fxml"));
            Parent root = loader.load();

            // Imposta i parametri specifici per la visualizzazione delle transazioni
            ListView controller = loader.getController();
            controller.setTransactionsParameters(getController(), currentUser,
                    this.controller.getTransactionsById(this.currentUser.getId()));

            // Mostra la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
// Gestisce il click sul
