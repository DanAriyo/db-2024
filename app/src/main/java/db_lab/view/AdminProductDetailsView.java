package db_lab.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import db_lab.controller.Controller;
import db_lab.data.dataentity.Product;
import db_lab.data.dataentity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AdminProductDetailsView implements View {

    private Product product; // Prodotto attualmente visualizzato
    private Controller controller; // Controller associato alla view
    private User user; // Utente (amministratore) corrente

    // Etichette per mostrare i dettagli del prodotto
    @FXML
    private Label nomeArticolo;

    @FXML
    private Label idCategoria;

    @FXML
    private Label materiale;

    @FXML
    private Label taglia;

    @FXML
    private Label condizioni;

    @FXML
    private Label brand;

    @FXML
    private TextArea descrizione; // Area di testo per la descrizione del prodotto (non editabile)

    @FXML
    private Label prezzo;

    @FXML
    private ImageView imageView; // Componente per mostrare l'immagine del prodotto

    @Override
    public Controller getController() {
        return this.controller;
    }

    /**
     * Imposta il controller e il prodotto da visualizzare in questa view.
     * 
     * @param controller controller associato
     * @param product    prodotto da mostrare
     */
    public void setParameter(Controller controller, Product product) {
        this.product = product;
        this.controller = controller;
    }

    /**
     * Gestisce il click sul pulsante "Users" per passare alla vista amministrativa
     * degli utenti.
     */
    @FXML
    public void handleUsersButton(final ActionEvent event) {
        try {
            // Carica il layout FXML della vista AdminUsersView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/AdminUsersView.fxml"));
            Parent root = loader.load();

            // Ottiene il controller della nuova vista e imposta parametri
            AdminUsersView controller = loader.getController();
            controller.setParameter(getController(), user);

            // Cambia scena nella finestra corrente
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Stampa stacktrace in caso di errore nel caricamento
        }
    }

    /**
     * Gestisce il click sul pulsante "Products" per passare alla vista
     * amministrativa dei prodotti.
     */
    @FXML
    public void handleProductsButton(final ActionEvent event) {
        try {
            // Carica il layout FXML della vista AdminProductsView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/AdminProductsView.fxml"));
            Parent root = loader.load();

            // Ottiene il controller della nuova vista e imposta parametri
            AdminProductsView controller = loader.getController();
            controller.setParameter(getController(), user);

            // Cambia scena nella finestra corrente
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Stampa stacktrace in caso di errore nel caricamento
        }
    }

    /**
     * Imposta i dettagli del prodotto nei campi della UI.
     * 
     * @param product prodotto da mostrare
     */
    public void setProductDetails(Product product) {
        if (product != null) {

            // Imposta testo delle etichette con i dati del prodotto
            nomeArticolo.setText("Nome: " + product.getNome());
            materiale.setText("Materiale: " + product.getMateriale());
            taglia.setText("Taglia: " + product.getTaglia());
            condizioni.setText("Condizioni: " + product.getCondizioni());
            brand.setText("Brand: " + product.getBrand());
            descrizione.setText("Descrizione: " + product.getDescrizione());
            descrizione.setEditable(false); // Disabilita l'editing della descrizione
            prezzo.setText("Prezzo:" + String.format("%.2f €", product.getPrezzo()));

            // Mappa per associare l'id categoria al nome immagine
            Map<Integer, String> categoryImages = new HashMap<>();
            categoryImages.put(1, "maglietta.jpeg");
            categoryImages.put(2, "accessori.jpeg");
            categoryImages.put(3, "pantaloni.jpeg");
            categoryImages.put(4, "scarpe.jpeg");

            // Mostra il nome dell'immagine corrispondente alla categoria
            idCategoria.setText(categoryImages.get(product.getIdCategoria()));

            // Ottiene il nome dell'immagine da caricare
            String imageName = categoryImages.getOrDefault(product.getIdCategoria(), "default.jpeg");
            String imagePath = "/images/" + imageName;

            // Carica l'immagine nel ImageView, con gestione degli errori
            try {
                imageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
            } catch (Exception e) {
                System.out.println("Immagine non trovata: " + imagePath);
                // Se immagine non trovata, carica immagine di default
                imageView.setImage(new Image(getClass().getResource("/images/default.jpeg").toExternalForm()));
            }
        }
    }

    /**
     * Gestisce il click sul pulsante "Remove" per attivare/disattivare lo stato del
     * prodotto.
     */
    @FXML
    public void handleRemoveButton(final ActionEvent event) {
        this.getController().toggleProductStatus(product);
    }

}
