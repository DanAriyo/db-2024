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

    private Product product;
    private Controller controller;
    private User user;

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
    private TextArea descrizione;

    @FXML
    private Label prezzo;

    @FXML
    private ImageView imageView;

    @Override
    public Controller getController() {
        return this.controller;
    }

    public void setParameter(Controller controller, Product product) {
        this.product = product;
        this.controller = controller;
    }

    @FXML
    public void handleUsersButton(final ActionEvent event) {
        try {
            // Carica il file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/AdminUsersView.fxml"));
            Parent root = loader.load();

            AdminUsersView controller = loader.getController();
            controller.setParameter(getController(), user);

            // Ottieni la finestra attuale e imposta la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleProductsButton(final ActionEvent event) {
        try {
            // Carica il file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/AdminProductsView.fxml"));
            Parent root = loader.load();

            AdminProductsView controller = loader.getController();
            controller.setParameter(getController(), user);

            // Ottieni la finestra attuale e imposta la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProductDetails(Product product) {
        if (product != null) {

            nomeArticolo.setText("Nome: " + product.getNome());
            materiale.setText("Materiale: " + product.getMateriale());
            taglia.setText("Taglia: " + product.getTaglia());
            condizioni.setText("Condizioni: " + product.getCondizioni());
            brand.setText("Brand: " + product.getBrand());
            descrizione.setText("Descrizione: " + product.getDescrizione());
            descrizione.setEditable(false);
            prezzo.setText("Prezzo:" + String.format("%.2f €", product.getPrezzo()));

            Map<Integer, String> categoryImages = new HashMap<>();
            categoryImages.put(1, "maglietta.jpeg");
            categoryImages.put(2, "accessori.jpeg");
            categoryImages.put(3, "pantaloni.jpeg");
            categoryImages.put(4, "scarpe.jpeg");
            idCategoria.setText(categoryImages.get(product.getIdCategoria()));

            String imageName = categoryImages.getOrDefault(product.getIdCategoria(), "default.jpeg");
            String imagePath = "/images/" + imageName;

            // Caricamento immagine con gestione errori
            try {
                imageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
            } catch (Exception e) {
                System.out.println("Immagine non trovata: " + imagePath);
                imageView.setImage(new Image(getClass().getResource("/images/default.jpeg").toExternalForm()));
            }
        }
    }

    @FXML
    public void handleRemoveButton(final ActionEvent event) {
        this.getController().toggleProductStatus(product);
    }

}
