package db_lab.view;

import java.awt.Button;
import java.awt.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import db_lab.controller.Controller;
import db_lab.model.Model;

public class LoginView implements View {

    private Controller controller;
    private Model model;

    @FXML
    private Button loginButton;

    // private Stage stage;

    public LoginView(Controller controller, Model model) {
        this.controller = controller;
        this.model = model;
    }

    public void handleLoginButton(final ActionEvent event) {

        // stage = (Stage) ((javafx.scene.Node)
        // event.getSource()).getScene().getWindow();
        // View homeView = new HomeView(stage);
        // homeView.show();
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public Controller getController() {
        return this.controller;
    }

    @Override
    public Model getModel() {
        return this.model;
    }

}
