package reportsas.com.reportsas.com.utilidades;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

import mobi.pdf417.demo.R;
import modelo.Opcion;

/**
 * Created by rytscc on 03/05/2015.
 */
public class DataOpciones {

   public  static List OPCIONES = new ArrayList<Opcion>();


    static{

        OPCIONES.add(new Opcion("Formularios", R.drawable.formulario));
        OPCIONES.add(new Opcion("Formularios Enviados",R.drawable.send));
        OPCIONES.add(new Opcion("Salir",R.drawable.salir));



    }
    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // No sólo wifi, también GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        // este bucle debería no ser tan ñapa
        for (int i = 0; i < redes.length; i++) {
            // ¿Tenemos conexión? ponemos a true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }

}
