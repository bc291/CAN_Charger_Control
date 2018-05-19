package com.ktoto.bazio.chargercontrol.Model;



public class ChargerStatusMessage {
    private boolean stationStatus;
    private boolean stationMalfunction;
    private boolean vehicleConnectorLock;
    private boolean batteryIncompatibility;
    private boolean chargingSystemMalfunction;
    private boolean chargerStopControl;


    public boolean isStationStatus() {
        return stationStatus;
    }

    public void setStationStatus(boolean stationStatus) {
        this.stationStatus = stationStatus;
    }

    public boolean isStationMalfunction() {
        return stationMalfunction;
    }

    public void setStationMalfunction(boolean stationMalfunction) {
        this.stationMalfunction = stationMalfunction;
    }

    public boolean isVehicleConnectorLock() {
        return vehicleConnectorLock;
    }

    public void setVehicleConnectorLock(boolean vehicleConnectorLock) {
        this.vehicleConnectorLock = vehicleConnectorLock;
    }

    public boolean isBatteryIncompatibility() {
        return batteryIncompatibility;
    }

    public void setBatteryIncompatibility(boolean batteryIncompatibility) {
        this.batteryIncompatibility = batteryIncompatibility;
    }

    public boolean isChargingSystemMalfunction() {
        return chargingSystemMalfunction;
    }

    public void setChargingSystemMalfunction(boolean chargingSystemMalfunction) {
        this.chargingSystemMalfunction = chargingSystemMalfunction;
    }

    public boolean isChargerStopControl() {
        return chargerStopControl;
    }

    public void setChargerStopControl(boolean chargerStopControl) {
        this.chargerStopControl = chargerStopControl;
    }
}
