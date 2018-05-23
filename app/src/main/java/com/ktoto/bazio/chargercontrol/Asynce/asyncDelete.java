package com.ktoto.bazio.chargercontrol.Asynce;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.ktoto.bazio.chargercontrol.Model.ChargingOperation;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

public class asyncDelete extends AsyncTask<asyncHelperDeleteMapping, Void, Boolean> {
    @Override
    protected Boolean doInBackground(asyncHelperDeleteMapping... asyncHelperDeleteMappings) {

        Context context;
        context = asyncHelperDeleteMappings[0].getContext();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String portNumber = sharedPreferences.getString("port_number", "23234");
        String ipAddres = sharedPreferences.getString("ip_number", "192.168.0.248");

        int chargingId = asyncHelperDeleteMappings[0].getChargingId();

        HttpDelete delete = new HttpDelete();
        try {
            delete.setURI(new URI("http://"+ipAddres+":"+portNumber+"/api/chargings/" + String.valueOf(chargingId)));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        delete.setHeader("Content-Type", "application/json; charset=utf-8");

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = null;
        String httpResponseString=null;
        boolean httpResponseBoolean=false;
        try {
            httpResponse = httpClient.execute(delete);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {

                HttpEntity entity = httpResponse.getEntity();

                httpResponseString = String.valueOf(EntityUtils.toString(entity));
                httpResponseBoolean = Boolean.parseBoolean(httpResponseString);
                Log.d("httpReponse", String.valueOf(httpResponseBoolean));
            }
            else if(httpResponse.getStatusLine().getStatusCode() == 500) Log.d("httpReponse", "500 - Internal Server Error");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return httpResponseBoolean;
    }
}
