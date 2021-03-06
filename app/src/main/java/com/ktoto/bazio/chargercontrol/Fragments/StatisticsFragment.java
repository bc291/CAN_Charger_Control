package com.ktoto.bazio.chargercontrol.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ktoto.bazio.chargercontrol.Model.ChargingOperation;
import com.ktoto.bazio.chargercontrol.Model.ChargingOperationGet;
import com.ktoto.bazio.chargercontrol.Model.Statistics;
import com.ktoto.bazio.chargercontrol.R;
import com.ktoto.bazio.chargercontrol.Adapters.StatisticsPopupListAdapter;
import com.ktoto.bazio.chargercontrol.Asynce.asyncGetStatistics;
import com.ktoto.bazio.chargercontrol.Asynce.asyncHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private ProgressBar progressBar;
    private List<Statistics> statisticsList;
    private TextView txtTotalCost, txtTotalChargingTime, txtTotalOperations, txtAveragePowerOfAll, txtAverageCost, txtAverageInitialCapacity;
    private Dialog dialog;
    private View myView;
    private LinearLayout linearLayout;

    private List<ChargingOperationGet> testowaLista2;
    private ListView listview;
    private TextView carModeltxt;
    private List<String> parameterToView;
    private Animation animation;
    private StatisticsFragment statisticsFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.statistics_fragment, null);
        statisticsFragment = this;
        progressBar = (ProgressBar) myView.findViewById(R.id.progressBar);
        txtTotalCost = (TextView) myView.findViewById(R.id.txtTotalCost);
        txtTotalChargingTime = (TextView) myView.findViewById(R.id.txtTotalChargingTime);
        txtTotalOperations = (TextView) myView.findViewById(R.id.txtTotalOperations);
        txtAveragePowerOfAll = (TextView) myView.findViewById(R.id.txtAveragePowerOfAll);
        txtAverageCost = (TextView) myView.findViewById(R.id.txtAverageCost);
        txtAverageInitialCapacity = (TextView) myView.findViewById(R.id.txtAverageInitialCapacity);

        animation = AnimationUtils.loadAnimation(getContext(),
                R.anim.animation_detailes_top_to_bottom);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.popup_statistic);
        listview = (ListView) dialog.findViewById(R.id.listViewStatisticsPopup);
        Button buttonPopupClose = (Button) dialog.findViewById(R.id.button55);

        buttonPopupClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

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

        for (CardView item : arrayOfCardViews) {
            item.setVisibility(View.INVISIBLE);
        }


        for (CardView item : arrayOfCardViews) {
            item.startAnimation(animation);
            item.setVisibility(View.VISIBLE);
        }

        FloatingActionButton fab = myView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Log.d("fab", "działa");

                asyncHelper asyncHelp = new asyncHelper(getContext(), new ChargingOperation());
                asyncGetStatistics asyncget = new asyncGetStatistics(statisticsFragment);
                asyncget.execute(asyncHelp);


                Snackbar snack = Snackbar.make(myView.findViewById(R.id.constraintLayoutStatistics),
                         "Odswieżono", Snackbar.LENGTH_SHORT);
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                snack.getView().getLayoutParams();
                params.setMargins(0, 0, 0, 100);
                snack.getView().setLayoutParams(params);
                snack.show();

            }
        });


        return myView;
    }


    public void showPopup(String message) {
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

                    if (testowaLista2 != null) {
                        switch (index) {
                            case 0: //totalCost

                                for (ChargingOperationGet temp : testowaLista2)
                                    parameterToView.add(String.valueOf(temp.getCost() + " zł") + ";" + temp.getId());

                                showPopup("Całkowity koszt");
                                break;

                            case 1: //totalTime
                                for (ChargingOperationGet temp : testowaLista2)
                                    parameterToView.add(String.valueOf(round(temp.getElapsedTime(), 2) + " min") + ";" + temp.getId());

                                showPopup("Całkowity czas ład.");
                                break;

                            case 2: //operationsCount
                                for (ChargingOperationGet temp : testowaLista2)
                                    parameterToView.add(String.valueOf(round(temp.getElapsedTime(), 2) + " min") + ";" + temp.getId());

                                showPopup("Liczba operacji");
                                break;

                            case 3: //averagePower
                                for (ChargingOperationGet temp : testowaLista2)
                                    parameterToView.add(String.valueOf(round(temp.getAveragePower(), 2) + " kWh") + ";" + temp.getId());

                                showPopup("Średnia moc ładowań");
                                break;

                            case 4: //averageCost
                                for (ChargingOperationGet temp : testowaLista2)
                                    parameterToView.add(String.valueOf(round(temp.getCost(), 2) + " zł") + ";" + temp.getId());

                                showPopup("Średni koszt ład.");
                                break;

                            case 5: //averageInitialCapacity
                                for (ChargingOperationGet temp : testowaLista2)
                                    parameterToView.add(String.valueOf(round(temp.getInitialCapacity(), 2) + " kWh") + ";" + temp.getId());

                                showPopup("Śr. nał. początkowe");
                                break;
                        }
                    }

                    if (parameterToView != null) {

                        customListAdapter = new StatisticsPopupListAdapter(getActivity(), R.layout.details_list_layout, parameterToView);
                        listview.setAdapter(customListAdapter);

                    }
                }
            });
        }
    }


    public BigDecimal round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }


    public void setMainList(List<ChargingOperationGet> testowaLista2) {
        this.testowaLista2 = testowaLista2;


    }


    public void setLayoutVisibility() {
        linearLayout.setVisibility(View.VISIBLE);
    }

    public void setProgressBarVisibility() {
        progressBar.setVisibility(View.GONE);
    }

    public void setListOnMainClass(List<Statistics> statisticsList) {
        this.statisticsList = statisticsList;

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


    public void setToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


}
