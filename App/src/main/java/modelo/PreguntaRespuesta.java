package modelo;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rytscc on 13/05/2015.
 */
public class PreguntaRespuesta  implements Serializable {

    private Context context;

    @SerializedName("idPregunta")
    private int idPregunta;
    @SerializedName("item")
    private int item;
    @SerializedName("respuesta")
    private String respuesta;
    @SerializedName("opcion")
    private String opcion;

    public PreguntaRespuesta() {
        opcion=null;
    }
    public PreguntaRespuesta(int idPregunta, int item, String respuesta, String opcion) {
        this.idPregunta = idPregunta;
        this.item = item;
        this.respuesta = respuesta;
        this.opcion = opcion;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public PreguntaRespuesta(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }
    public static PreguntaRespuesta cursorPreguntaR(Context context, Cursor c)
    {
        PreguntaRespuesta enc = null;

        if (c != null)
        {
            enc = new PreguntaRespuesta(context);

            enc.setIdPregunta(Integer.parseInt(c.getString(c.getColumnIndex("IdPregunta"))));
            enc.setItem(Integer.parseInt(c.getString(c.getColumnIndex("item"))));
            enc.setRespuesta(c.getString(c.getColumnIndex("repuesta")));
            if(!c.isNull(2))
            {
                enc.setOpcion(c.getString(c.getColumnIndex("opcion")));
            }


        }

        return enc ;
    }
}
