package reportsas.com.formulapp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import mobi.pdf417.demo.R;

public class dialogUbicacion extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LatLng localizacion;
    String coor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_ubicacion);
        Bundle bundle = getIntent().getExtras();
                   coor = bundle.getString("location");
            if (coor.equals("Posicion Desconocida")) {

                localizacion = new LatLng(0, 0);
            } else {

                double lat = Double.parseDouble(coor.substring(0, coor.indexOf(":")));
                double lon = Double.parseDouble(coor.substring(coor.indexOf(":") + 1, coor.length()));
                localizacion = new LatLng(lat, lon);
            }
        setUpMapIfNeeded();
    }
    @Override
    public void finish()
    {

        Intent i = new Intent(this, Formulario.class);
        i.putExtra("INPUT", "OK");
        setResult(RESULT_OK, i);
        super.finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapr))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
            else {
                return;
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        String descripcion="", direcc="";
        if (localizacion.latitude != 0.0 && localizacion.longitude != 0.0) {
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;

            try {
                addresses = gcd.getFromLocation(localizacion.latitude, localizacion.longitude, 1);
                if (!addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    if (addresses.size() > 0) {
                        descripcion = addresses.get(0).getLocality();
                        direcc = address.getAddressLine(0);


                    }

                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        mMap.addMarker(new MarkerOptions().position(localizacion).title("Ciudad : "+descripcion).snippet(direcc));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacion, 20));

      //  animate the zoom process
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);
    }

}
