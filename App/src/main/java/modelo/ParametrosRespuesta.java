package modelo;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rytscc on 13/05/2015.
 */
public class ParametrosRespuesta  implements Serializable {


    private Context context;
    @SerializedName("idParametro")
    private int idParametro;

    @SerializedName("Valor")
    private String valor;


    public ParametrosRespuesta()
    {

    }
    public ParametrosRespuesta(int idParametro) {
        this.idParametro = idParametro;

    }



    public ParametrosRespuesta(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getIdParametro() {
        return idParametro;
    }

    public void setIdParametro(int idParametro) {
        this.idParametro = idParametro;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public static ParametrosRespuesta cursorToParametroR(Context context, Cursor c)
    {
        ParametrosRespuesta enc = null;

        if (c != null)
        {
            enc = new ParametrosRespuesta(context);

            enc.setValor(c.getString(c.getColumnIndex("valor")));
            enc.setIdParametro(Integer.parseInt(c.getString(c.getColumnIndex("IdParametro"))));

        }

        return enc ;
    }
}
