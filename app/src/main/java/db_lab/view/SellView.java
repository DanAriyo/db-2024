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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SellView implements View {

    private User currentUser;
    private Controller controller;

    @FXML
    private TextField nomeArticolo;

    @FXML
    private TextField materiale;

    @FXML
    private TextField condizioni;

    @FXML
    private TextField brand;

    @FXML
    private TextArea descrizione;

    @FXML
    private TextField prezzo;

    @FXML
    private ChoiceBox<String> taglia;

    @FXML
    private ImageView imageView;

    @FXML
    private ChoiceBox<String> categoria;

    @FXML
    private Button sellButton;

    private Map<String, Integer> category;
    private Map<Integer, String> categoryImages;

    public Product createFictionalProduct() {
        String nomeArticoloText = nomeArticolo.getText();
        String materialeText = materiale.getText();
        String condizioniText = condizioni.getText();
        String brandText = brand.getText();
        String descrizioneText = descrizione.getText();
        int prezzoInt = Integer.parseInt(prezzo.getText());
        String tagliaText = taglia.getValue();
        String categoriaText = categoria.getValue();
        String categoryJpeg = categoriaText + ".jpeg";
        int idCategoria = this.category.get(categoryJpeg);
        return new Product(0, nomeArticoloText, this.currentUser.getId(), idCategoria, materialeText, tagliaText,
                condizioniText, brandText, descrizioneText, 0, prezzoInt);

    }

    public void setParameter(Controller controller, User user) {
        this.currentUser = user;
        this.controller = controller;
        taglia.getItems().addAll("S", "M", "L", "XL");
        categoria.getItems().addAll("maglietta", "accessori", "scarpe", "pantaloni");
        this.category = new HashMap<>();
        category.put("maglietta.jpeg", 1);
        category.put("accessori.jpeg", 2);
        category.put("pantaloni.jpeg", 3);
        category.put("scarpe.jpeg", 4);

        this.categoryImages = new HashMap<>();
        categoryImages.put(1, "maglietta.jpeg");
        categoryImages.put(2, "accessori.jpeg");
        categoryImages.put(3, "pantaloni.jpeg");
        categoryImages.put(4, "scarpe.jpeg");

        categoria.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateImagePreview(newVal);
        });
        this.sellButton.setDisable(false);

    }

    private void updateImagePreview(String categoriaText) {
        categoriaText = categoriaText + ".jpeg";
        int idCategoria = category.getOrDefault(categoriaText, -1);
        String imageName = categoryImages.getOrDefault(idCategoria, "default.jpeg");
        String imagePath = "/images/" + imageName;

        try {
            imageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
        } catch (Exception e) {
            System.out.println("Immagine non trovata: " + imagePath);
            imageView.setImage(new Image(getClass().getResource("/images/default.jpeg").toExternalForm()));
        }
    }

    @Override
    public Controller getController() {
        return this.controller;
    }

    @FXML
    public void handleSellProductButton() {
        getController().addProduct(createFictionalProduct());
        this.sellButton.setDisable(true);

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
