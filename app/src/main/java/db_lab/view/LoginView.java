package db_lab.view;

import java.awt.Button;
import javafx.fxml.FXML;
import db_lab.controller.Controller;
import db_lab.model.Model;

public class LoginView implements View {

    private Controller controller;
    private Model model;

    private Button LoginButton;

    public LoginView(Controller controller, Model model) {
        this.controller = controller;
        this.model = model;
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public Controller getController() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getController'");
    }

    @Override
    public Model getModel() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getModel'");
    }

}
