package com.ktoto.bazio.chargercontrol.Model;

/**
 * Created by bazio on 19.05.2018.
 */

public class CarStatusMessage {
    private boolean vehicleChargingEnabled; // bit 0; 1 - enabled
    private boolean vehicleShiftLeverPosition; //  bit 1; 0 - "parking" position
    private boolean chargingSystemFault; //  bit 2; 0 - normal
    private boolean vehicleConnectorStatus; //  bit 3; 0 - closed
    private boolean normalStopRequestBefCharging; //  bit 4; 0 - no request

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
}
