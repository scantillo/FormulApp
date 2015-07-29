package modelo;

import android.content.Context;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rytscc on 10/05/2015.
 */
public class Pregunta implements Serializable {
    private Context context;
    private String idEncuesta;
    @SerializedName("IdPregunta")
    private int idPregunta;
    @SerializedName("Tipo_Pregunta")
    private int tipoPregunta;
    @SerializedName("Titulo")
    private String titulo;
    @SerializedName("Texto_Ayuda")
    private String txtAyuda;
    @SerializedName("Orden")
    private int orden;
    @SerializedName("Obligatoria")
    private String obligatoria;
    @SerializedName("opciones")
    private ArrayList<OpcionForm> opciones =  null;

    public Pregunta(String idEncuesta, int idPregunta, int tipoPregunta, String titulo, String txtAyuda, int orden, String obligatoria) {
        this.idEncuesta = idEncuesta;
        this.idPregunta = idPregunta;
        this.tipoPregunta = tipoPregunta;
        this.titulo = titulo;
        this.txtAyuda = txtAyuda;
        this.orden = orden;
        this.obligatoria = obligatoria;

    }

    public Pregunta(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public String getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(String idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public int getTipoPregunta() {
        return tipoPregunta;
    }

    public void setTipoPregunta(int tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTxtAyuda() {
        return txtAyuda;
    }

    public void setTxtAyuda(String txtAyuda) {
        this.txtAyuda = txtAyuda;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getObligatoria() {
        return obligatoria;
    }

    public void setObligatoria(String obligatoria) {
        this.obligatoria = obligatoria;
    }



    public ArrayList<OpcionForm> getOpciones() {
        return opciones;
    }

    public void setOpciones(ArrayList<OpcionForm> opciones) {
        this.opciones = opciones;
    }
    public boolean isOpcionEditble(String  tag)
    {
        boolean resul = false;
       for(int i=0;i<opciones.size();i++)
        {
          if(opciones.get(i).getEtInicial().equals(tag))
          {
              if (opciones.get(i).getEditble().equals('S')){            resul=true;}
          }
        }
        return resul;
    }

    public static Pregunta cursorToPregunta(Context context, Cursor c)
    {
        Pregunta pre = null;

        if (c != null)
        {
            pre = new Pregunta(context);

            pre.setIdPregunta(c.getInt(c.getColumnIndex("IdPregunta")));
            pre.setTitulo(c.getString(c.getColumnIndex("Titulo")));
            pre.setTxtAyuda(c.getString(c.getColumnIndex("Texto_Ayuda")));
            pre.setObligatoria(c.getString(c.getColumnIndex("obligatoria")));
            pre.setTipoPregunta(c.getInt(c.getColumnIndex("Tipo_Pregunta")));
            pre.setOrden(c.getInt(c.getColumnIndex("orden")));

        }

        return pre ;
    }
}
