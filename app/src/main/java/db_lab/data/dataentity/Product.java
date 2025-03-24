package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

public class Product implements DataEntity {

    private String nomeArticolo;
    private int idProprietario; // Assumiamo che idProprietario sia un oggetto User
    private int idCategoria; // Assumiamo che idCategoria sia un oggetto Category
    private String materiale;
    private String taglia;
    private String condizioni;
    private String brand;
    private String descrizione;
    private int idSconto;
    private double prezzo;
    private int idArticolo;

    public Product() {

    }

    public Product(int idArticolo, String nomeArticolo, int idProprietario, int idCategoria, String materiale,
            String taglia, String condizioni, String brand, String descrizione,
            int idSconto, double prezzo) {
        this.nomeArticolo = nomeArticolo;
        this.idProprietario = idProprietario;
        this.idCategoria = idCategoria;
        this.materiale = materiale;
        this.taglia = taglia;
        this.condizioni = condizioni;
        this.brand = brand;
        this.descrizione = descrizione;
        this.idSconto = idSconto;
        this.prezzo = prezzo;
        this.idArticolo = idArticolo;
    }

    @Override
    public int getId() {
        return this.idArticolo;
    }

    public int getIdProprietario() {
        return this.idProprietario;
    }

    public int getIdCategoria() {
        return this.idCategoria;
    }

    public String getMateriale() {
        return this.materiale;
    }

    public String getTaglia() {
        return this.taglia;
    }

    public String getCondizioni() {
        return this.condizioni;
    }

    public String getBrand() {
        return this.brand;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public int getIdSconto() {
        return this.idSconto;
    }

    public double getPrezzo() {
        return this.prezzo;
    }

    public String getNome() {
        return this.nomeArticolo;
    }

    public final class ProductDAO {

        private Connection connection;

        public ProductDAO(Connection connection) {
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

        public List<Product> filterbyID(int idArticolo) throws DAOException {
            String query = "SELECT * FROM Articoli WHERE IdArticolo = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, idArticolo);
                ResultSet rs = statement.executeQuery();
                return createProductList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

        public void deletebyID(int idArticolo) throws DAOException {
            String query = "DELETE FROM Articoli WHERE idArticolo = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, idArticolo);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("wrong query", e);
            }
        }

        // DA GESTIRE IDSCONTO(RENDERE NULL ALL'INSERIMENTO DI UN NUOVO PRODOTTO)
        public void create(Product product) throws DAOException {
            String query = "INSERT INTO Articoli (nomeArticolo, idProprietario, idCategoria, Materiale, Taglia, Condizioni, Brand, Descrizione, idSconto, Prezzo) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?)";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, product.getNome());
                statement.setInt(2, product.getIdProprietario());
                statement.setInt(3, product.getIdCategoria());
                statement.setString(4, product.getMateriale());
                statement.setString(5, product.getTaglia());
                statement.setString(6, product.getCondizioni());
                statement.setString(7, product.getBrand());
                statement.setString(8, product.getDescrizione());
                statement.setInt(9, product.getIdSconto());
                statement.setDouble(10, product.getPrezzo());

                statement.executeUpdate();
            } catch (SQLException e) {

                throw new DAOException("Error creating product", e);
            }
        }

        public List<Product> createProductList(ResultSet resultSet) throws SQLException {
            List<Product> products = new ArrayList<>(); // La lista che conterrà i prodotti

            // Iteriamo su ogni riga del ResultSet
            while (resultSet.next()) {
                // Recuperiamo i dati da ogni colonna del ResultSet
                String nomeArticolo = resultSet.getString("nomeArticolo");
                int idProprietario = resultSet.getInt("idProprietario"); // Supponiamo che "idProprietario" sia una
                int idArticolo = resultSet.getInt("idArticolo");
                int idCategoria = resultSet.getInt("idCategoria"); // Supponiamo che "idCategoria" sia una colonna
                String materiale = resultSet.getString("Materiale"); // Supponiamo che "Materiale" sia una colonna
                String taglia = resultSet.getString("Taglia"); // Supponiamo che "Taglia" sia una colonna
                String condizioni = resultSet.getString("Condizioni"); // Supponiamo che "Condizioni" sia una colonna
                String brand = resultSet.getString("Brand"); // Supponiamo che "Brand" sia una colonna
                String descrizione = resultSet.getString("Descrizione"); // Supponiamo che "Descrizione" sia una colonna
                int idSconto = resultSet.getInt("idSconto");
                int prezzo = resultSet.getInt("Prezzo"); // Supponiamo che "Prezzo" sia una colonna

                // Crea l'oggetto Product
                Product product = new Product(idArticolo, nomeArticolo, idProprietario, idCategoria, materiale, taglia,
                        condizioni, brand, descrizione, idSconto, prezzo);

                // Aggiungiamo il product alla lista
                products.add(product);
            }

            // Restituiamo la lista di products
            return products;
        }
    }

}