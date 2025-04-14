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

        public List<Transaction> createTransactionList(ResultSet resultSet) throws SQLException {
            List<Transaction> transactions = new ArrayList<>();

            while (resultSet.next()) {
                int idTransazione = resultSet.getInt("idTransazione");
                int idVenditore = resultSet.getInt("idVenditore");
                int idAcquirente = resultSet.getInt("idAcquirente");
                int speseSpedizione = resultSet.getInt("speseSpedizione");
                int idRecensione = resultSet.getInt("IdRecensione");
                int idArticolo = resultSet.getInt("IdArticolo");
                int idSaldoVenditore = resultSet.getInt("idSaldoVenditore");
                int commissioni = resultSet.getInt("Commissioni");
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
                throw new DAOException("wrong query", e);
            }
        }

        public List<Transaction> filterbyID(int id) throws DAOException {
            String query = "SELECT * FROM Transazioni WHERE idTransazione = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                return createTransactionList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

        public List<Transaction> filterbyIDUser(int id) throws DAOException {
            String query = "SELECT * FROM Transazioni WHERE idAcquirente = ? OR IdVenditore = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, id);
                statement.setInt(2, id);
                ResultSet rs = statement.executeQuery();
                return createTransactionList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

        public List<Transaction> filterbySellerIdUser(int id) throws DAOException {
            String query = "SELECT * FROM Transazioni WHERE IdVenditore = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                return createTransactionList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

        public void create(Transaction transaction) throws DAOException {

            String query = "INSERT INTO Transazioni (idVenditore, idAcquirente, speseSpedizione, commissioni, idSaldoVenditore, idRecensione, idArticolo, idSaldoAcquirente)"
                    + "VALUES (?,?,?,?,?,?,?,?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, transaction.getIdVenditore());
                statement.setInt(2, transaction.getIdAcquirente());
                statement.setInt(3, transaction.getSpeseSpedizione());
                statement.setInt(4, transaction.getCommissioni());
                statement.setInt(5, transaction.getIdSaldoVenditore());
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