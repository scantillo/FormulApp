package modelo;

import android.content.Context;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rytscc on 10/05/2015.
 */
public class Valor implements Serializable {
    private Context context;
    private int idOpcion;
    @SerializedName("IdValor")
    private int idVlor;
    @SerializedName("Descripcion")
    private String descripcion;


    public Valor(int idVlor, int idOpcion, String descripcion) {

        this.idVlor = idVlor;
        this.idOpcion = idOpcion;
        this.descripcion = descripcion;

    }

    public Valor(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public int getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(int idOpcion) {
        this.idOpcion = idOpcion;
    }

    public int getIdVlor() {
        return idVlor;
    }

    public void setIdVlor(int idVlor) {
        this.idVlor = idVlor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public static Valor cursorToValor(Context context, Cursor c)
    {
        Valor val = null;

        if (c != null)
        {
            val = new Valor(context);

            val.setIdVlor(c.getInt(c.getColumnIndex("IdValor")));
            val.setDescripcion(c.getString(c.getColumnIndex("Descripcion")));



        }

        return val ;
    }
}
