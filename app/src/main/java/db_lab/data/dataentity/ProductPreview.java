package db_lab.data.dataentity;

public class ProductPreview implements DataEntity{

    private int id;
    private int prezzo;
    private String taglia;

    public ProductPreview(int id, int prezzo, String taglia){
        this.id = id;
        this.prezzo = prezzo;
        this.taglia = taglia;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public int getPrezzo(){
        return this.prezzo;
    }

    public String getTaglia(){
        return this.taglia;
    }

}
