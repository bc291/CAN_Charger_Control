package com.ktoto.bazio.chargercontrol.Model;

/**
 * Created by bazio on 19.05.2018.
 */

public class CarFaultsMessage {
    private boolean batteryOvervoltage; // 0 - normal
    private boolean batteryUnderVoltage; // 0 - normal
    private boolean batteryCurrentDeviation; // 0 - normal
    private boolean highBatteryTemperatury; // 0 - normal
    private boolean batteryVoltageDeviation; // 0 - normal

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
}
