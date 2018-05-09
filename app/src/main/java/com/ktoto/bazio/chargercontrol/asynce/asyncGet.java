package com.ktoto.bazio.chargercontrol.asynce;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.ktoto.bazio.chargercontrol.ChargingOperation;
import com.ktoto.bazio.chargercontrol.ChargingOperationGet;
import com.ktoto.bazio.chargercontrol.chargerOperationsList;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class asyncGet extends AsyncTask<asyncHelper, Void, List<ChargingOperationGet>> {

    private chargerOperationsList chargerOperations;

    @Override
    protected List<ChargingOperationGet> doInBackground(asyncHelper... asyncHelpers) {

        Context context;
        context = asyncHelpers[0].getContext();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String portNumber = sharedPreferences.getString("port_number", "23234");
        String ipAddres = sharedPreferences.getString("ip_number", "192.168.0.248");


        ChargingOperationGet[] chargingOperation = null;

        URL url = null;
        InputStreamReader reader2 = null;
        try {
            url = new URL("http://"+ipAddres+":"+portNumber+"/api/chargings");
            reader2 = new InputStreamReader(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        chargingOperation = new Gson().fromJson(reader2, ChargingOperationGet[].class);

        Scanner s = new Scanner(reader2).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";


        //Log.d("GetTest", reader2.toString());
    List<ChargingOperationGet> testowaLista2 = new ArrayList<ChargingOperationGet>(Arrays.asList(chargingOperation));



        return testowaLista2;
    }

    public asyncGet(chargerOperationsList chargerOperations) {
        this.chargerOperations=chargerOperations;
    }

    @Override
    protected void onPostExecute(List<ChargingOperationGet> chargingOperationGets) {
        chargerOperations.setListOnMainClass(chargingOperationGets);
        chargerOperations.setProgressBarInvisible();
    }
}
