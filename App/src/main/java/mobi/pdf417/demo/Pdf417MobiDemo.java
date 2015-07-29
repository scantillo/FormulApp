package mobi.pdf417.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.microblink.activity.Pdf417ScanActivity;
import com.microblink.recognizers.barcode.BarcodeType;
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
import com.microblink.view.recognition.RecognizerView;

import java.net.MalformedURLException;
import java.net.URL;

public class Pdf417MobiDemo extends Activity {

    private static final int MY_REQUEST_CODE = 1337;
    private TextView contentTxt;

    String DataR,Infocadena;
    private static final String TAG = "FormulaApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** request full screen window without title bar (looks better :-) ) */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        contentTxt = (TextView)findViewById(R.id.Texto);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);
        return true;
    }

    @SuppressWarnings("deprecation")
    private void showVersionString() {
        String nativeVersionString = RecognizerView.getNativeLibraryVersionString();
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String appVersion = pInfo.versionName;
            int appVersionCode = pInfo.versionCode;

            StringBuilder infoStr = new StringBuilder();
            infoStr.append("Application version: ");
            infoStr.append(appVersion);
            infoStr.append(", build ");
            infoStr.append(appVersionCode);
            infoStr.append("\nLibrary version: ");
            infoStr.append(nativeVersionString);

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Version info");
            alertDialog.setMessage(infoStr.toString());

            alertDialog.setButton(this.getString(R.string.photopayOK), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });

            alertDialog.setCancelable(false);
            alertDialog.show();
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_version:
            showVersionString();
            return true;
        }
        return false;
    }



    public void btnScan_click(View v) {
        Log.i(TAG, "scan will be performed");
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

    public void btnInfo_click(View v) {

    }

    private boolean checkIfDataIsUrlAndCreateIntent(String data) {
        // if barcode contains URL, create intent for browser
        // else, contain intent for message
        boolean barcodeDataIsUrl;

        try {
            @SuppressWarnings("unused")
            URL url = new URL(data);
            barcodeDataIsUrl = true;
        } catch (MalformedURLException exc) {
            barcodeDataIsUrl = false;
        }

        if (barcodeDataIsUrl) {
            // create intent for browser
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(data));
            startActivity(intent);
        }

        return barcodeDataIsUrl;
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_CODE && resultCode == Pdf417ScanActivity.RESULT_OK) {
            // First, obtain scan results array. If scan was successful, array will contain at least one element.
            // Multiple element may be in array if multiple scan results from single image were allowed in settings.

            Parcelable[] resultArray = data.getParcelableArrayExtra(Pdf417ScanActivity.EXTRAS_RECOGNITION_RESULT_LIST);

            // Each recognition result corresponds to active recognizer. As stated earlier, there are 4 types of
            // recognizers available (PDF417, Bardecoder, ZXing and USDL), so there are 4 types of results
            // available.

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
                    dia=fecha.substring(6,8);
                    mes=fecha.substring(4,6);
                    ano=fecha.substring(0,4);
                    fecha=dia+"/"+mes+"/"+ano;
                    Infocadena="Nombre: "+Nombre+"\n Apellido: "+Apellido +"\n Cedula: "+cedula+"\n Fecha de Nacimiento: "+fecha;
                    contentTxt.setText(Infocadena+"   ");



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
                    Log.i(TAG, "Customer full name is " + name);

                    sb.append(result.getTitle());
                    sb.append("\n\n");
                    sb.append(result.toString());
                }
            }

          /*  Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
            startActivity(Intent.createChooser(intent, getString(R.string.UseWith)));*/
        }
    }
}
