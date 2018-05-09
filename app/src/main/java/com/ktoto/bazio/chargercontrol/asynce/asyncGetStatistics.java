package com.ktoto.bazio.chargercontrol.asynce;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.ktoto.bazio.chargercontrol.ChargingOperation;
import com.ktoto.bazio.chargercontrol.ChargingOperationGet;
import com.ktoto.bazio.chargercontrol.Fragments.StatisticsFragment;
import com.ktoto.bazio.chargercontrol.Model.Statistics;
import com.ktoto.bazio.chargercontrol.chargerOperationsList;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by bazio on 09.05.2018.
 */

public class asyncGetStatistics extends AsyncTask<asyncHelper, Void, List<Statistics>> {

    StatisticsFragment statisticsFragment;


    @Override
    protected List<Statistics> doInBackground(asyncHelper... asyncHelpers) {

        Context context;
        context = asyncHelpers[0].getContext();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String portNumber = sharedPreferences.getString("port_number", "23234");
        String ipAddres = sharedPreferences.getString("ip_number", "192.168.0.248");



        Statistics[] statistics = null;
        URL url = null;
        InputStreamReader reader2 = null;
        try {
            url = new URL("http://"+ipAddres+":"+portNumber+"/api/statistics");
            reader2 = new InputStreamReader(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        statistics = new Gson().fromJson(reader2, Statistics[].class);

        Scanner s = new Scanner(reader2).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";

        List<Statistics> listStatistics = new ArrayList<>(Arrays.asList(statistics));

        return listStatistics;
    }


    public asyncGetStatistics(StatisticsFragment statisticsFragment) {
        this.statisticsFragment=statisticsFragment;
    }

    @Override
    protected void onPostExecute(List<Statistics> statistics) {
       // statisticsFragment.setListOnMainClass(chargingOperationGets);
        statisticsFragment.setProgressBarInvisible();
        statisticsFragment.setListOnMainClass(statistics);
    }


}
