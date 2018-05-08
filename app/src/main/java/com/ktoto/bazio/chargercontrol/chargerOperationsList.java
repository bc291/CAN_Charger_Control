package com.ktoto.bazio.chargercontrol;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.ktoto.bazio.chargercontrol.asynce.asyncGet;
import java.util.List;

public class chargerOperationsList extends Fragment {
    ListView listview;
    List<ChargingOperationGet> list2;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_charger_operations_list, null);

        listview = (ListView) myView.findViewById(R.id.listView);
        progressBar = (ProgressBar) myView.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);

        asyncGet asyncget = new asyncGet(this);
        asyncget.execute();


        return myView;
    }

    public void setListOnMainClass(List<ChargingOperationGet> list2)
    {
        this.list2=list2;
        CustomListAdapter customListAdapter = new CustomListAdapter(getActivity(), R.layout.customlistlayout, list2);
        listview.setAdapter(customListAdapter);
    }


    public void setProgressBarInvisible()
    {
        progressBar.setVisibility(View.INVISIBLE);
    }
}
