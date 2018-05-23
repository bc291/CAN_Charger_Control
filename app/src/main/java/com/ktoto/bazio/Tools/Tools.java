package com.ktoto.bazio.Tools;

import android.util.Log;

import com.ktoto.bazio.chargercontrol.Model.evStateEnum;

import java.util.Arrays;
import java.util.List;


public class Tools {



    public static List<String> retrieveArrayFromRawUartMessage(String data, String characterToSplit)
    {
        String afterSuffixRemoved=null;
        afterSuffixRemoved = data.replace("#", "");
       return  Arrays.asList(afterSuffixRemoved.split(characterToSplit));
    }

    public static boolean[] retrieveBitsFromByte(int byteAsInt)
    {
        boolean[] arrayOfBits = new boolean[5];
        arrayOfBits[0] = (byteAsInt & 0x01) == 1;
        arrayOfBits[1] = ((byteAsInt & 0x02) / 2) == 1;
        arrayOfBits[2] = ((byteAsInt & 0x04) / 4) == 1;
        arrayOfBits[3] = ((byteAsInt & 0x08) / 8) == 1;
        arrayOfBits[4] = ((byteAsInt & 0x010) / 16) == 1;

        String temp="";
        for(boolean tesss : arrayOfBits)
        {
            temp+=tesss+" ";
        }

        Log.d("bity",temp);
        Log.d("bity",String.valueOf(byteAsInt));

        return arrayOfBits;
    }

    public static evStateEnum retrieveEvState(String value)
    {
        evStateEnum evState = evStateEnum.IDDLING;
        switch(Integer.parseInt(value))
        {
            case 0:
                evState = evStateEnum.STARTUP;
                break;
            case 1:
                evState = evStateEnum.COMPATIBILITY_CHECK;
                break;
            case 2:
                evState = evStateEnum.TRASMIT_CHARGER_PARAMETERS;
                break;
            case 3:
                evState = evStateEnum.RECOGNIZE_START_PERMISSION;
                break;
            case 4:
                evState = evStateEnum.SEND_CHARGING_READY;
                break;
            case 5:
                evState = evStateEnum.WAIT_FOR_ZERO_CURRENT_STATE;
                break;
            case 6:
                evState = evStateEnum.ZERO_CURRENT_STATE;
                break;
            case 7:
                evState = evStateEnum.RECOGNIZE_STOP_CHARGING;
                break;
            case 8:
                evState = evStateEnum.TERMINATE_CHARGING;
                break;
            case 9:
                evState = evStateEnum.RUNNING;
                break;
            case 10:
                evState = evStateEnum.IDDLING;
                break;
            case 11:
                evState = evStateEnum.FAULT;
                break;
            case 12:
                evState = evStateEnum.DELAY_AFTER_EXTERNAL_STOP;
                break;

        }
        return evState;
    }
}
