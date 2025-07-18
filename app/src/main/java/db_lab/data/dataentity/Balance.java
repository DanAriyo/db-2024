package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

/**
 * Rappresenta un'entità "Saldo" nel sistema.
 */
public class Balance implements DataEntity {

    private int idSaldo; // Identificativo univoco del saldo
    private int ammontare; // Ammontare disponibile nel saldo

    // Costruttore vuoto richiesto da alcune librerie/framework
    public Balance() {
    }

    // Costruttore con parametri
    public Balance(int idSaldo, int ammontare) {
        this.idSaldo = idSaldo;
        this.ammontare = ammontare;
    }

    // Restituisce l'ID del saldo
    @Override
    public int getId() {
        return this.idSaldo;
    }

    // Restituisce l'ammontare del saldo
    public int getAmmontare() {
        return this.ammontare;
    }

    /**
     * Classe interna responsabile della gestione dell'accesso ai dati
     * per l'entità Balance (DAO = Data Access Object).
     */
    public final class BalanceDAO {

        private Connection connection;

        /**
         * Costruttore del DAO. Verifica la validità della connessione.
         */
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

        /**
         * Crea una lista di oggetti Balance a partire da un ResultSet.
         */
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

        /**
         * Restituisce tutti i saldi presenti nel database.
         */
        public List<Balance> getAll() throws DAOException {
            String query = "SELECT * FROM Saldi";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createBalanceList(rs);
            } catch (Exception e) {
                throw new DAOException("Error fetching all balances", e);
            }
        }

        /**
         * Restituisce il saldo corrispondente a un determinato ID.
         */
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

        /**
         * Aggiorna l'ammontare del saldo dato l'ID, aggiungendo o sottraendo l'importo
         * specificato.
         * 
         * @param idSaldo  ID del saldo da aggiornare
         * @param money    Importo da aggiungere o sottrarre
         * @param aggiungi Se true aggiunge, se false sottrae
         */
        public void updateById(int idSaldo, int money, boolean aggiungi) {
            String query;
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

        /**
         * Crea un nuovo saldo nel database.
         */
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
