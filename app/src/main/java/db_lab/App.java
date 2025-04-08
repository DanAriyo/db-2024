package db_lab;

import javafx.stage.Stage;
import db_lab.controller.Controller;
import db_lab.controller.ControllerImpl;
import db_lab.view.LoginView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class App extends Application {

    private Controller controller = new ControllerImpl();

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/LoginView.fxml"));
        Parent root = loader.load();

        LoginView controller = loader.getController();
        controller.setController(this.controller);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

}
