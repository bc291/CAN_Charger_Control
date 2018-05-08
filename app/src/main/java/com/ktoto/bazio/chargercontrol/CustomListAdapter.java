package com.ktoto.bazio.chargercontrol;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by bazio on 08.05.2018.
 */

public class CustomListAdapter extends ArrayAdapter<ChargingOperationGet> {
    Context mCtx;
    int resource;
    List<ChargingOperationGet> list;
    public CustomListAdapter(Context  mCtx, int resource, List<ChargingOperationGet> list)
    {
        super(mCtx, resource, list);
        this.mCtx=mCtx;
        this.resource=resource;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);

        View view= layoutInflater.inflate(resource, null);

        TextView textView_carModel = (TextView) view.findViewById(R.id.textView_carModel);
        TextView textView_capacityCharged = (TextView) view.findViewById(R.id.textView_capacityCharged);
        TextView textView_averagePower = (TextView) view.findViewById(R.id.textView_averagePower);
        TextView textView_cost = (TextView) view.findViewById(R.id.textView_cost);
        TextView textView_elapsedTime = (TextView) view.findViewById(R.id.textView_elapsedTime);

        ChargingOperationGet chargingOperationGet = list.get(position);
        textView_carModel.setText(chargingOperationGet.getCarModel());
        textView_capacityCharged.setText(chargingOperationGet.getCapacityCharged().toString());
        textView_averagePower.setText(chargingOperationGet.getAveragePower().toString());
        textView_cost.setText(chargingOperationGet.getCost().toString()+" zł");
        textView_elapsedTime.setText(chargingOperationGet.getElapsedTime().toString());

        return view;
    }
}
