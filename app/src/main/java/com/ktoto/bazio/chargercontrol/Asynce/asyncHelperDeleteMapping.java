package com.ktoto.bazio.chargercontrol.Asynce;

import android.content.Context;

import com.ktoto.bazio.chargercontrol.Model.ChargingOperation;



public class asyncHelperDeleteMapping {
    private Context context;
    private int chargingId;


    public asyncHelperDeleteMapping(Context context, int chargingId) {
        this.context = context;
        this.chargingId = chargingId;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getChargingId() {
        return chargingId;
    }

    public void setChargingId(int chargingId) {
        this.chargingId = chargingId;
    }
}
