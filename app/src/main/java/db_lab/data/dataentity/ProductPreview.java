package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

public class ProductPreview implements DataEntity {

    private int id;
    private int prezzo;
    private String taglia;

    public ProductPreview(int id, int prezzo, String taglia) {
        this.id = id;
        this.prezzo = prezzo;
        this.taglia = taglia;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public int getPrezzo() {
        return this.prezzo;
    }

    public String getTaglia() {
        return this.taglia;
    }

    public final class ProductPreviewDAO {
        private final Connection connection;

        public ProductPreviewDAO(Connection connection) {
            this.connection = connection;
        }

        public List<ProductPreview> getAll() throws DAOException {
            String query = "SELECT * FROM Articoli";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createProductPreviewList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

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

        public List<ProductPreview> createProductPreviewList(ResultSet resultSet) throws SQLException {
            List<ProductPreview> productPreviews = new ArrayList<>(); // La lista che conterrà i product previews

            // Iteriamo su ogni riga del ResultSet
            while (resultSet.next()) {
                // Creiamo un nuovo oggetto ProductPreview per ogni riga
                int idArticolo = resultSet.getInt("idArticolo"); // Supponiamo che "idArticolo" sia una colonna
                String taglia = resultSet.getString("taglia"); // Supponiamo che "taglia" sia una colonna
                int prezzo = resultSet.getInt("prezzo"); // Supponiamo che "prezzo" sia una colonna

                // Crea l'oggetto ProductPreview
                ProductPreview productPreview = new ProductPreview(idArticolo, prezzo, taglia);

                // Aggiungiamo il productPreview alla lista
                productPreviews.add(productPreview);
            }

            // Restituiamo la lista di productPreviews
            return productPreviews;
        }
    }

}
