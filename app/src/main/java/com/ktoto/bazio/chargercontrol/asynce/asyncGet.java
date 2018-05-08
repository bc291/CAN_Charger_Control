package com.ktoto.bazio.chargercontrol.asynce;

import android.os.AsyncTask;
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

/**
 * Created by bazio on 07.05.2018.
 */

public class asyncGet extends AsyncTask<Void, Void, List<ChargingOperationGet>> {

    private chargerOperationsList chargerOperations;

    @Override
    protected List<ChargingOperationGet> doInBackground(Void... voids) {
        ChargingOperationGet[] chargingOperation = null;
        URL url = null;
        InputStreamReader reader2 = null;
        try {
            url = new URL("http://192.168.0.248:12515/api/chargings");
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
