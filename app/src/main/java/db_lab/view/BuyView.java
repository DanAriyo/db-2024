package db_lab.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import db_lab.controller.Controller;
import db_lab.data.dataentity.Product;
import db_lab.data.dataentity.Review;
import db_lab.data.dataentity.Transaction;
import db_lab.data.dataentity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class BuyView implements View {

    // Utente attualmente connesso
    private User currentUser;
    // Controller per la gestione della logica dell'applicazione
    private Controller controller;
    // Prodotto visualizzato nella vista di acquisto
    private Product product;

    // Componenti UI legate al file FXML
    @FXML
    private Label nomeArticolo; // Mostra il nome del prodotto

    @FXML
    private Label idCategoria; // Mostra la categoria del prodotto (come testo o nome immagine)

    @FXML
    private Label materiale; // Mostra il materiale del prodotto

    @FXML
    private Label taglia; // Mostra la taglia del prodotto

    @FXML
    private Label condizioni; // Mostra le condizioni del prodotto

    @FXML
    private Label brand; // Mostra la marca del prodotto

    @FXML
    private TextArea descrizione; // Mostra la descrizione del prodotto

    @FXML
    private Label prezzo; // Mostra il prezzo del prodotto

    @FXML
    private ImageView imageView; // Mostra l'immagine del prodotto

    @FXML
    private Button buyButton; // Bottone per acquistare il prodotto

    @FXML
    private TextArea reviewDescription; // Area di testo dove l'utente può inserire la recensione

    @FXML
    private ChoiceBox<Integer> starChoiceBox; // Scelta del voto in stelle per la recensione

    @Override
    public Controller getController() {
        // Restituisce il controller associato a questa vista
        return this.controller;
    }

    // Imposta i parametri iniziali della vista: prodotto, controller e utente
    // corrente
    public void setParameter(Product product, Controller controller, User user) {
        this.product = product;
        this.currentUser = user;
        this.controller = controller;
    }

    // Imposta i dettagli del prodotto nei vari componenti UI
    public void setProductDetails(Product product) {
        if (product != null) {
            // Aggiorna le label con le informazioni del prodotto
            nomeArticolo.setText("Nome: " + product.getNome());
            materiale.setText("Materiale: " + product.getMateriale());
            taglia.setText("Taglia: " + product.getTaglia());
            condizioni.setText("Condizioni: " + product.getCondizioni());
            brand.setText("Brand: " + product.getBrand());
            descrizione.setText("Descrizione: " + product.getDescrizione());
            prezzo.setText("Prezzo:" + String.format("%.2f €", product.getPrezzo()));

            // Mappa per associare gli id categoria ai nomi delle immagini corrispondenti
            Map<Integer, String> categoryImages = new HashMap<>();
            categoryImages.put(1, "maglietta.jpeg");
            categoryImages.put(2, "accessori.jpeg");
            categoryImages.put(3, "pantaloni.jpeg");
            categoryImages.put(4, "scarpe.jpeg");

            // Imposta la label della categoria con il nome dell'immagine (probabilmente da
            // migliorare)
            idCategoria.setText(categoryImages.get(product.getIdCategoria()));

            // Popola la ChoiceBox delle stelle con i valori da 1 a 5
            starChoiceBox.getItems().addAll(1, 2, 3, 4, 5);

            // Costruisce il percorso dell'immagine in base alla categoria, o usa default se
            // non trovata
            String imageName = categoryImages.getOrDefault(product.getIdCategoria(), "default.jpeg");
            String imagePath = "/images/" + imageName;

            try {
                // Carica e imposta l'immagine nel ImageView
                imageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
            } catch (Exception e) {
                // Se l'immagine non viene trovata, stampa un messaggio e carica un'immagine di
                // default
                System.out.println("Immagine non trovata: " + imagePath);
                imageView.setImage(new Image(getClass().getResource("/images/default.jpeg").toExternalForm()));
            }
        }
    }

    // Gestisce l'azione di acquisto quando si preme il bottone "buy"
    @FXML
    public void handleBuyButton() {
        // Crea e registra una nuova recensione con i dati inseriti dall'utente
        this.controller.newReview(createFictionalReview());
        // Crea una nuova transazione associata all'ultima recensione inserita
        var trans = createFictionalTransaction(this.controller.getLatestReview());
        // Registra la nuova transazione
        this.getController().newTransaction(trans);
        // Disabilita il bottone per evitare acquisti multipli
        this.buyButton.setDisable(true);
        // Controlla se l'utente ha diritto a premi o ricompense in base alla
        // transazione
        this.controller.checkRewards(trans);
    }

    // Crea un oggetto Transaction fittizio basato sulla recensione e sul prodotto
    // correnti
    public Transaction createFictionalTransaction(Review review) {
        // Costruisce la transazione con id proprietario, id acquirente, e altri
        // parametri (alcuni fissi)
        Transaction transaction = new Transaction(
                product.getIdProprietario(), // Venditore
                this.currentUser.getId(), // Acquirente
                0, // Quantità o altro parametro (non specificato)
                1, // Quantità o altro parametro (non specificato)
                1, // Quantità o altro parametro (non specificato)
                getController().getUserById(product.getIdProprietario()).getIdSaldo(), // Saldo venditore
                review.getId(), // ID recensione
                product.getId(), // ID prodotto
                this.currentUser.getIdSaldo()); // Saldo acquirente
        return transaction;
    }

    // Crea un oggetto Review fittizio basato sulle informazioni inserite
    // dall'utente nella UI
    public Review createFictionalReview() {
        Review review = new Review(
                0, // ID recensione iniziale (probabilmente auto-incrementale nel DB)
                this.currentUser.getId(), // ID utente che scrive la recensione
                product.getIdProprietario(), // ID proprietario del prodotto recensito
                this.starChoiceBox.getValue().intValue(), // Numero di stelle selezionate
                reviewDescription.getText()); // Testo della recensione
        return review;
    }

    // Gestisce il click sul bottone Home per tornare alla vista principale
    @FXML
    public void handleHomeButton(final ActionEvent event) {
        try {
            // Carica il layout FXML della HomeView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/HomeView.fxml"));
            Parent root = loader.load();

            // Ottiene il controller della HomeView e imposta i parametri correnti
            HomeView controller = loader.getController();
            controller.setParameter(getController(), currentUser);

            // Cambia la scena della finestra corrente alla HomeView
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Stampa eventuali eccezioni in console
            e.printStackTrace();
        }
    }

    // Gestisce il click sul bottone Sell per andare alla vista di vendita
    @FXML
    public void handleSellButton(final ActionEvent event) {
        try {
            // Carica il layout FXML della SellView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/SellView.fxml"));
            Parent root = loader.load();

            // Ottiene il controller della SellView e imposta i parametri correnti
            SellView controller = loader.getController();
            controller.setParameter(getController(), currentUser);

            // Cambia la scena della finestra corrente alla SellView
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Stampa eventuali eccezioni in console
            e.printStackTrace();
        }
    }

    // Gestisce il click sul bottone Profile per andare alla vista profilo utente
    @FXML
    public void handleProfileButton(final ActionEvent event) {
        try {
            // Carica il layout FXML della ProfileView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ProfileView.fxml"));
            Parent root = loader.load();

            // Ottiene il controller della ProfileView e imposta i parametri correnti
            ProfileView controller = loader.getController();
            controller.setParameters(this.controller, this.currentUser);

            // Cambia la scena della finestra corrente alla ProfileView
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Stampa eventuali eccezioni in console
            e.printStackTrace();
        }
    }

}
