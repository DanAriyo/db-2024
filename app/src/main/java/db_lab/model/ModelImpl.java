package db_lab.model;

import db_lab.controller.Controller;
import db_lab.data.DAOException;
import db_lab.data.dataentity.Balance;
import db_lab.data.dataentity.BankAccount;
import db_lab.data.dataentity.Deposit;
import db_lab.data.dataentity.Product;
import db_lab.data.dataentity.User;
import db_lab.data.dataentity.Balance.BalanceDAO;
import db_lab.data.dataentity.BankAccount.BankAccountDAO;
import db_lab.data.dataentity.CreditReward.CreditRewardDAO;
import db_lab.data.dataentity.CreditReward;
import db_lab.data.dataentity.Deposit.DepositDAO;
import db_lab.data.dataentity.Product.ProductDAO;
import db_lab.data.dataentity.ProductPreview.ProductPreviewDAO;
import db_lab.data.dataentity.Review.ReviewDAO;
import db_lab.data.dataentity.SelfEvaluation.SelfEvaluationDAO;
import db_lab.data.dataentity.SelfEvaluation;
import db_lab.data.dataentity.Review;
import db_lab.data.dataentity.Transaction.TransactionDAO;
import db_lab.data.dataentity.ProductPreview;
import db_lab.data.dataentity.Transaction;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// This is the real model implementation that uses the DAOs we've defined to
// actually load data from the underlying database.
//
// As you can see this model doesn't do too much except loading data from the
// database and keeping a cache of the loaded previews.
// A real model might be doing much more, but for the sake of the example we're
// keeping it simple.
//
public final class ModelImpl implements Model {

    private Controller controller;
    private Connection connection;
    private User userHelper;
    private Deposit depositHelper;
    private DepositDAO depositDAO;
    private User.UserDAO daoHelper;
    private BankAccount bankAccountHelper;
    private BankAccountDAO bankAccountDAO;
    private Balance balanceHelper;
    private BalanceDAO balanceDAO;
    private Review reviewHelper;
    private ReviewDAO reviewDAO;
    private List<User> users;
    private List<Balance> balances;
    private List<BankAccount> bankAccounts;
    private List<Deposit> deposits;
    private Product productHelper;
    private ProductDAO productDao;
    private ProductPreview previewHelper;
    private ProductPreviewDAO previewDAO;
    private Transaction transactionHelper;
    private TransactionDAO transactionDAO;
    private int currentUserId;
    private CreditReward creditRewardHelper;
    private CreditRewardDAO creditRewardDAO;
    private SelfEvaluation selfEvaluationHelper;
    private SelfEvaluationDAO selfEvaluationDAO;

    public ModelImpl(Controller controller, Connection connection) throws SQLException {
        this.controller = controller;
        this.connection = connection;
        this.userHelper = new User();
        this.daoHelper = this.userHelper.new UserDAO(this.connection);
        this.bankAccountHelper = new BankAccount();
        this.balanceHelper = new Balance();
        this.users = new ArrayList<>();
        this.balances = new ArrayList<>();
        this.bankAccounts = new ArrayList<>();
        this.deposits = new ArrayList<>();
        this.productHelper = new Product();
        this.productDao = this.productHelper.new ProductDAO(connection);
        this.previewHelper = new ProductPreview();
        this.previewDAO = this.previewHelper.new ProductPreviewDAO(connection);
        this.transactionHelper = new Transaction();
        this.transactionDAO = this.transactionHelper.new TransactionDAO(connection);
        this.currentUserId = -1;
        this.depositHelper = new Deposit();
        this.depositDAO = this.depositHelper.new DepositDAO(connection);
        this.balanceDAO = this.balanceHelper.new BalanceDAO(connection);
        this.bankAccountDAO = this.bankAccountHelper.new BankAccountDAO(connection);
        this.reviewHelper = new Review();
        this.reviewDAO = this.reviewHelper.new ReviewDAO(connection);
        this.creditRewardHelper = new CreditReward();
        this.creditRewardDAO = this.creditRewardHelper.new CreditRewardDAO(connection);
        this.selfEvaluationHelper = new SelfEvaluation();
        this.selfEvaluationDAO = this.selfEvaluationHelper.new SelfEvaluationDAO(connection);
    }

