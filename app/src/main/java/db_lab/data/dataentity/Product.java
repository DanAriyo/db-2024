package db_lab.data.dataentity;

import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public  class Product implements DataEntity{

    private int idArticolo;
    private int idProprietario;  // Assumiamo che idProprietario sia un oggetto User
    private int idCategoria;  // Assumiamo che idCategoria sia un oggetto Category
    private String materiale;
    private String taglia;
    private String condizioni;
    private String brand;
    private String descrizione;
    private String foto;  // Se la foto è una stringa (URL o nome file)
    private double prezzo;

    public Product(int idArticolo, int idProprietario, int idCategoria, String materiale, 
                   String taglia, String condizioni, String brand, String descrizione, 
                   String foto, double prezzo) {
        this.idArticolo = idArticolo;
        this.idProprietario = idProprietario;
        this.idCategoria = idCategoria;
        this.materiale = materiale;
        this.taglia = taglia;
        this.condizioni = condizioni;
        this.brand = brand;
        this.descrizione = descrizione;
        this.foto = foto;
        this.prezzo = prezzo;
    }

    @Override
    public int getId() {
        return this.idArticolo;
    }

    // Getter per idProprietario
    public int getIdProprietario() {
        return this.idProprietario;
    }

    // Getter per idCategoria
    public int getIdCategoria() {
        return this.idCategoria;
    }

    // Getter per materiale
    public String getMateriale() {
        return this.materiale;
    }

    // Getter per taglia
    public String getTaglia() {
        return this.taglia;
    }

    // Getter per condizioni
    public String getCondizioni() {
        return this.condizioni;
    }

    // Getter per brand
    public String getBrand() {
        return this.brand;
    }

    // Getter per descrizione
    public String getDescrizione() {
        return this.descrizione;
    }

    // Getter per foto
    public String getFoto() {
        return this.foto;
    }

    // Getter per prezzo
    public double getPrezzo() {
        return this.prezzo;
    }


}