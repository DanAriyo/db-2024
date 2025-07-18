package db_lab.view;

import java.io.IOException;
import java.util.List;

import db_lab.controller.Controller;
import db_lab.data.dataentity.Balance;
import db_lab.data.dataentity.BankAccount;
import db_lab.data.dataentity.CreditReward;
import db_lab.data.dataentity.Review;
import db_lab.data.dataentity.SelfEvaluation;
import db_lab.data.dataentity.Transaction;
import db_lab.data.dataentity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Classe che gestisce la visualizzazione di liste varie (recensioni, transazioni, saldi, premi)
// e fornisce azioni per i bottoni di navigazione e ricarica saldo/conto.
public class ListView implements View {

    @FXML
    VBox vBox; // Contenitore verticale dove verranno aggiunti dinamicamente i componenti
               // (Label, Button)

    private Controller controller; // Riferimento al controller per gestire la logica
    private User user; // Utente corrente

    @Override
    public Controller getController() {
        return this.controller;
    }

    // Crea e aggiunge nel VBox una lista di Label con le recensioni ricevute.
    public void createReviewsLabel(List<Review> reviews) {
        vBox.getChildren().clear(); // Pulisce il contenitore prima di aggiungere nuovi elementi
        vBox.setAlignment(Pos.CENTER); // Centra gli elementi verticalmente

        // Per ogni recensione, crea una Label con testo e stile
        for (Review review : reviews) {
            Label label = new Label(review.getDescrizione() + " " + review.getStelle() + " STELLE");
            label.setStyle("-fx-font-weight: bold; " +
                    "-fx-font-size: 19px; " +
                    "-fx-border-color: black; " +
                    "-fx-border-width: 2px; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-padding: 10px; " +
                    "-fx-background-color: #f5f5f5;");
            VBox.setMargin(label, new Insets(5, 0, 5, 0)); // Margine sopra e sotto
            vBox.getChildren().add(label);
        }

        // Se non ci sono recensioni, mostra un messaggio apposito
        if (reviews.isEmpty()) {
            Label label = new Label("AL MOMENTO NON CI SONO RECENSIONI ");
            label.setStyle("-fx-font-weight: bold; " +
                    "-fx-font-size: 19px; " +
                    "-fx-border-color: black; " +
                    "-fx-border-width: 2px; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-padding: 10px; " +
                    "-fx-background-color: #f5f5f5;");

            VBox.setMargin(label, new Insets(5, 0, 5, 0));
            vBox.getChildren().add(label);
        }
    }

    // Crea e aggiunge Label che rappresentano le transazioni effettuate
    public void createTransactionLabels(List<Transaction> transactions) {
        vBox.getChildren().clear();
        vBox.setAlignment(Pos.CENTER);

        for (Transaction transaction : transactions) {
            // Recupera username venditore e acquirente tramite controller
            String text = "Transazione " + transaction.getId() +
                    " compiuta tra venditore " + this.controller.getUserById(transaction.getIdVenditore()).getUsername()
                    +
                    " e acquirente " + this.controller.getUserById(transaction.getIdAcquirente()).getUsername();

            Label label = new Label(text);
            label.setStyle("-fx-font-weight: bold; " +
                    "-fx-font-size: 19px; " +
                    "-fx-border-color: black; " +
                    "-fx-border-width: 2px; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-padding: 10px; " +
                    "-fx-background-color: #f5f5f5;");

            VBox.setMargin(label, new Insets(5, 0, 5, 0));
            vBox.getChildren().add(label);
        }

        // Se non ci sono transazioni, mostra messaggio
        if (transactions.isEmpty()) {
            Label label = new Label("AL MOMENTO NON CI SONO TRANSAZIONI ");
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 19px;");
            vBox.getChildren().add(label);
        }
    }

    // Crea una label che mostra il saldo attuale e un pulsante per ricaricarlo
    public void createBalanceLabel(Balance balance) {
        vBox.getChildren().clear();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10); // Spaziatura verticale tra elementi

        Label balanceLabel = new Label("Saldo attuale: " + balance.getAmmontare() + "€");
        balanceLabel.setStyle("-fx-font-weight: bold; " +
                "-fx-font-size: 19px; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 5px; " +
                "-fx-padding: 10px; " +
                "-fx-background-color: #f5f5f5;");

        VBox.setMargin(balanceLabel, new Insets(5, 0, 5, 0));
        VBox.setVgrow(balanceLabel, Priority.ALWAYS);

        // Bottone per ricaricare il saldo, chiama metodo del controller passando
        // l’utente
        Button rechargeButton = new Button("Ricarica saldo");
        rechargeButton.setStyle("-fx-font-weight: bold; -fx-font-size: 19px;");
        rechargeButton.setOnAction(event -> {
            this.controller.rechargeBalance(this.user);
        });

