package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;
import db_lab.data.dataentity.Balance.BalanceDAO;

/**
 * Rappresenta un premio in credito assegnato a un utente.
 */
public class CreditReward implements DataEntity {

    private int idPremio; // Identificativo univoco del premio
    private String descrizione; // Descrizione del premio (es. "Bonus fedeltà")
    private int importoBonus; // Ammontare del credito bonus
    private int idUtente; // ID dell’utente a cui è associato il premio

    // Costruttore vuoto (necessario per librerie/framework)
    public CreditReward() {
    }

    // Costruttore completo
    public CreditReward(int idPremio, String descrizione, int importoBonus, int idUtente) {
        this.idPremio = idPremio;
        this.descrizione = descrizione;
        this.importoBonus = importoBonus;
        this.idUtente = idUtente;
    }

    // Ritorna l'ID del premio
    @Override
    public int getId() {
        return this.idPremio;
    }

    // Ritorna la descrizione del premio
    public String getDescrizione() {
        return descrizione;
    }

    // Ritorna l'importo in credito bonus
    public int getImportoBonus() {
        return importoBonus;
    }

    // Ritorna l'ID dell'utente associato al premio
    public int getIdUtente() {
        return idUtente;
    }

    /**
     * Classe interna per la gestione dei premi tramite accesso al database.
     */
    public final class CreditRewardDAO {

        private Connection connection;

        /**
         * Costruttore che riceve una connessione al database.
         */
        public CreditRewardDAO(Connection connection) {
            this.connection = connection;
        }

        /**
         * Crea una lista di oggetti CreditReward a partire da un ResultSet.
         */
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

        /**
         * Restituisce tutti i premi in credito presenti nella tabella CreditoBonus.
         */
        public List<CreditReward> getAll() throws DAOException {
            String query = "SELECT * FROM CreditoBonus";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createCreditRewardList(rs);
            } catch (Exception e) {
                throw new DAOException("Errore nella query", e);
            }
        }

        /**
         * Filtra i premi per un dato ID utente.
         *
         * @param idUtente ID dell’utente per cui si vogliono recuperare i premi
         * @return Lista di premi associati all'utente
         */
        public List<CreditReward> filterByUserID(int idUtente) throws DAOException {
            String query = "SELECT * FROM CreditoBonus WHERE idUtente = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, idUtente);
                ResultSet rs = statement.executeQuery();
                return createCreditRewardList(rs);
            } catch (Exception e) {
                throw new DAOException("Errore nella query", e);
            }
        }

        /**
         * Crea un nuovo premio nel database, aggiornando anche il saldo utente.
         *
         * @param reward           Oggetto CreditReward da inserire nel DB
         * @param balanceDAOHelper DAO per aggiornare il saldo dell’utente
         * @param user             Utente a cui assegnare il premio
         */
        public void create(CreditReward reward, BalanceDAO balanceDAOHelper, User user)
                throws DAOException {
            String query = "INSERT INTO CreditoBonus (descrizione, importoBonus, idUtente) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, reward.getDescrizione());
                statement.setInt(2, reward.getImportoBonus());
                statement.setInt(3, reward.getIdUtente());

                // Inserisce il premio
                statement.executeUpdate();

                // Aggiorna il saldo associato all'utente aggiungendo il bonus
                balanceDAOHelper.updateById(user.getIdSaldo(), reward.importoBonus, true);
            } catch (SQLException e) {
                throw new DAOException("Errore nella creazione del premio", e);
            }
        }
    }
}
