package com.ktoto.bazio.chargercontrol;


public class ChargerData {

    private int amps;
    private int voltage;
    private float power;
    private float kwh;
    private float remainingTime;
    private float actualBatteryCapacity;
    private int availableCurrent;
    private int availableVoltage;
    private float chargingTime;

    public float getChargingTime() {
        return chargingTime;
    }

    public void setChargingTime(float chargingTime) {
        this.chargingTime = chargingTime;
    }

    public int getAvailableCurrent() {
        return availableCurrent;
    }

    public void setAvailableCurrent(int availableCurrent) {
        this.availableCurrent = availableCurrent;
    }

    public int getAvailableVoltage() {
        return availableVoltage;
    }

    public void setAvailableVoltage(int availableVoltage) {
        this.availableVoltage = availableVoltage;
    }

    private int isChargingActive;

    public float getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(float remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getAmps() {
        return amps;
    }

    public void setAmps(int amps) {
        this.amps = amps;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public float getKwh() {
        return kwh;
    }

    public void setKwh(float kwh) {
        this.kwh = kwh;
    }

    public int getIsChargingActive() {
        return isChargingActive;
    }

    public void setIsChargingActive(int isChargingActive) {
        this.isChargingActive = isChargingActive;
    }

    public float getActualBatteryCapacity() {
        return actualBatteryCapacity;
    }

    public void setActualBatteryCapacity(float actualBatteryCapacity) {
        this.actualBatteryCapacity = actualBatteryCapacity;
    }
}
