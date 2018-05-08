package com.ktoto.bazio.chargercontrol;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.ktoto.bazio.chargercontrol.asynce.asyncPost;
import com.ntt.customgaugeview.library.GaugeView;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Arrays;
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
    Button button2, btnPostTest;
    TextView textView6, txtActualBatteryCapacity, txtRemainingTime, txtPower, txtCost, txtInitialBatteryCapacity, txtMaxChargingVoltage, txtTotalBatteryCapacity;
    GaugeView gaugeView, gaugeView2;
    List<String> testowaLista;
    ChargerData chargerData = new ChargerData();
    CarData carData = new CarData();
    ChargingOperation chargingOperation = new ChargingOperation();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_connect, null);

        button2 = (Button)myView.findViewById(R.id.button2);

        btnPostTest = (Button) myView.findViewById(R.id.btnPostTest);


        textView6 = (TextView)myView.findViewById(R.id.textView6);
        txtActualBatteryCapacity = (TextView)myView.findViewById(R.id.txtActualBatteryCapacity);
        txtRemainingTime = (TextView)myView.findViewById(R.id.txtRemainingTime);
        txtPower = (TextView)myView.findViewById(R.id.txtPower);
        txtCost = (TextView)myView.findViewById(R.id.txtCost);

        txtMaxChargingVoltage = (TextView)myView.findViewById(R.id.txtMaxChargingVoltage);
        txtTotalBatteryCapacity = (TextView)myView.findViewById(R.id.txtTotalBatteryCapacity);


        gaugeView  = (GaugeView) myView.findViewById(R.id.gauge_view);
        gaugeView2  = (GaugeView) myView.findViewById(R.id.gauge_view2);


        gaugeView.setShowRangeValues(true);
        gaugeView.setTargetValue(0);
        gaugeView2.setShowRangeValues(true);
        gaugeView2.setTargetValue(0);

        address = "98:D3:31:FC:03:EF";
        new ConnectBT().execute();

        textView6.setText("DZIALA?");

        btnPostTest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                sendPostMessage();
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
                                                    textView6.setText(data);

                                                    if(data !=null && data.charAt(0)=='#') {
                                                        afterSuffixRemoved = data.replace("#", "");
                                                        testowaLista = Arrays.asList(afterSuffixRemoved.split(";"));
                                                        chargerData.setAmps(Integer.parseInt(testowaLista.get(0)));
                                                        chargerData.setVoltage(Integer.parseInt(testowaLista.get(1)));
                                                        chargerData.setKwh(round(Float.parseFloat(testowaLista.get(2)),4).floatValue());
                                                        chargerData.setRemainingTime(Float.parseFloat(testowaLista.get(3)));
                                                        chargerData.setActualBatteryCapacity(round(Float.parseFloat(testowaLista.get(4)),2).floatValue());
                                                        chargerData.setPower(chargerData.getAmps()* chargerData.getVoltage());
                                                        chargerData.setAvailableCurrent(Integer.parseInt(testowaLista.get(5)));
                                                        chargerData.setAvailableVoltage(Integer.parseInt(testowaLista.get(6)));
                                                        gaugeView.setTargetValue(chargerData.getAmps());
                                                        gaugeView2.setTargetValue(chargerData.getVoltage());
                                                        txtActualBatteryCapacity.setText(String.valueOf(chargerData.getActualBatteryCapacity())+ " kWh");
                                                        int temp = (int)((chargerData.getRemainingTime()-(int)(chargerData.getRemainingTime()))*60);
                                                        txtRemainingTime.setText(String.valueOf((int)(chargerData.getRemainingTime())+" m ")+temp+" s");
                                                        txtPower.setText(String.valueOf(chargerData.getPower()/1000)+" kWh");

                                                        double money = 0.51 * chargerData.getKwh();
                                                        Locale locale = new Locale("pl", "PL");
                                                        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
                                                        String moneyString = formatter.format(money);
                                                        txtCost.setText(moneyString);

                                                        carData.setMaxChargingVoltage(Integer.parseInt(testowaLista.get(8)));
                                                        carData.setTotalBatteryCapacity(Float.parseFloat(testowaLista.get(9))*0.11);
                                                        txtMaxChargingVoltage.setText(String.valueOf(carData.getMaxChargingVoltage())+" V");
                                                        txtTotalBatteryCapacity.setText(String.valueOf(carData.getTotalBatteryCapacity()+" kWh"));

                                                        chargerData.setChargingTime(Integer.parseInt(testowaLista.get(10))/1000);
                                                        isChargingActive = Integer.parseInt(testowaLista.get(11)) > 0 ? true : false;
                                                        //textView6.setText(String.valueOf(isChargingActive));

                                                        initialCapacity = round((chargerData.getActualBatteryCapacity()-chargerData.getKwh()),2).doubleValue();

                                                        if(!wasChargingActive && isChargingActive) wasChargingActive=true;

                                                        if(!isChargingActive && wasChargingActive)
                                                        {
                                                            wasChargingActive=false;
                                                            sendPostMessage();
                                                        }
                                                    }

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



    public BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    private void Wyloguj() {
        if (obslugaBluetooth != null) {
            try {
                obslugaBluetooth.close();
                //msg("Wylogowano. W celu połączenia proszę wciśnąć RECONNECT -->");

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
            progress = ProgressDialog.show(getActivity(), "Łaczenie", "Proszę upewnić się, że moduł BT został włączony");
        }

        @Override

        protected Void doInBackground(Void... devices) {
            try {
                if (obslugaBluetooth == null || !stanPolaczenia) {
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
                stanPolaczenia = true;
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
        chargingOperation.setInitialCapacity(initialCapacity);
        chargingOperation.setAveragePower((double)chargerData.getPower());
        chargingOperation.setCapacityCharged(round(chargerData.getActualBatteryCapacity(),2).doubleValue());
        chargingOperation.setCarModel("nissan leaf");
        chargingOperation.setCost(round(chargerData.getKwh(),2).doubleValue());
        chargingOperation.setElapsedTime(2323.4343);

        Gson gsonTest = new Gson();

        asyncPost asyncpostt = new asyncPost();
        asyncpostt.execute(chargingOperation);

    }
}


