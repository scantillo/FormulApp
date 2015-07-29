package reportsas.com.formulapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import mobi.pdf417.demo.R;
import reportsas.com.reportsas.com.utilidades.ServicioFormulApp;

import java.util.Timer;
import java.util.TimerTask;


public class SplashScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Timer t = new Timer();

        SharedPreferences pref = getSharedPreferences("ParametrosBasicos", Context.MODE_PRIVATE);
        boolean per = pref.getBoolean("acceso",false);
        if (per) {

            t.schedule(new TimerTask() {
                @Override
                public void run() {


                    if (!isMyServiceRunning(ServicioFormulApp.class) )
                    {
                        /*Intent servicio = new Intent();
                        servicio.setAction("MiServicio");
                        startService(servicio);*/
                         startService(new Intent(SplashScreen.this,
                                 ServicioFormulApp.class));

                    }
                    Intent i;
                    i = new Intent(SplashScreen.this, MenuFormularios.class);
                    startActivity(i);
                    finish();


                }
            }, 500);
        }else
        {

            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent i;
                    i = new Intent(SplashScreen.this,login.class);
                    startActivity(i);
                    finish();

                }
            },500);
        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
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
