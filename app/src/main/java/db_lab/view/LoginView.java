package db_lab.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import db_lab.controller.Controller;
import db_lab.data.dataentity.User;

public class LoginView implements View {

    private Controller controller;

    // Pulsante per effettuare il login
    @FXML
    private Button loginButton;

    // Campi di testo per login
    @FXML
    private TextField usernameLogin = new TextField();
    @FXML
    private TextField passwordLogin = new TextField();

    // Campi di testo per registrazione utente
    @FXML
    private TextField username = new TextField();
    @FXML
    private TextField password = new TextField();
    @FXML
    private TextField confirmPassword = new TextField();
    @FXML
    private TextField nome = new TextField();
    @FXML
    private TextField cognome = new TextField();
    @FXML
    private TextField email = new TextField();
    @FXML
    private TextField indirizzo = new TextField();
    @FXML
    private TextField telefono = new TextField();
    @FXML
    private TextField codiceFiscale = new TextField();

    // Gestore evento click sul bottone login
    @FXML
    public void handleLoginButton(final ActionEvent event) throws SQLException {

        // Se i campi di login sono vuoti, crea un nuovo utente con i dati di
        // registrazione
        if (isFieldEmpty(passwordLogin) && isFieldEmpty(usernameLogin)) {
            this.controller.newUser(setFictionalUser());
            try {
                // Carica la vista HomeView dopo la registrazione
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/HomeView.fxml"));
                Parent root = loader.load();

                // Passa il controller e l'utente alla nuova vista
                HomeView controller = loader.getController();
                controller.setParameter(getController(),
                        this.controller.getUser(username.getText(), password.getText()));

                // Imposta la nuova scena e mostra la finestra
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Se username e password sono "admin", carica la vista amministratore
            if (usernameLogin.getText().equals("admin") && passwordLogin.getText().equals("admin03")) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/AdminUsersView.fxml"));
                    Parent root = loader.load();

                    // Passa il controller e l'utente admin alla vista admin
                    AdminUsersView controller = loader.getController();
                    controller.setParameter(getController(),
                            this.controller.getUser(usernameLogin.getText(), passwordLogin.getText()));

                    // Imposta la nuova scena e mostra la finestra
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Se login utente valido, carica la HomeView
                if (this.controller.checkLogin(usernameLogin.getText(), passwordLogin.getText())) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/HomeView.fxml"));
                        Parent root = loader.load();

                        // Passa controller e utente alla vista home
                        HomeView controller = loader.getController();
                        controller.setParameter(getController(),
                                this.controller.getUser(usernameLogin.getText(), passwordLogin.getText()));

                        // Imposta la scena e mostra la finestra
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    @Override
    public Controller getController() {
        return this.controller;
    }

    // Setter per il controller
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @FXML
    public void initialize() {
        // Metodo chiamato all'inizializzazione della view (vuoto)
    }

    // Verifica se i campi di login o registrazione sono correttamente compilati
    // (solo uno dei due gruppi)
    public boolean areGroupsValid(TextField usernameLogin, TextField passwordLogin,
            TextField username, TextField password, TextField confirmPassword,
            TextField nome, TextField cognome, TextField email,
            TextField indirizzo, TextField telefono, TextField codiceFiscale) {

        boolean isLoginFilled = !isFieldEmpty(usernameLogin) && !isFieldEmpty(passwordLogin);

        boolean isRegistrationFilled = !isFieldEmpty(username) && !isFieldEmpty(password) &&
                !isFieldEmpty(confirmPassword) && !isFieldEmpty(nome) &&
                !isFieldEmpty(cognome) && !isFieldEmpty(email) &&
                !isFieldEmpty(indirizzo) && !isFieldEmpty(telefono) &&
                !isFieldEmpty(codiceFiscale);

        // Verifica che sia compilato solo login oppure solo registrazione
        return (isLoginFilled && !isRegistrationFilled) || (!isLoginFilled && isRegistrationFilled);
    }

    // Controlla se un campo TextField è vuoto o nullo
    private boolean isFieldEmpty(TextField textField) {
        return textField.getText() == null || textField.getText().trim().isEmpty();
    }

    // Crea un oggetto User con i dati raccolti dai campi di registrazione
    public User setFictionalUser() {

        String usernameText = username.getText();
        String confirmPasswordText = confirmPassword.getText();
        String nomeText = nome.getText();
        String cognomeText = cognome.getText();
        String emailText = email.getText();
        String indirizzoText = indirizzo.getText();
        String telefonoText = telefono.getText();
        String codiceFiscaleText = codiceFiscale.getText();

        User user = new User(0, nomeText, cognomeText, emailText, 0, 0, telefonoText, indirizzoText, codiceFiscaleText,
                usernameText, confirmPasswordText);

        return user;

    }

}
