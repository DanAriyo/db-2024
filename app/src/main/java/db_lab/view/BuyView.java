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

    private User currentUser;
    private Controller controller;
    private Product product;

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

    @FXML
    private Button buyButton;

    @FXML
    private TextArea reviewDescription;

    @FXML
    private ChoiceBox<Integer> starChoiceBox;

    @Override
    public Controller getController() {
        return this.controller;
    }

    public void setParameter(Product product, Controller controller, User user) {
        this.product = product;
        this.currentUser = user;
        this.controller = controller;
    }

    public void setProductDetails(Product product) {
        if (product != null) {

            nomeArticolo.setText("Nome: " + product.getNome());
            materiale.setText("Materiale: " + product.getMateriale());
            taglia.setText("Taglia: " + product.getTaglia());
            condizioni.setText("Condizioni: " + product.getCondizioni());
            brand.setText("Brand: " + product.getBrand());
            descrizione.setText("Descrizione: " + product.getDescrizione());
            prezzo.setText("Prezzo:" + String.format("%.2f €", product.getPrezzo()));

            Map<Integer, String> categoryImages = new HashMap<>();
            categoryImages.put(1, "maglietta.jpeg");
            categoryImages.put(2, "accessori.jpeg");
            categoryImages.put(3, "pantaloni.jpeg");
            categoryImages.put(4, "scarpe.jpeg");
            idCategoria.setText(categoryImages.get(product.getIdCategoria()));
            starChoiceBox.getItems().addAll(1, 2, 3, 4, 5);

            String imageName = categoryImages.getOrDefault(product.getIdCategoria(), "default.jpeg");
            String imagePath = "/images/" + imageName;

            try {
                imageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
            } catch (Exception e) {
                System.out.println("Immagine non trovata: " + imagePath);
                imageView.setImage(new Image(getClass().getResource("/images/default.jpeg").toExternalForm()));
            }
        }
    }

    @FXML
    public void handleBuyButton() {
        this.controller.newReview(createFictionalReview());
        var trans = createFictionalTransaction(this.controller.getLatestReview());
        this.getController().newTransaction(trans);
        this.buyButton.setDisable(true);
        this.controller.checkRewards(trans);
    }

    public Transaction createFictionalTransaction(Review review) {
        Transaction transaction = new Transaction(product.getIdProprietario(), this.currentUser.getId(), 0, 1, 1,
                getController().getUserById(product.getIdProprietario()).getIdSaldo(), review.getId(), product.getId(),
                this.currentUser.getIdSaldo());
        return transaction;
    }

    public Review createFictionalReview() {
        Review review = new Review(0, this.currentUser.getId(), product.getIdProprietario(),
                this.starChoiceBox.getValue().intValue(),
                reviewDescription.getText());
        return review;
    }

    @FXML
    public void handleHomeButton(final ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/HomeView.fxml"));
            Parent root = loader.load();

            HomeView controller = loader.getController();
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

    @FXML
    public void handleSellButton(final ActionEvent event) {
        try {
            // Carica il file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/SellView.fxml"));
            Parent root = loader.load();

            SellView controller = loader.getController();
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

    @FXML
    public void handleProfileButton(final ActionEvent event) {
        try {
            // Carica il file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ProfileView.fxml"));
            Parent root = loader.load();

            ProfileView controller = loader.getController();
            controller.setParameters(this.controller, this.currentUser);

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
