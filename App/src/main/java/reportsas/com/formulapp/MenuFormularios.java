package reportsas.com.formulapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import mobi.pdf417.demo.R;
import reportsas.com.reportsas.com.utilidades.DataOpciones;
import modelo.Opcion;
import reportsas.com.basedatos.FormsDataStore;

public class MenuFormularios extends ActionBarActivity implements AdapterView.OnItemClickListener {
    TextView txtTag;
    FormsDataStore DataSource;
    ListView lista;
    ArrayAdapter<Opcion> adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_formularios);

        SharedPreferences prefe = getSharedPreferences("ParametrosBasicos", Context.MODE_PRIVATE);
        String usename = prefe.getString("username","");
                DataSource = new FormsDataStore(this);
        SQLiteDatabase db = DataSource.openHelper.getWritableDatabase();
        lista = (ListView)findViewById(R.id.listaw);


        //Inicializar el adaptador con la fuente de datos
        adaptador = new OpcionAdadpter(
                this,
                DataOpciones.OPCIONES);

        //Relacionando la lista con el adaptador
        lista.setAdapter(adaptador);
        lista.setOnItemClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_formularios, menu);
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
    public  void clickBotonEnviar(View v) {

            }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Opcion OpcionActual = (Opcion)adaptador.getItem(position);
        switch (OpcionActual.getTitulo()) {

            case "Formularios":
                Intent i2;
                i2 = new Intent(MenuFormularios.this, FormulariosActivos.class);
                startActivity(i2);

                break;
            case "Salir":
                SharedPreferences pref = getSharedPreferences("ParametrosBasicos", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putInt("userid",0);
                edit.putInt("formularios_enviados",0);
                edit.putString("username","null");
                edit.putBoolean("acceso",false);
                edit.commit();
                Intent i;

                i = new Intent(this, login.class);
                startActivity(i);
                finish();
                break;
            case "Formularios Enviados":

                i = new Intent(MenuFormularios.this, FormulariosEnviados.class);
                startActivity(i);


                break;


            default:

                break;

        }


    }

}

