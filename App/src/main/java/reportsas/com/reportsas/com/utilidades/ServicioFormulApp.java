package reportsas.com.reportsas.com.utilidades;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import modelo.Encuesta;
import modelo.EncuestaRespuesta;
import modelo.Encuestas;
import modelo.ParametrosRespuesta;
import modelo.PreguntaRespuesta;
import reportsas.com.basedatos.FormDbAdapter;
import reportsas.com.formulapp.FormsAdapter;
import reportsas.com.formulapp.Formulario;

/**
 * Created by rytscc on 06/07/2015.
 */
public class ServicioFormulApp extends Service
{
    int counter = 0;
    static final int UPDATE_INTERVAL = 60000;
    private Timer timer = new Timer();
    final Gson gsn = new Gson();
    boolean validar=true;
    ArrayList<EncuestaRespuesta> respuetas;
    private FormDbAdapter dbAdapter;
    private final String HTTP_EVENT="apirest.php";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    public String ConvertirJson(EncuestaRespuesta ec)
    {
        return gsn.toJson(ec);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        // Queremos que este servicio se ejecute continuamente
        // hasta que sea detenido manualmente, por lo que retornaremos
        // START_STICKY
        dbAdapter = new FormDbAdapter(this);
        doSomethingRepeatedly();


        return START_STICKY;
    }

