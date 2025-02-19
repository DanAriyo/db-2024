package db_lab.data.dataentity;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import db_lab.data.DAOException;

public class Transaction implements DataEntity{

    private int idVenditore;
    private int idAcquirente;
    private int idTransazione;
    private int speseSpedizione;
    private int commissioni;
    private int idSaldo;
    private int idRecensione;
    private int idRecensito;
    private int idRecensiore;
    private int prezzo;
    private Date data;
    private Time ora;

    public Transaction(int idVenditore, int idAcquirente, int idTransazione, int speseSpedizione, 
                       int commissioni, int idSaldo, int idRecensione, int idRecensito, 
                       int idRecensiore, int prezzo, Date data, Time ora) {
        this.idVenditore = idVenditore;
        this.idAcquirente = idAcquirente;
        this.idTransazione = idTransazione;
        this.speseSpedizione = speseSpedizione;
        this.commissioni = commissioni;
        this.idSaldo = idSaldo;
        this.idRecensione = idRecensione;
        this.idRecensito = idRecensito;
        this.idRecensiore = idRecensiore;
        this.prezzo = prezzo;
        this.data = data;
        this.ora = ora;
    }

    public int getIdVenditore() {
        return this.idVenditore;
    }

    public int getIdAcquirente() {
        return this.idAcquirente;
    }

    public int getSpeseSpedizione() {
        return this.speseSpedizione;
    }

    public int getCommissioni() {
        return this.commissioni;
    }

    public int getIdSaldo() {
        return this.idSaldo;
    }

    public int getIdRecensione() {
        return this.idRecensione;
    }

    public int getIdRecensito() {
        return this.idRecensito;
    }

    public int getIdRecensiore() {
        return this.idRecensiore;
    }

    public int getPrezzo() {
        return this.prezzo;
    }

    public Date getData() {
        return this.data;
    }

    public Time getOra() {
        return this.ora;
    }

    @Override
    public int getId() {
        return this.idTransazione;
    }




}