package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

public class SelfEvaluation implements DataEntity {

    private int idPremio;
    private String descrizione;
    private int stelle;
    private int idUtente;

    public SelfEvaluation() {
    }

    public SelfEvaluation(int idPremio, String descrizione, int stelle, int idUtente) {
        this.idPremio = idPremio;
        this.descrizione = descrizione;
        this.stelle = stelle;
        this.idUtente = idUtente;
    }

    @Override
    public int getId() {
        return this.idPremio;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public int getStelle() {
        return stelle;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public final class SelfEvaluationDAO {

        private Connection connection;

        public SelfEvaluationDAO(Connection connection) {
            this.connection = connection;
        }

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

        public List<SelfEvaluation> getAll() throws DAOException {
            String query = "SELECT * FROM autovalutazione";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createSelfEvaluationList(rs);
            } catch (Exception e) {
                throw new DAOException("Error retrieving evaluations", e);
            }
        }

        public List<SelfEvaluation> filterByUserID(int id) throws DAOException {
            String query = "SELECT * FROM autovalutazione WHERE idUtente = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, idUtente);
                ResultSet rs = statement.executeQuery();
                return createSelfEvaluationList(rs);
            } catch (Exception e) {
                throw new DAOException("Error filtering evaluations", e);
            }
        }

        public void create(SelfEvaluation evaluation) throws DAOException {
            String query = "INSERT INTO autovalutazione (descrizione, stelle, idUtente) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, evaluation.getDescrizione());
                statement.setInt(2, evaluation.getStelle());
                statement.setInt(3, evaluation.getIdUtente());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Error creating evaluation", e);
            }
        }
    }
}
