package com.ktoto.bazio.chargercontrol.Asynce;

import android.content.Context;

import com.ktoto.bazio.chargercontrol.Model.ChargingOperation;

/**
 * Created by bazio on 09.05.2018.
 */

public class asyncHelper {
    private Context context;
    private ChargingOperation chargingOperation;


    public asyncHelper(Context context, ChargingOperation chargingOperation) {
        this.context = context;
        this.chargingOperation = chargingOperation;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ChargingOperation getChargingOperation() {
        return chargingOperation;
    }

    public void setChargingOperation(ChargingOperation chargingOperation) {
        this.chargingOperation = chargingOperation;
    }

}
