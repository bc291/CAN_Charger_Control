package com.ktoto.bazio.chargercontrol;

/**
 * Created by bazio on 02.05.2018.
 */

public class CarData {
    private int maxChargingVoltage;
    private int maxChargingCurrent;
    private double totalBatteryCapacity;
    private int carStatus;
    private int faults;


    public int getMaxChargingCurrent() {
        return maxChargingCurrent;
    }

    public void setMaxChargingCurrent(int maxChargingCurrent) {
        this.maxChargingCurrent = maxChargingCurrent;
    }

    public int getMaxChargingVoltage() {
        return maxChargingVoltage;
    }

    public void setMaxChargingVoltage(int maxChargingVoltage) {
        this.maxChargingVoltage = maxChargingVoltage;
    }

    public double getTotalBatteryCapacity() {
        return totalBatteryCapacity;
    }

    public void setTotalBatteryCapacity(double totalBatteryCapacity) {
        this.totalBatteryCapacity = totalBatteryCapacity;
    }

    public int getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(int carStatus) {
        this.carStatus = carStatus;
    }

    public int getFaults() {
        return faults;
    }

    public void setFaults(int faults) {
        this.faults = faults;
    }
}
