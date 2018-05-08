package com.ktoto.bazio.chargercontrol.asynce;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ktoto.bazio.chargercontrol.ChargingOperation;
import com.ktoto.bazio.chargercontrol.MainActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by bazio on 07.05.2018.
 */

public class asyncPost extends AsyncTask<ChargingOperation, Void, Void>{

    @Override
    protected Void doInBackground(ChargingOperation... chargingOperations) {

        ChargingOperation chargingOperation = chargingOperations[0];

       // Log.d("Test5555", new Gson().toJson(chargingOperation, ChargingOperation.class));

        HttpPost post = new HttpPost();
        try {
            post.setURI(new URI("http://192.168.0.248:12515/api/chargings"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        post.setHeader("Content-Type", "application/json; charset=utf-8");
        try {
            post.setEntity(new StringEntity(new Gson().toJson(chargingOperation, ChargingOperation.class)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                Log.d("httpReponse", EntityUtils.toString(entity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
