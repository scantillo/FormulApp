package modelo;

import android.content.Context;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rytscc on 10/05/2015.
 */
public class EncuestaParametro implements Serializable {
    private Context context;
    private String idEncuesta;
    @SerializedName("IdParametro")
    private int idParametro;
    private String Opcional;

    public EncuestaParametro(String idEncuesta, int idParametro) {
        this.idEncuesta = idEncuesta;
        this.idParametro = idParametro;
    }

    public String getOpcional() {
        return Opcional;
    }

    public void setOpcional(String opcional) {
        Opcional = opcional;
    }

    public String getIdEncuesta() {
        return idEncuesta;
    }

    public EncuestaParametro(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setIdEncuesta(String idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public int getIdParametro() {
        return idParametro;
    }

    public void setIdParametro(int idParametro) {
        this.idParametro = idParametro;
    }

    public static EncuestaParametro cursorToParametro(Context context, Cursor c)
    {
        EncuestaParametro par = null;

        if (c != null)
        {
            par = new EncuestaParametro(context);


            par.setIdParametro(c.getInt(c.getColumnIndex("IdParametro")));
            par.setOpcional(c.getString(c.getColumnIndex("Opcional")));



        }

        return par ;
    }


}
