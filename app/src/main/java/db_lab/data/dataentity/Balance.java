package db_lab.data.dataentity;

public class Balance implements DataEntity{

    private int idSaldo;
    private int ammontare;

    public Balance(int idSaldo,int ammontare){
        this.idSaldo = idSaldo;
        this.ammontare = ammontare;

    }
    @Override
    public int getId() {
        return this.idSaldo;
    }

    public int getAmmontare(){
        return this.ammontare;
    }

    
}