package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

public class Transaction implements DataEntity {

    private int idTransazione;
    private int idVenditore;
    private int idAcquirente;
    private int speseSpedizione;
    private int commissioni;
    private int idSaldoVenditore;
    private int idRecensione;
    private int idArticolo;
    private int idSaldoAcquirente;

    public Transaction() {
    }

    // Nota: l'ordine dei parametri è un po' insolito (idTransazione al terzo posto)
    public Transaction(int idVenditore, int idAcquirente, int idTransazione, int speseSpedizione,
            int commissioni, int idSaldoVenditore, int idRecensione, int idArticolo, int idSaldoAcquirente) {
        this.idVenditore = idVenditore;
        this.idAcquirente = idAcquirente;
        this.speseSpedizione = speseSpedizione;
        this.commissioni = commissioni;
        this.idSaldoVenditore = idSaldoVenditore;
        this.idRecensione = idRecensione;
        this.idArticolo = idArticolo;
        this.idSaldoAcquirente = idSaldoAcquirente;
        this.idTransazione = idTransazione;
    }

    // Getter semplici
    public int getIdVenditore() {
        return this.idVenditore;
    }

    public int getIdAcquirente() {
        return this.idAcquirente;
    }

    public int getSpeseSpedizione() {
        return this.speseSpedizione;
    }

    public int getCommissioni() {
        return this.commissioni;
    }

    public int getIdSaldoVenditore() {
        return this.idSaldoVenditore;
    }

    public int getIdRecensione() {
        return this.idRecensione;
    }

    public int getIdArticolo() {
        return this.idArticolo;
    }

    public int getIdSaldoAcquirente() {
        return this.idSaldoAcquirente;
    }

    @Override
    public int getId() {
        return this.idTransazione;
    }

    public final class TransactionDAO {

        private Connection connection;

        public TransactionDAO(Connection connection) {
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

        // Trasforma un ResultSet in lista di Transaction
        public List<Transaction> createTransactionList(ResultSet resultSet) throws SQLException {
            List<Transaction> transactions = new ArrayList<>();
            while (resultSet.next()) {
                int idTransazione = resultSet.getInt("idTransazione");
                int idVenditore = resultSet.getInt("idVenditore");
                int idAcquirente = resultSet.getInt("idAcquirente");
                int speseSpedizione = resultSet.getInt("speseSpedizione");
                int idRecensione = resultSet.getInt("idRecensione"); // attenzione a maiuscole e minuscole
                int idArticolo = resultSet.getInt("idArticolo");
                int idSaldoVenditore = resultSet.getInt("idSaldoVenditore");
                int commissioni = resultSet.getInt("commissioni");
                int idSaldoAcquirente = resultSet.getInt("idSaldoAcquirente");

                Transaction transaction = new Transaction(idVenditore, idAcquirente, idTransazione, speseSpedizione,
                        commissioni, idSaldoVenditore, idRecensione, idArticolo,
                        idSaldoAcquirente);

                transactions.add(transaction);
            }
            return transactions;
        }

        public List<Transaction> getAll() throws DAOException {
            String query = "SELECT * FROM Transazioni";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createTransactionList(rs);
            } catch (Exception e) {
                throw new DAOException("Error retrieving transactions", e);
            }
        }

        public List<Transaction> filterbyID(int id) throws DAOException {
            String query = "SELECT * FROM Transazioni WHERE idTransazione = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                return createTransactionList(rs);
            } catch (Exception e) {
                throw new DAOException("Error filtering transactions by ID", e);
            }
        }

        // Restituisce tutte le transazioni dove l'utente è venditore o acquirente
        public List<Transaction> filterbyIDUser(int id) throws DAOException {
            String query = "SELECT * FROM Transazioni WHERE idAcquirente = ? OR idVenditore = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, id);
                statement.setInt(2, id);
                ResultSet rs = statement.executeQuery();
                return createTransactionList(rs);
            } catch (Exception e) {
                throw new DAOException("Error filtering transactions by user ID", e);
            }
        }

        // Restituisce tutte le transazioni dove l'utente è solo venditore
        public List<Transaction> filterbySellerIdUser(int id) throws DAOException {
            String query = "SELECT * FROM Transazioni WHERE idVenditore = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                return createTransactionList(rs);
            } catch (Exception e) {
                throw new DAOException("Error filtering transactions by seller ID", e);
            }
        }

        // Inserisce una nuova transazione nel DB
        public void create(Transaction transaction) throws DAOException {
            String query = "INSERT INTO Transazioni (idVenditore, idAcquirente, speseSpedizione, commissioni, idSaldoVenditore, idRecensione, idArticolo, idSaldoAcquirente) "
                    + "VALUES (?,?,?,?,?,?,?,?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, transaction.getIdVenditore());
                statement.setInt(2, transaction.getIdAcquirente());
                statement.setInt(3, transaction.getSpeseSpedizione());
                statement.setInt(4, transaction.getCommissioni());
                statement.setInt(5, transaction.getIdSaldoVenditore());
                // Se idRecensione è 0, si inserisce NULL nel DB
                if (transaction.getIdRecensione() == 0) {
                    statement.setNull(6, java.sql.Types.INTEGER);
                } else {
                    statement.setInt(6, transaction.getIdRecensione());
                }
                statement.setInt(7, transaction.getIdArticolo());
                statement.setInt(8, transaction.getIdSaldoAcquirente());

                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Error creating transaction", e);
            }
        }
    }
}
