package reportsas.com.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by rytscc on 29/04/2015.
 */
public class FormsDataStore {


        public static final String CREATE_ENCUESTA_SCRIPT =
                "create table Encuesta"+" (" +
                        "IdEncuesta text primary key ," +
                        "titulo text not null," +
                        "Descripcion text," +
                        "Fecha_Inicio text not null," +
                        "Fecha_Fin text not null," +
                        "Fecha_Registro text not null)";


        public static final String CREATE_PARAMESTROENC_SCRIPT =
                "create table Encuesta_Parametro"+" (" +
                        "IdEncuesta text ," +
                        "IdParametro integer , Opcional text not null, PRIMARY KEY (IdEncuesta, IdParametro))";


        public static final String CREATE_ENCUESTARES_SCRIPT =
                "create table Encuesta_Repuesta"+" (" +
                        "IdUsuario integer not null," +
                        "IdEncuesta text not null," +
                        "Fecha_Realizacion text not null," +
                        "consecutivo integer not null, PRIMARY KEY (IdUsuario, IdEncuesta,consecutivo))";

        public static final String CREATE_PREGUNTA_SCRIPT =
                "create table Pregunta"+" (" +
                        "IdEncuesta text not null," +
                        "IdPregunta integer not null," +
                        "Tipo_Pregunta integer not null,"+
                        "Titulo text not null," +
                        "Texto_Ayuda text," +
                        "orden integer not null," +
                        "obligatoria text not null," +
                        " PRIMARY KEY (IdPregunta, IdEncuesta))";
        public static final String CREATE_PARAMENCUESTARES_SCRIPT =
                "create table Parametro_Encuesta_Respuesta"+" (" +
                        "IdUsuario integer not null ," +
                        "IdEncuesta text not null ," +
                        "valor blob not null," +
                        "IdParametro integer not null," +
                        "consecutivo integer not null,PRIMARY KEY (IdUsuario, IdEncuesta,IdParametro,consecutivo))";
        public static final String CREATE_OPCION_SCRIPT =
                "create table Opcion"+" (" +
                        "IdOpcion integer primary key," +
                        "IdEncuesta text not null," +
                        "IdPregunta integer not null," +
                        "Etiqueta_Inicial text not null," +
                        "Etiqueta_Final text," +
                        "obligatorio text not null," +
                        "Editable text not null," +
                        "orden integer not null)";

        public static final String CREATE_PREGUNTARES_SCRIPT =
                "create table Pregunta_Respuesta"+" (" +
                        "IdUsuario integer not null," +
                        "IdEncuesta text not null," +
                        "item integer not null," +
                        "repuesta text not null," +
                        "opcion text ," +
                        "IdPregunta integer not null," +
                        "consecutivo integer not null, PRIMARY KEY (IdUsuario, IdEncuesta,item,IdPregunta,consecutivo))";
        public static final String CREATE_VALOR_SCRIPT =
                "create table Valor"+" (" +
                        "IdValor integer primary key autoincrement," +
                        "IdOpcion integer not null," +
                        "Descripcion text not null)";


    public FormsReaderDbHelper openHelper;
    private SQLiteDatabase database;

    public FormsDataStore(Context context)

    {
        //Creando una instancia hacia la base de datos
       openHelper = new FormsReaderDbHelper(context);


    }

    public void Open()
    {
        database = openHelper.getWritableDatabase();
    }

}