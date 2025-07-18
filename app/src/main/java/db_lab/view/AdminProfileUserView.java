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

    private User user; // Utente corrente visualizzato
    private Controller controller; // Controller per logica e dati

    // Etichette per mostrare i dati dell'utente
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
    private ImageView imageView; // Immagine dell'utente

    @Override
    public Controller getController() {
        return this.controller;
    }

    /**
     * Metodo chiamato per impostare il controller e l'utente da mostrare.
     * Aggiorna tutte le label con i dati dell'utente.
     */
    public void setParameter(Controller controller, User user) {
        this.user = user;
        this.controller = controller;

        // Imposta i testi delle label con i dati dell'utente
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

        // Imposta immagine statica per l'utente
        String imagePath = "/images/user.jpeg";

        try {
            imageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
        } catch (Exception e) {
            System.out.println("Immagine non trovata: " + imagePath);
            // Se immagine user.jpeg non trovata, carica immagine di default
            imageView.setImage(new Image(getClass().getResource("/images/default.jpeg").toExternalForm()));
        }
    }

    /**
     * Gestisce il click sul pulsante "Users" per tornare alla vista degli utenti.
     */
    @FXML
    public void handleUsersButton(final ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/AdminUsersView.fxml"));
            Parent root = loader.load();

            AdminUsersView controller = loader.getController();
            controller.setParameter(getController(), user);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestisce il click sul pulsante "Products" per passare alla vista prodotti.
     */
    @FXML
    public void handleProductsButton(final ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/AdminProductsView.fxml"));
            Parent root = loader.load();

            AdminProductsView controller = loader.getController();
            controller.setParameter(getController(), user);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestisce il click sul pulsante per modificare lo stato dell'utente (es.
     * attivo/disabilitato).
     * Invoca il metodo del controller per eseguire il toggle dello stato.
     */
    @FXML
    public void handleUserStateChangeButton(final ActionEvent event) {
        this.getController().toggleUserStatus(user);
    }

}
