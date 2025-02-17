

package db_lab.data.dataentity;

public class Category implements DataEntity{

    private int idCategoria;
    private String tipo;

    public Category(int idCategoria, String tipo){
        this.idCategoria = idCategoria;
        this.tipo = tipo;
    }
    @Override
    public int getId() {
        return this.idCategoria;
    }

    public String getTipo(){
        return this.tipo;
    }

    
}