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
    private User.UserDAO UserDao;
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
    private int rewardCounter;

    // Constructor initializes the DAOs and helper objects for accessing database entities
    public ModelImpl(Controller controller, Connection connection) throws SQLException {
        this.controller = controller;
        this.connection = connection;
        this.userHelper = new User();
        this.UserDao = this.userHelper.new UserDAO(this.connection);
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
        this.rewardCounter = 0;
    }

    @Override
    public void newUser(User user) throws SQLException {
        try {
            // Creazione del conto bancario e saldo con valori iniziali
            BankAccount bankAccount = new BankAccount(0, 1000);
            Balance balance = new Balance(0, 100);

            // Inserimento del nuovo conto e saldo nel database
            this.bankAccountDAO.create(bankAccount);
            this.balanceDAO.create(balance);

            // Recupero delle liste aggiornate di conti e saldi dal database
            List<BankAccount> bankAccounts = this.bankAccountDAO.getAll();
            List<Balance> balances = this.balanceDAO.getAll();

            // Verifica che siano stati creati correttamente il conto e il saldo
            if (bankAccounts.isEmpty() || balances.isEmpty()) {
                throw new SQLException("Errore nella creazione di BankAccount o Balance: nessuna riga creata.");
            }

            // Prendo gli ID dell'ultimo conto e saldo creati
            int idBankAccount = bankAccounts.getLast().getId();
            int idSaldo = balances.getLast().getId();

            // Imposto gli ID di conto e saldo nell'utente da creare
            user.setIban(idBankAccount);
            user.setIdSaldo(idSaldo);

            // Creo il nuovo utente nel database
            this.UserDao.create(user);

            // Recupero la lista aggiornata degli utenti
            List<User> users = this.UserDao.getAll();
            if (users.isEmpty()) {
                throw new SQLException("Errore nella creazione dell'utente: nessuna riga creata.");
            }

            // Imposto l'ID dell'utente corrente come l'ultimo creato
            this.currentUserId = users.getLast().getId();

        } catch (DAOException e) {
            // Gestione dell'eccezione specifica DAO trasformata in SQLException
            throw new SQLException("Errore durante la creazione del nuovo utente: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean checkLogin(String username, String password) {
        // Controlla se esiste un utente con username e password corrispondenti
        return getUsers().stream()
                .anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(password));
    }

    @Override
    public List<User> getUsers() {
        // Recupera tutti gli utenti dal database tramite DAO
        var users = this.UserDao.getAll();
        return users;
    }

    @Override
    public void addMoney() {
        // Trova l'utente corrente tra quelli in memoria
        User currentUser = this.users.stream()
                .filter(u -> u.getId() == this.currentUserId)
                .findFirst()
                .orElse(null);
        // Se l'utente esiste, crea un deposito di 100 unità
        if (checkIdUtente(currentUser.getId())) {
            Deposit deposit = new Deposit(0, 100, currentUser.getIdSaldo(), currentUser.getIban());
            this.depositDAO.create(deposit);
        }

    }

    // Verifica che l'ID utente esista nella lista utenti
    private boolean checkIdUtente(int idUtente) {
        return this.users.stream().filter(u -> u.getId() == idUtente).count() == 1;
    }

    @Override
    public void addProduct(Product product) {
        // Inserisce un nuovo prodotto nel database
        this.productDao.create(product);

    }

    @Override
    public List<Product> getProducts() {
        // Restituisce la lista di tutti i prodotti
        return this.productDao.getAll();
    }

    @Override
    public void newTransaction(Transaction transaction) {
        // Recupera il saldo dell'acquirente e il prodotto interessato dalla transazione
        Balance saldoAcquirente = this.balanceDAO.filterbyID(transaction.getIdSaldoAcquirente()).getFirst();
        Product product = this.productDao.filterbyID(transaction.getIdArticolo()).getFirst();
        // Controlla che l'acquirente abbia abbastanza soldi per comprare il prodotto
        if (saldoAcquirente.getAmmontare() >= product.getPrezzo()) {
            // Crea la nuova transazione nel database
            this.transactionDAO.create(transaction);
            // Aggiorna lo stato del prodotto (presumibilmente come venduto)
            this.productDao.updatebyID(transaction.getIdArticolo());
            // Aggiorna il saldo dell'acquirente sottraendo il prezzo del prodotto
            this.balanceDAO.updateById(transaction.getIdSaldoAcquirente(), (int) Math.round(product.getPrezzo()),
                    false);
            // Aggiunge il prezzo del prodotto al saldo del venditore
            this.balanceDAO.updateById(transaction.getIdSaldoVenditore(), (int) Math.round(product.getPrezzo()), true);

        }
    }

    @Override
    public User getUser(String password, String username) {
        // Cerca un utente corrispondente a username e password dati
        var users = this.UserDao.getAll();
        for (User user : users) {
            if (user.getPassword().equals(password) && user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;

    }

    @Override
    public User getUserById(int id) {
        // Restituisce l'utente con il dato ID
        return this.UserDao.filterbyID(id).getFirst();
    }

    @Override
    public Balance getBalanceById(int idSaldo) {
        // Restituisce il saldo associato all'ID dato
        return this.balanceDAO.filterbyID(idSaldo).getFirst();
    }

    @Override
    public BankAccount getBankAccountById(int iban) {
        // Restituisce il conto bancario con l'IBAN dato
        return this.bankAccountDAO.filterbyID(iban).getFirst();
    }

    @Override
    public List<Review> getReviewsById(int id) {
        // Restituisce tutte le recensioni fatte dall'utente con l'ID dato
        return this.reviewDAO.filterByReviewer(id);
    }

    @Override
    public List<Transaction> getTransactionsById(int id) {
        // Restituisce tutte le transazioni effettuate dall'utente con l'ID dato
        return this.transactionDAO.filterbyIDUser(id);
    }

    @Override
    public List<CreditReward> getCreditRewardsById(int id) {
        // Restituisce tutte le ricompense di credito per l'utente con l'ID dato
        return this.creditRewardDAO.filterByUserID(id);
    }

    @Override
    public List<SelfEvaluation> getSelfEvaluationsById(int id) {
        // Restituisce tutte le auto-valutazioni dell'utente con l'ID dato
        return this.selfEvaluationDAO.filterByUserID(id);
    }

    @Override
    public void toggleProductStatus(Product product) {
        // Cambia lo stato attivo/disattivo di un prodotto
        this.productDao.toggleProductStatus(product);
    }

    @Override
    public void toggleUserStatus(User user) {
        // Cambia lo stato attivo/disattivo di un utente
        this.UserDao.toggleUserStatus(user);
    }

    @Override
    public void rechargeBalance(User user) {
        // Aggiunge 100 unità al saldo dell'utente e sottrae da conto bancario
        this.balanceDAO.updateById(user.getIdSaldo(), 100, true);
        this.bankAccountDAO.updateById(user.getIban(), 100, false);
    }

    @Override
    public void rechargeBankAccount(User user) {
        // Aggiunge 100 unità al conto bancario dell'utente
        this.bankAccountDAO.updateById(user.getIban(), 100, true);
    }

    @Override
    public void checkRewards(Transaction transaction) {

        // Recupera tutte le transazioni del venditore
        List<Transaction> transactions = this.transactionDAO.filterbySellerIdUser(transaction.getIdVenditore());

        // Se il venditore ha almeno 3 transazioni e multipli di 3
        if (transactions.size() >= 3 && transactions.size() % 3 == 0) {
            var listCreditReward = this.creditRewardDAO.getAll();
            var listSelfEvaluation = this.selfEvaluationDAO.getAll();
            // Controlla se creare un premio credito o un'auto valutazione
            if (listCreditReward.size() <= listSelfEvaluation.size()) {
                // Crea una ricompensa di credito fittizia e la inserisce nel database
                var fictionalCreditReward = new CreditReward(0, "Premio Ottenuto: Importo Bonus", 10,
                        transaction.getIdVenditore());
                this.creditRewardDAO.create(fictionalCreditReward, this.balanceDAO,
                        this.UserDao.filterbyID(fictionalCreditReward.getIdUtente()).getFirst());
            } else {
                // Crea un'auto valutazione fittizia come premio e la inserisce nel database
                var fictionalSelfEvaluationReward = new SelfEvaluation(0, "Premio Ottenuto: Recensione automatica", 5,
                        transaction.getIdVenditore());
                this.selfEvaluationDAO.create(fictionalSelfEvaluationReward, this.reviewDAO, this.UserDao.getAdmin());
            }
        }
    }

    @Override
    public void newReview(Review review) {
        // Inserisce una nuova recensione nel database
        this.reviewDAO.create(review);
    }

    @Override
    public Review getLatestReview() {
        // Restituisce l'ultima recensione inserita
        return this.reviewDAO.getAll().getLast();
    }

}
