

package db_lab.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;
import db_lab.data.dataentity.Product;

public class DAOProduct implements DAO<Product> {

    private final Connection connection;

    public DAOProduct(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<Product> getAll() throws DAOException {
        String query = "SELECT * FROM Articoli";
        try (PreparedStatement statement = this.connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            return createProductList(rs);
        } catch (Exception e) {
            throw new DAOException("wrong query", e);
        }
    }

    @Override
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

    @Override
    public void deletebyID(int id) throws DAOException {
        String query = "DELETE FROM Articoli WHERE idArticolo = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("wrong query", e);
        }
    }

    @Override
    public void updatebyID(int id) throws DAOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatebyID'");
    }

    @Override
    public void create(Product product) throws DAOException {
        String query = "INSERT INTO Articoli (idArticolo, idProprietario, idCategoria, Materiale, Taglia, Condizioni, Brand, Descrizione, Foto, Prezzo) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?)";
    
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        // Impostiamo i parametri per il PreparedStatement
        statement.setInt(1, product.getId());  // idArticolo
        statement.setInt(2, product.getIdProprietario());  // idProprietario (assumendo che `getId()` restituisca l'ID dell'utente)
        statement.setInt(3, product.getIdCategoria());  // idCategoria (assumendo che `getId()` restituisca l'ID della categoria)
        statement.setString(4, product.getMateriale());  // Materiale
        statement.setString(5, product.getTaglia());  // Taglia
        statement.setString(6, product.getCondizioni());  // Condizioni
        statement.setString(7, product.getBrand());  // Brand
        statement.setString(8, product.getDescrizione());  // Descrizione
        statement.setString(9, product.getFoto());  // Foto (potrebbe essere il nome del file o l'URL)
        statement.setDouble(10, product.getPrezzo());  // Prezzo

        // Eseguiamo l'operazione di aggiornamento
        statement.executeUpdate();
    } catch (SQLException e) {
        // Se si verifica un errore, lanciamo una DAOException
        throw new DAOException("Error creating product", e);
    }
    }

    public List<Product> createProductList(ResultSet resultSet) throws SQLException {
    List<Product> products = new ArrayList<>();  // La lista che conterrà i prodotti

    // Iteriamo su ogni riga del ResultSet
    while (resultSet.next()) {
        // Recuperiamo i dati da ogni colonna del ResultSet
        int idArticolo = resultSet.getInt("idArticolo");  // Supponiamo che "idArticolo" sia una colonna
        int idProprietario = resultSet.getInt("idProprietario");  // Supponiamo che "idProprietario" sia una colonna
        int idCategoria = resultSet.getInt("idCategoria");  // Supponiamo che "idCategoria" sia una colonna
        String materiale = resultSet.getString("Materiale");  // Supponiamo che "Materiale" sia una colonna
        String taglia = resultSet.getString("Taglia");  // Supponiamo che "Taglia" sia una colonna
        String condizioni = resultSet.getString("Condizioni");  // Supponiamo che "Condizioni" sia una colonna
        String brand = resultSet.getString("Brand");  // Supponiamo che "Brand" sia una colonna
        String descrizione = resultSet.getString("Descrizione");  // Supponiamo che "Descrizione" sia una colonna
        String foto = resultSet.getString("Foto");  // Supponiamo che "Foto" sia una colonna
        int prezzo = resultSet.getInt("Prezzo");  // Supponiamo che "Prezzo" sia una colonna

        // Crea l'oggetto Product
        Product product = new Product(idArticolo, idProprietario, idCategoria, materiale, taglia, condizioni, brand, descrizione, foto, prezzo);

        // Aggiungiamo il product alla lista
        products.add(product);
    }

    // Restituiamo la lista di products
    return products;
}
}