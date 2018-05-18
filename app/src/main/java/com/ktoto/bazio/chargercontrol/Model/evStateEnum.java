package com.ktoto.bazio.chargercontrol.Model;

/**
 * Created by bazio on 19.05.2018.
 */

public enum evStateEnum {

        STARTUP(0),
        COMPATIBILITY_CHECK(1),
        TRASMIT_CHARGER_PARAMETERS(2),
        RECOGNIZE_START_PERMISSION(3),
        SEND_CHARGING_READY(4),
        WAIT_FOR_ZERO_CURRENT_STATE(5),
        ZERO_CURRENT_STATE(6),
        RECOGNIZE_STOP_CHARGING(7),
        TERMINATE_CHARGING(8),
        RUNNING(9),
        IDDLING(10),
        FAULT(11),
        DELAY_AFTER_EXTERNAL_STOP(12);

    private final int value;
    private evStateEnum(int value)
    {
        this.value =value;
    }

    public int getValue()
    {
        return value;
    }
}
