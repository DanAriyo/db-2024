package db_lab.view;

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

    private Controller controller;
    private User currentUser;

    @FXML
    VBox vBox;

    @Override
    public Controller getController() {
        return this.controller;
    }

    public void setParameters(Controller controller, User user) {
        this.controller = controller;
        this.currentUser = user;
    }

    @FXML
    public void handleSellButton(final ActionEvent event) {
        try {
            // Carica il file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/SellView.fxml"));
            Parent root = loader.load();

            SellView controller = loader.getController();
            controller.setParameter(getController(), currentUser);

            // Ottieni la finestra attuale e imposta la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleHomeButton(final ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/HomeView.fxml"));
            Parent root = loader.load();

            HomeView controller = loader.getController();
            controller.setParameter(getController(), currentUser);

            // Ottieni la finestra attuale e imposta la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBalanceButton(final ActionEvent event) {
        try {
            // Carica il file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ListView.fxml"));
            Parent root = loader.load();

            ListView controller = loader.getController();

            controller.setBalanceParameters(this.controller, this.currentUser,
                    this.controller.getBalanceById(this.currentUser.getIdSaldo()));

            // Ottieni la finestra attuale e imposta la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBankAccountButton(final ActionEvent event) {
        try {
            // Carica il file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ListView.fxml"));
            Parent root = loader.load();

            ListView controller = loader.getController();

            controller.setBankAccountParameters(this.controller, this.currentUser,
                    this.controller.getBankAccountById(this.currentUser.getIban()));

            // Ottieni la finestra attuale e imposta la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleReviewsButton(final ActionEvent event) {
        try {
            // Carica il file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ListView.fxml"));
            Parent root = loader.load();

            ListView controller = loader.getController();

            controller.setReviewsParameters(getController(), currentUser,
                    this.controller.getReviewsById(this.currentUser.getId()));

            // Ottieni la finestra attuale e imposta la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleTransactionsButton(final ActionEvent event) {
        try {
            // Carica il file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ListView.fxml"));
            Parent root = loader.load();

            ListView controller = loader.getController();

            controller.setTransactionsParameters(getController(), currentUser,
                    this.controller.getTransactionsById(this.currentUser.getId()));

            // Ottieni la finestra attuale e imposta la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRewardButton(final ActionEvent event) {
        try {
            // Carica il file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ListView.fxml"));
            Parent root = loader.load();

            ListView controller = loader.getController();

            controller.setRewardsParameters(getController(), currentUser,
                    this.controller.getCreditRewardsById(currentUser.getId()),
                    this.controller.getSelfEvaluationsById(currentUser.getId()));

            // Ottieni la finestra attuale e imposta la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