    @Override
    public void newUser(User user) throws SQLException {
        try {
            // Creazione del conto bancario e saldo
            BankAccount bankAccount = new BankAccount(0, 1000);
            Balance balance = new Balance(0, 100);

            this.bankAccountDAO.create(bankAccount);
            this.balanceDAO.create(balance);

            // Recupero degli ID generati
            List<BankAccount> bankAccounts = this.bankAccountDAO.getAll();
            List<Balance> balances = this.balanceDAO.getAll();

            if (bankAccounts.isEmpty() || balances.isEmpty()) {
                throw new SQLException("Errore nella creazione di BankAccount o Balance: nessuna riga creata.");
            }

            int idBankAccount = bankAccounts.getLast().getId();
            int idSaldo = balances.getLast().getId();

            // Impostazione dei nuovi ID nell'utente
            user.setIban(idBankAccount);
            user.setIdSaldo(idSaldo);

            // Creazione dell'utente
            this.daoHelper.create(user);

            List<User> users = this.daoHelper.getAll();
            if (users.isEmpty()) {
                throw new SQLException("Errore nella creazione dell'utente: nessuna riga creata.");
            }

            this.currentUserId = users.getLast().getId();

        } catch (DAOException e) {
            throw new SQLException("Errore durante la creazione del nuovo utente: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean checkLogin(String username, String password) {
        return getUsers().stream()
                .anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(password));
    }

    @Override
    public List<User> getUsers() {
        var users = this.daoHelper.getAll();
        return users;
    }

    @Override
    public void addMoney() {
        User currentUser = this.users.stream()
                .filter(u -> u.getId() == this.currentUserId)
                .findFirst()
                .orElse(null);
        if (checkIdUtente(currentUser.getId())) {
            Deposit deposit = new Deposit(0, 100, currentUser.getIdSaldo(), currentUser.getIban());
            this.depositDAO.create(deposit);
        }

    }

    private boolean checkIdUtente(int idUtente) {
        return this.users.stream().filter(u -> u.getId() == idUtente).count() == 1;
    }

    @Override
    public void addProduct(Product product) {
        this.productDao.create(product);

    }

    @Override
    public List<Product> getProducts() {
        return this.productDao.getAll();
    }

    @Override
    public void newTransaction(Transaction transaction) {
        Balance saldoAcquirente = this.balanceDAO.filterbyID(transaction.getIdSaldoAcquirente()).getFirst();
        Product product = this.productDao.filterbyID(transaction.getIdArticolo()).getFirst();
        if (saldoAcquirente.getAmmontare() >= product.getPrezzo()) {
            this.transactionDAO.create(transaction);
            this.productDao.updatebyID(transaction.getIdArticolo());
            this.balanceDAO.updateById(transaction.getIdSaldoAcquirente(), (int) Math.round(product.getPrezzo()),
                    false);
            this.balanceDAO.updateById(transaction.getIdSaldoVenditore(), (int) Math.round(product.getPrezzo()), true);

        }
    }

    @Override
    public User getUser(String password, String username) {
        var users = this.daoHelper.getAll();
        for (User user : users) {
            if (user.getPassword().equals(password) && user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;

    }

    @Override
    public User getUserById(int id) {
        return this.daoHelper.filterbyID(id).getFirst();
    }

    @Override
    public Balance getBalanceById(int idSaldo) {
        return this.balanceDAO.filterbyID(idSaldo).getFirst();
    }

    @Override
    public BankAccount getBankAccountById(int iban) {
        return this.bankAccountDAO.filterbyID(iban).getFirst();
    }

    @Override
    public List<Review> getReviewsById(int id) {
        return this.reviewDAO.filterByReviewer(id);
    }

    @Override
    public List<Transaction> getTransactionsById(int id) {
        return this.transactionDAO.filterbyIDUser(id);
    }

    @Override
    public List<CreditReward> getCreditRewardsById(int id) {
        return this.creditRewardDAO.filterByUserID(id);
    }

    @Override
    public List<SelfEvaluation> getSelfEvaluationsById(int id) {
        return this.selfEvaluationDAO.filterByUserID(id);
    }

    @Override
    public void toggleProductStatus(Product product) {
        this.productDao.toggleProductStatus(product);
    }

    @Override
    public void toggleUserStatus(User user) {
        this.daoHelper.toggleUserStatus(user);
    }

    @Override
    public void rechargeBalance(User user) {
        this.balanceDAO.updateById(user.getIdSaldo(), 100, true);
        this.bankAccountDAO.updateById(user.getIban(), 100, false);
    }

    @Override
    public void rechargeBankAccount(User user) {
        this.bankAccountDAO.updateById(user.getIban(), 100, true);
    }

}