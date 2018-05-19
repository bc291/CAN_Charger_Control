package com.ktoto.bazio.chargercontrol;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.gson.Gson;
import com.ktoto.bazio.chargercontrol.ChangeListeners.ConnectionChangeListener;
import com.ktoto.bazio.chargercontrol.ChangeListeners.ConnectionStatus;
import com.ktoto.bazio.chargercontrol.Model.CarFaultsMessage;
import com.ktoto.bazio.chargercontrol.Model.CarStatusMessage;
import com.ktoto.bazio.chargercontrol.Model.evStateEnum;
import com.ktoto.bazio.chargercontrol.asynce.asyncHelper;
import com.ktoto.bazio.chargercontrol.asynce.asyncPost;
import com.ntt.customgaugeview.library.GaugeView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Connect extends Fragment {



    boolean isChargingActive, wasChargingActive;
    double initialCapacity;

    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    InputStream socketInputStream = null;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter urzadzenieBluetooth = null;
    BluetoothSocket obslugaBluetooth = null;
    static boolean stanPolaczenia = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Button button2, btnPostTest, btnBTOff;
    TextView txtActualBatteryCapacity, txtRemainingTime, txtPower, txtCost, txtInitialBatteryCapacity, txtMaxChargingVoltage, txtTotalBatteryCapacity, txtKwhours, txtCarStatus, txtMaxChargingCurrent;
    TextView txtIsChargingActive, carModelTop, txtEvState;

    GaugeView gaugeView, gaugeView2;
    List<String> testowaLista;
    ChargerData chargerData = new ChargerData();
    CarData carData = new CarData();
    ChargingOperation chargingOperation = new ChargingOperation();
    double energyCost;
    List<Float> averagePowerList;
    double averagePowerFromUART;
    CardView cardView, cardView2;
    Animation animFadein,animFadeinFromRight;
    LinearLayout linearIsChargingActive, gaugesView, linearButtonsBottom, linearEvState;
    evStateEnum evState;
    CarStatusMessage carStatusMessage;
    Dialog dialog, dialog2;
    TextView popup_txtVehicleChargingEnabled, popup_txtVehicleShiftLeverPosition,
            popup_txtChargingSystemFault, popup_txtVehicleConnectorStatus, popup_txtNormalStopRequestBefCharging;
    Button popup_button;
    LinearLayout linear_tile_1, linear_tile_2, linear_tile_3, linear_tile_4, linear_tile_5;
    CarFaultsMessage carFaultsMessage;

    TextView popup_txtBatteryOvervoltage, popup_txtBatteryUnderVoltage,
            popup_txtBatteryCurrentDeviation, popup_txtHighBatteryTemperatury, popup_txtBatteryVoltageDeviation;
    Button popup_button2;
    LinearLayout linear_tile_2_1, linear_tile_2_2, linear_tile_2_3, linear_tile_2_4, linear_tile_2_5;
    TextView txtCarFaults;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_connect, null);
        button2 = (Button)myView.findViewById(R.id.button2);
        txtEvState = (TextView) myView.findViewById(R.id.txtEvState);
        carStatusMessage = new CarStatusMessage();
        txtKwhours = (TextView) myView.findViewById(R.id.txtKwhours);
        txtCarStatus = (TextView) myView.findViewById(R.id.txtCarStatus);
        txtCarFaults = (TextView) myView.findViewById(R.id.txtCarFaults);
        txtMaxChargingCurrent = (TextView) myView.findViewById(R.id.txtMaxChargingCurrent);
        txtInitialBatteryCapacity = (TextView) myView.findViewById(R.id.txtInitialBatteryCapacity);
        btnBTOff = (Button)myView.findViewById(R.id.btnBTOff);
        carModelTop = (TextView) myView.findViewById(R.id.txtCarModelTop);
        btnPostTest = (Button) myView.findViewById(R.id.btnPostTest);
        cardView = (CardView) myView.findViewById(R.id.cardView);
        cardView.setVisibility(View.INVISIBLE);
        cardView2 = (CardView) myView.findViewById(R.id.cardView2);
        cardView2.setVisibility(View.INVISIBLE);
        linearIsChargingActive = (LinearLayout) myView.findViewById(R.id.linearIsChargingActive);
        gaugesView = (LinearLayout) myView.findViewById(R.id.gaugesView);
        linearButtonsBottom = (LinearLayout) myView.findViewById(R.id.linearButtonsBottom);
        linearEvState = (LinearLayout) myView.findViewById(R.id.linearEvState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        energyCost = Double.parseDouble(sharedPreferences.getString("power_cost", "0.51"));
        animFadein = AnimationUtils.loadAnimation(getContext(),
                R.anim.animation_left_to_right);
        animFadeinFromRight = AnimationUtils.loadAnimation(getContext(),
                R.anim.animation_right_to_left);
        //TODO zeby stringow nie wspisywac zabezpieczenie w settings

        txtActualBatteryCapacity = (TextView)myView.findViewById(R.id.txtActualBatteryCapacity);
        txtRemainingTime = (TextView)myView.findViewById(R.id.txtRemainingTime);
        txtPower = (TextView)myView.findViewById(R.id.txtPower);
        txtCost = (TextView)myView.findViewById(R.id.txtCost);
        carFaultsMessage = new CarFaultsMessage();
        txtMaxChargingVoltage = (TextView)myView.findViewById(R.id.txtMaxChargingVoltage);
        txtTotalBatteryCapacity = (TextView)myView.findViewById(R.id.txtTotalBatteryCapacity);

        txtIsChargingActive = (TextView) myView.findViewById(R.id.txtIsChargingActive);
        linearIsChargingActive.setVisibility(View.INVISIBLE);
        linearEvState.setVisibility(View.INVISIBLE);
        gaugesView.setVisibility(View.INVISIBLE);
        linearButtonsBottom.setVisibility(View.INVISIBLE);
        gaugeView  = (GaugeView) myView.findViewById(R.id.gauge_view);
        gaugeView2  = (GaugeView) myView.findViewById(R.id.gauge_view2);

        averagePowerList = new ArrayList<Float>();

        gaugeView.setShowRangeValues(true);
        gaugeView.setTargetValue(0);
        gaugeView2.setShowRangeValues(true);
        gaugeView2.setTargetValue(0);

       // address = "98:D3:31:FC:03:EF";
        address = sharedPreferences.getString("mac_address_hc_05", "98:D3:31:FC:03:EF");
        new ConnectBT().execute();

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.popup_car_status);

        linear_tile_1 = (LinearLayout)dialog.findViewById(R.id.linear_tile_1);
        linear_tile_2 = (LinearLayout)dialog.findViewById(R.id.linear_tile_2);
        linear_tile_3 = (LinearLayout)dialog.findViewById(R.id.linear_tile_3);
        linear_tile_4 = (LinearLayout)dialog.findViewById(R.id.linear_tile_4);
        linear_tile_5 = (LinearLayout)dialog.findViewById(R.id.linear_tile_5);

        popup_txtVehicleChargingEnabled  = (TextView) dialog.findViewById(R.id.txtVehicleChargingEnabled);
        popup_txtVehicleShiftLeverPosition  = (TextView) dialog.findViewById(R.id.txtVehicleShiftLeverPosition);
        popup_txtChargingSystemFault  = (TextView) dialog.findViewById(R.id.txtChargingSystemFault);
        popup_txtVehicleConnectorStatus  = (TextView) dialog.findViewById(R.id.txtVehicleConnectorStatus);
        popup_txtNormalStopRequestBefCharging  = (TextView) dialog.findViewById(R.id.txtNormalStopRequestBefCharging);
        popup_button = (Button) dialog.findViewById(R.id.button_popup_exit);

        popup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });




        dialog2 = new Dialog(getContext());
        dialog2.setContentView(R.layout.popup_car_faults);

        linear_tile_2_1 = (LinearLayout)dialog2.findViewById(R.id.linear_tile_2_1);
        linear_tile_2_2 = (LinearLayout)dialog2.findViewById(R.id.linear_tile_2_2);
        linear_tile_2_3 = (LinearLayout)dialog2.findViewById(R.id.linear_tile_2_3);
        linear_tile_2_4 = (LinearLayout)dialog2.findViewById(R.id.linear_tile_2_4);
        linear_tile_2_5 = (LinearLayout)dialog2.findViewById(R.id.linear_tile_2_5);

        popup_txtBatteryOvervoltage  = (TextView) dialog2.findViewById(R.id.txtBatteryOvervoltage);
        popup_txtBatteryUnderVoltage  = (TextView) dialog2.findViewById(R.id.txtBatteryUnderVoltage);
        popup_txtBatteryCurrentDeviation  = (TextView) dialog2.findViewById(R.id.txtBatteryCurrentDeviation);
        popup_txtHighBatteryTemperatury  = (TextView) dialog2.findViewById(R.id.txtHighBatteryTemperatury);
        popup_txtBatteryVoltageDeviation  = (TextView) dialog2.findViewById(R.id.txtBatteryVoltageDeviation);
        popup_button2 = (Button) dialog2.findViewById(R.id.button_popup_exit_2);

        popup_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.cancel();
            }
        });

        txtCarStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        txtCarFaults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.show();
            }
        });

        ConnectionStatus.addMyBooleanListener(new ConnectionChangeListener() {
            @Override
            public void onConnectionChanged(boolean... isBTConnectionLive) {
                if(ConnectionStatus.getBooleanValue()) carModelTop.setText("Nissan Leaf (Połączono)");
                else carModelTop.setText("Nissan Leaf (Brak połączenia)");
            }
        });

        btnBTOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Wyloguj();
            }
        });

        btnPostTest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                sendUARTStopMessage();


            }
        });

        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                final Handler handler = new Handler();
                final byte delimiter = 10; //This is the ASCII code for a newline character

                stopWorker = false;
                readBufferPosition = 0;
                readBuffer = new byte[1024];
                try {
                    socketInputStream = obslugaBluetooth.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                workerThread = new Thread(new Runnable()
                {
                    public void run()
                    {
                        while(!Thread.currentThread().isInterrupted() && !stopWorker)
                        {
                            try
                            {

                                int bytesAvailable = socketInputStream.available();
                                if(bytesAvailable > 0)
                                {
                                    byte[] packetBytes = new byte[bytesAvailable];
                                    socketInputStream.read(packetBytes);
                                    for(int i=0;i<bytesAvailable;i++)
                                    {
                                        byte b = packetBytes[i];
                                        if(b == delimiter)
                                        {
                                            byte[] encodedBytes = new byte[readBufferPosition];
                                            System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                            final String data = new String(encodedBytes, "US-ASCII").replaceAll("\0", "").replaceAll("inf", "1000");
                                            final int testowo =1;
                                            readBufferPosition = 0;

                                            handler.post(new Runnable()
                                            {
                                                public void run()
                                                {
                                                    String afterSuffixRemoved=null;
                                                    Log.d("logowanie", data);


                                                    if(data !=null && data.length()!=0 && data.charAt(0)=='#') { //data.length()!=0
                                                        afterSuffixRemoved = data.replace("#", "");
                                                        testowaLista = Arrays.asList(afterSuffixRemoved.split(";"));
                                                        chargerData.setAmps(Integer.parseInt(testowaLista.get(0)));
                                                        chargerData.setVoltage(Integer.parseInt(testowaLista.get(1)));
                                                        chargerData.setKwh(round(Float.parseFloat(testowaLista.get(2)),4).floatValue());
                                                        chargerData.setRemainingTime(Float.parseFloat(testowaLista.get(3)));
                                                        chargerData.setActualBatteryCapacity(round(Float.parseFloat(testowaLista.get(4)),2).floatValue());
                                                        chargerData.setPower(chargerData.getAmps()* chargerData.getVoltage());
                                                        chargerData.setAvailableCurrent(Integer.parseInt(testowaLista.get(6)));
                                                        chargerData.setAvailableVoltage(Integer.parseInt(testowaLista.get(7)));
                                                        gaugeView.setTargetValue(chargerData.getAmps());
                                                        gaugeView2.setTargetValue(chargerData.getVoltage());
                                                        txtActualBatteryCapacity.setText(String.valueOf(chargerData.getActualBatteryCapacity())+ " kWh");
                                                        int temp = (int)((chargerData.getRemainingTime()-(int)(chargerData.getRemainingTime()))*60);
                                                        txtRemainingTime.setText(String.valueOf((int)(chargerData.getRemainingTime())+" m ")+temp+" s");
                                                        txtPower.setText(String.valueOf(chargerData.getPower()/1000)+" kW");
                                                        carData.setMaxChargingCurrent(Integer.parseInt(testowaLista.get(6)));
                                                        double money = getMoneyFromKwh(chargerData.getKwh());
                                                        txtCost.setText(getCurrencyFromNumber(money));

                                                        carData.setMaxChargingVoltage(Integer.parseInt(testowaLista.get(8)));
                                                        carData.setTotalBatteryCapacity(Float.parseFloat(testowaLista.get(9))*0.11);
                                                        txtMaxChargingVoltage.setText(String.valueOf(carData.getMaxChargingVoltage())+" V");
                                                        txtTotalBatteryCapacity.setText(String.valueOf(carData.getTotalBatteryCapacity()+" kWh"));
                                                        carData.setCarStatus(Integer.parseInt(testowaLista.get(13)));
                                                        chargerData.setChargingTime(Integer.parseInt(testowaLista.get(10))/1000);
                                                        isChargingActive = Integer.parseInt(testowaLista.get(15)) > 0 ? true : false;
                                                        //textView6.setText(String.valueOf(isChargingActive));
                                                        averagePowerFromUART = Double.parseDouble(testowaLista.get(11));

                                                        if(isChargingActive) { txtIsChargingActive.setText("Ładowanie w toku"); }
                                                        else { txtIsChargingActive.setText("Ładowanie nieaktywne");}
                                                        carData.setFaults(Integer.parseInt(testowaLista.get(14)));
                                                        initialCapacity = round((chargerData.getActualBatteryCapacity()-chargerData.getKwh()),2).doubleValue();
                                                        txtInitialBatteryCapacity.setText(String.valueOf(initialCapacity+ " kWh"));
                                                        if(!wasChargingActive && isChargingActive) wasChargingActive=true;

                                                        if(!isChargingActive && wasChargingActive && money>0.01)
                                                        {
                                                            wasChargingActive=false;
                                                            sendPostMessage();
                                                        }

                                                        if(chargerData.getPower()>0) averagePowerList.add(chargerData.getPower());

                                                        if(isChargingActive)
                                                        {
                                                            txtIsChargingActive.setBackground(getResources().getDrawable(R.drawable.conn_status));
                                                        }

                                                        else
                                                        {
                                                            txtIsChargingActive.setBackground(getResources().getDrawable(R.drawable.conn_status_off));
                                                        }
                                                        Log.d("carStatus", Integer.toBinaryString(carData.getCarStatus()));

                                                        txtKwhours.setText(String.valueOf(round(chargerData.getKwh(),2) + " kWh"));
                                                        txtMaxChargingCurrent.setText(String.valueOf(chargerData.getAvailableCurrent()+ " A")); //bez dodania do struktury

                                                        switch(Integer.parseInt(testowaLista.get(12)))
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
                                                        txtEvState.setText(evState.toString());
                                                    }

                                                    carStatusMessage.setVehicleChargingEnabled((carData.getCarStatus() & 0x01) == 1);
                                                    carStatusMessage.setVehicleShiftLeverPosition((carData.getCarStatus() & 0x02) / 2 == 1);
                                                    carStatusMessage.setChargingSystemFault((carData.getCarStatus() & 0x04) / 4 == 1);
                                                    carStatusMessage.setVehicleConnectorStatus((carData.getCarStatus() & 0x08) / 8 == 1);
                                                    carStatusMessage.setNormalStopRequestBefCharging((carData.getCarStatus() & 0x10) / 16 == 1);



                                                    popup_txtVehicleChargingEnabled.setText(String.valueOf(carStatusMessage.isVehicleChargingEnabled()));
                                                    popup_txtVehicleShiftLeverPosition.setText(String.valueOf(carStatusMessage.isVehicleShiftLeverPosition()));
                                                    popup_txtChargingSystemFault.setText(String.valueOf(carStatusMessage.isChargingSystemFault()));
                                                    popup_txtVehicleConnectorStatus.setText(String.valueOf(carStatusMessage.isVehicleConnectorStatus()));
                                                    popup_txtNormalStopRequestBefCharging.setText(String.valueOf(carStatusMessage.isNormalStopRequestBefCharging()));


                                                    changeTileBackground(linear_tile_1, carStatusMessage.isVehicleChargingEnabled());
                                                    changeTileBackground(linear_tile_2, carStatusMessage.isVehicleShiftLeverPosition());
                                                    changeTileBackground(linear_tile_3, carStatusMessage.isChargingSystemFault());
                                                    changeTileBackground(linear_tile_4, carStatusMessage.isVehicleConnectorStatus());
                                                    changeTileBackground(linear_tile_5, carStatusMessage.isNormalStopRequestBefCharging());

                                                    carFaultsMessage.setBatteryOvervoltage((carData.getFaults() & 0x01) == 1);
                                                    carFaultsMessage.setBatteryUnderVoltage((carData.getFaults() & 0x02) / 2 == 1);
                                                    carFaultsMessage.setBatteryCurrentDeviation((carData.getFaults() & 0x04) / 4 == 1);
                                                    carFaultsMessage.setHighBatteryTemperatury((carData.getFaults() & 0x08) / 8 == 1);
                                                    carFaultsMessage.setBatteryVoltageDeviation((carData.getFaults() & 0x10) / 16 == 1);

                                                    Log.d("faults", carFaultsMessage.toString());

                                                    popup_txtBatteryOvervoltage.setText(String.valueOf(carFaultsMessage.isBatteryOvervoltage()));
                                                    popup_txtBatteryUnderVoltage.setText(String.valueOf(carFaultsMessage.isBatteryUnderVoltage()));
                                                    popup_txtBatteryCurrentDeviation.setText(String.valueOf(carFaultsMessage.isBatteryCurrentDeviation()));
                                                    popup_txtHighBatteryTemperatury.setText(String.valueOf(carFaultsMessage.isHighBatteryTemperatury()));
                                                    popup_txtBatteryVoltageDeviation.setText(String.valueOf(carFaultsMessage.isBatteryVoltageDeviation()));


                                                    changeTileBackground(linear_tile_2_1, carFaultsMessage.isBatteryOvervoltage());
                                                    changeTileBackground(linear_tile_2_2, carFaultsMessage.isBatteryUnderVoltage());
                                                    changeTileBackground(linear_tile_2_3, carFaultsMessage.isBatteryCurrentDeviation());
                                                    changeTileBackground(linear_tile_2_4, carFaultsMessage.isHighBatteryTemperatury());
                                                    changeTileBackground(linear_tile_2_5, carFaultsMessage.isBatteryVoltageDeviation());
                                                }
                                            });
                                        }
                                        else
                                        {
                                            readBuffer[readBufferPosition++] = b;
                                        }
                                    }
                                }
                            }
                            catch (IOException ex)
                            {
                                stopWorker = true;
//                                msg("Exception");
                            }
                        }
                    }
                });

                workerThread.start();

            }
        });

        return myView;
    }

