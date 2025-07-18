package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

// Entità che rappresenta un prodotto/annuncio nel sistema
public class Product implements DataEntity {

    // Attributi del prodotto
    private String nomeArticolo;
    private int idProprietario;
    private int idCategoria;
    private String materiale;
    private String taglia;
    private String condizioni;
    private String brand;
    private String descrizione;
    private int idSconto;
    private double prezzo;
    private int idArticolo;

    // Costruttore vuoto
    public Product() {
    }

    // Costruttore completo
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

    // Getter per l'ID del prodotto (implementa DataEntity)
    @Override
    public int getId() {
        return this.idArticolo;
    }

    // Getter degli altri attributi
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

    // Classe interna per la gestione DAO del prodotto
    public final class ProductDAO {

        private Connection connection;

        // Costruttore che controlla se la connessione è valida
        public ProductDAO(Connection connection) {
            if (connection == null) {
                throw new DAOException("Connection is null.");
            }

            try {
                if (connection.isClosed()) {
                    throw new DAOException("Connection is closed.");
                }
            } catch (SQLException e) {
                throw new DAOException("Error checking connection status.", e);
            }

            this.connection = connection;
        }

        // Restituisce tutti i prodotti disponibili
        public List<Product> getAll() throws DAOException {
            String query = "SELECT * FROM Articoli WHERE stato = 'disponibile'";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createProductList(rs);
            } catch (SQLException e) {
                throw new DAOException("Error fetching all products", e);
            }
        }

        // Restituisce un prodotto filtrato per ID
        public List<Product> filterbyID(int idArticolo) throws DAOException {
            String query = "SELECT * FROM Articoli WHERE IdArticolo = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, idArticolo);
                ResultSet rs = statement.executeQuery();
                return createProductList(rs);
            } catch (SQLException e) {
                throw new DAOException("Error fetching product by ID", e);
            }
        }

        // Aggiorna lo stato del prodotto impostandolo a 'non_disponibile'
        public void updatebyID(int idArticolo) throws DAOException {
            String query = "UPDATE articoli SET stato = 'non_disponibile' WHERE idArticolo = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, idArticolo);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Error updating product by ID", e);
            }
        }

        // Crea un nuovo prodotto nel database
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

                // Se idSconto è 0 lo consideriamo nullo
                if (product.getIdSconto() == 0) {
                    statement.setNull(9, java.sql.Types.INTEGER);
                } else {
                    statement.setInt(9, product.getIdSconto());
                }

                statement.setDouble(10, product.getPrezzo());

                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Error creating product", e);
            }
        }

        // Converte il ResultSet in una lista di oggetti Product
        public List<Product> createProductList(ResultSet resultSet) throws SQLException {
            List<Product> products = new ArrayList<>();

            while (resultSet.next()) {
                String nomeArticolo = resultSet.getString("nomeArticolo");
                int idProprietario = resultSet.getInt("idProprietario");
                int idArticolo = resultSet.getInt("idArticolo");
                int idCategoria = resultSet.getInt("idCategoria");
                String materiale = resultSet.getString("Materiale");
                String taglia = resultSet.getString("Taglia");
                String condizioni = resultSet.getString("Condizioni");
                String brand = resultSet.getString("Brand");
                String descrizione = resultSet.getString("Descrizione");
                int idSconto = resultSet.getInt("idSconto");
                int prezzo = resultSet.getInt("Prezzo"); // NB: se in DB è DOUBLE, usare getDouble()

                Product product = new Product(idArticolo, nomeArticolo, idProprietario, idCategoria, materiale, taglia,
                        condizioni, brand, descrizione, idSconto, prezzo);

                products.add(product);
            }

            return products;
        }

        // Alterna lo stato di un prodotto tra disponibile e non disponibile
        public void toggleProductStatus(Product product) throws DAOException {
            String query = "UPDATE Articoli SET stato = CASE WHEN stato = 'disponibile' THEN 'non_disponibile' ELSE 'disponibile' END WHERE idArticolo = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, product.getId());

                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new DAOException("No product found with id: " + product.getId());
                }
            } catch (SQLException e) {
                throw new DAOException("Error toggling product status", e);
            }
        }
    }
}
