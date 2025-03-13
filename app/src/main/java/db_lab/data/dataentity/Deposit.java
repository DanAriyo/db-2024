package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

public class Deposit implements DataEntity {

    private int idVersamento;
    private int importo;
    private int idSaldo;
    private int iban;

    public Deposit() {

    }

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

    public final class DepositDAO {

        private Connection connection;

        public DepositDAO(Connection connection) {
            this.connection = connection;
        }

        public List<Deposit> createDepositList(ResultSet resultSet) throws SQLException {
            List<Deposit> deposits = new ArrayList<>(); // La lista che conterrà i prodotti

            // Iteriamo su ogni riga del ResultSet
            while (resultSet.next()) {
                // Recuperiamo i dati da ogni colonna del ResultSet
                int idVersamento = resultSet.getInt("idVersamento");
                int idsaldo = resultSet.getInt("idSaldo");
                int iban = resultSet.getInt("iban");
                int importo = resultSet.getInt("importo");

                Deposit deposit = new Deposit(idVersamento, importo, idsaldo, iban);
                // Aggiungiamo il product alla lista
                deposits.add(deposit);
            }

            // Restituiamo la lista di products
            return deposits;
        }

        public List<Deposit> getAll() throws DAOException {
            String query = "SELECT * FROM Versamenti";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createDepositList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

        public List<Deposit> filterbyID(int id) throws DAOException {
            String query = "SELECT * FROM Versamenti WHERE idVersamento = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                return createDepositList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

    }

}