//TODO ten bottom navigartion size w historii ładowania na 50dp to tak średnio





    public double getMoneyFromKwh(double kwH)
    {
        return energyCost*kwH;
    }


    public String getCurrencyFromNumber(double money)
    {
        Locale locale = new Locale("pl", "PL");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        String moneyString = formatter.format(money);
        return moneyString;
    }


    public BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public BigDecimal round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    private void Wyloguj() {
        if (obslugaBluetooth != null) {
            try {
                obslugaBluetooth.close();
               // msg("Wylogowano. W celu połączenia proszę wciśnąć RECONNECT -->");
                ConnectionStatus.setBooleanValue(false);

            } catch (IOException e) {
               // msg("Error");
            }
        }
        //finish();

    }

    private void msg(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean bladPolaczenia = false;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), "Łaczenie", "Proszę czekać");
        }

        @Override

        protected Void doInBackground(Void... devices) {
            try {
                if (obslugaBluetooth == null || !ConnectionStatus.getBooleanValue()) {
                    urzadzenieBluetooth = BluetoothAdapter.getDefaultAdapter();
                    //nowy kod



                    BluetoothDevice dispositivo = urzadzenieBluetooth.getRemoteDevice(address);
                    obslugaBluetooth = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    obslugaBluetooth.connect();
                }
            } catch (IOException e) {
                bladPolaczenia = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (bladPolaczenia) {
                msg("Połączenie nie udane. Powrót do okna głównego.");


                //android.support.v4.app.FragmentManager fm = getFragmentManager();
              //  fm.beginTransaction().replace(R.id.mainNavigation, new SecondFragment()).commit();


            } else {
                msg("Połączono");
                ConnectionStatus.setBooleanValue(true);

                cardView2.startAnimation(animFadein);
                cardView2.setVisibility(View.VISIBLE);
                linearIsChargingActive.startAnimation(animFadein);
                linearIsChargingActive.setVisibility(View.VISIBLE);
                cardView.startAnimation(animFadeinFromRight);
                cardView.setVisibility(View.VISIBLE);
                gaugesView.startAnimation(animFadeinFromRight);
                gaugesView.setVisibility(View.VISIBLE);
                linearButtonsBottom.startAnimation(animFadein);
                linearButtonsBottom.setVisibility(View.VISIBLE);
                linearEvState.startAnimation(animFadein);
                linearEvState.setVisibility(View.VISIBLE);
            }
            progress.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Wyloguj();
    }

    public void sendPostMessage()
    {
        float averagePowerTakenFromList=0f;
        double averagePowerTakenFromListAsDouble=0.0;
        for(float temp : averagePowerList) averagePowerTakenFromList+=temp;


        averagePowerTakenFromList/=averagePowerList.size();
        averagePowerTakenFromListAsDouble = round(averagePowerTakenFromList/1000, 2).doubleValue();

        double elapsTime = (Math.floor(chargerData.getChargingTime()/60));



        chargingOperation.setInitialCapacity(initialCapacity);
        chargingOperation.setAveragePower(round(averagePowerFromUART,2).doubleValue());
        chargingOperation.setCapacityCharged(round(chargerData.getActualBatteryCapacity(),2).doubleValue());
        chargingOperation.setCarModel("nissan leaf");
        chargingOperation.setCost(round(getMoneyFromKwh(chargerData.getKwh()),2).doubleValue());
        chargingOperation.setElapsedTime(elapsTime);


        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date now = Calendar.getInstance().getTime();

        chargingOperation.setDateAndTime(dateFormat.format(now));

        Gson gsonTest = new Gson();

        asyncPost asyncpostt = new asyncPost();
        asyncHelper asyncHelp = new asyncHelper(getActivity(), chargingOperation);
        asyncpostt.execute(asyncHelp);

    }


    public void sendUARTStopMessage()
    {
        OutputStream outputStream=null;
        if (obslugaBluetooth != null && isChargingActive) //isChargingActive
        {
            try {
                outputStream = obslugaBluetooth.getOutputStream();
                msg("Wyslano Wiadomość UART");
                outputStream.write("1".toString().getBytes());
            } catch (IOException e) {
                msg("Błąd");
            }
        }

    }

private void changeTileBackground(LinearLayout linearLayout, boolean changed)
{
    if(changed)
    {
        linearLayout.setBackground(getResources().getDrawable(R.drawable.conn_status));
    }

    else
    {
        linearLayout.setBackground(getResources().getDrawable(R.drawable.conn_status_off));
    }
}
}


