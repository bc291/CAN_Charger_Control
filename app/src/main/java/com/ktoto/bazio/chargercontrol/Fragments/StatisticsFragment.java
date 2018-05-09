package com.ktoto.bazio.chargercontrol.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ktoto.bazio.chargercontrol.ChargingOperation;
import com.ktoto.bazio.chargercontrol.ChargingOperationGet;
import com.ktoto.bazio.chargercontrol.CustomListAdapter;
import com.ktoto.bazio.chargercontrol.Model.Statistics;
import com.ktoto.bazio.chargercontrol.R;
import com.ktoto.bazio.chargercontrol.asynce.asyncGet;
import com.ktoto.bazio.chargercontrol.asynce.asyncGetStatistics;
import com.ktoto.bazio.chargercontrol.asynce.asyncHelper;
import com.ktoto.bazio.chargercontrol.asynce.asyncPost;

import java.util.List;

public class StatisticsFragment extends Fragment {

    ProgressBar progressBar;
    List<Statistics> statisticsList;
    TextView txtTotalCost, txtTotalChargingTime, txtTotalOperations, txtAveragePowerOfAll, txtAverageCost, txtAverageInitialCapacity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.statistics_fragment,  null);

       // progressBar = (ProgressBar) myView.findViewById(R.id.progressBar);
        txtTotalCost = (TextView) myView.findViewById(R.id.txtTotalCost);
        txtTotalChargingTime = (TextView) myView.findViewById(R.id.txtTotalChargingTime);
        txtTotalOperations = (TextView) myView.findViewById(R.id.txtTotalOperations);
        txtAveragePowerOfAll = (TextView) myView.findViewById(R.id.txtAveragePowerOfAll);
        txtAverageCost = (TextView) myView.findViewById(R.id.txtAverageCost);
        txtAverageInitialCapacity = (TextView) myView.findViewById(R.id.txtAverageInitialCapacity);

        asyncHelper asyncHelp = new asyncHelper(getContext(), new ChargingOperation());

        asyncGetStatistics asyncget = new asyncGetStatistics(this);
        asyncget.execute(asyncHelp);
       // progressBar.setVisibility(View.VISIBLE);

        return myView;
    }

    public void setProgressBarInvisible()
    {
        //progressBar.setVisibility(View.INVISIBLE);
    }

    public void setListOnMainClass(List<Statistics> statisticsList)
    {
        this.statisticsList=statisticsList;

        txtTotalCost.setText(String.valueOf(statisticsList.get(0).getTotalCost()));
        txtTotalChargingTime.setText(String.valueOf(statisticsList.get(0).getTotalChargingTime()));
        txtTotalOperations.setText(String.valueOf(statisticsList.get(0).getTotalOperations()));
        txtAveragePowerOfAll.setText(String.valueOf(statisticsList.get(0).getAveragePowerOfAll()));
        txtAverageCost.setText(String.valueOf(statisticsList.get(0).getAverageCost()));
        txtAverageInitialCapacity.setText(String.valueOf(statisticsList.get(0).getAverageInitialCapacity()));
      //  Log.d("testListy", String.valueOf(statisticsList.get(0).getTotalCost()));

    }
}
