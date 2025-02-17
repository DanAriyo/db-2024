package db_lab.data.dataentity;

public class Deposit implements DataEntity{

    private int idVersamento;
    private int importo;
    private int idSaldo;
    private int iban;

    public Deposit(int idVersamento,int importo, int idSaldo, int iban){
        this.idVersamento = idVersamento;
        this.idSaldo = idSaldo;
        this.iban = iban;
        this.importo = importo;
    }

    @Override
    public int getId() {
        return this.idVersamento;
    }

    public int getImporto(){
        return this.importo;
    }

    
}