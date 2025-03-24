package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

public class CreditReward implements DataEntity {

    private int idPremio;
    private String descrizione;
    private int importoBonus;
    private int idUtente;

    public CreditReward() {
    }

    public CreditReward(int idPremio, String descrizione, int importoBonus, int idUtente) {
        this.idPremio = idPremio;
        this.descrizione = descrizione;
        this.importoBonus = importoBonus;
        this.idUtente = idUtente;
    }

    @Override
    public int getId() {
        return this.idPremio;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public int getImportoBonus() {
        return importoBonus;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public final class CreditRewardDAO {

        private Connection connection;

        public CreditRewardDAO(Connection connection) {
            this.connection = connection;
        }

        public List<CreditReward> createCreditRewardList(ResultSet resultSet) throws SQLException {
            List<CreditReward> rewards = new ArrayList<>();
            while (resultSet.next()) {
                int idPremio = resultSet.getInt("idPremio");
                String descrizione = resultSet.getString("descrizione");
                int importoBonus = resultSet.getInt("importoBonus");
                int idUtente = resultSet.getInt("idUtente");

                CreditReward reward = new CreditReward(idPremio, descrizione, importoBonus, idUtente);
                rewards.add(reward);
            }
            return rewards;
        }

        public List<CreditReward> getAll() throws DAOException {
            String query = "SELECT * FROM CreditoBonus";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createCreditRewardList(rs);
            } catch (Exception e) {
                throw new DAOException("Errore nella query", e);
            }
        }

        public List<CreditReward> filterByUserID(int idPremio) throws DAOException {
            String query = "SELECT * FROM CreditoBonus WHERE idPremio = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, idUtente);
                ResultSet rs = statement.executeQuery();
                return createCreditRewardList(rs);
            } catch (Exception e) {
                throw new DAOException("Errore nella query", e);
            }
        }

        public void create(CreditReward reward) throws DAOException {
            String query = "INSERT INTO CreditoBonus (descrizione, importoBonus, idUtente) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, reward.getDescrizione());
                statement.setInt(2, reward.getImportoBonus());
                statement.setInt(3, reward.getIdUtente());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Errore nella creazione del premio", e);
            }
        }
    }
}
