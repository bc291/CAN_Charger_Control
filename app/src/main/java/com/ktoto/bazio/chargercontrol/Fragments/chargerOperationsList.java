package com.ktoto.bazio.chargercontrol.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ktoto.bazio.chargercontrol.Asynce.asyncDelete;
import com.ktoto.bazio.chargercontrol.Asynce.asyncHelperDeleteMapping;
import com.ktoto.bazio.chargercontrol.Model.ChargingOperation;
import com.ktoto.bazio.chargercontrol.Model.ChargingOperationGet;
import com.ktoto.bazio.chargercontrol.Adapters.CustomListAdapter;
import com.ktoto.bazio.chargercontrol.R;
import com.ktoto.bazio.chargercontrol.Asynce.asyncGet;
import com.ktoto.bazio.chargercontrol.Asynce.asyncHelper;

import java.util.List;

public class chargerOperationsList extends Fragment implements AdapterView.OnItemClickListener {
    private ListView listview;
    private List<ChargingOperationGet> list2;
    private ProgressBar progressBar;
    private Dialog dialog;
    private asyncGet asyncget;
    private int idParse;
    private BottomNavigationView navigation;
    private int actualID;
    private View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_charger_operations_list, null);

        listview = (ListView) myView.findViewById(R.id.listView);
        progressBar = (ProgressBar) myView.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);

        dialog = new Dialog(getContext());

        asyncget = new asyncGet(this);
        asyncHelper asyncHelp = new asyncHelper(getContext(), new ChargingOperation());
        asyncget.execute(asyncHelp);
        return myView;
    }

    public void setListOnMainClass(List<ChargingOperationGet> list2) {
        this.list2 = list2;
        CustomListAdapter customListAdapter = new CustomListAdapter(getActivity(), R.layout.customlistlayout, list2);
        listview.setAdapter(customListAdapter);
        listview.setOnItemClickListener(this);

        if (idParse != 0) {
            navigation.getMenu().getItem(1).setChecked(true);
            Log.d("notificationLink", "działam");
            int positionNotification = 0;
            for (ChargingOperationGet tempItemt : list2) {
                if (tempItemt.getId() == idParse) break;
                positionNotification++;
            }
            showPopup(list2.get(positionNotification));
        }
    }

    public void setProgressBarInvisible() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String temp = list2.get(position).getCarModel();
        Toast.makeText(getContext(), "Kliknięto samochód: " + temp, Toast.LENGTH_SHORT).show();
        showPopup(list2.get(position));

    }

    public void showPopup(ChargingOperationGet chargingOperation) {
        TextView txtClose, txtDateAndTime, txtID, txtCarModel, txtCapacityCharged, txtAveragePower, txtCost, txtElapsedTime, txtInitialCapacity;
        Button btnExit, btnDeleteCharging;
        dialog.setContentView(R.layout.popup);
        // txtClose = (TextView) dialog.findViewById(R.id.txtView1);


        actualID = chargingOperation.getId();


        txtDateAndTime = (TextView) dialog.findViewById(R.id.txtDateAndTime);
        txtID = (TextView) dialog.findViewById(R.id.txtID);
        txtCarModel = (TextView) dialog.findViewById(R.id.txtCarModel);
        txtCapacityCharged = (TextView) dialog.findViewById(R.id.txtCapacityCharged);
        txtAveragePower = (TextView) dialog.findViewById(R.id.txtAveragePower);
        txtCost = (TextView) dialog.findViewById(R.id.txtCost);
        txtElapsedTime = (TextView) dialog.findViewById(R.id.txtElapsedTime);
        txtInitialCapacity = (TextView) dialog.findViewById(R.id.txtInitialCapacity);
        btnExit = (Button) dialog.findViewById(R.id.button55);
        btnDeleteCharging = (Button) dialog.findViewById(R.id.btnDeleteCharging);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDeleteCharging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPostDeleteRequest(actualID);
            }
        });


        txtCarModel.setText(chargingOperation.getCarModel());
        txtDateAndTime.setText(chargingOperation.getDateAndTime());
        txtID.setText("#" + String.valueOf(chargingOperation.getId()));
        txtCapacityCharged.setText(String.valueOf(chargingOperation.getCapacityCharged()));
        txtAveragePower.setText(String.valueOf(chargingOperation.getAveragePower()));
        txtCost.setText(String.valueOf(chargingOperation.getCost()));
        txtElapsedTime.setText(String.valueOf(chargingOperation.getElapsedTime()));
        txtInitialCapacity.setText(String.valueOf(chargingOperation.getInitialCapacity()));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void setToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        asyncget.cancel(true);
    }

    public void messageFromNotification(String id) {
        try {
            idParse = Integer.parseInt(id);
        }
        catch(NumberFormatException ex)
        {
            ex.printStackTrace();
        }
        Log.d("notificationLink", String.valueOf(idParse));
    }

    public void setNavigationBar(BottomNavigationView navigation) {
        this.navigation = navigation;
    }

   private void sendPostDeleteRequest(int chargingID)
    {
        asyncDelete async = new asyncDelete(){
            @Override
            protected void onPostExecute(Boolean isChargingDeleted) {
                if(isChargingDeleted)
                {
                    Snackbar snack = Snackbar.make(myView.findViewById(R.id.linearChargerOperationList),
                            "Usunięto", Snackbar.LENGTH_LONG);
                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                            snack.getView().getLayoutParams();
                    params.setMargins(0, 0, 0, 100);
                    snack.getView().setLayoutParams(params);
                    snack.show();
                }

                Fragment frg = getFragmentManager().findFragmentById(getId());
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
                dialog.dismiss();

            }
        };
        asyncHelperDeleteMapping asyncHelper = new asyncHelperDeleteMapping(getContext(), chargingID);
        async.execute(asyncHelper);

    }
}
