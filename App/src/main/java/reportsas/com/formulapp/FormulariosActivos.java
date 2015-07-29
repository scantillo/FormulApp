package reportsas.com.formulapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import mobi.pdf417.demo.R;
import modelo.Encuesta;
import modelo.EncuestaParametro;
import modelo.Encuestas;
import modelo.OpcionForm;
import modelo.ParametrosRespuesta;
import modelo.Pregunta;
import modelo.PreguntaRespuesta;
import modelo.Valor;
import reportsas.com.basedatos.FormDbAdapter;
import reportsas.com.reportsas.com.utilidades.DataOpciones;
import reportsas.com.reportsas.com.utilidades.MyRestFulGP;

public class FormulariosActivos extends ActionBarActivity implements SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener {
    ListView lista;
    ArrayAdapter<Encuesta> adaptador;
    private FormDbAdapter dbAdapter;
    private Cursor cursor;
    private FormsAdapter formAdap;
    String mensaje_default, idUSer;
    TextView txt_textoaux;
    private final String HTTP_EVENT="FormWS.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formularios_activos);
        lista = (ListView) findViewById(R.id.listaFormularios);
        txt_textoaux=(TextView)findViewById(R.id.textoAux);
        SharedPreferences pref = getSharedPreferences("ParametrosBasicos", Context.MODE_PRIVATE);
        long per = pref.getInt("userid",0);
        idUSer=per+"";
        dbAdapter = new FormDbAdapter(this);
        mensaje_default="No hay Formularios almacenados, por favor oprima el boton de actualizar!";
        llenarLista("Fecha_Fin >= Datetime('" + getDatePhone() + "')");


    }


    public static String getDatePhone()

    {

        Calendar cal = new GregorianCalendar();

        Date date = cal.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String formatteDate = df.format(date);

        return formatteDate;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formularios_activos, menu);

        MenuItem searchItem = menu.findItem(R.id.menu3_buscar);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(searchItem, this);

        return super.onCreateOptionsMenu(menu);

    }
    public void ActualizarFormularios()
    {
        if(DataOpciones.verificaConexion(this)) {
            SharedPreferences pref = getSharedPreferences("ParametrosBasicos", Context.MODE_PRIVATE);
            String per = pref.getString("ruta", "54.164.174.129:8081");
            //http://10.200.5.8:8081/
            String ruta="http://"+per+"/"+HTTP_EVENT;

            new MyAsyncTask(FormulariosActivos.this)
                    .execute("GET", ruta);

        }else
        {
            Toast toast1 =
                    Toast.makeText(FormulariosActivos.this,
                            "Debe tener conexión a internet para actualizar la lista de Formularios", Toast.LENGTH_SHORT);
            toast1.show();

        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.upd) {
            ActualizarFormularios();
            mensaje_default="No hay Formularios para diligenciar!";
            llenarLista("Fecha_Fin >= Datetime('" + getDatePhone() + "')");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void llenarLista(String where)
    {
        try {
            dbAdapter.abrir();
            String columns[] = new String[]{"IdEncuesta", "titulo", "Descripcion", "Fecha_Fin"};
            //String where = "Fecha_Fin >= Datetime('" + getDatePhone() + "')";
            List<Encuesta> result = dbAdapter.getFormularios("Encuesta", columns, where);
            lista.setAdapter(null);
            if (result.size() > 0) {
                txt_textoaux.setText("");
                adaptador = new FormsAdapter(this, result);

                lista.setAdapter(adaptador);
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Encuesta encuestaActual = (Encuesta) adaptador.getItem(position);

                        Intent i2;
                        i2 = new Intent(FormulariosActivos.this, Formulario.class);
                        i2.putExtra("formulario", encuestaActual.getIdEncuesta());
                        startActivity(i2);

                    }
                });
            }
            else
            {
                txt_textoaux.setText(mensaje_default);
            }
            dbAdapter.cerrar();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void insertarEncuentas(Encuestas resultado)
    {
        try {
            dbAdapter.abrir();
            dbAdapter.Eliminar("Valor", null);
            dbAdapter.Eliminar("Opcion",null);
            dbAdapter.Eliminar("Pregunta",null);
            dbAdapter.Eliminar("Encuesta_Parametro",null);
            dbAdapter.Eliminar("Encuesta", null);
            if (resultado.getEncuestas() !=null)
            {
                for (int k = 0; k < resultado.getEncuestas().size(); k++) {
                    Encuesta encuesta= resultado.getEncuestas().get(k);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("IdEncuesta", encuesta.getIdEncuesta());
                    contentValues.put("Descripcion", encuesta.getDescripcion());
                    contentValues.put("Fecha_Fin", encuesta.getFechaFin());
                    contentValues.put("Fecha_Registro", encuesta.getFechaReg());
                    contentValues.put("Fecha_Inicio", encuesta.getFechaIni());
                    contentValues.put("titulo", encuesta.getTitulo());
                    if(dbAdapter.Insertar("Encuesta",contentValues)> 0) {
                        if(encuesta.getPreguntas() != null){
                            for (int j = 0; j < encuesta.getPreguntas().size(); j++) {
                                Pregunta pregunta = encuesta.getPreguntas().get(j);
                                contentValues = new ContentValues();
                                contentValues.put("IdEncuesta", encuesta.getIdEncuesta());
                                contentValues.put("IdPregunta", pregunta.getIdPregunta());
                                contentValues.put("Tipo_Pregunta",pregunta.getTipoPregunta());
                                contentValues.put("Titulo", pregunta.getTitulo());
                                contentValues.put("Texto_Ayuda", pregunta.getTxtAyuda());
                                contentValues.put("orden", pregunta.getOrden());
                                contentValues.put("obligatoria", pregunta.getObligatoria());
                                if(dbAdapter.Insertar("Pregunta",contentValues)> 0) {
                                    if(pregunta.getOpciones() != null){
                                        for (int i = 0; i <pregunta.getOpciones().size(); i++) {
                                            OpcionForm opcion = pregunta.getOpciones().get(i);
                                            contentValues = new ContentValues();
                                            contentValues.put("IdEncuesta", encuesta.getIdEncuesta());
                                            contentValues.put("IdPregunta", pregunta.getIdPregunta());
                                            contentValues.put("IdOpcion", opcion.getIdOpcion());
                                            contentValues.put("Etiqueta_Inicial", opcion.getEtInicial());
                                            contentValues.put("Etiqueta_Final", opcion.getEtFinal());
                                            contentValues.put("orden", opcion.getOrden());
                                            contentValues.put("Editable", opcion.getEditble());
                                            contentValues.put("obligatorio", opcion.getObligatoria());
                                            if (dbAdapter.Insertar("Opcion", contentValues) > 0) {
                                                if(opcion.getValores() != null){
                                                    for (int h = 0; h < opcion.getValores().size(); h++) {
                                                        Valor valor = opcion.getValores().get(h);
                                                        contentValues = new ContentValues();
                                                        contentValues.put("IdOpcion", opcion.getIdOpcion());
                                                        contentValues.put("IdValor", valor.getIdVlor());
                                                        contentValues.put("Descripcion", valor.getDescripcion());
                                                        dbAdapter.Insertar("Valor", contentValues);
                                                    }
                                                }


                                            }
                                        }

                                    }

                                }
                            }
                        }

                        if(encuesta.getParametros() != null){
                            for (int j = 0; j < encuesta.getParametros().size(); j++) {
                                EncuestaParametro parametroR = encuesta.getParametros().get(j);
                                contentValues = new ContentValues();
                                contentValues.put("IdEncuesta", encuesta.getIdEncuesta());
                                contentValues.put("IdParametro", parametroR.getIdParametro());
                                contentValues.put("Opcional", parametroR.getOpcional());

                                if(dbAdapter.Insertar("Encuesta_Parametro",contentValues)> 0) {

                                }
                            }
                        }


                    }


                }

            }



            dbAdapter.cerrar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {

        return true;


    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        mensaje_default="No formuarios para diligencias, por favor oprima el boton de actualizar";
        llenarLista("Fecha_Fin >= Datetime('" + getDatePhone() + "')");
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.trim().length()>0)
        {
            mensaje_default="No hay coincidencias para el formulario con codigo "+newText;
            llenarLista("Fecha_Fin >= Datetime('" + getDatePhone() + "') and IdEncuesta like '"+newText+"%'");
        }

        return false;
    }
    public static void reiniciarActivity(Activity actividad){
        Intent intent=new Intent();

        intent.setClass(actividad, actividad.getClass());

        //llamamos a la actividad
        actividad.startActivity(intent);
        //finalizamos la actividad actual
        actividad.finish();
    }

    class MyAsyncTask extends AsyncTask<String,Void,String> {

        private ProgressDialog progressDialog;
        private Context context;
        String mensaje="Descargando Formularios...";


        /**Constructor de clase */
        public MyAsyncTask(Context context) {
            this.context = context;

        }
        /**
         * Realiza la tarea en segundo plano
         * @param params[0] Comando GET/POST
         * @param params[1] Nombre
         * @param params[2] Edad
         * */
        @Override
        protected String doInBackground(String... params) {
            if (this.isCancelled()) {
                return null;
            }

                    MyRestFulGP myRestFulGP = new MyRestFulGP();
                    List<NameValuePair> parames = new ArrayList<NameValuePair>();
                    parames.add(new BasicNameValuePair("tag", "getFormsAll"));
                    parames.add(new BasicNameValuePair("user", idUSer));
                    try {
                        if (params[0].equals("GET")) {
                            String jsonResult = null;

                            jsonResult = myRestFulGP.addEventGet(parames, params[1]);
                            if(!jsonResult.substring(1,1).equals('{'))
                            {
                                jsonResult = jsonResult.substring(jsonResult.indexOf('{'));
                            }
                            mensaje="Actulizando Lista de Formularios...";
                            Log.i("jsonResult", jsonResult);
                           // if (object.getInt("success") == 1) {
                            final Gson gsonR= new Gson();
                            Encuestas en = (Encuestas)gsonR.fromJson(jsonResult, Encuestas.class);
                            insertarEncuentas(en);
                            //llenarLista("Fecha_Fin >= Datetime('" + getDatePhone() + "')");
                            //llenarLista();
                            return "Formularios Actualizados";
                         /*   } else {
                                return "Error: (" + object.getInt("error") + ") " + object.getString("error_msg");
                            }*/


                        }
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                        return "Error: Protocolo " + e.getMessage();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        return "Error: Conexión a Internet" + e.getMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "Problemas al realizar la conexión con el servidor, Intentelo mas tarde.";
                    }

          return "";
        }

        /**
         * Antes de comenzar la tarea muestra el progressDialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(
                    context, "Por favor espere", mensaje);
        }

        /**
         * Cuando se termina de ejecutar, cierra el progressDialog y retorna el resultado a la interfaz
         * **/
        @Override
        protected void onPostExecute(String resul) {
            progressDialog.dismiss();
            // textView.setText(resul);
            if (resul.length() > 0) {
                Toast toast1 =
                        Toast.makeText(FormulariosActivos.this,
                                resul, Toast.LENGTH_SHORT);

                toast1.show();

                reiniciarActivity(FormulariosActivos.this);

            }
        }
    }
}
