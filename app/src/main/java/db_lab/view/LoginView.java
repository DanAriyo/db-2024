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

    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameLogin = new TextField();

    @FXML
    private TextField passwordLogin = new TextField();

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

    @FXML
    public void handleLoginButton(final ActionEvent event) throws SQLException {

        if (isFieldEmpty(passwordLogin) && isFieldEmpty(usernameLogin)) {
            this.controller.newUser(setFictionalUser());
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/HomeView.fxml"));
                Parent root = loader.load();

                HomeView controller = loader.getController();
                controller.setParameter(getController(),
                        this.controller.getUser(username.getText(), password.getText()));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (usernameLogin.getText().equals("admin") && passwordLogin.getText().equals("admin03")) {
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/AdminUsersView.fxml"));
                    Parent root = loader.load();

                    AdminUsersView controller = loader.getController();
                    controller.setParameter(getController(),
                            this.controller.getUser(usernameLogin.getText(), passwordLogin.getText()));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (this.controller.checkLogin(usernameLogin.getText(), passwordLogin.getText())) {
                    try {

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/HomeView.fxml"));
                        Parent root = loader.load();

                        HomeView controller = loader.getController();
                        controller.setParameter(getController(),
                                this.controller.getUser(usernameLogin.getText(), passwordLogin.getText()));

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

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @FXML
    public void initialize() {

    }

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

        return (isLoginFilled && !isRegistrationFilled) || (!isLoginFilled && isRegistrationFilled);
    }

    private boolean isFieldEmpty(TextField textField) {
        return textField.getText() == null || textField.getText().trim().isEmpty();
    }

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
