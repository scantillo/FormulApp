package reportsas.com.formulapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import mobi.pdf417.demo.R;
import reportsas.com.reportsas.com.utilidades.MyRestFulGP;

import static android.view.View.VISIBLE;

public class login extends ActionBarActivity {
    EditText edtUser,edPass;
    TextView txtResul;
    String usuario;
    public String tag_login="login";
    public String tag_pass="1";
    public String tag_tipo="2";
    private final String HTTP_EVENT="FormWS.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtUser= (EditText)findViewById(R.id.txtuser);
        edPass= (EditText)findViewById(R.id.password);
        txtResul= (TextView)findViewById(R.id.txtResult);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            // create a Dialog component

            final Dialog dialog = new Dialog(this);


            //tell the Dialog to use the dialog.xml as it's layout description

            dialog.setContentView(R.layout.dialogservidor);

            dialog.setTitle("Ruta al Servidor");

            final EditText txt = (EditText) dialog.findViewById(R.id.editcampoT);
            final EditText txtRuta = (EditText) dialog.findViewById(R.id.editTextRuta);
            final TextView txtR=(TextView)dialog.findViewById(R.id.txtRuta);

            final Button dialogButton = (Button) dialog.findViewById(R.id.OKG);
            Button dialogButton2 = (Button) dialog.findViewById(R.id.CANCELG);
            final Button dialogButton3 = (Button) dialog.findViewById(R.id.SaveRuta);

            dialogButton2.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {

                    dialog.dismiss();

                }

            });
            dialogButton3.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {
                    SharedPreferences pref = getSharedPreferences("ParametrosBasicos", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();

                    edit.putString("ruta", txtRuta.getText().toString());

                    edit.commit();

                    dialog.dismiss();

                }

            });
            dialogButton.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {
                        String ress=txt.getText().toString();
                        if(ress.equals("1993"))
                        {


                            txtRuta.setEnabled(true);
                            dialogButton3.setEnabled(true);
                            SharedPreferences pref = getSharedPreferences("ParametrosBasicos", Context.MODE_PRIVATE);
                            String per = pref.getString("ruta", "54.164.174.129:8081");
                            txtRuta.setText(per);




                        }





                }

            });

            dialog.show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public  void clickBotonEnviar(View v)
    {

      /*  SharedPreferences pref = getSharedPreferences("ParametrosBasicos", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("userid", 3);
        edit.putString("username","APATINO");
        edit.putBoolean("acceso", true);
        edit.commit();
        Intent i;
        i = new Intent(login.this, MenuFormularios.class);
        startActivity(i);
        finish();*/
        SharedPreferences pref = getSharedPreferences("ParametrosBasicos", Context.MODE_PRIVATE);
        String per = pref.getString("ruta", "54.164.174.129:8081");

        String ruta="http://"+per+"/"+HTTP_EVENT;
        usuario = edtUser.getText().toString();
        new MyAsyncTask(login.this, txtResul)
                .execute("GET", usuario, edPass.getText().toString(), ruta);


    }


    class MyAsyncTask extends AsyncTask<String,Void,String> {

        private ProgressDialog progressDialog;
        private Context context;
        TextView textView;

        /**Constructor de clase */
        public MyAsyncTask(Context context, TextView textView) {
            this.context = context;
            this.textView = textView;
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
            if( params[1].length()==0 )
            {
                return "Se debe ingresar el Nombre de Usuario";
            }
            else {

                if( params[2].length()==0)
                {
                    return "Se debe ingresar la Contraseña";
                }
                else{
                MyRestFulGP myRestFulGP = new MyRestFulGP();
                List<NameValuePair> parames = new ArrayList<NameValuePair>();
                parames.add(new BasicNameValuePair("user", params[1]));
                parames.add(new BasicNameValuePair("pass",params[2]));
                parames.add(new BasicNameValuePair("tag", tag_login));
                try {
                    if (params[0].equals("GET")) {
                        String jsonResult = null;

                        jsonResult = myRestFulGP.addEventGet(parames, params[3]);

                        JSONObject object;
                        if(jsonResult.substring(1,1).equals('{'))
                        {
                            object = new JSONObject(jsonResult);
                        }else
                        {
                            object = new JSONObject(jsonResult.substring(jsonResult.indexOf('{')));
                        }

                        Log.i("jsonResult", jsonResult);
                        if (object.getInt("success") == 1) {
                            SharedPreferences pref = getSharedPreferences("ParametrosBasicos", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = pref.edit();
                            String idu=object.getString("uid");
                            edit.putInt("userid", Integer.parseInt(idu));
                            edit.putInt("formularios_enviados", 0);
                            edit.putString("username", object.getString("uname"));
                            edit.putBoolean("acceso", true);
                            edit.commit();
                            Intent i;
                            i = new Intent(login.this, MenuFormularios.class);
                            startActivity(i);
                            finish();

                            return "";
                        } else {
                            return "Error: (" + object.getInt("error") + ") " + object.getString("error_msg");
                        }


                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    return "Error: Protocolo " + e.getMessage();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    return "Error: Conexión a Internet" + e.getMessage();
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "Error: Cadena JSON " + e.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error General: Se ha presentado un error al conectarse al servidor" ;
                }
              }
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
                    context, "Por favor espere", "Procesando...");
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
                        Toast.makeText(login.this,
                                resul, Toast.LENGTH_SHORT);

                toast1.show();
            }
        }
    }
}
