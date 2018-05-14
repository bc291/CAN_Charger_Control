package com.ktoto.bazio.chargercontrol.asynce;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ktoto.bazio.chargercontrol.ChargingOperation;
import com.ktoto.bazio.chargercontrol.ChargingOperationGet;
import com.ktoto.bazio.chargercontrol.Fragments.StatisticsFragment;
import com.ktoto.bazio.chargercontrol.Model.Statistics;
import com.ktoto.bazio.chargercontrol.chargerOperationsList;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;



public class asyncGetStatistics extends AsyncTask<asyncHelper, Void, List<Statistics>> {

    StatisticsFragment statisticsFragment;

    private chargerOperationsList chargerOperations;
    ChargingOperationGet[] chargingOperation = null;
    List<ChargingOperationGet> testowaLista2=null;

    @Override
    protected List<Statistics> doInBackground(asyncHelper... asyncHelpers) {

        Context context;
        context = asyncHelpers[0].getContext();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String portNumber = sharedPreferences.getString("port_number", "23234");
        String ipAddres = sharedPreferences.getString("ip_number", "192.168.0.248");
        int serverTimeout = Integer.parseInt(sharedPreferences.getString("server_timeout_list", "1000"));


        Statistics[] statistics = null;
        URL url = null;
        InputStreamReader reader2 = null;
        List<Statistics> listStatistics=null;

        try
        {
            url = new URL("http://"+ipAddres+":"+portNumber+"/api/statistics");
            Log.d("visibility", "opening connection");
            URLConnection connection =  url.openConnection();
            connection.setConnectTimeout(serverTimeout);
            connection.setReadTimeout(serverTimeout);//timeout
            reader2 = new InputStreamReader(connection.getInputStream());

            statistics = new Gson().fromJson(reader2, Statistics[].class);

            Scanner s = new Scanner(reader2).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";
           // Thread.sleep(500);

        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.d("visibility", "timeout");
        }

        if(statistics!=null) listStatistics = new ArrayList<>(Arrays.asList(statistics));




        url = null;
        reader2 = null;
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




        return listStatistics;
    }


    public asyncGetStatistics(StatisticsFragment statisticsFragment) {
        this.statisticsFragment=statisticsFragment;
    }

    @Override
    protected void onPostExecute(List<Statistics> statistics) {
       // statisticsFragment.setListOnMainClass(chargingOperationGets);
        statisticsFragment.setProgressBarVisibility();

        if(statistics!=null) {
            statisticsFragment.setListOnMainClass(statistics);
            statisticsFragment.setLayoutVisibility();
            statisticsFragment.setMainList(testowaLista2);
        }

        else {
            statisticsFragment.setToast("Server timeout");
        }


    }


}
