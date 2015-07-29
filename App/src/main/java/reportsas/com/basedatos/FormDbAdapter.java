package reportsas.com.basedatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import modelo.Encuesta;
import modelo.EncuestaParametro;
import modelo.EncuestaRespuesta;
import modelo.OpcionForm;
import modelo.ParametrosRespuesta;
import modelo.Pregunta;
import modelo.PreguntaRespuesta;
import modelo.Valor;

/**
 * Created by rytscc on 10/05/2015.
 */
public class FormDbAdapter {
    private Context contexto;
    private FormsReaderDbHelper dbHelper;
    private SQLiteDatabase db;

    public FormDbAdapter(Context context)
    {
        this.contexto = context;
    }

    public FormDbAdapter abrir() throws SQLException
    {
        dbHelper = new FormsReaderDbHelper(contexto);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar()
    {
        dbHelper.close();
    }

    public Cursor getCursor(String  tabla,String[] columnas, String where) throws SQLException
    {
        Cursor c = db.query( true, tabla, columnas, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    public long Insertar( String tabla,  ContentValues contentValues ) {


        return db.insert(tabla, null, contentValues);
    }
    public long Eliminar( String tabla, String condicion) {

        if (condicion == null)
        {
            return db.delete(tabla, null, null);
        }else {
            return db.delete(tabla, condicion, null);
        }
    }
    public  ArrayList<Valor> getValores(int idOpcion)
    {
        ArrayList<Valor> valores = new ArrayList<Valor>();
        if (db == null) {
            try {
                abrir();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        String columns[] = new String[]{"IdValor","Descripcion"};
        String where = "IdOpcion = "+idOpcion;
        Cursor cval = db.query(true, "Valor", columns, where, null, null, null, null, null);
        for (cval.moveToFirst(); !cval.isAfterLast(); cval.moveToNext()) {
            Valor va=Valor.cursorToValor(contexto, cval);
            valores.add(va);

        }
        return valores;
    }
    public  int getFormAlmacenados(int iduser)
    {
        int cantidad = 0;
        if (db == null) {
            try {
                abrir();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        Cursor cursor = db.rawQuery("SELECT count(consecutivo)  FROM Encuesta_Repuesta where IdUsuario = "+iduser+";", null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    cantidad = cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        }
        return cantidad;
    }

    public ArrayList<OpcionForm> getOpciones(String idEncuesta,int idPregunta)
    {
        ArrayList<OpcionForm> opciones = new ArrayList<OpcionForm>();
        if (db == null) {
            try {
                abrir();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        String columns[] = new String[]{"IdOpcion","Etiqueta_Inicial","Etiqueta_Final","obligatorio","Editable"};
        String where = "IdEncuesta = '"+idEncuesta+"' and IdPregunta="+idPregunta;
        Cursor cop = db.query(true, "Opcion", columns, where, null, null, null, "orden , Editable", null);
        for (cop.moveToFirst(); !cop.isAfterLast(); cop.moveToNext()) {
            OpcionForm op=OpcionForm.cursorToOpcion(contexto, cop);
            op.setValores(getValores(op.getIdOpcion()));
            opciones.add(op);

        }

        cop.close();
        return opciones;
    }
    public ArrayList<Pregunta> getPreguntas(String idEncuesta)
    {
       ArrayList<Pregunta> preguntas = new ArrayList<Pregunta>();
        if (db == null) {
            try {
                abrir();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        String columns[] = new String[]{"IdPregunta","Titulo","Texto_Ayuda","obligatoria","Tipo_Pregunta","orden"};

        Cursor cpre = db.query(true, "Pregunta", columns, "IdEncuesta = '"+idEncuesta+"'", null, null, null, "orden", null);
        for (cpre.moveToFirst(); !cpre.isAfterLast(); cpre.moveToNext()) {
            Pregunta pr=Pregunta.cursorToPregunta(contexto, cpre);
            pr.setOpciones(getOpciones(idEncuesta, pr.getIdPregunta()));
            preguntas.add(pr);

        }

        cpre.close();
       return preguntas;
    }

    public ArrayList<EncuestaParametro> getParametros(String idEncuesta)
    {
        ArrayList<EncuestaParametro> parametros = new ArrayList<EncuestaParametro>();
        if (db == null) {
            try {
                abrir();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        String columns[] = new String[]{"IdParametro", "Opcional"};
        String where = "IdEncuesta = '"+idEncuesta+"'";
        Cursor cpar = db.query(true, "Encuesta_Parametro", columns, where, null, null, null, null, null);
        for (cpar.moveToFirst(); !cpar.isAfterLast(); cpar.moveToNext()) {
            EncuestaParametro pr=EncuestaParametro.cursorToParametro(contexto, cpar);
            parametros.add(pr);

        }

        cpar.close();
        return parametros;
    }

    public List<Encuesta> getFormularios(String  tabla,String[] columnas, String where)
    {
        List<Encuesta> encues = new ArrayList<Encuesta>();

     if (db == null) {
         try {
             abrir();

         } catch (SQLException e) {
             e.printStackTrace();
         }

     }
        Cursor c = db.query(true, tabla, columnas, where, null, null, null, null, null);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            encues.add(Encuesta.cursorToEncuesta(contexto, c));

        }

        c.close();

        return encues;
    }

    public ArrayList<EncuestaRespuesta> getFormulariosRespuestas()
    {
        ArrayList<EncuestaRespuesta> encues = new ArrayList<EncuestaRespuesta>();
        String columnas[] = new String[]{"IdUsuario","IdEncuesta","Fecha_Realizacion","consecutivo"};

        if (db == null) {
            try {
                abrir();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        Cursor c = db.query(true, "Encuesta_Repuesta", columnas, null, null, null, null, null, null);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            EncuestaRespuesta encRes=EncuestaRespuesta.cursorToEncuestaR(contexto, c);
            encRes.setRespuesta(getPreguntasR(encRes));
            encRes.setParametros(getParametosR(encRes));

            encues.add(encRes);

        }

        c.close();

        return encues;
    }
    public ArrayList<PreguntaRespuesta> getPreguntasR(EncuestaRespuesta ec)
    {
        ArrayList<PreguntaRespuesta> preguntas = new ArrayList<PreguntaRespuesta>();
        if (db == null) {
            try {
                abrir();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        String columns[] = new String[]{"item","repuesta","opcion","IdPregunta"};

        Cursor cpre = db.query(true, "Pregunta_Respuesta", columns, "IdEncuesta = '"+ec.getIdEncuesta()+"' and IdUsuario= "+ec.getIdUsuario()+"  and consecutivo = "+ec.getConsecutivo(), null, null, null,null, null);
        for (cpre.moveToFirst(); !cpre.isAfterLast(); cpre.moveToNext()) {
            PreguntaRespuesta pr=PreguntaRespuesta.cursorPreguntaR(contexto, cpre);

            preguntas.add(pr);

        }

        cpre.close();
        return preguntas;
    }

    public ArrayList<ParametrosRespuesta> getParametosR(EncuestaRespuesta ec)
    {
        ArrayList<ParametrosRespuesta> paramet = new ArrayList<ParametrosRespuesta>();
        if (db == null) {
            try {
                abrir();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        String columns[] = new String[]{"IdParametro","valor"};

        Cursor cpre = db.query(true, "Parametro_Encuesta_Respuesta", columns, "IdEncuesta = '"+ec.getIdEncuesta()+"' and IdUsuario= "+ec.getIdUsuario()+"  and consecutivo = "+ec.getConsecutivo(), null, null, null,null, null);
        for (cpre.moveToFirst(); !cpre.isAfterLast(); cpre.moveToNext()) {
            ParametrosRespuesta pr=ParametrosRespuesta.cursorToParametroR(contexto, cpre);

            paramet.add(pr);

        }

        cpre.close();
        return paramet;
    }

    public  Encuesta formFind(Context context, String id)
    {

        Encuesta encuesta = null;
        String columns[] = new String[]{"IdEncuesta","titulo","Descripcion","Fecha_Fin"};
        String where = "Fecha_Fin >= Datetime('"+getDatePhone()+"') and IdEncuesta = '"+id+"'";
        Cursor c = null;
        try {
            c = getCursor("Encuesta",columns,where);
            encuesta = encuesta.cursorToEncuesta(context, c);
            encuesta.setPreguntas(getPreguntas(id));
            encuesta.setParametros(getParametros(id));
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }



        return encuesta;
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