        vBox.getChildren().addAll(balanceLabel, rechargeButton);
    }

    // Come sopra ma per il conto corrente bancario
    public void createBankAccountLabel(BankAccount bankAccount) {
        vBox.getChildren().clear();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        Label bankAccountLabel = new Label("Saldo attuale: " + bankAccount.getSaldo() + "€");
        bankAccountLabel.setStyle("-fx-font-weight: bold; " +
                "-fx-font-size: 19px; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 5px; " +
                "-fx-padding: 10px; " +
                "-fx-background-color: #f5f5f5;");

        VBox.setMargin(bankAccountLabel, new Insets(5, 0, 5, 0));
        VBox.setVgrow(bankAccountLabel, Priority.ALWAYS);

        // Bottone per ricaricare conto corrente
        Button rechargeButton = new Button("Ricarica conto corrente");
        rechargeButton.setStyle("-fx-font-weight: bold; -fx-font-size: 19px;");
        rechargeButton.setOnAction(event -> {
            this.controller.rechargeBankAccount(this.user);
        });

        vBox.getChildren().addAll(bankAccountLabel, rechargeButton);
    }

    // Metodo per impostare parametri e creare la vista del saldo
    public void setBalanceParameters(Controller controller, User currentUser, Balance balance) {
        this.controller = controller;
        this.user = currentUser;
        createBalanceLabel(balance);
    }

    // Metodo per impostare parametri e creare la vista del conto corrente
    public void setBankAccountParameters(Controller controller, User currentUser, BankAccount bankAccount) {
        this.controller = controller;
        this.user = currentUser;
        createBankAccountLabel(bankAccount);
    }

    // Metodo per impostare parametri e creare la lista delle recensioni
    public void setReviewsParameters(Controller controller, User currentUser, List<Review> reviews) {
        this.controller = controller;
        this.user = currentUser;
        createReviewsLabel(reviews);
    }

    // Metodo per impostare parametri e creare la lista delle transazioni
    public void setTransactionsParameters(Controller controller, User currentUser, List<Transaction> transactions) {
        this.controller = controller;
        this.user = currentUser;
        createTransactionLabels(transactions);
    }

    // Metodo per impostare parametri e creare la lista di premi e autovalutazioni
    public void setRewardsParameters(Controller controller, User currentUser, List<CreditReward> creditRewards,
            List<SelfEvaluation> selfEvaluations) {
        this.controller = controller;
        this.user = currentUser;
        createRewardsLabels(creditRewards, selfEvaluations);
    }

    // Crea le Label per premi (credit rewards) e autovalutazioni (self evaluations)
    public void createRewardsLabels(List<CreditReward> creditRewards, List<SelfEvaluation> selfEvaluations) {
        vBox.getChildren().clear();
        vBox.setAlignment(Pos.CENTER);

        // Aggiunge ogni premio come Label con descrizione e bonus
        for (CreditReward reward : creditRewards) {
            Label label = new Label(reward.getDescrizione() + " " + reward.getImportoBonus() + " €");
            label.setStyle("-fx-font-weight: bold; " +
                    "-fx-font-size: 19px; " +
                    "-fx-border-color: black; " +
                    "-fx-border-width: 2px; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-padding: 10px; " +
                    "-fx-background-color: #f5f5f5;");

            VBox.setMargin(label, new Insets(5, 0, 5, 0));
            vBox.getChildren().add(label);
        }

        // Aggiunge ogni autovalutazione come Label con descrizione e stelle
        for (SelfEvaluation reward : selfEvaluations) {
            Label label = new Label(reward.getDescrizione() + " " + reward.getStelle() + " STELLE");
            label.setStyle("-fx-font-weight: bold; " +
                    "-fx-font-size: 19px; " +
                    "-fx-border-color: black; " +
                    "-fx-border-width: 2px; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-padding: 10px; " +
                    "-fx-background-color: #f5f5f5;");

            VBox.setMargin(label, new Insets(5, 0, 5, 0));
            vBox.getChildren().add(label);
        }

        // Se non ci sono né premi né autovalutazioni, mostra messaggio
        if (creditRewards.isEmpty() && selfEvaluations.isEmpty()) {
            Label label = new Label("AL MOMENTO NON CI SONO PREMI ");
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 19px;");
            vBox.getChildren().add(label);
        }

    }

    // Handler per il bottone "Home", cambia scena e passa controller e user alla
    // HomeView
    @FXML
    public void handleHomeButton(final ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/HomeView.fxml"));
            Parent root = loader.load();

            HomeView controller = loader.getController();
            controller.setParameter(getController(), this.user);

            // Ottieni la finestra attuale e imposta la nuova scena caricata
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // In caso di errore stampa stack trace
        }
    }

    // Handler per il bottone "Sell", cambia scena e passa controller e user alla
    // SellView
    @FXML
    public void handleSellButton(final ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/SellView.fxml"));
            Parent root = loader.load();

            SellView controller = loader.getController();
            controller.setParameter(getController(), this.user);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handler per il bottone "Profile", cambia scena e passa controller e user alla
    // ProfileView
    @FXML
    public void handleProfileButton(final ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ProfileView.fxml"));
            Parent root = loader.load();

            ProfileView controller = loader.getController();
            controller.setParameters(this.controller, this.user);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
