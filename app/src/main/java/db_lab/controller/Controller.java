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
import db_lab.view.View;

public interface Controller {

    public void newUser(User user) throws SQLException;

    public boolean checkLogin(String username, String password);

    public User getUser(String username, String password);

    public void newTransaction(Transaction fictionalTransaction);

    public User getUserById(int idProprietario);

    public void addProduct(Product fictionalProduct);

    public List<Product> getProducts();

    public void setView(View view);

    public Balance getBalanceById(int idSaldo);

    public BankAccount getBankAccountById(int iban);

    public List<Review> getReviewsById(int id);

    public List<Transaction> getTransactionsById(int id);

    public List<CreditReward> getCreditRewardsById(int id);

    public List<SelfEvaluation> getSelfEvaluationsById(int id);

    public List<User> getUsers();

    public void toggleProductStatus(Product product);

    public void toggleUserStatus(User user);

    public void rechargeBalance(User user);

    public void rechargeBankAccount(User user);

}
