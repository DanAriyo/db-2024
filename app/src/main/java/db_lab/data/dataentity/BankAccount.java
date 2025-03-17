package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

public class BankAccount implements DataEntity {

    private int iban;
    private int saldo;

    public BankAccount() {

    }

    public BankAccount(int iban, int saldo) {
        this.iban = iban;
        this.saldo = saldo;
    }

    @Override
    public int getId() {
        return this.iban;
    }

    public int getSaldo() {
        return this.saldo;
    }

    public final class BankAccountDAO {
        private Connection connection;

        public BankAccountDAO(Connection connection) {
            this.connection = connection;
        }

        public List<BankAccount> createBankAccountList(ResultSet resultSet) throws SQLException {
            List<BankAccount> bankAccounts = new ArrayList<>(); // La lista che conterrà i prodotti

            // Iteriamo su ogni riga del ResultSet
            while (resultSet.next()) {
                // Recuperiamo i dati da ogni colonna del ResultSet
                int iban = resultSet.getInt("iban");
                int saldo = resultSet.getInt("SaldoContoCorrente");

                BankAccount bankAccount = new BankAccount(iban, saldo);
                // Aggiungiamo il product alla lista
                bankAccounts.add(bankAccount);
            }

            // Restituiamo la lista di products
            return bankAccounts;
        }

        public List<BankAccount> getAll() throws DAOException {
            String query = "SELECT * FROM Conti Corrente";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createBankAccountList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

        public List<BankAccount> filterbyID(int id) throws DAOException {
            String query = "SELECT * FROM COnti Corrente WHERE iban = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                return createBankAccountList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

        public void create(BankAccount bankAccount) throws DAOException {
            String query = "INSERT INTO Conti Corrente (iban,ammontare) VALUES (?,?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, bankAccount.getId());
                statement.setInt(2, bankAccount.getSaldo());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Error creating user", e);
            }
        }
    }

}
