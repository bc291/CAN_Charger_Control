package com.ktoto.bazio.chargercontrol.Model;


import com.ktoto.bazio.Tools.BitsByIndexRetriever;

public class CarStatusMessage implements BitsByIndexRetriever
{
    private boolean vehicleChargingEnabled; // bit 0; 1 - enabled
    private boolean vehicleShiftLeverPosition; //  bit 1; 0 - "parking" position
    private boolean chargingSystemFault; //  bit 2; 0 - normal
    private boolean vehicleConnectorStatus; //  bit 3; 0 - closed
    private boolean normalStopRequestBefCharging; //  bit 4; 0 - no request
    private boolean[] arrayOfBits;

    public boolean isVehicleChargingEnabled() {
        return vehicleChargingEnabled;
    }

    public void setVehicleChargingEnabled(boolean vehicleChargingEnabled) {
        this.vehicleChargingEnabled = vehicleChargingEnabled;
    }

    public boolean isVehicleShiftLeverPosition() {
        return vehicleShiftLeverPosition;
    }

    public void setVehicleShiftLeverPosition(boolean vehicleShiftLeverPosition) {
        this.vehicleShiftLeverPosition = vehicleShiftLeverPosition;
    }

    public boolean isChargingSystemFault() {
        return chargingSystemFault;
    }

    public void setChargingSystemFault(boolean chargingSystemFault) {
        this.chargingSystemFault = chargingSystemFault;
    }

    public boolean isVehicleConnectorStatus() {
        return vehicleConnectorStatus;
    }

    public void setVehicleConnectorStatus(boolean vehicleConnectorStatus) {
        this.vehicleConnectorStatus = vehicleConnectorStatus;
    }

    public boolean isNormalStopRequestBefCharging() {
        return normalStopRequestBefCharging;
    }

    public void setNormalStopRequestBefCharging(boolean normalStopRequestBefCharging) {
        this.normalStopRequestBefCharging = normalStopRequestBefCharging;
    }


    @Override
    public String toString() {
        return "Status: vehicleChargingEnabled: "+this.vehicleChargingEnabled+" vehicleShiftLeverPosition: "+this.vehicleShiftLeverPosition+" chargingSystemFault: "
                +this.chargingSystemFault+ "vehicleConnectorStatus: "+this.vehicleConnectorStatus+" normalStopRequestBefCharging: "+this.normalStopRequestBefCharging;
    }

    public CarStatusMessage(boolean[] arrayOfBits)
    {
        this.arrayOfBits = arrayOfBits;
        this.setVehicleChargingEnabled(arrayOfBits[0]);
        this.setVehicleShiftLeverPosition(arrayOfBits[1]);
        this.setChargingSystemFault(arrayOfBits[2]);
        this.setVehicleConnectorStatus(arrayOfBits[3]);
        this.setNormalStopRequestBefCharging(arrayOfBits[4]);
    }

    public boolean getBitByIndex(int index)
    {
        if(arrayOfBits.length>0) return this.arrayOfBits[index];
        else return false;
    }
}
