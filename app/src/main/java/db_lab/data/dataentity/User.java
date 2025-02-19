package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

public class User implements DataEntity {

    private int id;
    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    private String indirizzo;
    private String cf;
    private String iban;
    private int idSaldo;
    private Date data;

    public User(int id, String nome, String cognome, String email,
            String iban, int idSaldo, String telefono, String indirizzo, Date data, String cf) {

        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.iban = iban;
        this.idSaldo = idSaldo;
        this.telefono = telefono;
        this.indirizzo = indirizzo;
        this.data = data;
        this.cf = cf;
    }

    @Override
    public int getId() {
        return this.id;
    }

    // Getter per nome
    public String getNome() {
        return this.nome;
    }

    // Getter per cognome
    public String getCognome() {
        return this.cognome;
    }

    // Getter per email
    public String getEmail() {
        return this.email;
    }

    // Getter per iban
    public String getIban() {
        return this.iban;
    }

    // Getter per idSaldo
    public int getIdSaldo() {
        return this.idSaldo;
    }

    // Getter per telefono
    public String getTelefono() {
        return this.telefono;
    }

    // Getter per indirizzo
    public String getIndirizzo() {
        return this.indirizzo;
    }

    // Getter per data
    public Date getData() {
        return this.data;
    }

    // Getter per cf
    public String getCf() {
        return this.cf;
    }

    public final class UserDAO {

        private Connection connection;

        public UserDAO(Connection connection) {

            this.connection = connection;
        }

        public List<User> getAll() throws DAOException {
            String query = "SELECT * FROM Utenti";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createUserList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

        public List<User> filterbyID(int id) throws DAOException {
            String query = "SELECT * FROM Utenti WHERE IdUtente = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                return createUserList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

        public void deletebyID(int id) throws DAOException {
            String query = "DELETE FROM Utenti WHERE idUtente = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("wrong query", e);
            }
        }

        public void create(User user) throws DAOException {
            
            String query = "INSERT INTO Utenti (id, nome,cognome, email, iban, idSaldo, telefono, indirizzo, data, cf) VALUES (?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, user.getId());
                statement.setString(2, user.getNome());
                statement.setString(3, user.getCognome());
                statement.setString(4, user.getEmail());
                statement.setString(5, user.getIban());
                statement.setInt(6, user.getIdSaldo());
                statement.setString(7, user.getTelefono());
                statement.setString(8, user.getIndirizzo());
                statement.setDate(9, user.getData());
                statement.setString(10, user.getCf());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Error creating user", e);
            }
        }

        public List<User> createUserList(ResultSet resultSet) throws SQLException {
            List<User> utenti = new ArrayList<>(); // La lista che conterrà gli utenti

            // Iteriamo su ogni riga del ResultSet
            while (resultSet.next()) {
                // Creiamo un nuovo oggetto Utente per ogni riga
                int id = resultSet.getInt("id"); // Supponiamo che "id" sia una colonna
                String nome = resultSet.getString("nome"); // Supponiamo che "nome" sia una colonna
                String cognome = resultSet.getString("cognome"); // Supponiamo che "cognome" sia una colonna
                String email = resultSet.getString("email"); // Supponiamo che "email" sia una colonna
                String indirizzo = resultSet.getString("indirizzo");
                int idSaldo = resultSet.getInt("idSaldo");
                String iban = resultSet.getString("iban");
                String telefono = resultSet.getString("telefono");
                Date data = resultSet.getDate("DataNascita");
                String cf = resultSet.getString("CF");

                // Crea l'oggetto Utente
                User utente = new User(id, nome, cognome, email, iban, idSaldo, telefono, indirizzo, data, cf);

                // Aggiungiamo l'utente alla lista
                utenti.add(utente);
            }

            // Restituiamo la lista di utenti
            return utenti;
        }

    }

}