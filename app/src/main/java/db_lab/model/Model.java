package db_lab.model;

import db_lab.data.dataentity.Product;
import db_lab.data.dataentity.ProductPreview;
import db_lab.data.dataentity.User;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface Model {

    public void newUser(String nome, String cognome, String email,
            String telefono, String indirizzo, Date data, String cf, String username,
            String password);

    public List<User> getUsers();

    public boolean checkLogin(String username, String password);

}
