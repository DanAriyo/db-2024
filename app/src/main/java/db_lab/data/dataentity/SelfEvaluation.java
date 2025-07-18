package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;
import db_lab.data.dataentity.Review.ReviewDAO;

// Classe che rappresenta un'autovalutazione (SelfEvaluation)
public class SelfEvaluation implements DataEntity {

    // Attributi dell'autovalutazione
    private int idPremio; // ID univoco dell'autovalutazione
    private String descrizione; // Descrizione testuale dell'autovalutazione
    private int stelle; // Valutazione in stelle (es. 1-5)
    private int idUtente; // ID dell'utente che ha fatto l'autovalutazione

    // Costruttore vuoto
    public SelfEvaluation() {
    }

    // Costruttore completo con parametri
    public SelfEvaluation(int idPremio, String descrizione, int stelle, int idUtente) {
        this.idPremio = idPremio;
        this.descrizione = descrizione;
        this.stelle = stelle;
        this.idUtente = idUtente;
    }

    // Restituisce l'ID (obbligatorio da DataEntity)
    @Override
    public int getId() {
        return this.idPremio;
    }

    // Getter per la descrizione
    public String getDescrizione() {
        return descrizione;
    }

    // Getter per le stelle
    public int getStelle() {
        return stelle;
    }

    // Getter per l'ID utente
    public int getIdUtente() {
        return idUtente;
    }

    // Classe DAO interna per gestire l'accesso ai dati delle autovalutazioni nel DB
    public final class SelfEvaluationDAO {

        private Connection connection;

        // Costruttore che riceve la connessione al DB
        public SelfEvaluationDAO(Connection connection) {
            this.connection = connection;
        }

        // Metodo di utilità per trasformare un ResultSet in una lista di SelfEvaluation
        public List<SelfEvaluation> createSelfEvaluationList(ResultSet resultSet) throws SQLException {
            List<SelfEvaluation> evaluations = new ArrayList<>();
            while (resultSet.next()) {
                int idPremio = resultSet.getInt("idPremio");
                String descrizione = resultSet.getString("descrizione");
                int stelle = resultSet.getInt("stelle");
                int idUtente = resultSet.getInt("idUtente");
                evaluations.add(new SelfEvaluation(idPremio, descrizione, stelle, idUtente));
            }
            return evaluations;
        }

        // Recupera tutte le autovalutazioni dal database
        public List<SelfEvaluation> getAll() throws DAOException {
            String query = "SELECT * FROM autovalutazione";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createSelfEvaluationList(rs);
            } catch (Exception e) {
                throw new DAOException("Error retrieving evaluations", e);
            }
        }

        // Recupera le autovalutazioni filtrate per ID utente
        public List<SelfEvaluation> filterByUserID(int idUtente) throws DAOException {
            String query = "SELECT * FROM autovalutazione WHERE idUtente = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, idUtente);
                ResultSet rs = statement.executeQuery();
                return createSelfEvaluationList(rs);
            } catch (Exception e) {
                throw new DAOException("Error filtering evaluations", e);
            }
        }

        // Crea una nuova autovalutazione e, contestualmente, crea una recensione
        // automatica legata
        // reviewDAO: DAO per la gestione delle recensioni
        // admin: utente amministratore che "firma" la recensione automatica
        public void create(SelfEvaluation evaluation, ReviewDAO reviewDAO, User admin) throws DAOException {
            String query = "INSERT INTO autovalutazione (descrizione, stelle, idUtente) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, evaluation.getDescrizione());
                statement.setInt(2, evaluation.getStelle());
                statement.setInt(3, evaluation.getIdUtente());
                statement.executeUpdate();

                // Dopo aver creato l'autovalutazione, si crea una recensione automatica
                // collegata
                reviewDAO.create(createFictionalReview(admin, evaluation.idUtente));
            } catch (SQLException e) {
                throw new DAOException("Error creating evaluation", e);
            }
        }

        // Crea una recensione "fittizia" automatica da parte dell'admin per l'utente
        public Review createFictionalReview(User admin, int idUtente) {
            Review review = new Review(0, admin.getId(), idUtente,
                    5, "Recensione Automatica");
            return review;
        }
    }
}
