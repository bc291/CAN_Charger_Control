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
        String positionFixed=null;
        View view= layoutInflater.inflate(resource, null);

        if(position<10) positionFixed="0"+(position+1);
        else if(position>9 && position<100) positionFixed = String.valueOf(position+1);


        TextView textView_carModel = (TextView) view.findViewById(R.id.textView_carModel);
        TextView textView_cost = (TextView) view.findViewById(R.id.textView_cost);
        TextView textView_transactionId = (TextView) view.findViewById(R.id.textView_transactionId);
        TextView textView_dateAndTime = (TextView) view.findViewById(R.id.textView_dateAndTime);

        ChargingOperationGet chargingOperationGet = list.get(position);
        textView_carModel.setText(chargingOperationGet.getCarModel());
        textView_cost.setText(chargingOperationGet.getCost().toString()+" zł");
        textView_transactionId.setText(positionFixed);
        textView_dateAndTime.setText(chargingOperationGet.getDateAndTime());
        return view;
    }
}
