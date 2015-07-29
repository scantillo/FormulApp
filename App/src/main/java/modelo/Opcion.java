package modelo;

/**
 * Created by rytscc on 03/05/2015.
 */
public class Opcion {
   private String titulo;
   private int imagen;

    public Opcion(String tirulo, int imagen) {
        this.titulo = tirulo;
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
