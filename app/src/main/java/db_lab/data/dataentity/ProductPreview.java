package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

// Classe che rappresenta una "preview" di un prodotto (solo alcune informazioni)
public class ProductPreview implements DataEntity {

    // Attributi ridotti rispetto al prodotto completo
    private String nomeArticolo;
    private int prezzo;
    private String taglia;

    // Costruttore vuoto
    public ProductPreview() {
    }

    // Costruttore con parametri
    public ProductPreview(String nomeArticolo, int prezzo, String taglia) {
        this.nomeArticolo = nomeArticolo;
        this.prezzo = prezzo;
        this.taglia = taglia;
    }

    // Metodo obbligatorio da DataEntity, ma non significativo in questo contesto
    @Override
    public int getId() {
        return 1; // Valore fittizio, non usato
    }

    // Getter per gli attributi
    public int getPrezzo() {
        return this.prezzo;
    }

    public String getTaglia() {
        return this.taglia;
    }

    public String getNome() {
        return this.nomeArticolo;
    }

    // Classe interna DAO per gestire il recupero di ProductPreview dal DB
    public final class ProductPreviewDAO {
        private final Connection connection;

        // Costruttore: richiede una connessione JDBC valida
        public ProductPreviewDAO(Connection connection) {
            this.connection = connection;
        }

        // Recupera tutti i prodotti (con tutti i campi, ma usa solo quelli necessari)
        public List<ProductPreview> getAll() throws DAOException {
            String query = "SELECT * FROM Articoli";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createProductPreviewList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

        // Recupera un singolo prodotto in base all'id
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

        // Converte un ResultSet in una lista di oggetti ProductPreview
        public List<ProductPreview> createProductPreviewList(ResultSet resultSet) throws SQLException {
            List<ProductPreview> productPreviews = new ArrayList<>(); // La lista che conterrà i product previews

            while (resultSet.next()) {
                // Ottiene i valori necessari dalla riga del ResultSet
                String nomeArticolo = resultSet.getString("nomeArticolo");
                String taglia = resultSet.getString("taglia");
                int prezzo = resultSet.getInt("prezzo");

                // Crea l'oggetto ProductPreview
                ProductPreview productPreview = new ProductPreview(nomeArticolo, prezzo, taglia);

                // Aggiunge alla lista
                productPreviews.add(productPreview);
            }

            // Restituisce la lista finale
            return productPreviews;
        }
    }
}
