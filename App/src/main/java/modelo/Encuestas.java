package modelo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rytscc on 03/07/2015.
 */
public class Encuestas implements Serializable {
    @SerializedName("encuestas")
    ArrayList<Encuesta> encuestas;

    public Encuestas(ArrayList<Encuesta> encuestas) {
        this.encuestas = encuestas;
    }

    public ArrayList<Encuesta> getEncuestas() {
        return encuestas;
    }

    public void setEncuestas(ArrayList<Encuesta> encuestas) {
        this.encuestas = encuestas;
    }
}
