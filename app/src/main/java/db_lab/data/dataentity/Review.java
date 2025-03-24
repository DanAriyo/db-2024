package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

public class Review implements DataEntity {

    private int idRecensione;
    private int idRecensito;
    private int idRecensitore;
    private int stelle;
    private String descrizione;

    public Review() {

    }

    public Review(int idRecensione, int idRecensitore, int idRecensito, int stelle, String descrizione) {
        this.idRecensito = idRecensito;
        this.idRecensitore = idRecensitore;
        this.stelle = stelle;
        this.descrizione = descrizione;
        this.idRecensione = idRecensione;
    }

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

    @Override
    public int getId() {
        return this.idRecensione;
    }

    public final class ReviewDAO {

        private Connection connection;

        public ReviewDAO(Connection connection) {
            this.connection = connection;
        }

        public List<Review> getAll() throws DAOException {
            String query = "SELECT * FROM Recensioni";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createReviewList(rs);
            } catch (SQLException e) {
                throw new DAOException("Error fetching reviews", e);
            }
        }

        public List<Review> filterByReviewer(int idRecensione) throws DAOException {
            String query = "SELECT * FROM Recensioni WHERE IdRecensione = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, idRecensitore);
                ResultSet rs = statement.executeQuery();
                return createReviewList(rs);
            } catch (SQLException e) {
                throw new DAOException("Error fetching reviews by reviewer", e);
            }
        }

        public void deleteByID(int idRecensione) throws DAOException {
            String query = "DELETE FROM Recensioni WHERE IdURecensione = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, idRecensione);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Error deleting review", e);
            }
        }

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

        private List<Review> createReviewList(ResultSet resultSet) throws SQLException {
            List<Review> reviews = new ArrayList<>();
            while (resultSet.next()) {
                int idRecensione = resultSet.getInt("idRecensione");
                int idRecensito = resultSet.getInt("IdURecensito");
                int idRecensitore = resultSet.getInt("IdURecensitore");
                int stelle = resultSet.getInt("Stelle");
                String descrizione = resultSet.getString("Descrizione");

                Review review = new Review(idRecensione, idRecensito, idRecensitore, stelle, descrizione);
                reviews.add(review);
            }
            return reviews;
        }

    }

}
