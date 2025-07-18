package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

/**
 * Classe che rappresenta un'entità "Sconto" nel sistema.
 * Ogni sconto ha un ID e una percentuale di riduzione.
 */
public class Discount implements DataEntity {

    private int idSconto; // Identificativo univoco dello sconto
    private int percentuale; // Percentuale di sconto (es. 10 = 10%)

    // Costruttore vuoto (necessario per alcuni framework o deserializzatori)
    public Discount() {
    }

    // Costruttore completo
    public Discount(int idSconto, int percentuale) {
        this.idSconto = idSconto;
        this.percentuale = percentuale;
    }

    @Override
    public int getId() {
        return this.idSconto;
    }

    public int getPercentuale() {
        return this.percentuale;
    }

    /**
     * Classe interna responsabile della gestione dei dati (DAO) relativi agli
     * sconti.
     */
    public final class DiscountDAO {

        private Connection connection;

        /**
         * Costruttore che riceve la connessione al database.
         */
        public DiscountDAO(Connection connection) {
            this.connection = connection;
        }

        /**
         * Costruisce una lista di oggetti Discount a partire da un ResultSet.
         *
         * @param resultSet Il ResultSet ottenuto da una query SQL
         * @return Lista di oggetti Discount
         */
        public List<Discount> createDiscountList(ResultSet resultSet) throws SQLException {
            List<Discount> discounts = new ArrayList<>();

            while (resultSet.next()) {
                int idSconto = resultSet.getInt("idSconto");
                int percentuale = resultSet.getInt("percentuale");

                Discount discount = new Discount(idSconto, percentuale);
                discounts.add(discount);
            }

            return discounts;
        }

        /**
         * Recupera tutti gli sconti presenti nel database.
         *
         * @return Lista di sconti
         * @throws DAOException se si verifica un errore durante l'esecuzione della
         *                      query
         */
        public List<Discount> getAll() throws DAOException {
            String query = "SELECT * FROM Sconti";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createDiscountList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e); // Puoi migliorare questo messaggio per renderlo più
                                                          // descrittivo
            }
        }

        /**
         * Recupera uno o più sconti filtrati per ID.
         *
         * @param id L'ID dello sconto da cercare
         * @return Lista contenente lo sconto trovato (o vuota se non esiste)
         * @throws DAOException se si verifica un errore durante l'esecuzione della
         *                      query
         */
        public List<Discount> filterbyID(int id) throws DAOException {
            String query = "SELECT * FROM Sconti WHERE idSconto = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                return createDiscountList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

        /**
         * Inserisce un nuovo sconto nel database.
         *
         * @param discount Oggetto Discount da inserire
         * @throws DAOException se si verifica un errore durante l'inserimento
         */
        public void create(Discount discount) throws DAOException {
            String query = "INSERT INTO Sconti (percentuale) VALUES (?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, discount.getPercentuale());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Error creating discount", e);
            }
        }
    }
}
