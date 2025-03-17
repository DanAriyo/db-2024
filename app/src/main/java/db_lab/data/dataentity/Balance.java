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
    private int BalanceCounter = 100;

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

    public void increaseCounter() {
        this.BalanceCounter++;
    }

    public final class BalanceDAO {

        private Connection connection;

        public BalanceDAO(Connection connection) {
            this.connection = connection;
        }

        public List<Balance> createBalanceList(ResultSet resultSet) throws SQLException {
            List<Balance> balances = new ArrayList<>(); // La lista che conterrà i prodotti

            // Iteriamo su ogni riga del ResultSet
            while (resultSet.next()) {
                // Recuperiamo i dati da ogni colonna del ResultSet
                int idsaldo = resultSet.getInt("idSaldo");
                int ammontare = resultSet.getInt("ammontare");

                Balance balance = new Balance(idsaldo, ammontare);
                // Aggiungiamo il product alla lista
                balances.add(balance);
            }

            // Restituiamo la lista di products
            return balances;
        }

        public List<Balance> getAll() throws DAOException {
            String query = "SELECT * FROM Saldi";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createBalanceList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

        public List<Balance> filterbyID(int id) throws DAOException {
            String query = "SELECT * FROM Saldi WHERE idVersamento = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                return createBalanceList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

    }

}