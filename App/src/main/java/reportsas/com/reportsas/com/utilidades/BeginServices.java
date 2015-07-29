package reportsas.com.reportsas.com.utilidades;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by rytscc on 06/07/2015.
 */
public class BeginServices extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {

      if(!isMyServiceRunning(ServicioFormulApp.class, context))
        {
            /*  Intent servicio = new Intent();
            servicio.setAction("MiServicio");
            context.startService(servicio);*/
            context.startService(new Intent(context,
                                 ServicioFormulApp.class));
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass, Context ths) {
        ActivityManager manager = (ActivityManager) ths.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}