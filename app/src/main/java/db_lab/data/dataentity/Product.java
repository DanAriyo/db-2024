package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

public class Product implements DataEntity {

    private int idArticolo;
    private int idProprietario; // Assumiamo che idProprietario sia un oggetto User
    private int idCategoria; // Assumiamo che idCategoria sia un oggetto Category
    private String materiale;
    private String taglia;
    private String condizioni;
    private String brand;
    private String descrizione;
    private String foto; // Se la foto è una stringa (URL o nome file)
    private double prezzo;

    public Product(int idArticolo, int idProprietario, int idCategoria, String materiale,
            String taglia, String condizioni, String brand, String descrizione,
            String foto, double prezzo) {
        this.idArticolo = idArticolo;
        this.idProprietario = idProprietario;
        this.idCategoria = idCategoria;
        this.materiale = materiale;
        this.taglia = taglia;
        this.condizioni = condizioni;
        this.brand = brand;
        this.descrizione = descrizione;
        this.foto = foto;
        this.prezzo = prezzo;
    }

    @Override
    public int getId() {
        return this.idArticolo;
    }

    // Getter per idProprietario
    public int getIdProprietario() {
        return this.idProprietario;
    }

    // Getter per idCategoria
    public int getIdCategoria() {
        return this.idCategoria;
    }

    // Getter per materiale
    public String getMateriale() {
        return this.materiale;
    }

    // Getter per taglia
    public String getTaglia() {
        return this.taglia;
    }

    // Getter per condizioni
    public String getCondizioni() {
        return this.condizioni;
    }

    // Getter per brand
    public String getBrand() {
        return this.brand;
    }

    // Getter per descrizione
    public String getDescrizione() {
        return this.descrizione;
    }

    // Getter per foto
    public String getFoto() {
        return this.foto;
    }

    // Getter per prezzo
    public double getPrezzo() {
        return this.prezzo;
    }

    public final class DAO {

        private Connection connection;

        public DAO(Connection connection) {
            this.connection = connection;
        }

        public List<Product> getAll() throws DAOException {
            String query = "SELECT * FROM Articoli";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createProductList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

        public List<Product> filterbyID(int id) throws DAOException {
            String query = "SELECT * FROM Articoli WHERE IdArticolo = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                return createProductList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

        public void deletebyID(int id) throws DAOException {
            String query = "DELETE FROM Articoli WHERE idArticolo = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("wrong query", e);
            }
        }

        public void create(Product product) throws DAOException {
            String query = "INSERT INTO Articoli (idArticolo, idProprietario, idCategoria, Materiale, Taglia, Condizioni, Brand, Descrizione, Foto, Prezzo) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?)";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                // Impostiamo i parametri per il PreparedStatement
                statement.setInt(1, product.getId()); // idArticolo
                statement.setInt(2, product.getIdProprietario()); // idProprietario (assumendo che `getId()` restituisca
                                                                  // l'ID dell'utente)
                statement.setInt(3, product.getIdCategoria()); // idCategoria (assumendo che `getId()` restituisca l'ID
                                                               // della categoria)
                statement.setString(4, product.getMateriale()); // Materiale
                statement.setString(5, product.getTaglia()); // Taglia
                statement.setString(6, product.getCondizioni()); // Condizioni
                statement.setString(7, product.getBrand()); // Brand
                statement.setString(8, product.getDescrizione()); // Descrizione
                statement.setString(9, product.getFoto()); // Foto (potrebbe essere il nome del file o l'URL)
                statement.setDouble(10, product.getPrezzo()); // Prezzo

                // Eseguiamo l'operazione di aggiornamento
                statement.executeUpdate();
            } catch (SQLException e) {
                // Se si verifica un errore, lanciamo una DAOException
                throw new DAOException("Error creating product", e);
            }
        }

        public List<Product> createProductList(ResultSet resultSet) throws SQLException {
            List<Product> products = new ArrayList<>(); // La lista che conterrà i prodotti

            // Iteriamo su ogni riga del ResultSet
            while (resultSet.next()) {
                // Recuperiamo i dati da ogni colonna del ResultSet
                int idArticolo = resultSet.getInt("idArticolo"); // Supponiamo che "idArticolo" sia una colonna
                int idProprietario = resultSet.getInt("idProprietario"); // Supponiamo che "idProprietario" sia una
                                                                         // colonna
                int idCategoria = resultSet.getInt("idCategoria"); // Supponiamo che "idCategoria" sia una colonna
                String materiale = resultSet.getString("Materiale"); // Supponiamo che "Materiale" sia una colonna
                String taglia = resultSet.getString("Taglia"); // Supponiamo che "Taglia" sia una colonna
                String condizioni = resultSet.getString("Condizioni"); // Supponiamo che "Condizioni" sia una colonna
                String brand = resultSet.getString("Brand"); // Supponiamo che "Brand" sia una colonna
                String descrizione = resultSet.getString("Descrizione"); // Supponiamo che "Descrizione" sia una colonna
                String foto = resultSet.getString("Foto"); // Supponiamo che "Foto" sia una colonna
                int prezzo = resultSet.getInt("Prezzo"); // Supponiamo che "Prezzo" sia una colonna

                // Crea l'oggetto Product
                Product product = new Product(idArticolo, idProprietario, idCategoria, materiale, taglia, condizioni,
                        brand, descrizione, foto, prezzo);

                // Aggiungiamo il product alla lista
                products.add(product);
            }

            // Restituiamo la lista di products
            return products;
        }
    }

}