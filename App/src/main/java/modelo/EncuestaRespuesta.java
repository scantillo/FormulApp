package modelo;

import android.content.Context;
import android.database.Cursor;

import java.io.Serializable;
import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rytscc on 13/05/2015.
 */
public class EncuestaRespuesta  implements Serializable {

    private Context context;
    @SerializedName("idUser")
    private long idUsuario ;
    @SerializedName("idEncuesta")
    private String idEncuesta;
    @SerializedName("consecutivo")
    private long consecutivo;
    @SerializedName("fecha")
    private String fecha;
    @SerializedName("respuestas")
    private ArrayList<PreguntaRespuesta> respuesta ;
    @SerializedName("parametros")
    private ArrayList<ParametrosRespuesta> parametros ;


    public EncuestaRespuesta( String idEncuesta, long idUsuario, long consecutivo, String fecha) {

        this.idEncuesta = idEncuesta;
        this.idUsuario = idUsuario;
        this.consecutivo = consecutivo;
        this.fecha = fecha;
        this.respuesta= null;
        this.parametros= null;
    }

    public EncuestaRespuesta(Context context) {
        this.context = context;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(String idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public long getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(long consecutivo) {
        this.consecutivo = consecutivo;
    }

    public ArrayList<PreguntaRespuesta> getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(ArrayList<PreguntaRespuesta> respuesta) {
        this.respuesta = respuesta;
    }

    public ArrayList<ParametrosRespuesta> getParametros() {
        return parametros;
    }

    public void setParametros(ArrayList<ParametrosRespuesta> parametros) {
        this.parametros = parametros;
    }
    public String toString(){
        return "";
    }


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public static EncuestaRespuesta cursorToEncuestaR(Context context, Cursor c)
    {
        EncuestaRespuesta enc = null;

        if (c != null)
        {
            enc = new EncuestaRespuesta(context);

            enc.setIdEncuesta(c.getString(c.getColumnIndex("IdEncuesta")));
            enc.setIdUsuario(Integer.parseInt(c.getString(c.getColumnIndex("IdUsuario"))));
            enc.setConsecutivo(Long.parseLong(c.getString(c.getColumnIndex("consecutivo"))));
            enc.setFecha(c.getString(c.getColumnIndex("Fecha_Realizacion")));

        }

        return enc ;
    }
}
