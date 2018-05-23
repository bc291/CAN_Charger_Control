package com.ktoto.bazio.chargercontrol.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ktoto.bazio.chargercontrol.R;

import java.util.List;

/**
 * Created by bazio on 08.05.2018.
 */

public class StatisticsPopupListAdapter extends ArrayAdapter<String>  {
    private Context mCtx;
    private int resource;
    private List<String> list;
    public StatisticsPopupListAdapter(Context  mCtx, int resource, List<String> list)
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

        if(position<9) positionFixed="0"+(position+1);
        else if(position>8 && position<99) positionFixed = String.valueOf(position+1);


        TextView textView_parameter = (TextView) view.findViewById(R.id.textView_parameter);
        TextView textView_transactionId = (TextView) view.findViewById(R.id.textView_transactionId);
        TextView textView_ID = (TextView) view.findViewById(R.id.textView_ID);

        String[] stringArray = list.get(position).split(";");


        textView_parameter.setText(stringArray[0]);
        textView_ID.setText("#" + stringArray[1]);
        textView_transactionId.setText(positionFixed);
        return view;
    }


}
