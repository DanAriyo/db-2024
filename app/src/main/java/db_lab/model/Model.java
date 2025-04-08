package db_lab.model;

import db_lab.data.dataentity.Balance;
import db_lab.data.dataentity.BankAccount;
import db_lab.data.dataentity.CreditReward;
import db_lab.data.dataentity.Product;
import db_lab.data.dataentity.Review;
import db_lab.data.dataentity.SelfEvaluation;
import db_lab.data.dataentity.Transaction;
import db_lab.data.dataentity.User;
import java.sql.SQLException;
import java.util.List;

public interface Model {

        public void newUser(User user) throws SQLException;

        public List<User> getUsers();

        public boolean checkLogin(String username, String password);

        public void addMoney();

        public void addProduct(Product product);

        public List<Product> getProducts();

        public void newTransaction(Transaction transaction);

        public User getUser(String password, String username);

        public User getUserById(int id);

        public Balance getBalanceById(int idSaldo);

        public BankAccount getBankAccountById(int iban);

        public List<Review> getReviewsById(int id);

        public List<Transaction> getTransactionsById(int id);

        public List<CreditReward> getCreditRewardsById(int id);

        public List<SelfEvaluation> getSelfEvaluationsById(int id);

        public void toggleProductStatus(Product product);

        public void toggleUserStatus(User user);

        public void rechargeBalance(User user);

        public void rechargeBankAccount(User user);

}
