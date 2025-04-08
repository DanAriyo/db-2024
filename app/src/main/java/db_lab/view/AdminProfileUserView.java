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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AdminProfileUserView implements View {

    private User user;
    private Controller controller;

    @FXML
    private Label idUtente;

    @FXML
    private Label nome;

    @FXML
    private Label cognome;

    @FXML
    private Label email;

    @FXML
    private Label telefono;

    @FXML
    private Label indirizzo;

    @FXML
    private Label codiceFiscale;

    @FXML
    private Label iban;

    @FXML
    private Label idSaldo;

    @FXML
    private Label username;

    @FXML
    private Label password;

    @FXML
    private ImageView imageView;

    @Override
    public Controller getController() {
        return this.controller;
    }

    public void setParameter(Controller controller, User user) {
        this.user = user;
        this.controller = controller;
        this.idUtente.setText("IDUTENTE: " + String.valueOf(user.getId()));
        this.nome.setText("NOME: " + user.getNome());
        this.cognome.setText("COGNOME: " + user.getCognome());
        this.email.setText("EMAIL: " + user.getEmail());
        this.telefono.setText("TELEFONO: " + user.getTelefono());
        this.indirizzo.setText("INDIRIZZO: " + user.getIndirizzo());
        this.codiceFiscale.setText("CODICE FISCALE: " + user.getCf());
        this.iban.setText("IBAN: " + String.valueOf(user.getIban()));
        this.idSaldo.setText("IDSALDO: " + String.valueOf(user.getIdSaldo()));
        this.username.setText("USERNAME: " + user.getUsername());
        this.password.setText("PASSWORD: " + user.getPassword());
        String imagePath = "/images/user.jpeg";

        try {
            imageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
        } catch (Exception e) {
            System.out.println("Immagine non trovata: " + imagePath);
            imageView.setImage(new Image(getClass().getResource("/images/default.jpeg").toExternalForm()));
        }
    }

    @FXML
    public void handleUsersButton(final ActionEvent event) {
        try {
            // Carica il file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/AdminUsersView.fxml"));
            Parent root = loader.load();

            AdminUsersView controller = loader.getController();
            controller.setParameter(getController(), user);

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
    public void handleProductsButton(final ActionEvent event) {
        try {
            // Carica il file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/AdminProductsView.fxml"));
            Parent root = loader.load();

            AdminProductsView controller = loader.getController();
            controller.setParameter(getController(), user);

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
    public void handleUserStateChangeButton(final ActionEvent event) {
        this.getController().toggleUserStatus(user);
    }

}
