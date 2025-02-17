package db_lab.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;
import db_lab.data.dataentity.ProductPreview;

public class DAOProductPreview implements DAO<ProductPreview>{

    private final Connection connection;

    public DAOProductPreview(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<ProductPreview> getAll() throws DAOException {
        String query = "SELECT * FROM Articoli";
        try (PreparedStatement statement = this.connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            return createProductPreviewList(rs);
        } catch (Exception e) {
            throw new DAOException("wrong query", e);
        }
    }

    @Override
    public List<ProductPreview> filterbyID(int id) throws DAOException {
        String query = "SELECT * FROM Articoli WHERE IdArticolo = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            return createProductPreviewList(rs);
        } catch (Exception e) {
            throw new DAOException("wrong query", e);
        }
    }

    @Override
    public void deletebyID(int id) throws DAOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletebyID'");
    }

    @Override
    public void updatebyID(int id) throws DAOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatebyID'");
    }

    @Override
    public void create(ProductPreview entity) throws DAOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

   public List<ProductPreview> createProductPreviewList(ResultSet resultSet) throws SQLException {
    List<ProductPreview> productPreviews = new ArrayList<>();  // La lista che conterrà i product previews

    // Iteriamo su ogni riga del ResultSet
    while (resultSet.next()) {
        // Creiamo un nuovo oggetto ProductPreview per ogni riga
        int idArticolo = resultSet.getInt("idArticolo");  // Supponiamo che "idArticolo" sia una colonna
        String taglia = resultSet.getString("taglia");  // Supponiamo che "taglia" sia una colonna
        int prezzo = resultSet.getInt("prezzo");  // Supponiamo che "prezzo" sia una colonna

        // Crea l'oggetto ProductPreview
        ProductPreview productPreview = new ProductPreview(idArticolo, prezzo, taglia);

        // Aggiungiamo il productPreview alla lista
        productPreviews.add(productPreview);
    }

    // Restituiamo la lista di productPreviews
    return productPreviews;
}
    
}