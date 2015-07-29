package reportsas.com.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import reportsas.com.basedatos.FormsDataStore;

/**
 * Created by rytscc on 29/04/2015.
 */
public class FormsReaderDbHelper  extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "EncuestasReports.db";
    public static final int DATABASE_VERSION = 1;

    public FormsReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Crear la tabla Quotes
        db.execSQL(FormsDataStore.CREATE_ENCUESTA_SCRIPT);
        db.execSQL(FormsDataStore.CREATE_PARAMESTROENC_SCRIPT);
        db.execSQL(FormsDataStore.CREATE_ENCUESTARES_SCRIPT);
        db.execSQL(FormsDataStore.CREATE_PREGUNTA_SCRIPT);
        db.execSQL(FormsDataStore.CREATE_PARAMENCUESTARES_SCRIPT);
        db.execSQL(FormsDataStore.CREATE_OPCION_SCRIPT);
        db.execSQL(FormsDataStore.CREATE_PREGUNTARES_SCRIPT);
        db.execSQL(FormsDataStore.CREATE_VALOR_SCRIPT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
