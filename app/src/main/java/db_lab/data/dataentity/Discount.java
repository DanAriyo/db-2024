package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

public class Discount implements DataEntity {

    private int idSconto;
    private int percentuale;

    public Discount() {
    }

    public Discount(int idSconto, int percentuale) {
        this.idSconto = idSconto;
        this.percentuale = percentuale;
    }

    @Override
    public int getId() {
        return this.idSconto;
    }

    public int getPercentuale() {
        return this.percentuale;
    }

    public final class DiscountDAO {

        private Connection connection;

        public DiscountDAO(Connection connection) {
            this.connection = connection;
        }

        public List<Discount> createDiscountList(ResultSet resultSet) throws SQLException {
            List<Discount> discounts = new ArrayList<>();

            while (resultSet.next()) {
                int idSconto = resultSet.getInt("idSconto");
                int percentuale = resultSet.getInt("percentuale");
                Discount discount = new Discount(idSconto, percentuale);
                discounts.add(discount);
            }
            return discounts;
        }

        public List<Discount> getAll() throws DAOException {
            String query = "SELECT * FROM Sconti";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                return createDiscountList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

        public List<Discount> filterbyID(int id) throws DAOException {
            String query = "SELECT * FROM Sconti WHERE idSconto = ?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                return createDiscountList(rs);
            } catch (Exception e) {
                throw new DAOException("wrong query", e);
            }
        }

        public void create(Discount discount) throws DAOException {
            String query = "INSERT INTO Sconti (percentuale) VALUES (?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, discount.getPercentuale());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("Error creating discount", e);
            }
        }
    }
}
