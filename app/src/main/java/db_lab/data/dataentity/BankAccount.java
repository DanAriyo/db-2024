package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

/**
 * Rappresenta un conto bancario (conto corrente) nel sistema.
 */
public class BankAccount implements DataEntity {

    private int iban; // IBAN identificativo del conto
    private int saldo; // Saldo attuale del conto

    // Costruttore vuoto (necessario per la serializzazione o framework)
    public BankAccount() {
    }

    // Costruttore completo
    public BankAccount(int iban, int saldo) {
        this.iban = iban;
        this.saldo = saldo;
    }

    // Restituisce l'ID del conto, in questo caso corrisponde all'IBAN
    @Override
    public int getId() {
        return this.iban;
    }

    // Restituisce il saldo del conto
    public int getSaldo() {
        return this.saldo;
    }

    /**
     * Classe interna responsabile della gestione dei dati relativi ai conti
     * correnti.
     */
    public final class BankAccountDAO {

        private Connection connection;

        /**
         * Costruttore del DAO: imposta la connessione al database.
         */
        public BankAccountDAO(Connection connection) {
            this.connection = connection;
        }

        /**
         * Crea una lista di oggetti BankAccount a partire da un ResultSet.
         */
        public List<BankAccount> createBankAccountList(ResultSet resultSet) throws SQLException {
            List<BankAccount> bankAccounts = new ArrayList<>();

            while (resultSet.next()) {
                int iban = resultSet.getInt("iban");
                int saldo = resultSet.getInt("SaldoConto");

                BankAccount bankAccount = new BankAccount(iban, saldo);
                bankAccounts.add(bankAccount);
            }

            return bankAccounts;
        }

        /**
         * Restituisce tutti i conti correnti presenti nel database.
         */
        public List<BankAccount> getAll() throws DAOException {
            String query = "SELECT * FROM ContiCorrente";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createBankAccountList(rs);
            } catch (SQLException e) {
                throw new DAOException("Error fetching all bank accounts", e);
            }
        }

        /**
         * Filtra i conti correnti per IBAN specificato.
         *
         * @param id L'IBAN del conto da cercare
         * @return Lista di conti (normalmente uno solo) corrispondente all'ID
         */
        public List<BankAccount> filterbyID(int id) throws DAOException {
            String query = "SELECT * FROM ContiCorrente WHERE iban = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                return createBankAccountList(rs);
            } catch (SQLException e) {
                throw new DAOException("Error fetching bank account by ID", e);
            }
        }

        /**
         * Aggiorna il saldo di un conto dato l'IBAN, aggiungendo o sottraendo una
         * somma.
         *
         * @param iban     IBAN del conto da aggiornare
         * @param money    Importo da aggiungere o sottrarre
         * @param aggiungi True per aggiungere, False per sottrarre
         */
        public void updateById(int iban, int money, boolean aggiungi) {
            String query;
            if (aggiungi) {
                query = "UPDATE ContiCorrente SET SaldoConto = SaldoConto + ? WHERE iban = ?";
            } else {
                query = "UPDATE ContiCorrente SET SaldoConto = SaldoConto - ? WHERE iban = ?";
            }

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, money);
                statement.setInt(2, iban);

                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated == 0) {
                    throw new DAOException("No account found with the given IBAN.");
                }
            } catch (SQLException e) {
                throw new DAOException("Error updating bank account", e);
            }
        }

        /**
         * Crea un nuovo conto corrente nel database, inizializzandolo con un saldo.
         */
        public void create(BankAccount bankAccount) throws DAOException {
            String query = "INSERT INTO ContiCorrente (SaldoConto) VALUES (?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, bankAccount.getSaldo());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Error creating user's bank account", e);
            }
        }
    }
}
