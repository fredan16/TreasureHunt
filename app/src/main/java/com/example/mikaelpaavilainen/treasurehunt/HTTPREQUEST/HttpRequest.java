package com.example.mikaelpaavilainen.treasurehunt.HTTPREQUEST;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mikael.paavilainen on 2018-02-02.
 */

public class HttpRequest extends AsyncTask<String, Void, String> {

    public String respons = "";
    public boolean gotResponse = false;
    public String targetURL, urlParameters;

    Context context;

    private Object json;

    public void setValues(String TargetUrl, String UrlParams,Context c){
        this.targetURL = TargetUrl;
        this.urlParameters = UrlParams;
        json = new Object();
        this.context = c;
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url;
        HttpURLConnection connection = null;
        try {
            Log.d("HTTPS", "HttpRequest: Tried" + targetURL);
            //Create connection
            url = new URL(targetURL);
            Log.d("HTTPS", "doInBackground: " + url + urlParameters);
            connection = (HttpURLConnection)url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches (false);
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");


            Log.d("HTTPS", "HttpRequest: Sending now" );
            //Send request
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
            Log.d("HTTPS", "doInBackground: Params - " + urlParameters);
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();
            Log.d("HTTPS", "HttpRequest: getting resåonse" );
            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            Log.d("HTTPS", "HttpRequest: Got shit" + response.toString() );
            respons = response.toString();

            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();

            Log.d("HTTPSN", "HttpRequest: error" + e.getMessage());


        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
        return null;

    }
    @Override
    protected void                                                             onPostExecute(String result) {
        super.onPostExecute(result);

        //Toast.makeText(context,"String retrived:" + respons, Toast.LENGTH_SHORT).show();
    }

}
