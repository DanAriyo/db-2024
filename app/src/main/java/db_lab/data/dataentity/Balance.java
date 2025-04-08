package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

public class Balance implements DataEntity {

    private int idSaldo;
    private int ammontare;

    public Balance() {

    }

    public Balance(int idSaldo, int ammontare) {
        this.idSaldo = idSaldo;
        this.ammontare = ammontare;

    }

    @Override
    public int getId() {
        return this.idSaldo;
    }

    public int getAmmontare() {
        return this.ammontare;
    }

    public final class BalanceDAO {

        private Connection connection;

        public BalanceDAO(Connection connection) {
            if (connection == null) {
                throw new DAOException("Connection is null.");
            }

            try {
                if (connection.isClosed()) {
                    throw new DAOException("Connection is closed.");
                }
            } catch (SQLException e) {
                throw new DAOException("Error checking connection status.", e);
            }

            this.connection = connection;
        }

        public List<Balance> createBalanceList(ResultSet resultSet) throws SQLException {
            List<Balance> balances = new ArrayList<>();

            while (resultSet.next()) {

                int idsaldo = resultSet.getInt("idSaldo");
                int ammontare = resultSet.getInt("ammontare");

                Balance balance = new Balance(idsaldo, ammontare);

                balances.add(balance);
            }

            return balances;
        }

        public List<Balance> getAll() throws DAOException {
            String query = "SELECT * FROM Saldi";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createBalanceList(rs);
            } catch (Exception e) {
                throw new DAOException("Error fetching all balances", e);
            }
        }

        public List<Balance> filterbyID(int id) throws DAOException {
            String query = "SELECT * FROM Saldi WHERE idSaldo = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                return createBalanceList(rs);
            } catch (SQLException e) {
                throw new DAOException("Error fetching balance by ID", e);
            }
        }

        public void updateById(int idSaldo, int money, boolean aggiungi) {
            String query = "";
            if (aggiungi) {
                query = "UPDATE Saldi SET ammontare = ammontare + ? WHERE idSaldo = ?";
            } else {
                query = "UPDATE Saldi SET ammontare = ammontare - ? WHERE idSaldo = ?";
            }

            try (PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, money);
                statement.setInt(2, idSaldo);

                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated == 0) {

                    throw new DAOException("No balance found with the given ID.");
                }
            } catch (SQLException e) {

                throw new DAOException("Error updating balance", e);
            }
        }

        public void create(Balance balance) throws DAOException {
            String query = "INSERT INTO Saldi (ammontare) VALUES (?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, balance.getAmmontare());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Error creating balance", e);
            }
        }

    }

}