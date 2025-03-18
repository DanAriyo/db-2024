package db_lab.model;

import db_lab.controller.Controller;
import db_lab.data.dataentity.Balance;
import db_lab.data.dataentity.BankAccount;
import db_lab.data.dataentity.Deposit;
import db_lab.data.dataentity.User;
import db_lab.utilities.Pair;
import db_lab.view.View;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    private View view;
    private Connection connection;
    private User userHelper;
    private int userCounter = 0;
    private int balanceCounter = 10000;
    private int bankAccountCounter = 1000;
    private Map<String, String> passowordMap;
    private User.UserDAO daoHelper;
    private BankAccount bankAccountHelper;
    private Balance balanceHelper;
    private List<User> users;
    private List<Balance> balances;
    private List<BankAccount> bankAccounts;
    private List<Deposit> deposits;

    public ModelImpl(Controller controller, View view, Connection connection) {
        this.controller = controller;
        this.view = view;
        this.connection = connection;
        this.passowordMap = new HashMap<>();
        this.userHelper = new User();
        this.daoHelper = this.userHelper.new UserDAO(this.connection);
        this.bankAccountHelper = new BankAccount();
        this.balanceHelper = new Balance();
        this.users = new ArrayList<>();
        this.balances = new ArrayList<>();
        this.bankAccounts = new ArrayList<>();
        this.deposits = new ArrayList<>();

    }

    @Override
    public void newUser(String nome, String cognome, String email,
            String telefono, String indirizzo, Date data, String cf, String username,
            String password) {

        BankAccount bankAccount = new BankAccount(bankAccountCounter, 200);
        Balance balance = new Balance(balanceCounter, 100);
        BankAccount.BankAccountDAO bankDaoHelper = this.bankAccountHelper.new BankAccountDAO(connection);
        bankDaoHelper.create(bankAccount);
        Balance.BalanceDAO balanceDaoHelper = this.balanceHelper.new BalanceDAO(connection);
        balanceDaoHelper.create(balance);
        User user = new User(this.userCounter, nome, cognome, email, bankAccountCounter, balanceCounter, telefono,
                indirizzo, data, cf);
        this.passowordMap.put(username, password);
        this.bankAccountCounter++;
        this.bankAccountCounter++;
        this.userCounter++;
        User.UserDAO daoHelper = this.userHelper.new UserDAO(this.connection);
        daoHelper.create(user);
        this.users.add(user);
        this.balances.add(balance);
        this.bankAccounts.add(bankAccount);

    }

    @Override
    public boolean checkLogin(String username, String password) {
        return passowordMap.get(username).equals(password);

    }

    @Override
    public List<User> getUsers() {
        return this.daoHelper.getAll();
    }

    @Override
    public void addMoney(int idSaldo, int iban, int idUtente) {
        // crea una nuova entita versamento e poi decidi se fare tutto su model e
        // aggiornare db o se fare operazione tramite db e aggiornare model
    }

    private boolean checkIdUtente(int idUtente) {
        return this.users.stream().filter(u -> u.getId() == idUtente).count() == 1;
    }

}