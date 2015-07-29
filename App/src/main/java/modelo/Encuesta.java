package modelo;

import android.content.Context;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import reportsas.com.basedatos.FormDbAdapter;

/**
 * Created by rytscc on 10/05/2015.
 */
public class Encuesta implements Serializable {

    private Context context;
    @SerializedName("idEncuesta")
    private String idEncuesta;
    @SerializedName("titulo")
    private String titulo;
    @SerializedName("descripcion")
    private String descripcion;
    @SerializedName("fechaInicial")
    private String fechaIni;
    @SerializedName("Fecha_Registro")
    private String fechaReg;
    @SerializedName("fechaFinal")
    private String fechaFin;
    @SerializedName("preguntas")
    private ArrayList<Pregunta> preguntas = null;
    @SerializedName("parametros")
    private ArrayList<EncuestaParametro> parametros = null;

    public Encuesta(String idEncuesta, String titulo, String descripcion, String fechaFin) {

        this.idEncuesta = idEncuesta;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaFin = fechaFin;
    }

    public String getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(String fechaReg) {
        this.fechaReg = fechaReg;
    }

    public String getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(String fechaIni) {
        this.fechaIni = fechaIni;
    }
    public Context getContext() {
        return context;
    }

    public Encuesta(Context context) {
        this.context = context;
    }

    public String getIdEncuesta() {
        return idEncuesta;
    }

    public  void setIdEncuesta(String idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public ArrayList<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(ArrayList<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    public ArrayList<EncuestaParametro> getParametros() {
        return parametros;
    }

    public void setParametros(ArrayList<EncuestaParametro> parametros) {
        this.parametros = parametros;
    }

    public static Encuesta formFind(Context context, String id)
    {
        FormDbAdapter dbAdapter = new FormDbAdapter(context);
        Encuesta encuesta = null;
        String columns[] = new String[]{"IdEncuesta","titulo","Descripcion","Fecha_Fin"};
        String where = "Fecha_Fin >= Datetime('"+getDatePhone()+"') and IdEncuesta = "+id+"'";
        Cursor c = null;
        try {
            c = dbAdapter.getCursor("Encuesta",columns,where);
            encuesta = encuesta.cursorToEncuesta(context, c);

            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }



        return encuesta;
    }

    public static Encuesta cursorToEncuesta(Context context, Cursor c)
    {
        Encuesta enc = null;

        if (c != null)
        {
            enc = new Encuesta(context);

            enc.setIdEncuesta(c.getString(c.getColumnIndex("IdEncuesta")));
            enc.setTitulo(c.getString(c.getColumnIndex("titulo")));
            enc.setDescripcion(c.getString(c.getColumnIndex("Descripcion")));
            enc.setFechaFin(c.getString(c.getColumnIndex("Fecha_Fin")));

        }

        return enc ;
    }
    public static String getDatePhone()

    {

        Calendar cal = new GregorianCalendar();

        Date date = cal.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String formatteDate = df.format(date);

        return formatteDate;

    }
}
