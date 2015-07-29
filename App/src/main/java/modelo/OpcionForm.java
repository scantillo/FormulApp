package modelo;

import android.content.Context;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rytscc on 10/05/2015.
 */
public class OpcionForm implements Serializable {
    private Context context;
    @SerializedName("IdOpcion")
    private int idOpcion;

    private int idEncuesta;
    private int idPregunta;

    @SerializedName("EtInicial")
    private String etInicial;
    @SerializedName("Etiqueta_Final")
    private String etFinal;
    @SerializedName("Orden")
    private int orden;
    @SerializedName("Editable")
    private String editble;
    @SerializedName("Obligatorio")
    private String obligatoria;
    @SerializedName("valores")
    private ArrayList<Valor> valores= null;

    public OpcionForm(String estado, int idOpcion, int idEncuesta, int idPregunta, String etInicial, String etFinal, int orden, String editble, String obligatoria) {

        this.idOpcion = idOpcion;
        this.idEncuesta = idEncuesta;
        this.idPregunta = idPregunta;
        this.etInicial = etInicial;
        this.etFinal = etFinal;
        this.orden = orden;
        this.editble = editble;
        this.obligatoria = obligatoria;
    }

    public OpcionForm(Context context) {
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

    public int getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(int idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getEditble() {
        return editble;
    }

    public void setEditble(String editble) {
        this.editble = editble;
    }

    public String getObligatoria() {
        return obligatoria;
    }

    public void setObligatoria(String obligatoria) {
        this.obligatoria = obligatoria;
    }



    public ArrayList<Valor> getValores() {
        return valores;
    }

    public void setValores(ArrayList<Valor> valores) {
        this.valores = valores;
    }

    public String getEtInicial() {
        return etInicial;
    }

    public void setEtInicial(String etInicial) {
        this.etInicial = etInicial;
    }

    public String getEtFinal() {
        return etFinal;
    }

    public void setEtFinal(String etFinal) {
        this.etFinal = etFinal;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }


    public static OpcionForm cursorToOpcion(Context context, Cursor c)
    {
        OpcionForm opc = null;

        if (c != null)
        {
            opc = new OpcionForm(context);

            opc.setIdOpcion(c.getInt(c.getColumnIndex("IdOpcion")));
            opc.setEtInicial(c.getString(c.getColumnIndex("Etiqueta_Inicial")));
            opc.setEtFinal(c.getString(c.getColumnIndex("Etiqueta_Final")));
            opc.setEditble(c.getString(c.getColumnIndex("Editable")));
            opc.setObligatoria(c.getString(c.getColumnIndex("obligatorio")));



        }

        return opc ;
    }
}
