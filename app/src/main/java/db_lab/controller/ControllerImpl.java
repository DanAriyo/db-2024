package db_lab.controller;

import java.sql.SQLException;
import java.util.List;

import db_lab.data.dataentity.Balance;
import db_lab.data.dataentity.BankAccount;
import db_lab.data.dataentity.CreditReward;
import db_lab.data.dataentity.Product;
import db_lab.data.dataentity.Review;
import db_lab.data.dataentity.SelfEvaluation;
import db_lab.data.dataentity.Transaction;
import db_lab.data.dataentity.User;
import db_lab.model.Model;
import db_lab.model.ModelImpl;
import db_lab.view.View;
import db_lab.utilities.*;

public class ControllerImpl implements Controller {

    private Model model;
    private View view;

    public ControllerImpl() {
        try {
            this.model = new ModelImpl(this, DAOUtils.localMySQLConnection("myvinted", "root", "Shirou03!"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void newUser(User user) throws SQLException {
        this.model.newUser(user);
    }

    @Override
    public boolean checkLogin(String username, String password) {
        return this.model.checkLogin(username, password);
    }

    @Override
    public User getUser(String username, String password) {
        return this.model.getUser(password, username);
    }

    @Override
    public void newTransaction(Transaction fictionalTransaction) {
        this.model.newTransaction(fictionalTransaction);
    }

    @Override
    public User getUserById(int idProprietario) {
        return this.model.getUserById(idProprietario);
    }

    @Override
    public void addProduct(Product fictionalProduct) {
        this.model.addProduct(fictionalProduct);
    }

    @Override
    public List<Product> getProducts() {
        return this.model.getProducts();
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public Balance getBalanceById(int idSaldo) {
        return this.model.getBalanceById(idSaldo);
    }

    @Override
    public BankAccount getBankAccountById(int iban) {
        return this.model.getBankAccountById(iban);
    }

    @Override
    public List<Review> getReviewsById(int id) {
        return this.model.getReviewsById(id);
    }

    @Override
    public List<Transaction> getTransactionsById(int id) {
        return this.model.getTransactionsById(id);
    }

    @Override
    public List<CreditReward> getCreditRewardsById(int id) {
        return this.model.getCreditRewardsById(id);
    }

    @Override
    public List<SelfEvaluation> getSelfEvaluationsById(int id) {
        return this.model.getSelfEvaluationsById(id);
    }

    @Override
    public List<User> getUsers() {
        return this.model.getUsers();
    }

    @Override
    public void toggleProductStatus(Product product) {
        this.model.toggleProductStatus(product);
    }

    @Override
    public void toggleUserStatus(User user) {
        this.model.toggleUserStatus(user);
    }

    @Override
    public void rechargeBalance(User user) {
        this.model.rechargeBalance(user);
    }

    @Override
    public void rechargeBankAccount(User user) {
        this.model.rechargeBankAccount(user);
    }

}
