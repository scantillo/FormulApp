package reportsas.com.formulapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.sql.SQLException;

import mobi.pdf417.demo.R;
import reportsas.com.basedatos.FormDbAdapter;

public class FormulariosEnviados extends ActionBarActivity {
    int enviados,guardados, usuario;
    TextView txtFEn, txtFGuarda, txtFTpotal;
    private FormDbAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formularios_enviados);
        txtFEn=(TextView)findViewById(R.id.txtFenvuados);
        txtFGuarda=(TextView)findViewById(R.id.txtFAlmacenados);
        txtFTpotal=(TextView)findViewById(R.id.txtFTotal);
        SharedPreferences pref = getSharedPreferences("ParametrosBasicos", Context.MODE_PRIVATE);
        enviados=  pref.getInt("formularios_enviados", 0);
        usuario=  pref.getInt("userid",0);
        dbAdapter = new FormDbAdapter(this);
        try {
            dbAdapter.abrir();
            guardados= dbAdapter.getFormAlmacenados(usuario);
        } catch (SQLException e) {
            e.printStackTrace();
        }

            txtFEn.setText("Enviados         : "+enviados);
        txtFGuarda.setText("Almacenados : "+guardados);
        txtFTpotal.setText("Total                : "+(enviados+guardados));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formularios_enviados, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
