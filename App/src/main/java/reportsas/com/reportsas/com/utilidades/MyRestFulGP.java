package reportsas.com.reportsas.com.utilidades;

/**
 * Created by rytscc on 28/04/2015.
 */

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;

public class MyRestFulGP {


    private HttpClient httpclient;

    /**
     * Envia los datos por GET
     * @throws java.io.IOException
     * @throws org.apache.http.client.ClientProtocolException
     * @throws org.json.JSONException
     * */
    public String addEventGet(List<NameValuePair> params , String url) throws IOException, URISyntaxException {

     try {
        httpclient = new DefaultHttpClient();
        //los datos a enviar

        String paramString = URLEncodedUtils.format(params, "utf-8");
        // name = URLEncoder.encode(name,"UTF-8");
        //age = URLEncoder.encode(age,"UTF-8");
        //url, cabecera JSON y ejecuta
        HttpGet httpget = new HttpGet(url + "?" + paramString);
        httpget.addHeader("Content-Type", "application/json");
        HttpResponse response = httpclient.execute(httpget);
        //obtiene la respuesta del servidor se transforma a objeto JSON
        String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

        return jsonResult;

    } catch (ClientProtocolException e) {
        throw new ClientProtocolException("Protocol error");
    } catch (IOException e) {
        throw new IOException("Error IO"+e.getMessage());
    }
    }

    /**
     * Envia los datos por POST
     * @throws java.io.IOException
     * @throws org.apache.http.client.ClientProtocolException
     * @throws org.json.JSONException
     * */
    public String addEventPost(List<NameValuePair> params ,String url) throws  IOException, URISyntaxException
    {
        try {
            httpclient = new DefaultHttpClient();

            //url y tipo de contenido
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Content-Type", "application/json");
            //forma el JSON y tipo de contenido
           // JSONObject jsonObject = new JSONObject();

            BasicNameValuePair item;
           // for (int i = 0; i < params.size(); i++) {
                item = (BasicNameValuePair) params.get(0);
              //  jsonObject.put(item.getName().toString(), item.getValue().toString());
            //}

            StringEntity stringEntity = new StringEntity(item.getValue().toString());
            stringEntity.setContentType((Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(stringEntity);
            //ejecuta
            HttpResponse response = httpclient.execute(httppost);
            //obtiene la respuesta y transorma a objeto JSON
            String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
            return jsonResult;
        }catch (ClientProtocolException e) {
            throw new ClientProtocolException("Protocol error");
        } catch (IOException e) {
            throw new IOException("Error IO"+e.getMessage());
        }
    }

    /**
     * Transforma el InputStream en un String
     * @return StringBuilder
     * */
    private StringBuilder inputStreamToString(InputStream is)
    {
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader rd = new BufferedReader( new InputStreamReader(is) );
        try
        {
            while( (line = rd.readLine()) != null )
            {
                stringBuilder.append(line);
            }
        }
        catch( IOException e)
        {
            e.printStackTrace();
        }

        return stringBuilder;
    }
}