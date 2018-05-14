package com.ktoto.bazio.chargercontrol.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ktoto.bazio.chargercontrol.ChargingOperation;
import com.ktoto.bazio.chargercontrol.ChargingOperationGet;
import com.ktoto.bazio.chargercontrol.CustomListAdapter;
import com.ktoto.bazio.chargercontrol.MainActivity;
import com.ktoto.bazio.chargercontrol.Model.Statistics;
import com.ktoto.bazio.chargercontrol.R;
import com.ktoto.bazio.chargercontrol.StatisticsPopupListAdapter;
import com.ktoto.bazio.chargercontrol.asynce.asyncGet;
import com.ktoto.bazio.chargercontrol.asynce.asyncGetStatistics;
import com.ktoto.bazio.chargercontrol.asynce.asyncHelper;
import com.ktoto.bazio.chargercontrol.asynce.asyncPost;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

    ProgressBar progressBar;
    List<Statistics> statisticsList;
    TextView txtTotalCost, txtTotalChargingTime, txtTotalOperations, txtAveragePowerOfAll, txtAverageCost, txtAverageInitialCapacity;
    Dialog dialog;
    View myView;
    LinearLayout linearLayout;

    List<ChargingOperationGet> testowaLista2;
    ListView listview;
    TextView carModeltxt;
    List<String> parameterToView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.statistics_fragment,  null);

        progressBar = (ProgressBar) myView.findViewById(R.id.progressBar);
        txtTotalCost = (TextView) myView.findViewById(R.id.txtTotalCost);
        txtTotalChargingTime = (TextView) myView.findViewById(R.id.txtTotalChargingTime);
        txtTotalOperations = (TextView) myView.findViewById(R.id.txtTotalOperations);
        txtAveragePowerOfAll = (TextView) myView.findViewById(R.id.txtAveragePowerOfAll);
        txtAverageCost = (TextView) myView.findViewById(R.id.txtAverageCost);
        txtAverageInitialCapacity = (TextView) myView.findViewById(R.id.txtAverageInitialCapacity);


        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.popup_statistic);
        listview = (ListView) dialog.findViewById(R.id.listViewStatisticsPopup);


        asyncHelper asyncHelp = new asyncHelper(getContext(), new ChargingOperation());

        asyncGetStatistics asyncget = new asyncGetStatistics(this);
        asyncget.execute(asyncHelp);



       // progressBar.setVisibility(View.VISIBLE);
        Log.d("visibility", "recreated statistics fragment");


        linearLayout = (LinearLayout) myView.findViewById(R.id.scrollView);
        linearLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        CardView card_view = (CardView) myView.findViewById(R.id.cardView11);
        CardView card_view2 = (CardView) myView.findViewById(R.id.cardView12);
        CardView card_view3 = (CardView) myView.findViewById(R.id.cardView13);
        CardView card_view4 = (CardView) myView.findViewById(R.id.cardView14);
        CardView card_view5 = (CardView) myView.findViewById(R.id.cardView15);
        CardView card_view6 = (CardView) myView.findViewById(R.id.cardView16);

CardView[] arrayOfCardViews = {card_view, card_view2, card_view3, card_view4, card_view5, card_view6};
setOnClickListenerToCardViews(arrayOfCardViews);

        return myView;
    }






    public void showPopup(String message)
    {
        carModeltxt = (TextView) dialog.findViewById(R.id.txtCarModel);
        carModeltxt.setText(message);
        TextView txtOperationNumber = (TextView) dialog.findViewById(R.id.txtOperationNumber);
        txtOperationNumber.setText("Liczba operacji: " + String.valueOf(statisticsList.get(0).getTotalOperations()));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }


    public void setOnClickListenerToCardViews(CardView[] array) {

        CardView[] cardViews = array;

        for (int i = 0; i < cardViews.length; ++i) {

            final int index = i;
            cardViews[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d("cardView", String.valueOf(index));
                    StatisticsPopupListAdapter customListAdapter = null;
                    parameterToView = new ArrayList<String>();
                    switch (index)
                    {
                        case 0: //totalCost

                            for(ChargingOperationGet temp : testowaLista2)  parameterToView.add(temp.getCost()+ " zł");

                            showPopup("Całkowity koszt");
                            break;

                        case 1: //totalTime
                            for(ChargingOperationGet temp : testowaLista2)  parameterToView.add(String.valueOf(temp.getElapsedTime())+" min");

                            showPopup("Całkowity czas ład.");
                            break;

                        case 2: //operationsCount
                            for(ChargingOperationGet temp : testowaLista2)  parameterToView.add(String.valueOf(temp.getElapsedTime())+" min");

                            showPopup("Liczba operacji");
                            break;

                        case 3: //averagePower
                            for(ChargingOperationGet temp : testowaLista2)  parameterToView.add(String.valueOf(temp.getAveragePower())+" kWh");

                            showPopup("Średnia moc ładowań");
                            break;

                        case 4: //averageCost
                            for(ChargingOperationGet temp : testowaLista2)  parameterToView.add(String.valueOf(temp.getCost())+" zł");

                            showPopup("Średni koszt ład.");
                            break;

                        case 5: //averageInitialCapacity
                            for(ChargingOperationGet temp : testowaLista2)  parameterToView.add(String.valueOf(temp.getInitialCapacity())+" kWh");

                            showPopup("Śr. nał. początkowe");
                            break;
                    }
                    customListAdapter = new StatisticsPopupListAdapter(getActivity(), R.layout.details_list_layout, parameterToView);
                    listview.setAdapter(customListAdapter);
                }
            });
        }
    }


    public void setMainList(List<ChargingOperationGet> testowaLista2)
    {
        this.testowaLista2 = testowaLista2;



    }


    public void setLayoutVisibility()
    {
        linearLayout.setVisibility(View.VISIBLE);
    }

    public void setProgressBarVisibility()
    {
        progressBar.setVisibility(View.GONE);
    }

    public void setListOnMainClass(List<Statistics> statisticsList)
    {
        this.statisticsList=statisticsList;

        txtTotalCost.setText(String.valueOf(statisticsList.get(0).getTotalCost()) + " zł");
        txtTotalChargingTime.setText(String.valueOf(statisticsList.get(0).getTotalChargingTime()) + " m");
        txtTotalOperations.setText(String.valueOf(statisticsList.get(0).getTotalOperations()));
        txtAveragePowerOfAll.setText(String.valueOf(statisticsList.get(0).getAveragePowerOfAll()) + " kWh");
        txtAverageCost.setText(String.valueOf(statisticsList.get(0).getAverageCost()) + " zł");
        txtAverageInitialCapacity.setText(String.valueOf(statisticsList.get(0).getAverageInitialCapacity()) + " kWh");
      //  Log.d("testListy", String.valueOf(statisticsList.get(0).getTotalCost()));

    }





    @Override
    public void onResume() {
        super.onResume();
        Log.d("visibility", "resumed");
    }


public void setToast(String message)
{
    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
}



}
