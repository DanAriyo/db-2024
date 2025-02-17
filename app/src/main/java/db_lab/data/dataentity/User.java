package db_lab.data.dataentity;

import java.sql.Date;

public class User implements DataEntity {

    private int id;
    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    private String indirizzo;
    private String cf;
    private String iban;
    private int idSaldo;
    private Date data;


    public User(int id, String nome,String cognome, String email, 
        String iban, int idSaldo, String telefono, String indirizzo, Date data,String cf ){

        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.iban = iban;
        this.idSaldo = idSaldo;
        this.telefono = telefono;
        this.indirizzo = indirizzo;
        this.data = data;
        this.cf = cf;
    }

    @Override
    public int getId() {
        return this.id;
    }

    // Getter per nome
    public String getNome() {
        return this.nome;
    }

    // Getter per cognome
    public String getCognome() {
        return this.cognome;
    }

    // Getter per email
    public String getEmail() {
        return this.email;
    }

    // Getter per iban
    public String getIban() {
        return this.iban;
    }

    // Getter per idSaldo
    public int getIdSaldo() {
        return this.idSaldo;
    }

    // Getter per telefono
    public String getTelefono() {
        return this.telefono;
    }

    // Getter per indirizzo
    public String getIndirizzo() {
        return this.indirizzo;
    }

    // Getter per data
    public Date getData() {
        return this.data;
    }

    // Getter per cf
    public String getCf() {
        return this.cf;
    }

    
}