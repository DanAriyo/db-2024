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

//SETTA LE ACTION PER I BOTTONI DI AGGIORNAMENTO SALDO E CONTO CORRENTE

public class ListView implements View {

    @FXML
    VBox vBox;

    private Controller controller;
    private User user;

    @Override
    public Controller getController() {
        return this.controller;
    }

    public void createReviewsLabel(List<Review> reviews) {
        vBox.getChildren().clear();
        vBox.setAlignment(Pos.CENTER);

        for (Review review : reviews) {
            Label label = new Label(review.getDescrizione() + " " + review.getStelle() + " STELLE");
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

    public void createTransactionLabels(List<Transaction> transactions) {
        vBox.getChildren().clear();
        vBox.setAlignment(Pos.CENTER);

        for (Transaction transaction : transactions) {
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

        if (transactions.isEmpty()) {
            Label label = new Label("AL MOMENTO NON CI SONO TRANSAZIONI ");
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 19px;");
            vBox.getChildren().add(label);
        }
    }

    public void createBalanceLabel(Balance balance) {
        vBox.getChildren().clear();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

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

        Button rechargeButton = new Button("Ricarica saldo");
        rechargeButton.setStyle("-fx-font-weight: bold; -fx-font-size: 19px;");
        rechargeButton.setOnAction(event -> {
            this.controller.rechargeBalance(this.user);
        });

        vBox.getChildren().addAll(balanceLabel, rechargeButton);
    }

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

        Button rechargeButton = new Button("Ricarica conto corrente");
        rechargeButton.setStyle("-fx-font-weight: bold; -fx-font-size: 19px;");
        rechargeButton.setOnAction(event -> {
            this.controller.rechargeBankAccount(this.user);
        });

        vBox.getChildren().addAll(bankAccountLabel, rechargeButton);
    }

    public void setBalanceParameters(Controller controller, User currentUser, Balance balance) {
        this.controller = controller;
        this.user = currentUser;
        createBalanceLabel(balance);
    }

    public void setBankAccountParameters(Controller controller, User currentUser, BankAccount bankAccount) {
        this.controller = controller;
        this.user = currentUser;
        createBankAccountLabel(bankAccount);
    }

    public void setReviewsParameters(Controller controller, User currentUser, List<Review> reviews) {
        this.controller = controller;
        this.user = currentUser;
        createReviewsLabel(reviews);
    }

    public void setTransactionsParameters(Controller controller, User currentUser, List<Transaction> transactions) {
        this.controller = controller;
        this.user = currentUser;
        createTransactionLabels(transactions);
    }

    public void setRewardsParameters(Controller controller, User currentUser, List<CreditReward> creditRewards,
            List<SelfEvaluation> selfEvaluations) {
        this.controller = controller;
        this.user = currentUser;
        createRewardsLabels(creditRewards, selfEvaluations);
    }

    public void createRewardsLabels(List<CreditReward> creditRewards, List<SelfEvaluation> selfEvaluations) {
        vBox.getChildren().clear();
        vBox.setAlignment(Pos.CENTER);

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

        for (SelfEvaluation reward : selfEvaluations) {
            Label label = new Label(reward.getDescrizione() + " " + reward.getStelle() + "STELLE");
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

        if (creditRewards.isEmpty() && selfEvaluations.isEmpty()) {
            Label label = new Label("AL MOMENTO NON CI SONO PREMI ");
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 19px;");
            vBox.getChildren().add(label);
        }

    }

    @FXML
    public void handleHomeButton(final ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/HomeView.fxml"));
            Parent root = loader.load();

            HomeView controller = loader.getController();
            controller.setParameter(getController(), this.user);

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
            controller.setParameter(getController(), this.user);

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
            controller.setParameters(this.controller, this.user);

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
