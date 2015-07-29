package reportsas.com.formulapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.microblink.activity.Pdf417ScanActivity;
import com.microblink.recognizers.barcode.bardecoder.BarDecoderRecognizerSettings;
import com.microblink.recognizers.barcode.bardecoder.BarDecoderScanResult;
import com.microblink.recognizers.barcode.pdf417.Pdf417RecognizerSettings;
import com.microblink.recognizers.barcode.pdf417.Pdf417ScanResult;
import com.microblink.recognizers.barcode.usdl.USDLRecognizerSettings;
import com.microblink.recognizers.barcode.usdl.USDLScanResult;
import com.microblink.recognizers.barcode.zxing.ZXingRecognizerSettings;
import com.microblink.recognizers.barcode.zxing.ZXingScanResult;
import com.microblink.recognizers.settings.GenericRecognizerSettings;
import com.microblink.recognizers.settings.RecognizerSettings;
import com.microblink.results.barcode.BarcodeDetailedData;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import mobi.pdf417.demo.R;
import modelo.Encuesta;
import modelo.EncuestaParametro;
import modelo.EncuestaRespuesta;
import modelo.Encuestado;
import modelo.ObjetoSpinner;
import modelo.OpcionForm;
import modelo.ParametrosRespuesta;
import modelo.Pregunta;
import modelo.PreguntaRespuesta;
import modelo.Valor;
import reportsas.com.basedatos.FormDbAdapter;
import reportsas.com.reportsas.com.utilidades.DataOpciones;
import reportsas.com.reportsas.com.utilidades.MyRestFulGP;

public class Formulario extends ActionBarActivity implements LocationListener {

    private static final int MY_REQUEST_CODE = 1337;
    private FormDbAdapter dbAdapter;
    private Cursor cursor;
    TextView txtTitulo, prueba;
    TextView txtDescrip;
    ImageButton ibScan, ibCam, ibGPS;
    private int  rePaintMenu = 0;
    private ViewGroup layout;
    Encuesta encuesta;
    public String idFormulario;
    String DataR,Infocadena;
    ParametrosRespuesta parametroGPS = null;
    ParametrosRespuesta parametroCam = null;
    ParametrosRespuesta parametroScan = null;
    EncuestaRespuesta respuestaEncuesta;
    private LocationManager manejador;
    private String proveedor;
    private final String HTTP_EVENT="apirest.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_formulario);
        Bundle bundle = getIntent().getExtras();
        idFormulario=bundle.getString("formulario");
        setTitle("Formulario "+idFormulario);
        dbAdapter = new FormDbAdapter(this);
        txtTitulo=(TextView)findViewById(R.id.txtTitulo);
        txtDescrip=(TextView)findViewById(R.id.txtDescripcion);
        prueba=(TextView)findViewById(R.id.prueba);
        layout = (ViewGroup) findViewById(R.id.content);
        try {
            dbAdapter.abrir();

            encuesta = dbAdapter.formFind(this,idFormulario);
            rePaintMenu=1;
            invalidateOptionsMenu();
            if (encuesta != null)
            {
                txtTitulo.setText(encuesta.getTitulo());
                txtDescrip.setText(encuesta.getDescripcion());
            }
            for(int i=0 ;i< encuesta.getPreguntas().size();i++ ) {
                addPreguntas(encuesta.getPreguntas().get(i));

            }



            dbAdapter.cerrar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void capturarLocalizacion(Location localizacion)
    {
        if (!(localizacion == null)) {
            String pos =String.valueOf(localizacion.getLatitude())+":"+String.valueOf(localizacion.getLongitude());
            parametroGPS.setValor(pos);
        }else
        {
            parametroGPS.setValor("Posicion Desconocida");
        }
    }
    public void addPreguntas(Pregunta pregunta)
    {
        LayoutInflater inflater = LayoutInflater.from(this);


        LinearLayout relativeLayout = obtenerLayout(inflater, pregunta);


         layout.addView(relativeLayout);




    }
    public LinearLayout obtenerLayout(LayoutInflater infla, Pregunta preg)
    {
        int id ;
        int tipo_pregunta = preg.getTipoPregunta();
        LinearLayout pregunta;
        TextView textView;
        TextView textAyuda;
        switch (tipo_pregunta) {
            case 1:
                id = R.layout.pregunta_texto;
                pregunta =(LinearLayout) infla.inflate(id, null, false);

                textView = (TextView) pregunta.findViewById(R.id.TituloPregunta);
                textAyuda = (TextView) pregunta.findViewById(R.id.texto_ayuda);
                textView.setText(preg.getOrden()+". "+preg.getTitulo());
                textAyuda.setText(preg.getTxtAyuda());
            break;
            case 2:
                id = R.layout.pregunta_multitexto;
                pregunta =(LinearLayout) infla.inflate(id, null, false);

                textView = (TextView) pregunta.findViewById(R.id.mtxtTritulo);
                textAyuda = (TextView) pregunta.findViewById(R.id.mtxtAyuda);
                textView.setText(preg.getOrden()+". "+preg.getTitulo());
                textAyuda.setText(preg.getTxtAyuda());

            break;
            case 3:
                id = R.layout.pregunta_seleccion;
                pregunta =(LinearLayout) infla.inflate(id, null, false);

                textView = (TextView) pregunta.findViewById(R.id.TituloSeleccion);
                textAyuda = (TextView) pregunta.findViewById(R.id.texto_ayuda_seleccion);
                textView.setText(preg.getOrden()+". "+preg.getTitulo());
                textAyuda.setText(preg.getTxtAyuda());
                RadioGroup rg =(RadioGroup)pregunta.findViewById(R.id.opcionesUnica);
                ArrayList<OpcionForm> opciones = preg.getOpciones();
                final ArrayList<RadioButton>  rb = new ArrayList<RadioButton>();


                for(int i=0; i<opciones.size(); i++)
                {
                    OpcionForm opcion =opciones.get(i);
                    rb.add(new RadioButton(this));
                    rg.addView(rb.get(i));
                    rb.get(i).setText(opcion.getEtInicial());


                }
                final TextView respt = (TextView) pregunta.findViewById(R.id.respuestaGruop);
                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int radioButtonID = group.getCheckedRadioButtonId();
                        RadioButton radioButton =(RadioButton)group.findViewById(radioButtonID);
                        respt.setText(radioButton.getText());
                    }
                });

