package com.ktoto.bazio.chargercontrol.Model;

import android.util.Log;

import com.ktoto.bazio.Tools.BitsByIndexRetriever;

/**
 * Created by bazio on 19.05.2018.
 */

public class CarFaultsMessage implements BitsByIndexRetriever
{
    private boolean batteryOvervoltage; // 0 - normal
    private boolean batteryUnderVoltage; // 0 - normal
    private boolean batteryCurrentDeviation; // 0 - normal
    private boolean highBatteryTemperatury; // 0 - normal
    private boolean batteryVoltageDeviation; // 0 - normal
    private boolean[] arrayOfBits;

    public boolean isBatteryOvervoltage() {
        return batteryOvervoltage;
    }

    public void setBatteryOvervoltage(boolean batteryOvervoltage) {
        this.batteryOvervoltage = batteryOvervoltage;
    }

    public boolean isBatteryUnderVoltage() {
        return batteryUnderVoltage;
    }

    public void setBatteryUnderVoltage(boolean batteryUnderVoltage) {
        this.batteryUnderVoltage = batteryUnderVoltage;
    }

    public boolean isBatteryCurrentDeviation() {
        return batteryCurrentDeviation;
    }

    public void setBatteryCurrentDeviation(boolean batteryCurrentDeviation) {
        this.batteryCurrentDeviation = batteryCurrentDeviation;
    }

    public boolean isHighBatteryTemperatury() {
        return highBatteryTemperatury;
    }

    public void setHighBatteryTemperatury(boolean highBatteryTemperatury) {
        this.highBatteryTemperatury = highBatteryTemperatury;
    }

    public boolean isBatteryVoltageDeviation() {
        return batteryVoltageDeviation;
    }

    public void setBatteryVoltageDeviation(boolean batteryVoltageDeviation) {
        this.batteryVoltageDeviation = batteryVoltageDeviation;
    }

    @Override
    public String toString() {
        return batteryOvervoltage+" "+batteryUnderVoltage+" "+batteryCurrentDeviation+" "+highBatteryTemperatury+" "+batteryVoltageDeviation;
    }


    public CarFaultsMessage(boolean[] arrayOfBits)
    {
        this.arrayOfBits = arrayOfBits;
        this.setBatteryOvervoltage(arrayOfBits[0]);
        this.setBatteryUnderVoltage(arrayOfBits[1]);
        this.setBatteryCurrentDeviation(arrayOfBits[2]);
        this.setHighBatteryTemperatury(arrayOfBits[3]);
        this.setBatteryVoltageDeviation(arrayOfBits[4]);
        Log.d("faults5", arrayOfBits[0]+" "+arrayOfBits[1]+" "+arrayOfBits[2]+" "+arrayOfBits[3]+" "+arrayOfBits[4]);
    }

    @Override
    public boolean getBitByIndex(int index) {
        if(arrayOfBits.length>0) return this.arrayOfBits[index];
        else return false;
    }
}
