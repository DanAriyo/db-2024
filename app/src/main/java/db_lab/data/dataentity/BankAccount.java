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
            List<BankAccount> bankAccounts = new ArrayList<>();

            while (resultSet.next()) {

                int iban = resultSet.getInt("iban");
                int saldo = resultSet.getInt("SaldoConto");

                BankAccount bankAccount = new BankAccount(iban, saldo);
                bankAccounts.add(bankAccount);
            }

            return bankAccounts;
        }

        public List<BankAccount> getAll() throws DAOException {
            String query = "SELECT * FROM ContiCorrente";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createBankAccountList(rs);
            } catch (SQLException e) {
                throw new DAOException("Error fetching all bank accounts", e);
            }
        }

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

        public void updateById(int iban, int money, boolean aggiungi) {
            String query = "";
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
