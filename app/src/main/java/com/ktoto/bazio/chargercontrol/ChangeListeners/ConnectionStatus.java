package com.ktoto.bazio.chargercontrol.ChangeListeners;

import java.util.ArrayList;
import java.util.List;





public class ConnectionStatus {
    private static boolean isBTConnectionAlive;
    private static List<ConnectionChangeListener> listeners = new ArrayList<ConnectionChangeListener>();

    public static boolean getBooleanValue() { return isBTConnectionAlive; }

    public static void setBooleanValue(boolean value) {
        isBTConnectionAlive = value;

        for (ConnectionChangeListener l : listeners) {
            l.onConnectionChanged();
        }
    }

    public static void addMyBooleanListener(ConnectionChangeListener l) {
        listeners.add(l);
    }
}