            break;
            case 4:
                id = R.layout.pregunta_multiple;
                pregunta =(LinearLayout) infla.inflate(id, null, false);

                textView = (TextView) pregunta.findViewById(R.id.TituloMultiple);
                textAyuda = (TextView) pregunta.findViewById(R.id.texto_ayuda_mltiple);
                textView.setText(preg.getOrden()+". "+preg.getTitulo());
                textAyuda.setText(preg.getTxtAyuda());
                ArrayList<OpcionForm> opciones2 = preg.getOpciones();
                final EditText ediOtros = new EditText(this);
                ArrayList<CheckBox> cb = new  ArrayList<CheckBox>();

                for(int i=0; i<opciones2.size(); i++)
                {
                    OpcionForm opcion =opciones2.get(i);
                    cb.add(new CheckBox(this));
                    pregunta.addView(cb.get(i));
                    cb.get(i).setText(opcion.getEtInicial());
                    if (opcion.getEditble().equals("S"))
                    {

                        ediOtros.setEnabled(false);
                        ediOtros.setId(R.id.edtTexto);
                        pregunta.addView(ediOtros);
                        cb.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    ediOtros.setEnabled(true);
                                }
                                else
                                {
                                    ediOtros.setText("");
                                    ediOtros.setEnabled(false);
                                }
                            }
                        });
                    }

                }
                TextView spacio = new TextView(this);
                spacio.setText("        ");
                spacio.setVisibility(View.INVISIBLE);
                pregunta.addView(spacio);
            break;
            case 5:
                id = R.layout.pregunta_escala;
                pregunta =(LinearLayout) infla.inflate(id, null, false);

                textView = (TextView) pregunta.findViewById(R.id.TituloEscala);
                textAyuda = (TextView) pregunta.findViewById(R.id.texto_ayuda_escala);
                textView.setText(preg.getOrden()+". "+preg.getTitulo());
                textAyuda.setText(preg.getTxtAyuda());
                textView.setText(preg.getOrden()+". "+preg.getTitulo());

                TextView etInicial = (TextView) pregunta.findViewById(R.id.etInicial);
                TextView etFinal = (TextView) pregunta.findViewById(R.id.etFinal);
                OpcionForm opci=preg.getOpciones().get(0);
                etInicial.setText(opci.getEtInicial());
                etFinal.setText(opci.getEtFinal());
                final TextView respEscala = (TextView)pregunta.findViewById(R.id.seleEscala);
                RatingBar rtBar = (RatingBar)pregunta.findViewById(R.id.escala);
                rtBar.setNumStars(Integer.parseInt(opci.getValores().get(0).getDescripcion()));
                rtBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        respEscala.setText(""+Math.round(rating));
                    }
                });

                break;
            case 6:
                id = R.layout.pregunta_lista;
                pregunta =(LinearLayout) infla.inflate(id, null, false);

                textView = (TextView) pregunta.findViewById(R.id.TituloLista);
                textAyuda = (TextView) pregunta.findViewById(R.id.texto_ayuda_lista);
                textView.setText(preg.getOrden()+". "+preg.getTitulo());
                textAyuda.setText(preg.getTxtAyuda());
                ArrayList<OpcionForm> opciones3 = preg.getOpciones();
               //Creamos la lista
                LinkedList<ObjetoSpinner> opcn = new LinkedList<ObjetoSpinner>();
                //La poblamos con los ejemplos
                for(int i=0 ; i < opciones3.size();i++ )
                {
                    opcn.add(new ObjetoSpinner(opciones3.get(i).getIdOpcion(),opciones3.get(i).getEtInicial()));
                }


                //Creamos el adaptador*/
                Spinner listad = (Spinner)pregunta.findViewById(R.id.opcionesListado);
                ArrayAdapter<ObjetoSpinner> spinner_adapter = new ArrayAdapter<ObjetoSpinner>(this, android.R.layout.simple_spinner_item, opcn);
                //Añadimos el layout para el menú y se lo damos al spinner
                spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                listad.setAdapter(spinner_adapter);

            break;
            case 7:
                id = R.layout.pregunta_tabla;
                pregunta =(LinearLayout) infla.inflate(id, null, false);

                textView = (TextView) pregunta.findViewById(R.id.TituloTabla);
                textAyuda = (TextView) pregunta.findViewById(R.id.texto_ayuda_tabla);
                textView.setText(preg.getOrden()+". "+preg.getTitulo());
                textAyuda.setText(preg.getTxtAyuda());
                TableLayout tba = (TableLayout)pregunta.findViewById(R.id.tablaOpciones);
                ArrayList<OpcionForm> opciones4 = preg.getOpciones();
                ArrayList<RadioButton> radiosbotonoes = new ArrayList<RadioButton>();
                for(int i=0 ;i < opciones4.size(); i++)
                {
                    TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.row_pregunta_tabla, null);
                    RadioGroup tg_valores = (RadioGroup)row.findViewById(R.id.valoresRow);

                    final ArrayList<RadioButton> valoOpc = new ArrayList<RadioButton>();
                    ArrayList<Valor> valoresT = opciones4.get(i).getValores();
                    for(int k=0; k<valoresT.size();k++) {
                     RadioButton rb_nuevo = new RadioButton(this);
                        rb_nuevo.setText(valoresT.get(k).getDescripcion());
                        tg_valores.addView(rb_nuevo);
                        valoOpc.add(rb_nuevo);
                    }

                    ((TextView) row.findViewById(R.id.textoRow)).setText(opciones4.get(i).getEtInicial());
                    tba.addView(row);
                }
                TextView espacio = new TextView(this);
                espacio.setText("        ");
                pregunta.addView(espacio);
                break;
            case 8:
                id = R.layout.pregunta_fecha;
                pregunta =(LinearLayout) infla.inflate(id, null, false);

                textView = (TextView) pregunta.findViewById(R.id.TituloFecha);
                textAyuda = (TextView) pregunta.findViewById(R.id.texto_ayuda_fecha);
                textView.setText(preg.getOrden()+". "+preg.getTitulo());
                textAyuda.setText(preg.getTxtAyuda());

            break;
            case 9:
                id = R.layout.pregunta_hora;
                pregunta =(LinearLayout) infla.inflate(id, null, false);

                textView = (TextView) pregunta.findViewById(R.id.TituloHora);
                textAyuda = (TextView) pregunta.findViewById(R.id.texto_ayuda_hora);
                textView.setText(preg.getOrden()+". "+preg.getTitulo());
                textAyuda.setText(preg.getTxtAyuda());


            break;
            default:
                id = R.layout.pregunta_multiple;
                pregunta =(LinearLayout) infla.inflate(id, null, false);

                textView = (TextView) pregunta.findViewById(R.id.TituloMultiple);
                textAyuda = (TextView) pregunta.findViewById(R.id.texto_ayuda_mltiple);
                textView.setText(preg.getOrden()+". "+preg.getTitulo());
                textAyuda.setText(preg.getTxtAyuda());
            break;
        }


        return pregunta;
    }
    public static String getDatePhone(String Format)

    {

        Calendar cal = new GregorianCalendar();

        Date date = cal.getTime();

        SimpleDateFormat df = new SimpleDateFormat(Format);

        String formatteDate = df.format(date);

        return formatteDate;

    }

    public void CapturaF()
    {
        if ( parametroCam == null) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 1);

        }else
        {

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_imagen);
            dialog.setTitle("Captura de Formulario");
            byte[] decodedByte = Base64.decode(parametroCam.getValor(), 0);

            ImageView imageview = (ImageView) dialog.findViewById(R.id.ImaVcaptura);
            Button Button1 = (Button)    dialog.findViewById(R.id.NuevaToma);
            Button Button2 = (Button)    dialog.findViewById(R.id.btn_cerrar);


            imageview.setImageBitmap(BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length));
            Button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                    dialog.dismiss();
                }
            });

            Button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });

            dialog.show();




        }

    }


    public void CapturaL()
    {
        if ( parametroGPS == null) {

            parametroGPS = new ParametrosRespuesta(1);
            manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criterio = new Criteria();
            criterio.setCostAllowed(false);
            criterio.setAltitudeRequired(false);
            criterio.setAccuracy(Criteria.ACCURACY_FINE);
            proveedor = manejador.getBestProvider(criterio, true);
            Location localizacion = manejador.getLastKnownLocation(proveedor);
            capturarLocalizacion(localizacion);


        }

        Intent intentoDlgUno = new Intent(this, dialogUbicacion.class);
        intentoDlgUno.putExtra("location", parametroGPS.getValor());
        startActivityForResult(intentoDlgUno, 0);



    }

    public void btnScan_click() {
        if ( parametroScan == null) {
            callScaner();
        }
        else
        {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_scaner);
            dialog.setTitle("Lectura Codigo  de Barras");


            TextView textoS = (TextView) dialog.findViewById(R.id.tvScan);
            Button Button1 = (Button)    dialog.findViewById(R.id.NuevoScan);
            Button Button2 = (Button)    dialog.findViewById(R.id.btn_close);

            textoS.setText(parametroScan.getValor());

            Button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callScaner();
                    dialog.dismiss();
                }
            });

            Button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });

            dialog.show();
        }

    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (data != null) {
                if (data.hasExtra("data")) {

                    Bitmap photobmp = (Bitmap) data.getParcelableExtra("data");


                   // iv.setImageBitmap(photobmp);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photobmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                    if(parametroCam == null) {
                        parametroCam = new ParametrosRespuesta(2);
                        }


                    parametroCam.setValor(encodedImage);

                   // prueba.setText(encodedImage);
                //    new MyAsyncTask(Formulario.this)
                  //          .execute("POST",encodedImage, HTTP_EVENT);
                }

            }
        }

        if (requestCode == MY_REQUEST_CODE && resultCode == Pdf417ScanActivity.RESULT_OK) {
            // First, obtain scan results array. If scan was successful, array will contain at least one element.
            // Multiple element may be in array if multiple scan results from single image were allowed in settings.

            Parcelable[] resultArray = data.getParcelableArrayExtra(Pdf417ScanActivity.EXTRAS_RECOGNITION_RESULT_LIST);


            StringBuilder sb = new StringBuilder();

            for(Parcelable p : resultArray) {
                if(p instanceof Pdf417ScanResult) { // check if scan result is result of Pdf417 recognizer
                    Pdf417ScanResult result = (Pdf417ScanResult) p;
                    // getStringData getter will return the string version of barcode contents
                    String barcodeData = result.getStringData();

                    // isUncertain getter will tell you if scanned barcode contains some uncertainties
                    boolean uncertainData = result.isUncertain();
                    // getRawData getter will return the raw data information object of barcode contents
                    BarcodeDetailedData rawData = result.getRawData();
                    // BarcodeDetailedData contains information about barcode's binary layout, if you
                    // are only interested in raw bytes, you can obtain them with getAllData getter
                    byte[] rawDataBuffer = rawData.getAllData();
                    DataR=rawData.toString();


                    String[] arrayElements= DataR.split("Element #");
                    String Nombre="",Apellido="", cedula="",fecha="", dia, mes, ano;
                    if(arrayElements.length >= 7 )
                    {
                        String[] auxliarArray=arrayElements[7].split("decoded\\):");

                        String strDatos =auxliarArray[1];
                        char[] ca = strDatos.toCharArray();
                        for(int i=0; i<strDatos.length();i++)
                        {
                            if(Character.isLetter(ca[i]))       //Si es letra
                                Apellido+=ca[i];    //Salto de línea e imprimimos el carácter
                            else                                //Si no es letra
                                cedula+=ca[i];           //Imprimimos el carácter
                        }
                        Apellido=Apellido.trim();
                        cedula=(cedula.replaceAll("\n", "")).trim();
                        if (cedula.length()== 0 )
                        {
                            auxliarArray=arrayElements[5].split("decoded\\):");
                            strDatos =auxliarArray[1];
                            ca = strDatos.toCharArray();
                            Apellido="";
                            for(int i=0; i<strDatos.length();i++)
                            {
                                if(Character.isLetter(ca[i]))       //Si es letra
                                    Apellido+=ca[i];    //Salto de línea e imprimimos el carácter
                                else                                //Si no es letra
                                    cedula+=ca[i];           //Imprimimos el carácter
                            }
                            Apellido=Apellido.trim();
                            cedula=(cedula.replaceAll("\n", "")).trim();
                            cedula=cedula.substring(cedula.length() - 10,cedula.length() );
                            cedula = eliminarceros(cedula);
                            auxliarArray=arrayElements[9].split("decoded\\):");
                            Nombre=(auxliarArray[1].replaceAll("\n", "")).trim();

                        }else
                        {

                            cedula = eliminarceros(cedula);
                            auxliarArray=arrayElements[11].split("decoded\\):");
                            Nombre=(auxliarArray[1].replaceAll("\n", "")).trim();
                        }

                        auxliarArray=result.getStringData().toString().split(Nombre);
                        strDatos =auxliarArray[1];
                        ca = strDatos.toCharArray();
                        Boolean result_ciclo = true;
                        int i=0;
                        while(result_ciclo)
                        {
                            if(Character.isDigit(ca[i])) {
                                fecha += ca[i];
                            }
                            if (fecha.length() >=9) {
                                result_ciclo=false;
                            }
                            i++;
                        }
                        fecha=fecha.substring(1,fecha.length());
                    }else
                    {
                        int puntoI=0;
                        if (barcodeData.indexOf("1F")>0)
                        {
                            puntoI=barcodeData.indexOf("1F");
                        }else if(barcodeData.indexOf("0M")>0)
                        {
                            puntoI=barcodeData.indexOf("0M");
                        }else if(barcodeData.indexOf("0F")>0)
                         {
                            puntoI=barcodeData.indexOf("0F");
                         }else if(barcodeData.indexOf("1M")>0)
                        {
                            puntoI=barcodeData.indexOf("1M");
                        }else
                        {

                        }
                        if(puntoI>0)
                        {
                            String seb = barcodeData.substring(1,puntoI);
                            fecha=barcodeData.substring(puntoI+ 2, puntoI + 10);

                            int posL=0,posE;
                            char[] ca = seb.toCharArray();
                            for(int w = seb.length()-1; w>0;w--)
                            {
                                if(Character.isLetter(ca[w]))
                                {
                                    posL=w;
                                    break;
                                }
                            }
                            seb=seb.substring(1,posL+1);
                            ca = seb.toCharArray();
                            for(int w = seb.length()-1; w>0;w--)
                            {
                                if(Character.isLetter(ca[w]))
                                {
                                    Nombre=ca[w]+Nombre;
                                    posL=w;
                                }else
                                {
                                    break;
                                }
                            }
                            seb=seb.substring(1,posL);
                            ca = seb.toCharArray();
                            for(int w = seb.length()-1; w>0;w--)
                            {
                                if(Character.isDigit(ca[w]))
                                {
                                    posL=w;
                                    break;
                                }
                            }

                            for(int w = posL +1; w<=seb.length();w++)
                            {
                                if(Character.isLetter(ca[w]))
                                {
                                    Apellido+=ca[w];
                                }else
                                {
                                    break;
                                }
                            }

                            cedula=seb.substring(posL - 9, posL+1);
                            cedula = eliminarceros(cedula);
                        }else
                        {
                            fecha="";
                        }
                    }
                    if (fecha.length()==0)
                    {
                        parametroScan=null;

                        Toast toast1 =
                                Toast.makeText(this,
                                        "Los datos de codigo no pudieron ser interpretados!", Toast.LENGTH_SHORT);

                        toast1.show();

                    }else
                    {
                        dia=fecha.substring(6,8);
                        mes=fecha.substring(4,6);
                        ano=fecha.substring(0,4);
                        fecha=dia+"/"+mes+"/"+ano;

                        Infocadena="Nombre: \n"+Nombre+".\nApellido: \n"+Apellido +". \nCedula: \n"+cedula+". \nFecha de Nacimiento: \n"+fecha+".";
                        if (parametroScan==null)
                        {
                            parametroScan= new ParametrosRespuesta(3);

                        }
                        parametroScan.setValor(Infocadena);

                    }

                  //  new MyAsyncTask(Formulario.this)
                    //        .execute("POST",Infocadena, HTTP_EVENT);


                } else if(p instanceof BarDecoderScanResult) { // check if scan result is result of BarDecoder recognizer
                   /* BarDecoderScanResult result = (BarDecoderScanResult) p;
                    // with getBarcodeType you can obtain barcode type enum that tells you the type of decoded barcode
                    BarcodeType type = result.getBarcodeType();
                    // as with PDF417, getStringData will return the string contents of barcode
                    String barcodeData = result.getStringData();
                    if(checkIfDataIsUrlAndCreateIntent(barcodeData)) {
                        return;
                    } else {
                        sb.append(type.name());
                        sb.append(" string data:\n");
                        sb.append(barcodeData);
                        sb.append("\n\n\n");=
                    }*/
                } else if(p instanceof ZXingScanResult) { // check if scan result is result of ZXing recognizer
                   /* ZXingScanResult result= (ZXingScanResult) p;
                    // with getBarcodeType you can obtain barcode type enum that tells you the type of decoded barcode
                    BarcodeType type = result.getBarcodeType();
                    // as with PDF417, getStringData will return the string contents of barcode
                    String barcodeData = result.getStringData();
                    if(checkIfDataIsUrlAndCreateIntent(barcodeData)) {
                        return;
                    } else {
                        sb.append(type.name());
                        sb.append(" string data:\n");
                        sb.append(barcodeData);
                        sb.append("\n\n\n");
                    }*/
                } else if(p instanceof USDLScanResult) { // check if scan result is result of US Driver's Licence recognizer
                    USDLScanResult result = (USDLScanResult) p;

                    // USDLScanResult can contain lots of information extracted from driver's licence
                    // you can obtain information using the getField method with keys defined in
                    // USDLScanResult class

                    String name = result.getField(USDLScanResult.kCustomerFullName);


                    sb.append(result.getTitle());
                    sb.append("\n\n");
                    sb.append(result.toString());
                }
            }


        }
    }

    public String eliminarceros(String cadena)
    {
        boolean salida=true;
        int j=0;
        String resultado=cadena;
        while (salida)
        {
            if (Integer.parseInt(""+resultado.charAt(j))> 0 )
            {
                salida= false;
            }else
            {
                resultado= resultado.substring(1,resultado.length())  ;
            }
        }
        return resultado;
    }

    public void callScaner()
    {
        // Intent for ScanActivity
        Intent intent = new Intent(this, Pdf417ScanActivity.class);

        // If you want sound to be played after the scanning process ends,
        // put here the resource ID of your sound file. (optional)
        intent.putExtra(Pdf417ScanActivity.EXTRAS_BEEP_RESOURCE, R.raw.beep);

        // set the license key (for commercial versions only) - obtain your key at
        // http://pdf417.mobi
        // after setting the correct license key,
        intent.putExtra(Pdf417ScanActivity.EXTRAS_LICENSE_KEY, "R3P2TG6U-O4CBA7TV-JLYGRAKQ-QIQQG2Z2-YLVAIBAE-AQCAIBAE-AQCAIBAE-AQCFKMFM"); // demo license key for package mobi.pdf417.demo
//
        // If you want to open front facing camera, uncomment the following line.
        // Note that front facing cameras do not have autofocus support, so it will not
        // be possible to scan denser and smaller codes.
//        intent.putExtra(Pdf417ScanActivity.EXTRAS_CAMERA_TYPE, (Parcelable)CameraType.CAMERA_FRONTFACE);

        // You need to define array of recognizer settings. There are 4 types of recognizers available
        // in PDF417.mobi SDK.

        // Pdf417RecognizerSettings define the settings for scanning plain PDF417 barcodes.
        Pdf417RecognizerSettings pdf417RecognizerSettings = new Pdf417RecognizerSettings();
        // Set this to true to scan barcodes which don't have quiet zone (white area) around it
        // Use only if necessary because it drastically slows down the recognition process
        pdf417RecognizerSettings.setNullQuietZoneAllowed(true);
        // Set this to true to scan even barcode not compliant with standards
        // For example, malformed PDF417 barcodes which were incorrectly encoded
        // Use only if necessary because it slows down the recognition process
//        pdf417RecognizerSettings.setUncertainScanning(true);

        // BarDecoderRecognizerSettings define settings for scanning 1D barcodes with algorithms
        // implemented by Microblink team.
        BarDecoderRecognizerSettings oneDimensionalRecognizerSettings = new BarDecoderRecognizerSettings();
        // set this to true to enable scanning of Code 39 1D barcodes
        oneDimensionalRecognizerSettings.setScanCode39(true);
        // set this to true to enable scanning of Code 128 1D barcodes
        oneDimensionalRecognizerSettings.setScanCode128(true);
        // set this to true to use heavier algorithms for scanning 1D barcodes
        // those algorithms are slower, but can scan lower resolution barcodes
//        oneDimensionalRecognizerSettings.setTryHarder(true);

        // USDLRecognizerSettings define settings for scanning US Driver's Licence barcodes
        // options available in that settings are similar to those in Pdf417RecognizerSettings
        // if license key does not allow scanning of US Driver's License, this settings will
        // be thrown out from settings array and error will be logged to logcat.
        USDLRecognizerSettings usdlRecognizerSettings = new USDLRecognizerSettings();

        // ZXingRecognizerSettings define settings for scanning barcodes with ZXing library
        // We use modified version of ZXing library to support scanning of barcodes for which
        // we still haven't implemented our own algorithms.
        ZXingRecognizerSettings zXingRecognizerSettings = new ZXingRecognizerSettings();
        // set this to true to enable scanning of QR codes
        zXingRecognizerSettings.setScanQRCode(true);

        // finally, when you have defined your scanning settings, you should put them into array
        // and send that array over intent to scan activity

        RecognizerSettings[] settArray = new RecognizerSettings[] {pdf417RecognizerSettings, oneDimensionalRecognizerSettings, zXingRecognizerSettings, usdlRecognizerSettings};
        // use Pdf417ScanActivity.EXTRAS_RECOGNIZER_SETTINGS_ARRAY to set array of recognizer settings
        intent.putExtra(Pdf417ScanActivity.EXTRAS_RECOGNIZER_SETTINGS_ARRAY, settArray);

        // additionally, there are generic settings that are used by all recognizers or the
        // whole recognition process
        GenericRecognizerSettings genericSettings = new GenericRecognizerSettings();
        // set this to true to enable returning of multiple scan results from single camera frame
        // default is false, which means that as soon as first barcode is found (no matter which type)
        // its contents will be returned.
        genericSettings.setAllowMultipleScanResultsOnSingleImage(true);
        intent.putExtra(Pdf417ScanActivity.EXTRAS_GENERIC_SETTINGS, genericSettings);

        // if you do not want the dialog to be shown when scanning completes, add following extra
        // to intent
        intent.putExtra(Pdf417ScanActivity.EXTRAS_SHOW_DIALOG_AFTER_SCAN, false);

        // Start Activity
        startActivityForResult(intent, MY_REQUEST_CODE);
    }


    public void enviarR(View v)
    {
        String resul="";
        SharedPreferences pref = getSharedPreferences("ParametrosBasicos", Context.MODE_PRIVATE);
        long per = pref.getInt("userid",0);

        respuestaEncuesta= new EncuestaRespuesta(encuesta.getIdEncuesta(),per,Integer.parseInt(getDatePhone("MMddHHmmss")),""+getDatePhone("yyyy-MM-dd"));
        ArrayList<PreguntaRespuesta> respuestas = new  ArrayList<PreguntaRespuesta>();
        ArrayList<ParametrosRespuesta> respuestaParametros=new   ArrayList<ParametrosRespuesta>() ;


        boolean validator = false;
        for (int i =0 ; i < layout.getChildCount(); i++)
        {
            LinearLayout child =(LinearLayout) layout.getChildAt(i);
            int numeRes=ObtenerRespuesta(child, encuesta.getPreguntas().get(i),respuestas);
            if(encuesta.getPreguntas().get(i).getObligatoria().equals("S"))
            {
               for(int j=respuestas.size() - 1 ; j>(respuestas.size() - 1) - numeRes ;j-- )
                {
                    PreguntaRespuesta respuestaValidar= respuestas.get(j);
                    if(respuestaValidar.getOpcion()== null)
                    {
                        if(respuestaValidar.getRespuesta().trim().length()==0)
                        {
                            Toast toast1 =
                                    Toast.makeText(this,
                                            "La pregunta :"+encuesta.getPreguntas().get(i).getTitulo()+", Es obligatoria!", Toast.LENGTH_LONG);

                            toast1.show();
                            validator=true;
                        }
                    }
                    else{
                        if(respuestaValidar.getOpcion().trim().length()==0)
                        {
                            Toast toast1 =
                                    Toast.makeText(this,
                                            "Debe escoger un valor para "+respuestaValidar.getRespuesta()+" en la pregunta :"+encuesta.getPreguntas().get(i).getTitulo(), Toast.LENGTH_LONG);

                            toast1.show();
                            validator=true;
                        }


                    }

                }

            }
            if(validator)
            {
                respuestas.clear();
                break;
            }


        }
       if(encuesta.getParametros() !=null)
       {
           for(int w=0;w<encuesta.getParametros().size();w++)
           {
               EncuestaParametro ep = encuesta.getParametros().get(w);

               if ( ep.getOpcional().indexOf('N') > -1 )
               {

                   switch (ep.getIdParametro()) {
                       // Captura GPS
                       case 1:
                           if(parametroGPS==null )

                           {
                               Toast toast1 =
                                       Toast.makeText(this,
                                               "Debe diligenciar la ubicación GPS ", Toast.LENGTH_LONG);

                               toast1.show();
                               validator=true;
                           }

                           break;
                       // Captura Imágen
                       case 2:
                           if(parametroCam==null )
                           {
                               Toast toast1 =
                                       Toast.makeText(this,
                                               "Debe obtener la captura de una imagen.", Toast.LENGTH_LONG);

                               toast1.show();
                               validator=true;

                           }

                            break;
                           // Lectura de Codigo
                       case 3:
                           if(parametroScan==null )
                           {
                               Toast toast1 =
                                       Toast.makeText(this,
                                               "Debe obtener el scaner de la cedula", Toast.LENGTH_LONG);

                               toast1.show();
                               validator=true;
                           }


                           break;

                       default:

                           break;
                   }
                   if(validator)
                   {

                       break;
                   }

               }


           }
           if(!validator)
           {
               if (parametroGPS != null)
               {
                   respuestaParametros.add(parametroGPS);
               }

               if (parametroCam != null)
               {
                   respuestaParametros.add(parametroCam);
               }

               if (parametroScan != null) {
                   respuestaParametros.add(parametroScan);
               }

               if (respuestas.size() > 0 ) {
                   respuestaEncuesta.setRespuesta(respuestas);
                   if( respuestaParametros.size() > 0) {
                       respuestaEncuesta.setParametros(respuestaParametros);
                   }
               }

           }

       }
        if(!validator)
        {

            final Gson gson = new Gson();

            if(DataOpciones.verificaConexion(this)) {

                String pr = pref.getString("ruta", "54.164.174.129:8081");
                //http://10.200.5.8:8081/
                String ruta="http://"+pr+"/"+HTTP_EVENT;
                new MyAsyncTask(Formulario.this)
                        .execute("POST", gson.toJson(respuestaEncuesta), ruta);

            } else
            {
                insertarEncuentas(respuestaEncuesta);
            }

        }




    }
    public void insertarEncuentas(EncuestaRespuesta res)
    {
        try {
            dbAdapter.abrir();
            ContentValues contentValues = new ContentValues();
            contentValues.put("IdUsuario", res.getIdUsuario());
            contentValues.put("IdEncuesta", res.getIdEncuesta());
            contentValues.put("Fecha_Realizacion", res.getFecha());
            contentValues.put("consecutivo", res.getConsecutivo());
            if(dbAdapter.Insertar("Encuesta_Repuesta",contentValues)> 0)
            {
                for (int i =0 ; i < res.getRespuesta().size(); i++) {
                    PreguntaRespuesta preguntR = res.getRespuesta().get(i);
                    contentValues = new ContentValues();
                    contentValues.put("IdUsuario", res.getIdUsuario());
                    contentValues.put("IdEncuesta", res.getIdEncuesta());
                    contentValues.put("consecutivo", res.getConsecutivo());
                    contentValues.put("item", preguntR.getItem());
                    contentValues.put("IdPregunta", preguntR.getIdPregunta());
                    contentValues.put("repuesta", preguntR.getRespuesta());
                    if(preguntR.getOpcion()!=null)
                    {

                        contentValues.put("opcion", preguntR.getOpcion());
                    }
                    else
                    {
                        contentValues.putNull("opcion");
                    }
                    if(dbAdapter.Insertar("Pregunta_Respuesta", contentValues)> 0) {

                        }
                    }

                    for (int k = 0; k < res.getParametros().size(); k++) {
                        ParametrosRespuesta paramR =  res.getParametros().get(k);
                        contentValues = new ContentValues();
                        contentValues.put("IdUsuario", res.getIdUsuario());
                        contentValues.put("IdEncuesta", res.getIdEncuesta());
                        contentValues.put("consecutivo", res.getConsecutivo());
                        contentValues.put("IdParametro", paramR.getIdParametro());
                        contentValues.put("valor", paramR.getValor());
                        if(dbAdapter.Insertar("Parametro_Encuesta_Respuesta", contentValues)> 0) {

                        }
                    }

                    Toast toast1 =
                            Toast.makeText(Formulario.this,
                                    "Fromulario Enviado", Toast.LENGTH_SHORT);
                    toast1.show();
                    reiniciarActivity(this,idFormulario);
                }




            dbAdapter.cerrar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int ObtenerRespuesta(LinearLayout contenedor , Pregunta Pregunta, ArrayList<PreguntaRespuesta> respuestaList)
    {
        PreguntaRespuesta result = new PreguntaRespuesta();
        int numRespuesta=0;
        result.setIdPregunta(Pregunta.getIdPregunta());
        EditText resp;
        TextView selectio;
        switch (Pregunta.getTipoPregunta()) {
            case 1:
                resp=(EditText)contenedor.findViewById(R.id.edtTexto);
                result.setItem(1);
                result.setRespuesta(resp.getText().toString());
                respuestaList.add(result);
                numRespuesta=1;
                break;
            case 2:
                resp=(EditText)contenedor.findViewById(R.id.mtxtEdit);
                result.setItem(1);
                result.setRespuesta(resp.getText().toString());
                respuestaList.add(result);
                numRespuesta=1;
                break;
            case 3:
                selectio =(TextView)contenedor.findViewById(R.id.respuestaGruop);
                result.setItem(1);
                result.setRespuesta(selectio.getText().toString());
                respuestaList.add(result);
                numRespuesta=1;
                break;
            case 4:

               String resp_opcio="";
                for(int j=0;j<contenedor.getChildCount();j++)
                {
                    View child = contenedor.getChildAt(j);
                    if (child instanceof CheckBox)
                    {
                        CheckBox hijo=(CheckBox)child;
                        if(hijo.isChecked())
                        {
                            if(resp_opcio.length()==0)
                            {
                                if(Pregunta.isOpcionEditble(hijo.getText().toString()))
                                {
                                    EditText otrosR = (EditText)contenedor.findViewById(R.id.edtTexto);
                                    resp_opcio=otrosR.getText().toString();
                                }else
                                {
                                    resp_opcio = hijo.getText().toString();
                                }
                            }else {
                                if(Pregunta.isOpcionEditble(hijo.getText().toString()))
                                {
                                    EditText otrosR = (EditText)contenedor.findViewById(R.id.edtTexto);
                                    resp_opcio+=" , "+otrosR.getText().toString()+" ";
                                }else
                                {
                                    resp_opcio = resp_opcio + " , " + hijo.getText()+" ";
                                }

                            }
                        }
                    }

                }
                result.setItem(1);
                result.setRespuesta(resp_opcio);
                respuestaList.add(result);
                numRespuesta=1;
                break;
            case 5:
                selectio =(TextView)contenedor.findViewById(R.id.seleEscala);
                result.setItem(1);
                result.setRespuesta(selectio.getText().toString());
                respuestaList.add(result);
                numRespuesta=1;
                break;
            case 6:
                Spinner lista = (Spinner)contenedor.findViewById(R.id.opcionesListado);
                result.setItem(1);
                result.setRespuesta(lista.getSelectedItem().toString()+"");
                respuestaList.add(result);
                numRespuesta=1;
                break;
            case 7:
                TableLayout tabla =(TableLayout) contenedor.findViewById(R.id.tablaOpciones);
                for(int i=0 ;i< tabla.getChildCount(); i++)
                {
                  TableRow registro = (TableRow)tabla.getChildAt(i);
                  TextView etiq =(TextView)registro.findViewById(R.id.textoRow);
                  RadioGroup selector =(RadioGroup)registro.findViewById(R.id.valoresRow);
                    PreguntaRespuesta itemA = new PreguntaRespuesta();
                    itemA.setIdPregunta(Pregunta.getIdPregunta());
                    itemA.setItem(i+1);
                    itemA.setRespuesta(etiq.getText().toString());
                    if (selector.getCheckedRadioButtonId() > 0 ) {
                        RadioButton rb = (RadioButton) selector.findViewById(selector.getCheckedRadioButtonId());
                        itemA.setOpcion(rb.getText() + "");
                    }

                  respuestaList.add(itemA);
                    numRespuesta++;
                }

                break;
            case 8:
                DatePicker dp=(DatePicker)contenedor.findViewById(R.id.Fecha_resutl);
                result.setItem(1);
                result.setRespuesta(dp.getYear()+"-"+dp.getMonth()+"-"+dp.getDayOfMonth());
                respuestaList.add(result);
                numRespuesta=1;
                break;
            case 9:
                TimePicker tp =(TimePicker)contenedor.findViewById(R.id.hora_result);
                result.setItem(1);
                result.setRespuesta(tp.getCurrentHour()+":"+tp.getCurrentMinute());
                respuestaList.add(result);
                numRespuesta=1;
                break;

            default:
                result.setItem(1);
                result.setRespuesta("Proceso");
                break;

        }

        return numRespuesta;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_formulario, menu);

        if (rePaintMenu == 1)
        {

            HabilitarParametros(menu);
        }

        return true;
    }

    public void HabilitarParametros(Menu menu)
    {
        for (int i =0 ; i < encuesta.getParametros().size(); i++)
        {

            switch (encuesta.getParametros().get(i).getIdParametro()) {
                // Captura GPS
                case 1:


                    menu.getItem(2).setVisible(true);
                    parametroGPS = new ParametrosRespuesta(1);
                    manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if ( !manejador.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        AlertDialog alert = null;
                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("El sistema GPS esta desactivado, Debe activarlo!")
                                .setCancelable(false)
                                .setPositiveButton("Activar GPS", new DialogInterface.OnClickListener() {
                                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    }
                                })
                                ;
                        alert = builder.create();
                        alert.show();
                    }
                    Criteria criterio = new Criteria();
                    criterio.setCostAllowed(false);
                    criterio.setAltitudeRequired(false);
                    criterio.setAccuracy(Criteria.ACCURACY_FINE);
                    proveedor = manejador.getBestProvider(criterio, true);
                    Location localizacion = manejador.getLastKnownLocation(proveedor);
                    capturarLocalizacion(localizacion);
                    manejador.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, 30000, 0, this);
                   Toast toast1;
                   if(parametroGPS.getValor().equals("Posicion Desconocida"))
                   {
                       toast1   =
                               Toast.makeText(this,
                                       "Posicion Desconocida.", Toast.LENGTH_SHORT);

                   }else
                   {
                       toast1   =
                               Toast.makeText(this,
                                       "Localización obtenida exitosamente.", Toast.LENGTH_SHORT);

                   }


                    toast1.show();
                    break;
                // Captura Imágen
                case 2:

                    menu.getItem(0).setVisible(true);
                    break;
                // Lectura de Codigo
                case 3:

                    menu.getItem(1).setVisible(true);
                    break;

                default:

                    break;
            }

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
        if (id == R.id.cam) {
            CapturaF();
            return true;
        }
        if (id == R.id.scan) {
            btnScan_click();
            return true;
        }
        if (id == R.id.gps) {
            CapturaL();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
    capturarLocalizacion(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public static void reiniciarActivity(Activity actividad, String parametro){
        Intent intent=new Intent();

        intent.setClass(actividad, actividad.getClass());
        intent.putExtra("formulario", parametro);
        //llamamos a la actividad
        actividad.startActivity(intent);
        //finalizamos la actividad actual
        actividad.finish();
    }

    class MyAsyncTask extends AsyncTask<String,Void,String> {

        private ProgressDialog progressDialog;
        private Context context;
       ;

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
            parames.add(new BasicNameValuePair("insert", params[1]));

            try {
                if( params[0].equals("POST"))
                {
                    String jsonResult = myRestFulGP.addEventPost(parames,params[2]);
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
                        return "OK";
                    }else                    {
                        return params[1];
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

                return "Error General: t " + e.getMessage();
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



                if(!resul.equals("OK"))
                {

                    insertarEncuentas(respuestaEncuesta);

                }else
                {

                    Toast toast1 =
                            Toast.makeText(Formulario.this,
                                    "Fromulario Enviado", Toast.LENGTH_SHORT);
                    toast1.show();
                    reiniciarActivity(Formulario.this,idFormulario);
                    SharedPreferences pref = getSharedPreferences("ParametrosBasicos", Context.MODE_PRIVATE);
                    int formsend = pref.getInt("formularios_enviados", 0);
                    formsend=formsend+1;
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putInt("formularios_enviados", formsend);
                    edit.commit();
                }
            }
        }
    }
}
