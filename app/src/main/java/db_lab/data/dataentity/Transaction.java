package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

public class Transaction implements DataEntity{

    private int idVenditore;
    private int idAcquirente;
    private int idTransazione;
    private int speseSpedizione;
    private int commissioni;
    private int idSaldo;
    private int idRecensione;
    private int idRecensito;
    private int idRecensiore;
    private int prezzo;
    private Date data;
    private Time ora;

    public Transaction(int idVenditore, int idAcquirente, int idTransazione, int speseSpedizione, 
                       int commissioni, int idSaldo, int idRecensione, int idRecensito, 
                       int idRecensiore, int prezzo, Date data, Time ora) {
        this.idVenditore = idVenditore;
        this.idAcquirente = idAcquirente;
        this.idTransazione = idTransazione;
        this.speseSpedizione = speseSpedizione;
        this.commissioni = commissioni;
        this.idSaldo = idSaldo;
        this.idRecensione = idRecensione;
        this.idRecensito = idRecensito;
        this.idRecensiore = idRecensiore;
        this.prezzo = prezzo;
        this.data = data;
        this.ora = ora;
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

    public int getIdSaldo() {
        return this.idSaldo;
    }

    public int getIdRecensione() {
        return this.idRecensione;
    }

    public int getIdRecensito() {
        return this.idRecensito;
    }

    public int getIdRecensiore() {
        return this.idRecensiore;
    }

    public int getPrezzo() {
        return this.prezzo;
    }

    public Date getData() {
        return this.data;
    }

    public Time getOra() {
        return this.ora;
    }

    @Override
    public int getId() {
        return this.idTransazione;
    }

    public final class DAO {
    
        private Connection connection;

        public DAO(Connection connection){
            this.connection = connection;
        }

        public List<Transaction> createTransactionList(ResultSet resultSet) throws SQLException {
            List<Transaction> transactions = new ArrayList<>(); // La lista che conterrà i prodotti

            // Iteriamo su ogni riga del ResultSet
            while (resultSet.next()) {
                // Recuperiamo i dati da ogni colonna del ResultSet
                int idTransazione = resultSet.getInt("idTransazione"); 
                int idVenditore = resultSet.getInt("idVenditore"); 
                int idAcquirente = resultSet.getInt("idAcquirente"); // Supponiamo che "idCategoria" sia una colonna
                int speseSpedizione = resultSet.getInt("speseSpedizione");
                int idRecensione = resultSet.getInt("IdRecensione"); 
                int idRecensitore = resultSet.getInt("IdRecensitore"); 
                int idRecensito = resultSet.getInt("IdRecensito");
                int idSaldo = resultSet.getInt("idSaldo"); 
                int commissioni = resultSet.getInt("Commissioni");
                Date data = resultSet.getDate("Data");
                Time ora = resultSet.getTime("Ora"); 
                int prezzo = resultSet.getInt("Prezzo");

                Transaction transaction = new Transaction(idVenditore, idAcquirente, idTransazione,speseSpedizione, 
                    commissioni,idSaldo,idRecensione,idRecensito, 
                        idRecensitore,prezzo,data,ora);

                // Aggiungiamo il product alla lista
                transactions.add(transaction);
            }

            // Restituiamo la lista di products
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
    }


}