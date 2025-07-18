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

    private Controller controller; // Riferimento al controller per gestire la logica
    private User currentUser; // Utente corrente (amministratore)

    @FXML
    GridPane gridPane; // Griglia dove verranno mostrati gli utenti

    @FXML
    Button usersButton; // Bottone per passare alla vista utenti (presente ma non usato qui)

    @FXML
    Button productsButton; // Bottone per passare alla vista prodotti

    @FXML
    ScrollPane scrollPane; // ScrollPane che contiene la GridPane per la lista utenti

    @Override
    public Controller getController() {
        return this.controller; // Getter per il controller
    }

    /**
     * Metodo per impostare i parametri iniziali della vista:
     * controller per la logica e utente corrente,
     * oltre a popolare la griglia con gli utenti.
     */
    public void setParameter(Controller controller, User user) {
        this.currentUser = user;
        this.controller = controller;
        this.populateGridPane(this.gridPane, this.controller.getUsers());
    }

    /**
     * Popola la GridPane con una lista di utenti.
     * Ogni utente è rappresentato da un VBox con immagine, username e bottone
     * azione.
     */
    public void populateGridPane(GridPane gridPane, List<User> users) {
        gridPane.getChildren().clear(); // Pulisce la griglia da eventuali contenuti precedenti

        int columns = 2; // Numero di colonne fisse nella griglia
        int row = 0; // Indice riga corrente
        int col = 0; // Indice colonna corrente

        double vBoxWidth = 160; // Larghezza fissa di ogni VBox contenente utente
        double vBoxHeight = 250; // Altezza fissa di ogni VBox contenente utente
        double spacing = 10; // Spaziatura tra elementi nella griglia

        gridPane.setVgap(spacing); // Spaziatura verticale tra righe della griglia
        gridPane.setHgap(spacing); // Spaziatura orizzontale tra colonne della griglia

        // Ciclo su tutti gli utenti per creare la loro rappresentazione grafica
        for (User user : users) {
            // VBox che conterrà immagine, nome utente e bottone azione
            VBox vbox = new VBox();
            vbox.setPrefWidth(vBoxWidth);
            vbox.setPrefHeight(vBoxHeight);
            vbox.setSpacing(5);
            vbox.setAlignment(Pos.CENTER);
            vbox.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px; -fx-padding: 10px;");

            // ImageView per mostrare l'immagine dell'utente
            ImageView imageView = new ImageView();
            imageView.setFitWidth(120);
            imageView.setFitHeight(120);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            // Percorso dell'immagine di default dell'utente
            String imagePath = "/images/user.jpeg";

            try {
                // Prova a caricare l'immagine dall'URL delle risorse
                imageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
            } catch (Exception e) {
                // Se immagine non trovata, carica un'immagine di default alternativa
                System.out.println("Immagine non trovata: " + imagePath);
                imageView.setImage(new Image(getClass().getResource("/images/default.jpeg").toExternalForm()));
            }

            // StackPane per centrare l'immagine all'interno del VBox
            StackPane imageContainer = new StackPane(imageView);
            imageContainer.setPrefSize(140, 140);
            imageContainer.setAlignment(Pos.CENTER);

            // Label per mostrare il nome utente
            Label nameLabel = new Label(user.getUsername());
            nameLabel.setPrefWidth(140);
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-alignment: center;");
            nameLabel.setWrapText(true);
            nameLabel.setTextAlignment(TextAlignment.CENTER);

            // Bottone che permette di fare un'azione (es. visualizzare dettagli utente)
            Button actionButton = new Button("AZIONE");
            actionButton.setPrefWidth(140);
            actionButton.setStyle(
                    "-fx-font-weight: bold; -fx-font-size: 12px; -fx-background-color: #27ae60; -fx-text-fill: white;");

            // Evento click sul bottone che apre la vista dettagliata dell'utente
            // selezionato
            actionButton.setOnAction(event -> {
                try {
                    // Carica il layout FXML della vista profilo utente
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/AdminProfileUserView.fxml"));
                    Parent root = loader.load();

                    // Ottiene il controller associato al layout caricato
                    AdminProfileUserView controller = loader.getController();

                    // Passa il controller e l'utente selezionato al controller della nuova vista
                    controller.setParameter(getController(), user);

                    // Recupera la finestra corrente e cambia la scena con la nuova vista
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    // Stampa eventuali errori di caricamento
                    e.printStackTrace();
                }
            });

            // Aggiunge immagine, nome utente e bottone al VBox
            vbox.getChildren().addAll(imageContainer, nameLabel, actionButton);

            // Aggiunge il VBox alla posizione corretta della GridPane
            gridPane.add(vbox, col, row);

            // Aggiorna le coordinate colonna e riga per la prossima cella
            col = (col + 1) % columns;
            if (col == 0) {
                row++;
            }
        }

        // Calcola il numero totale di righe necessarie per contenere tutti gli utenti
        int totalRows = (int) Math.ceil((double) users.size() / columns);

        // Calcola l'altezza totale della GridPane basata sul numero di righe e
        // l'altezza di ogni VBox
        double gridHeight = totalRows * (vBoxHeight + spacing);

        // Imposta l'altezza minima, preferita e massima della GridPane
        gridPane.setMinHeight(gridHeight);
        gridPane.setPrefHeight(gridHeight);
        gridPane.setMaxHeight(gridHeight);

        // Imposta ScrollPane per adattarsi in larghezza e permettere lo scroll
        // verticale
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
    }

    /**
     * Gestisce il click sul bottone "Products" per passare alla vista prodotti.
     */
    @FXML
    public void handleProductsButton(final ActionEvent event) {
        try {
            // Carica il layout FXML della vista prodotti
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/AdminProductsView.fxml"));
            Parent root = loader.load();

            // Ottiene il controller associato alla vista prodotti
            AdminProductsView controller = loader.getController();

            // Passa il controller e l'utente corrente al controller della vista prodotti
            controller.setParameter(getController(), currentUser);

            // Recupera la finestra corrente e cambia la scena con la vista prodotti
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Stampa errori di caricamento
            e.printStackTrace();
        }
    }

}
