package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

// Classe che rappresenta una recensione nel sistema
public class Review implements DataEntity {

    // Attributi della recensione
    private int idRecensione;
    private int idRecensito; // Chi è recensito (es. prodotto o utente)
    private int idRecensitore; // Chi ha scritto la recensione
    private int stelle; // Valutazione in stelle
    private String descrizione; // Testo della recensione

    // Costruttore vuoto
    public Review() {
    }

    // Costruttore con parametri per creare un oggetto completo
    public Review(int idRecensione, int idRecensitore, int idRecensito, int stelle, String descrizione) {
        this.idRecensito = idRecensito;
        this.idRecensitore = idRecensitore;
        this.stelle = stelle;
        this.descrizione = descrizione;
        this.idRecensione = idRecensione;
    }

    // Getters per gli attributi
    public int getIdRecensito() {
        return this.idRecensito;
    }

    public int getIdRecensitore() {
        return this.idRecensitore;
    }

    public int getStelle() {
        return this.stelle;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    // Implementazione del metodo getId() richiesto da DataEntity
    @Override
    public int getId() {
        return this.idRecensione;
    }

    // Classe DAO interna per gestire l'accesso ai dati delle recensioni nel DB
    public final class ReviewDAO {

        private Connection connection;

        // Costruttore con connessione al DB
        public ReviewDAO(Connection connection) {
            this.connection = connection;
        }

        // Recupera tutte le recensioni dal DB
        public List<Review> getAll() throws DAOException {
            String query = "SELECT * FROM Recensioni";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createReviewList(rs);
            } catch (SQLException e) {
                throw new DAOException("Error fetching reviews", e);
            }
        }

        // Recupera recensioni filtrando per idRecensione (ID della recensione)
        public List<Review> filterByReview(int idRecensione) throws DAOException {
            String query = "SELECT * FROM Recensioni WHERE IdRecensione = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                // ATTENZIONE: qui c'era un errore, correggo con il parametro giusto
                statement.setInt(1, idRecensione);
                ResultSet rs = statement.executeQuery();
                return createReviewList(rs);
            } catch (SQLException e) {
                throw new DAOException("Error fetching reviews by review ID", e);
            }
        }

        // Recupera recensioni filtrando per idRecensito (chi è recensito)
        public List<Review> filterByReviewer(int idRecensito) throws DAOException {
            String query = "SELECT * FROM Recensioni WHERE IdRecensito = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                // ATTENZIONE: corretto parametro
                statement.setInt(1, idRecensito);
                ResultSet rs = statement.executeQuery();
                return createReviewList(rs);
            } catch (SQLException e) {
                throw new DAOException("Error fetching reviews by reviewed ID", e);
            }
        }

        // Elimina una recensione dato l'id
        public void deleteByID(int idRecensione) throws DAOException {
            String query = "DELETE FROM Recensioni WHERE IdRecensione = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, idRecensione);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Error deleting review", e);
            }
        }

        // Inserisce una nuova recensione nel DB
        public void create(Review review) throws DAOException {
            String query = "INSERT INTO Recensioni (IdRecensito, IdRecensitore, Stelle, Descrizione) VALUES (?,?,?,?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, review.getIdRecensito());
                statement.setInt(2, review.getIdRecensitore());
                statement.setInt(3, review.getStelle());
                statement.setString(4, review.getDescrizione());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Error creating review", e);
            }
        }

        // Metodo helper per trasformare il ResultSet in lista di oggetti Review
        private List<Review> createReviewList(ResultSet resultSet) throws SQLException {
            List<Review> reviews = new ArrayList<>();
            while (resultSet.next()) {
                int idRecensione = resultSet.getInt("idRecensione");
                int idRecensito = resultSet.getInt("IdRecensito");
                int idRecensitore = resultSet.getInt("IdRecensitore");
                int stelle = resultSet.getInt("Stelle");
                String descrizione = resultSet.getString("Descrizione");

                // Creo l'oggetto Review con i dati presi dal DB
                Review review = new Review(idRecensione, idRecensitore, idRecensito, stelle, descrizione);
                reviews.add(review);
            }
            return reviews;
        }
    }
}
