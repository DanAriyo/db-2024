package db_lab.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db_lab.controller.Controller;
import db_lab.data.dataentity.Product;
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

public class AdminProductsView implements View {

    private Controller controller;
    private User currentUser; // Utente amministratore corrente

    @FXML
    GridPane gridPane; // Griglia in cui inserire i prodotti

    @FXML
    Button usersButton;

    @FXML
    Button productsButton;

    @FXML
    ScrollPane scrollPane; // ScrollPane contenente la griglia per lo scrolling

    @Override
    public Controller getController() {
        return this.controller;
    }

    /**
     * Inizializza la view con il controller e l'utente corrente,
     * e popola la GridPane con la lista dei prodotti ottenuti dal controller.
     */
    public void setParameter(Controller controller, User user) {
        this.currentUser = user;
        this.controller = controller;
        this.populateGridPane(this.gridPane, this.controller.getProducts());
    }

    /**
     * Popola la GridPane con prodotti rappresentati da VBox contenenti immagine,
     * nome, prezzo e un pulsante di azione.
     * 
     * @param gridPane griglia da popolare
     * @param products lista di prodotti da mostrare
     */
    public void populateGridPane(GridPane gridPane, List<Product> products) {
        gridPane.getChildren().clear(); // Pulisce la griglia

        int columns = 2; // Numero colonne nella griglia
        int row = 0;
        int col = 0;

        // Dimensioni e spaziatura dei VBox (contenitori prodotti)
        double vBoxWidth = 160;
        double vBoxHeight = 250;
        double spacing = 10;

        // Imposta spaziatura verticale e orizzontale della griglia
        gridPane.setVgap(spacing);
        gridPane.setHgap(spacing);

        // Mappa id categoria -> nome immagine
        Map<Integer, String> categoryImages = new HashMap<>();
        categoryImages.put(1, "maglietta.jpeg");
        categoryImages.put(2, "accessori.jpeg");
        categoryImages.put(3, "pantaloni.jpeg");
        categoryImages.put(4, "scarpe.jpeg");

        // Per ogni prodotto crea un VBox con immagine, nome, prezzo e pulsante
        for (Product product : products) {
            VBox vbox = new VBox();
            vbox.setPrefWidth(vBoxWidth);
            vbox.setPrefHeight(vBoxHeight);
            vbox.setSpacing(5);
            vbox.setAlignment(Pos.CENTER);
            vbox.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px; -fx-padding: 10px;");

            // ImageView per l'immagine del prodotto
            ImageView imageView = new ImageView();
            imageView.setFitWidth(120);
            imageView.setFitHeight(120);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            // Ottiene immagine associata alla categoria del prodotto
            String imageName = categoryImages.getOrDefault(product.getIdCategoria(), "default.jpeg");
            String imagePath = "/images/" + imageName;

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

            // Label per il nome prodotto
            Label nameLabel = new Label(product.getNome());
            nameLabel.setPrefWidth(140);
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-alignment: center;");
            nameLabel.setWrapText(true);
            nameLabel.setTextAlignment(TextAlignment.CENTER);

            // Label per il prezzo prodotto
            Label priceLabel = new Label(String.format("€ %.2f", product.getPrezzo()));
            priceLabel.setPrefWidth(140);
            priceLabel.setAlignment(Pos.CENTER);
            priceLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: #2c3e50;");
            priceLabel.setWrapText(true);
            priceLabel.setTextAlignment(TextAlignment.CENTER);

            // Pulsante azione ("AZIONE") per aprire i dettagli del prodotto
            Button actionButton = new Button("AZIONE");
            actionButton.setPrefWidth(140);
            actionButton.setStyle(
                    "-fx-font-weight: bold; -fx-font-size: 12px; -fx-background-color: #27ae60; -fx-text-fill: white;");

            // Evento click del pulsante: apre la finestra di dettaglio prodotto
            actionButton.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/AdminProductDetailsView.fxml"));
                    Parent root = loader.load();

                    AdminProductDetailsView controller = loader.getController();
                    controller.setParameter(getController(), product);
                    controller.setProductDetails(product);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Aggiunge immagine, nome, prezzo e pulsante al VBox
            vbox.getChildren().addAll(imageContainer, nameLabel, priceLabel, actionButton);

            // Inserisce il VBox nella griglia
            gridPane.add(vbox, col, row);

            col = (col + 1) % columns; // Avanza colonna
            if (col == 0) {
                row++; // Nuova riga
            }
        }

        // Calcola e imposta dinamicamente l'altezza della GridPane in base al numero di
        // righe
        int totalRows = (int) Math.ceil((double) products.size() / columns);
        double gridHeight = totalRows * (vBoxHeight + spacing);

        gridPane.setMinHeight(gridHeight);
        gridPane.setPrefHeight(gridHeight);
        gridPane.setMaxHeight(gridHeight);

        // Configura lo ScrollPane per adattarsi alla larghezza, ma non all'altezza
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
    }

    /**
     * Gestisce il click sul pulsante "Users" per passare alla vista amministrativa
     * degli utenti.
     */
    @FXML
    public void handleUsersButton(final ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/AdminUsersView.fxml"));
            Parent root = loader.load();

            AdminUsersView controller = loader.getController();
            controller.setParameter(getController(), currentUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
