package db_lab.controller;

import java.sql.SQLException;
import java.util.List;

import db_lab.data.dataentity.*;
import db_lab.model.Model;
import db_lab.model.ModelImpl;
import db_lab.view.View;
import db_lab.utilities.*;

public class ControllerImpl implements Controller {

    private Model model;
    private View view;

    // Costruttore: inizializza il modello con una connessione al database MySQL
    public ControllerImpl() {
        try {
            this.model = new ModelImpl(this, DAOUtils.localMySQLConnection("myvinted", "root", "Shirou03!"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Registra un nuovo utente nel sistema
    @Override
    public void newUser(User user) throws SQLException {
        this.model.newUser(user);
    }

    // Verifica le credenziali di accesso di un utente
    @Override
    public boolean checkLogin(String username, String password) {
        return this.model.checkLogin(username, password);
    }

    // Ottiene un oggetto User a partire da username e password
    @Override
    public User getUser(String username, String password) {
        return this.model.getUser(password, username);
    }

    // Registra una nuova transazione e controlla l’assegnazione di premi
    @Override
    public void newTransaction(Transaction fictionalTransaction) {
        this.model.newTransaction(fictionalTransaction);
        this.model.checkRewards(fictionalTransaction);
    }

    // Ottiene un utente dato il suo ID
    @Override
    public User getUserById(int idProprietario) {
        return this.model.getUserById(idProprietario);
    }

    // Aggiunge un nuovo prodotto al sistema
    @Override
    public void addProduct(Product fictionalProduct) {
        this.model.addProduct(fictionalProduct);
    }

    // Restituisce la lista di tutti i prodotti presenti nel sistema
    @Override
    public List<Product> getProducts() {
        return this.model.getProducts();
    }

    // Imposta la view collegata al controller
    @Override
    public void setView(View view) {
        this.view = view;
    }

    // Ottiene un saldo (Balance) dato il suo ID
    @Override
    public Balance getBalanceById(int idSaldo) {
        return this.model.getBalanceById(idSaldo);
    }

    // Ottiene un conto bancario (BankAccount) dato l’IBAN
    @Override
    public BankAccount getBankAccountById(int iban) {
        return this.model.getBankAccountById(iban);
    }

    // Restituisce tutte le recensioni associate a un determinato ID utente
    @Override
    public List<Review> getReviewsById(int id) {
        return this.model.getReviewsById(id);
    }

    // Restituisce tutte le transazioni associate a un determinato ID utente
    @Override
    public List<Transaction> getTransactionsById(int id) {
        return this.model.getTransactionsById(id);
    }

    // Restituisce tutti i premi in crediti associati a un determinato ID utente
    @Override
    public List<CreditReward> getCreditRewardsById(int id) {
        return this.model.getCreditRewardsById(id);
    }

    // Restituisce tutte le autovalutazioni associate a un determinato ID utente
    @Override
    public List<SelfEvaluation> getSelfEvaluationsById(int id) {
        return this.model.getSelfEvaluationsById(id);
    }

    // Restituisce la lista di tutti gli utenti del sistema
    @Override
    public List<User> getUsers() {
        return this.model.getUsers();
    }

    // Cambia lo stato (es. attivo/disattivo) di un prodotto
    @Override
    public void toggleProductStatus(Product product) {
        this.model.toggleProductStatus(product);
    }

    // Cambia lo stato (es. attivo/disattivo) di un utente
    @Override
    public void toggleUserStatus(User user) {
        this.model.toggleUserStatus(user);
    }

    // Ricarica il saldo interno dell’utente
    @Override
    public void rechargeBalance(User user) {
        this.model.rechargeBalance(user);
    }

    // Ricarica il conto bancario associato all’utente
    @Override
    public void rechargeBankAccount(User user) {
        this.model.rechargeBankAccount(user);
    }

    // Controlla se una transazione dà diritto a un premio
    @Override
    public void checkRewards(Transaction transaction) {
        this.model.checkRewards(transaction);
    }

    // Registra una nuova recensione
    @Override
    public void newReview(Review review) {
        this.model.newReview(review);
    }

    // Restituisce l’ultima recensione registrata nel sistema
    @Override
    public Review getLatestReview() {
        return this.model.getLatestReview();
    }
}
