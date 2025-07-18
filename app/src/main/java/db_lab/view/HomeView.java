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

public class HomeView implements View {

    // Controller associato alla vista
    private Controller controller;
    // Utente corrente loggato
    private User currentUser;

    // Componenti FXML definiti nel file .fxml
    @FXML
    GridPane gridPane;

    @FXML
    Button homeButton;

    @FXML
    Button sellButton;

    @FXML
    Button profileButton;

    @FXML
    ScrollPane scrollPane;

    @Override
    public Controller getController() {
        // Ritorna il controller associato alla vista
        return this.controller;
    }

    public void setParameter(Controller controller, User user) {
        // Imposta i parametri iniziali della vista: controller e utente corrente
        this.currentUser = user;
        this.controller = controller;
        // Popola la griglia con i prodotti disponibili ottenuti dal controller
        this.populateGridPane(this.gridPane, this.controller.getProducts());
    }

    public void populateGridPane(GridPane gridPane, List<Product> products) {
        // Pulisce la griglia prima di aggiungere nuovi elementi
        gridPane.getChildren().clear();

        // Configurazione layout: 2 colonne per riga
        int columns = 2;
        int row = 0;
        int col = 0;

        // Dimensioni e spaziatura per ogni contenitore VBox che rappresenta un prodotto
        double vBoxWidth = 160;
        double vBoxHeight = 250;
        double spacing = 10;

        // Imposta gap orizzontale e verticale della griglia
        gridPane.setVgap(spacing);
        gridPane.setHgap(spacing);

        // Mappa per associare categorie di prodotto a immagini specifiche
        Map<Integer, String> categoryImages = new HashMap<>();
        categoryImages.put(1, "maglietta.jpeg");
        categoryImages.put(2, "accessori.jpeg");
        categoryImages.put(3, "pantaloni.jpeg");
        categoryImages.put(4, "scarpe.jpeg");

        // Ciclo per ogni prodotto da visualizzare nella griglia
        for (Product product : products) {
            // Crea un VBox per contenere immagine, nome, prezzo e pulsante
            VBox vbox = new VBox();
            vbox.setPrefWidth(vBoxWidth);
            vbox.setPrefHeight(vBoxHeight);
            vbox.setSpacing(5);
            vbox.setAlignment(Pos.CENTER);
            // Stile con bordo e padding
            vbox.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px; -fx-padding: 10px;");

            // Crea ImageView per mostrare l'immagine del prodotto
            ImageView imageView = new ImageView();
            imageView.setFitWidth(120);
            imageView.setFitHeight(120);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            // Ottiene il nome dell'immagine in base alla categoria del prodotto
            String imageName = categoryImages.getOrDefault(product.getIdCategoria(), "default.jpeg");
            String imagePath = "/images/" + imageName;

            // Carica l'immagine, se non trovata usa immagine di default
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

            // Label con il nome del prodotto, centrata e con testo a capo
            Label nameLabel = new Label(product.getNome());
            nameLabel.setPrefWidth(140);
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-alignment: center;");
            nameLabel.setWrapText(true);
            nameLabel.setTextAlignment(TextAlignment.CENTER);

            // Label con il prezzo formattato, centrata e colorata
            Label priceLabel = new Label(String.format("€ %.2f", product.getPrezzo()));
            priceLabel.setPrefWidth(140);
            priceLabel.setAlignment(Pos.CENTER);
            priceLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: #2c3e50;");
            priceLabel.setWrapText(true);
            priceLabel.setTextAlignment(TextAlignment.CENTER);

            // Pulsante "ACQUISTA"
            Button buyButton = new Button("ACQUISTA");
            buyButton.setPrefWidth(140);
            buyButton.setStyle(
                    "-fx-font-weight: bold; -fx-font-size: 12px; -fx-background-color: #27ae60; -fx-text-fill: white;");

            // Evento click sul pulsante acquista: apre la vista BuyView per il prodotto
            // selezionato
            buyButton.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/BuyView.fxml"));
                    Parent root = loader.load();

                    BuyView controller = loader.getController();
                    controller.setParameter(product, getController(), currentUser);
                    controller.setProductDetails(product);

                    // Ottiene la finestra attuale e sostituisce la scena con BuyView
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Disabilita il pulsante se il prodotto appartiene all'utente corrente (non può
            // comprare se è il proprietario)
            if (product.getIdProprietario() == this.currentUser.getId()) {
                buyButton.setDisable(true);
            }

            // Aggiunge tutti gli elementi al VBox
            vbox.getChildren().addAll(imageContainer, nameLabel, priceLabel, buyButton);

            // Aggiunge il VBox nella griglia nella posizione colonna e riga correnti
            gridPane.add(vbox, col, row);

            // Aggiorna colonna e riga per la prossima iterazione (layout 2 colonne)
            col = (col + 1) % columns;
            if (col == 0) {
                row++;
            }
        }

        // Calcola dinamicamente l'altezza totale della GridPane basandosi sul numero di
        // righe e dimensioni dei VBox
        int totalRows = (int) Math.ceil((double) products.size() / columns);
        double gridHeight = totalRows * (vBoxHeight + spacing);

        // Imposta altezza minima, preferita e massima della GridPane per adattarsi al
        // contenuto
        gridPane.setMinHeight(gridHeight);
        gridPane.setPrefHeight(gridHeight);
        gridPane.setMaxHeight(gridHeight);

        // Configura lo ScrollPane: si adatta alla larghezza, ma non all'altezza per
        // permettere lo scrolling verticale
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
    }

    @FXML
    public void handleSellButton(final ActionEvent event) {
        try {
            // Carica il file FXML relativo alla vista SellView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/SellView.fxml"));
            Parent root = loader.load();

            // Ottiene il controller della vista SellView e imposta i parametri
            SellView controller = loader.getController();
            controller.setParameter(getController(), currentUser);

            // Ottiene la finestra attuale e imposta la nuova scena SellView
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleProfileButton(final ActionEvent event) {
        try {
            // Carica il file FXML relativo alla vista ProfileView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ProfileView.fxml"));
            Parent root = loader.load();

            // Ottiene il controller della vista ProfileView e imposta i parametri
            ProfileView controller = loader.getController();
            controller.setParameters(this.controller, this.currentUser);

            // Ottiene la finestra attuale e imposta la nuova scena ProfileView
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
