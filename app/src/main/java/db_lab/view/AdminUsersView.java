package db_lab.view;

import java.io.IOException;
import java.util.List;

import db_lab.controller.Controller;
import db_lab.data.dataentity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class AdminUsersView implements View {

    private Controller controller;
    private User currentUser;

    @FXML
    GridPane gridPane;

    @FXML
    Button usersButton;

    @FXML
    Button productsButton;

    @FXML
    ScrollPane scrollPane;

    @Override
    public Controller getController() {
        return this.controller;
    }

    public void setParameter(Controller controller, User user) {
        this.currentUser = user;
        this.controller = controller;
        this.populateGridPane(this.gridPane, this.controller.getUsers());
    }

    public void populateGridPane(GridPane gridPane, List<User> users) {
        gridPane.getChildren().clear();

        int columns = 2;
        int row = 0;
        int col = 0;

        double vBoxWidth = 160;
        double vBoxHeight = 250;
        double spacing = 10;

        gridPane.setVgap(spacing);
        gridPane.setHgap(spacing);

        for (User user : users) {
            // Creazione del VBox con dimensioni fisse
            VBox vbox = new VBox();
            vbox.setPrefWidth(vBoxWidth);
            vbox.setPrefHeight(vBoxHeight);
            vbox.setSpacing(5);
            vbox.setAlignment(Pos.CENTER);
            vbox.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px; -fx-padding: 10px;");

            // Creazione dell'ImageView
            ImageView imageView = new ImageView();
            imageView.setFitWidth(120);
            imageView.setFitHeight(120);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            String imagePath = "/images/user.jpeg";

            try {
                imageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
            } catch (Exception e) {
                System.out.println("Immagine non trovata: " + imagePath);
                imageView.setImage(new Image(getClass().getResource("/images/default.jpeg").toExternalForm()));
            }

            // StackPane per centrare l'immagine nel VBox
            StackPane imageContainer = new StackPane(imageView);
            imageContainer.setPrefSize(140, 140);
            imageContainer.setAlignment(Pos.CENTER);

            // Creazione delle Label
            Label nameLabel = new Label(user.getUsername());
            nameLabel.setPrefWidth(140);
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-alignment: center;");
            nameLabel.setWrapText(true);
            nameLabel.setTextAlignment(TextAlignment.CENTER);

            Button actionButton = new Button("AZIONE");
            actionButton.setPrefWidth(140);
            actionButton.setStyle(
                    "-fx-font-weight: bold; -fx-font-size: 12px; -fx-background-color: #27ae60; -fx-text-fill: white;");

            actionButton.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/AdminProfileUserView.fxml"));
                    Parent root = loader.load();

                    AdminProfileUserView controller = loader.getController();
                    controller.setParameter(getController(), user);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Aggiunta degli elementi al VBox
            vbox.getChildren().addAll(imageContainer, nameLabel, actionButton);

            // Aggiunta del VBox alla griglia
            gridPane.add(vbox, col, row);

            col = (col + 1) % columns;
            if (col == 0) {
                row++;
            }
        }

        // **Calcola dinamicamente l'altezza della GridPane**
        int totalRows = (int) Math.ceil((double) users.size() / columns);
        double gridHeight = totalRows * (vBoxHeight + spacing);

        gridPane.setMinHeight(gridHeight);
        gridPane.setPrefHeight(gridHeight);
        gridPane.setMaxHeight(gridHeight);

        // **Forza lo ScrollPane a gestire lo scrolling**
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
    }

    @FXML
    public void handleProductsButton(final ActionEvent event) {
        try {
            // Carica il file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/AdminProductsView.fxml"));
            Parent root = loader.load();

            AdminProductsView controller = loader.getController();
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

}
