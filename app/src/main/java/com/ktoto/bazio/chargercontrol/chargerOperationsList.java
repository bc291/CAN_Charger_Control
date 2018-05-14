package com.ktoto.bazio.chargercontrol;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ktoto.bazio.chargercontrol.asynce.asyncGet;
import com.ktoto.bazio.chargercontrol.asynce.asyncHelper;

import org.w3c.dom.Text;

import java.util.List;

public class chargerOperationsList extends Fragment implements AdapterView.OnItemClickListener {
    ListView listview;
    List<ChargingOperationGet> list2;
    ProgressBar progressBar;
    Dialog dialog;
    asyncGet asyncget;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_charger_operations_list, null);

        listview = (ListView) myView.findViewById(R.id.listView);
        progressBar = (ProgressBar) myView.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);

        dialog = new Dialog(getContext());

        asyncget = new asyncGet(this);
        asyncHelper asyncHelp = new asyncHelper(getContext(), new ChargingOperation());
        asyncget.execute(asyncHelp);


        return myView;
    }

    public void setListOnMainClass(List<ChargingOperationGet> list2)
    {
        this.list2=list2;
        CustomListAdapter customListAdapter = new CustomListAdapter(getActivity(), R.layout.customlistlayout, list2);
        listview.setAdapter(customListAdapter);
        listview.setOnItemClickListener(this);
    }


    public void setProgressBarInvisible()
    {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String temp = list2.get(position).getCarModel();
        Toast.makeText(getContext(), "Kliknięto samochód: "+temp, Toast.LENGTH_SHORT).show();
        showPopup(list2.get(position));

    }

    public void showPopup(ChargingOperationGet chargingOperation)
    {
        TextView txtClose, txtDateAndTime, txtID, txtCarModel, txtCapacityCharged, txtAveragePower, txtCost, txtElapsedTime, txtInitialCapacity;
        Button btnFollow;
        dialog.setContentView(R.layout.popup);
       // txtClose = (TextView) dialog.findViewById(R.id.txtView1);
        txtDateAndTime = (TextView) dialog.findViewById(R.id.txtDateAndTime);
        txtID = (TextView) dialog.findViewById(R.id.txtID);
        txtCarModel  = (TextView) dialog.findViewById(R.id.txtCarModel);
        txtCapacityCharged = (TextView) dialog.findViewById(R.id.txtCapacityCharged);
        txtAveragePower = (TextView) dialog.findViewById(R.id.txtAveragePower);
        txtCost  = (TextView) dialog.findViewById(R.id.txtCost);
        txtElapsedTime = (TextView) dialog.findViewById(R.id.txtElapsedTime);
        txtInitialCapacity    = (TextView) dialog.findViewById(R.id.txtInitialCapacity);
        btnFollow = (Button) dialog.findViewById(R.id.button55);


        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        txtCarModel.setText(chargingOperation.getCarModel());
        txtDateAndTime.setText(chargingOperation.getDateAndTime());
        txtID.setText("#"+String.valueOf(chargingOperation.getId()));
        txtCapacityCharged.setText(String.valueOf(chargingOperation.getCapacityCharged()));
        txtAveragePower.setText(String.valueOf(chargingOperation.getAveragePower()));
        txtCost.setText(String.valueOf(chargingOperation.getCost()));
        txtElapsedTime.setText(String.valueOf(chargingOperation.getElapsedTime()));
        txtInitialCapacity.setText(String.valueOf(chargingOperation.getInitialCapacity()));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void setToast(String message)
    {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStop() {
        super.onStop();
        asyncget.cancel(true);
    }
}
