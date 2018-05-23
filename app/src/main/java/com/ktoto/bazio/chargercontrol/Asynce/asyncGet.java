package com.ktoto.bazio.chargercontrol.Asynce;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.ktoto.bazio.chargercontrol.Model.ChargingOperationGet;
import com.ktoto.bazio.chargercontrol.Fragments.chargerOperationsList;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class asyncGet extends AsyncTask<asyncHelper, Void, List<ChargingOperationGet>> {

    private chargerOperationsList chargerOperations;
    private ChargingOperationGet[] chargingOperation = null;

    @Override
    protected List<ChargingOperationGet> doInBackground(asyncHelper... asyncHelpers) {

        Context context;
        context = asyncHelpers[0].getContext();
        List<ChargingOperationGet> testowaLista2=null;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String portNumber = sharedPreferences.getString("port_number", "23234");
        String ipAddres = sharedPreferences.getString("ip_number", "192.168.0.248");
        int serverTimeout = Integer.parseInt(sharedPreferences.getString("server_timeout_list", "1000"));



        URL url = null;
        InputStreamReader reader2 = null;
        try {
            url = new URL("http://"+ipAddres+":"+portNumber+"/api/chargings");
            Log.d("visibility", "opening connection");
            URLConnection connection =  url.openConnection();
            connection.setConnectTimeout(serverTimeout);
            connection.setReadTimeout(serverTimeout);//timeout
            reader2 = new InputStreamReader(connection.getInputStream());

            chargingOperation = new Gson().fromJson(reader2, ChargingOperationGet[].class);

            Scanner s = new Scanner(reader2).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";


        } catch (IOException e) {
            e.printStackTrace();
            Log.d("visibility", "timeout");
        }

        if(chargingOperation!=null)   testowaLista2 = new ArrayList<ChargingOperationGet>(Arrays.asList(chargingOperation));


        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return testowaLista2;
    }

    public asyncGet(chargerOperationsList chargerOperations) {
        this.chargerOperations=chargerOperations;
    }

    @Override
    protected void onPostExecute(List<ChargingOperationGet> chargingOperationGets) {

        if(chargingOperation!=null) {
            chargerOperations.setListOnMainClass(chargingOperationGets);

            chargerOperations.setProgressBarInvisible();
        }
        else
        {
            chargerOperations.setToast("Server timeout");
            chargerOperations.setProgressBarInvisible();
        }
    }
}