    private void doSomethingRepeatedly() {
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (verificaConexion(getBaseContext()) && validar) {
                    validar = false;
                    Log.d("MyService", "Si hay");
                    try {
                        dbAdapter.abrir();

                        respuetas = dbAdapter.getFormulariosRespuestas();

                        dbAdapter.cerrar();
                        Log.d("MyService respuetas", respuetas.size() + "");
                        if (respuetas.size() > 0 && respuetas != null) {
                            SharedPreferences pref = getSharedPreferences("ParametrosBasicos", Context.MODE_PRIVATE);
                            String per = pref.getString("ruta", "54.164.174.129:8081");
                            //http://10.200.5.8:8081/
                            String ruta="http://"+per+"/"+HTTP_EVENT;
                            new MyAsyncTask(getBaseContext(),respuetas)
                                    .execute("POST", ruta);

                        } else {
                            validar = true;
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                        validar = true;
                    }

                } else {
                    Log.d("MyService", "No hay");

                }
            }

        }, 0, UPDATE_INTERVAL);
    }



    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        Toast.makeText(getBaseContext(), "Servicio Detenido",
                Toast.LENGTH_SHORT).show();
    }

    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // No s�lo wifi, tambi�n GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        // este bucle deber�a no ser tan �apa
        for (int i = 0; i < redes.length; i++) {
            // �Tenemos conexi�n? ponemos a true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }

    class MyAsyncTask extends AsyncTask<String,Void,String> {

       // private ProgressDialog progressDialog;
        private Context context;
        private  List<EncuestaRespuesta> encRes;

        //String mensaje="Descargando Formularios...";


        /**Constructor de clase */
        public MyAsyncTask(Context context,List<EncuestaRespuesta> eRes) {
            this.context = context;
            encRes=eRes;
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
            int contadorOk= 0;

            MyRestFulGP myRFGP = new MyRestFulGP();
            for (int i = 0; i < encRes.size(); i++) {
                EncuestaRespuesta itemRes = new EncuestaRespuesta(encRes.get(i).getIdEncuesta(),encRes.get(i).getIdUsuario(),encRes.get(i).getConsecutivo(),encRes.get(i).getFecha());
                ArrayList<PreguntaRespuesta> itemPre = new ArrayList<PreguntaRespuesta>();
                for(int j=0;j<encRes.get(i).getRespuesta().size();j++)
                {
                    PreguntaRespuesta enResI= new PreguntaRespuesta();
                    enResI.setIdPregunta(encRes.get(i).getRespuesta().get(j).getIdPregunta());
                    enResI.setItem(encRes.get(i).getRespuesta().get(j).getItem());
                    enResI.setRespuesta(encRes.get(i).getRespuesta().get(j).getRespuesta());
                    enResI.setOpcion(encRes.get(i).getRespuesta().get(j).getOpcion());
                    itemPre.add(enResI);

                }
                itemRes.setRespuesta(itemPre);
                ArrayList<ParametrosRespuesta> itemParam = new ArrayList<ParametrosRespuesta>();
                for(int j=0;j<encRes.get(i).getParametros().size();j++)
                {
                    ParametrosRespuesta enResIP= new ParametrosRespuesta();
                    enResIP.setIdParametro(encRes.get(i).getParametros().get(j).getIdParametro());
                    enResIP.setValor(encRes.get(i).getParametros().get(j).getValor());

                    itemParam.add(enResIP);

                }
                itemRes.setParametros(itemParam);
                Log.d("MyService respueta:" + (i + 1), itemRes.getIdEncuesta() + "-" + itemRes.getConsecutivo());

                List<NameValuePair> pares = new ArrayList<NameValuePair>();
                String stringJson= ConvertirJson(itemRes);
                pares.add(new BasicNameValuePair("insert",stringJson));
                Log.i("Enviar: ", stringJson);

                try {
                    if( params[0].equals("POST"))
                    {
                        String jsonResult = myRFGP.addEventPost(pares,params[1]);
                        JSONObject object;
                        if(jsonResult.substring(1,1).equals('{'))
                        {
                            object = new JSONObject(jsonResult);
                        }else
                        {
                            object = new JSONObject(jsonResult.substring(jsonResult.indexOf('{')));
                        }

                        Log.i("jsonResult",jsonResult);
                        if( object.getString("Result").equals("200"))
                        {
                            try {
                                dbAdapter.abrir();
                                dbAdapter.Eliminar("Parametro_Encuesta_Respuesta", "IdEncuesta = '" + itemRes.getIdEncuesta() + "' and IdUsuario= " + itemRes.getIdUsuario() + "  and consecutivo = " + itemRes.getConsecutivo());
                                dbAdapter.Eliminar("Pregunta_Respuesta", "IdEncuesta = '"+itemRes.getIdEncuesta() +"' and IdUsuario= "+itemRes.getIdUsuario()+"  and consecutivo = "+itemRes.getConsecutivo());
                                dbAdapter.Eliminar("Encuesta_Repuesta", "IdEncuesta = '" + itemRes.getIdEncuesta() + "' and IdUsuario= " + itemRes.getIdUsuario() + "  and consecutivo = " + itemRes.getConsecutivo());
                                Log.i("elimando", "encuesta : "+itemRes.getIdEncuesta()+" - "+itemRes.getConsecutivo()+" eliminada!");
                                dbAdapter.cerrar();
                                SharedPreferences pref = getSharedPreferences("ParametrosBasicos", Context.MODE_PRIVATE);
                                int formsend = pref.getInt("formularios_enviados", 0);
                                formsend=formsend+1;
                                SharedPreferences.Editor edit = pref.edit();
                                edit.putInt("formularios_enviados", formsend);
                                edit.commit();
                                contadorOk++;
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }




                        }




                    }

                } catch (ClientProtocolException e) {
                    e.printStackTrace();

                } catch (URISyntaxException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();

                }catch (JSONException e) {
                    e.printStackTrace();

                }

            }



            return "Terminado "+contadorOk+" formualario arriba";
        }

        /**
         * Antes de comenzar la tarea muestra el progressDialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progressDialog = ProgressDialog.show(
                    context, "Por favor espere", mensaje);*/
        }

        /**
         * Cuando se termina de ejecutar, cierra el progressDialog y retorna el resultado a la interfaz
         * **/
        @Override
        protected void onPostExecute(String resul) {
          //  progressDialog.dismiss();
            // textView.setText(resul);
            if (resul.length() > 0) {
               /* Toast toast1 =
                        Toast.makeText(FormulariosActivos.this,
                                resul, Toast.LENGTH_SHORT);

                toast1.show();*/

                Log.i("Resultado",resul);

                validar=true;

            }
        }
    }
}
