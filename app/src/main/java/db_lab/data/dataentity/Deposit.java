package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;
import db_lab.data.dataentity.Balance.BalanceDAO;
import db_lab.data.dataentity.BankAccount.BankAccountDAO;

/**
 * Rappresenta un versamento effettuato da un utente dal proprio conto bancario
 * verso il saldo del sistema (ad esempio per acquistare crediti).
 */
public class Deposit implements DataEntity {

    private int idVersamento; // ID univoco del versamento
    private int importo; // Importo versato
    private int idSaldo; // ID del saldo interno dell'utente
    private int iban; // IBAN del conto da cui parte il versamento

    // Costruttore vuoto richiesto da framework/librerie
    public Deposit() {
    }

    // Costruttore completo
    public Deposit(int idVersamento, int importo, int idSaldo, int iban) {
        this.idVersamento = idVersamento;
        this.idSaldo = idSaldo;
        this.iban = iban;
        this.importo = importo;
    }

    @Override
    public int getId() {
        return this.idVersamento;
    }

    public int getImporto() {
        return this.importo;
    }

    public int getIban() {
        return this.iban;
    }

    public int getIdSaldo() {
        return this.idSaldo;
    }

    /**
     * Classe interna per la gestione dell'accesso ai dati dei versamenti.
     */
    public final class DepositDAO {

        private Connection connection;

        /**
         * Costruttore che imposta la connessione al database.
         */
        public DepositDAO(Connection connection) {
            this.connection = connection;
        }

        /**
         * Crea una lista di oggetti Deposit a partire da un ResultSet.
         */
        public List<Deposit> createDepositList(ResultSet resultSet) throws SQLException {
            List<Deposit> deposits = new ArrayList<>();

            while (resultSet.next()) {
                int idVersamento = resultSet.getInt("idVersamento");
                int idsaldo = resultSet.getInt("idSaldo");
                int iban = resultSet.getInt("iban");
                int importo = resultSet.getInt("importo");

                Deposit deposit = new Deposit(idVersamento, importo, idsaldo, iban);
                deposits.add(deposit);
            }

            return deposits;
        }

        /**
         * Restituisce tutti i versamenti registrati nel database.
         */
        public List<Deposit> getAll() throws DAOException {
            String query = "SELECT * FROM Versamenti";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createDepositList(rs);
            } catch (SQLException e) {
                throw new DAOException("Error executing query to fetch all deposits", e);
            }
        }

        /**
         * Filtra i versamenti per ID specifico.
         */
        public List<Deposit> filterbyID(int id) throws DAOException {
            String query = "SELECT * FROM Versamenti WHERE idVersamento = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                return createDepositList(rs);
            } catch (Exception e) {
                throw new DAOException("Error executing query to fetch deposit by ID", e);
            }
        }

        /**
         * Crea un nuovo versamento e aggiorna di conseguenza sia il conto bancario
         * (sottraendo l'importo) sia il saldo interno (aggiungendo l'importo).
         *
         * @param deposit Oggetto Deposit contenente i dati del versamento
         * @throws DAOException se l'inserimento o gli aggiornamenti falliscono
         */
        public void create(Deposit deposit) throws DAOException {
            String query = "INSERT INTO Versamenti (importo, iban, idSaldo) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                // Inserisce il versamento nella tabella
                statement.setInt(1, deposit.getImporto());
                statement.setInt(2, deposit.getIban());
                statement.setInt(3, deposit.getIdSaldo());
                statement.executeUpdate();

                // Inizializza i DAO per aggiornare saldi e conti
                BankAccount bankAccount = new BankAccount();
                BankAccountDAO bankAccountDAO = bankAccount.new BankAccountDAO(connection);

                Balance balance = new Balance();
                BalanceDAO balanceDAO = balance.new BalanceDAO(connection);

                // Sottrae l'importo dal conto bancario
                bankAccountDAO.updateById(deposit.getIban(), deposit.getImporto(), false);

                // Aggiunge l'importo al saldo interno
                balanceDAO.updateById(deposit.getIdSaldo(), deposit.getImporto(), true);

            } catch (SQLException e) {
                throw new DAOException("Error creating deposit and updating balances", e);
            }
        }
    }
}
