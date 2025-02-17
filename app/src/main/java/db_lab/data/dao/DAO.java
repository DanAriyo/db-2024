package db_lab.data.dao;

import java.util.List;

import db_lab.data.DAOException;
import db_lab.data.dataentity.DataEntity;

public interface DAO<T extends DataEntity> {

    public List<T> getAll() throws DAOException;

    public List<T> filterbyID(int id) throws DAOException;

    public void deletebyID(int id) throws DAOException;

    public void updatebyID(int id) throws DAOException;

    public void create(T entity) throws DAOException;

}