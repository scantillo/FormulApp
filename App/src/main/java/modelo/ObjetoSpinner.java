package modelo;

/**
 * Created by rytscc on 13/05/2015.
 */
public class ObjetoSpinner {
    int id;
    String nombre;
    //Constructor
    public ObjetoSpinner(int id, String nombre) {
        super();
        this.id = id;
        this.nombre = nombre;
    }
    @Override
    public String toString() {
        return nombre;
    }
    public int getId() {
        return id;
    }
}
