package com.ktoto.bazio.chargercontrol.Fragments;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ktoto.bazio.Tools.BitsByIndexRetriever;
import com.ktoto.bazio.Tools.Tools;
import com.ktoto.bazio.chargercontrol.Activities.MainBottom;
import com.ktoto.bazio.chargercontrol.Asynce.asyncHelper;
import com.ktoto.bazio.chargercontrol.Asynce.asyncPost;
import com.ktoto.bazio.chargercontrol.ChangeListeners.ConnectionChangeListener;
import com.ktoto.bazio.chargercontrol.ChangeListeners.ConnectionStatus;
import com.ktoto.bazio.chargercontrol.Model.CarData;
import com.ktoto.bazio.chargercontrol.Model.CarFaultsMessage;
import com.ktoto.bazio.chargercontrol.Model.CarStatusMessage;
import com.ktoto.bazio.chargercontrol.Model.ChargerData;
import com.ktoto.bazio.chargercontrol.Model.ChargingOperation;
import com.ktoto.bazio.chargercontrol.Model.ChargingOperationGet;
import com.ktoto.bazio.chargercontrol.Model.evStateEnum;
import com.ktoto.bazio.chargercontrol.R;
import com.ntt.customgaugeview.library.GaugeView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Connect extends Fragment {


    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int NOTIFICATION_ID = 1;
    private static final int NOTIFICATION_ID_2 = 2;
    private static final String NOTIFICATION_CHANNEL_ID = "Charging completion";
    private static final String NOTIFICATION_CHANNEL_ID_2 = "Realtime charging parameters";
    private boolean isChargingActive, wasChargingActive;
    private double initialCapacity;
    private Thread uartThread;
    private byte[] buffer;
    private int bufPos;
    volatile boolean stopUartThread;
    private InputStream socketInputStream;
    private String address;
    BluetoothAdapter bAdapter;
    BluetoothSocket bSocket;
    private Button button2, btnPostTest, btnBTOff;
    private  TextView txtActualBatteryCapacity, txtRemainingTime, txtPower, txtCost, txtInitialBatteryCapacity, txtMaxChargingVoltage, txtTotalBatteryCapacity, txtKwhours, txtCarStatus, txtMaxChargingCurrent;
    private TextView txtIsChargingActive, carModelTop, txtEvState;
    private GaugeView gaugeView, gaugeView2;
    private List<String> dataFromUart;
    private ChargerData chargerData;
    private CarData carData;
    private ChargingOperation chargingOperation;
    private double energyCost;
    private List<Float> averagePowerList;
    private double averagePowerFromUART;
    private CardView cardView, cardView2;
    private Animation animFadeIn, animFadeInFromRight;
    private LinearLayout linearIsChargingActive, gaugesView, linearButtonsBottom, linearEvState;
    private evStateEnum evState;
    private CarStatusMessage carStatusMessage;
    private Dialog dialog, dialog2;
    private  TextView popup_txtVehicleChargingEnabled, popup_txtVehicleShiftLeverPosition,
            popup_txtChargingSystemFault, popup_txtVehicleConnectorStatus, popup_txtNormalStopRequestBefCharging;
    private Button popup_button;
    private LinearLayout linear_tile_1, linear_tile_2, linear_tile_3, linear_tile_4, linear_tile_5;
    private CarFaultsMessage carFaultsMessage;
    private TextView popup_txtBatteryOvervoltage, popup_txtBatteryUnderVoltage,
            popup_txtBatteryCurrentDeviation, popup_txtHighBatteryTemperatury, popup_txtBatteryVoltageDeviation;
    private Button popup_button2;
    private  LinearLayout linear_tile_2_1, linear_tile_2_2, linear_tile_2_3, linear_tile_2_4, linear_tile_2_5;
    private TextView txtCarFaults;
    private BottomNavigationView navigation;
    private ProgressDialog progress;
    private NotificationManager notificationManager, notificationManager2;
    private NotificationCompat.Builder builder2;
    private Bitmap battery20, battery30, battery50,battery60, battery80, battery90, batteryFull;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_connect, null);
        chargerData = new ChargerData();
        carData = new CarData();
        chargingOperation = new ChargingOperation();
        button2 = (Button) myView.findViewById(R.id.button2);
        txtEvState = (TextView) myView.findViewById(R.id.txtEvState);
        //  carStatusMessage = new CarStatusMessage(); @@@@@@@@@@@@@@@@@@@@@
        txtKwhours = (TextView) myView.findViewById(R.id.txtKwhours);
        txtCarStatus = (TextView) myView.findViewById(R.id.txtCarStatus);
        txtCarFaults = (TextView) myView.findViewById(R.id.txtCarFaults);
        txtMaxChargingCurrent = (TextView) myView.findViewById(R.id.txtMaxChargingCurrent);
        txtInitialBatteryCapacity = (TextView) myView.findViewById(R.id.txtInitialBatteryCapacity);
        btnBTOff = (Button) myView.findViewById(R.id.btnBTOff);
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
        animFadeIn = AnimationUtils.loadAnimation(getContext(),
                R.anim.animation_left_to_right);
        animFadeInFromRight = AnimationUtils.loadAnimation(getContext(),
                R.anim.animation_right_to_left);

        txtActualBatteryCapacity = (TextView) myView.findViewById(R.id.txtActualBatteryCapacity);
        txtRemainingTime = (TextView) myView.findViewById(R.id.txtRemainingTime);
        txtPower = (TextView) myView.findViewById(R.id.txtPower);
        txtCost = (TextView) myView.findViewById(R.id.txtCost);
        //carFaultsMessage = new CarFaultsMessage(); @@@@@@@@@@@@@@
        txtMaxChargingVoltage = (TextView) myView.findViewById(R.id.txtMaxChargingVoltage);
        txtTotalBatteryCapacity = (TextView) myView.findViewById(R.id.txtTotalBatteryCapacity);

        txtIsChargingActive = (TextView) myView.findViewById(R.id.txtIsChargingActive);
        linearIsChargingActive.setVisibility(View.INVISIBLE);
        linearEvState.setVisibility(View.INVISIBLE);
        gaugesView.setVisibility(View.INVISIBLE);
        linearButtonsBottom.setVisibility(View.INVISIBLE);
        gaugeView = (GaugeView) myView.findViewById(R.id.gauge_view);
        gaugeView2 = (GaugeView) myView.findViewById(R.id.gauge_view2);

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

        linear_tile_1 = (LinearLayout) dialog.findViewById(R.id.linear_tile_1);
        linear_tile_2 = (LinearLayout) dialog.findViewById(R.id.linear_tile_2);
        linear_tile_3 = (LinearLayout) dialog.findViewById(R.id.linear_tile_3);
        linear_tile_4 = (LinearLayout) dialog.findViewById(R.id.linear_tile_4);
        linear_tile_5 = (LinearLayout) dialog.findViewById(R.id.linear_tile_5);

        popup_txtVehicleChargingEnabled = (TextView) dialog.findViewById(R.id.txtVehicleChargingEnabled);
        popup_txtVehicleShiftLeverPosition = (TextView) dialog.findViewById(R.id.txtVehicleShiftLeverPosition);
        popup_txtChargingSystemFault = (TextView) dialog.findViewById(R.id.txtChargingSystemFault);
        popup_txtVehicleConnectorStatus = (TextView) dialog.findViewById(R.id.txtVehicleConnectorStatus);
        popup_txtNormalStopRequestBefCharging = (TextView) dialog.findViewById(R.id.txtNormalStopRequestBefCharging);
        popup_button = (Button) dialog.findViewById(R.id.button_popup_exit);

        popup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        dialog2 = new Dialog(getContext());
        dialog2.setContentView(R.layout.popup_car_faults);

        linear_tile_2_1 = (LinearLayout) dialog2.findViewById(R.id.linear_tile_2_1);
        linear_tile_2_2 = (LinearLayout) dialog2.findViewById(R.id.linear_tile_2_2);
        linear_tile_2_3 = (LinearLayout) dialog2.findViewById(R.id.linear_tile_2_3);
        linear_tile_2_4 = (LinearLayout) dialog2.findViewById(R.id.linear_tile_2_4);
        linear_tile_2_5 = (LinearLayout) dialog2.findViewById(R.id.linear_tile_2_5);

        popup_txtBatteryOvervoltage = (TextView) dialog2.findViewById(R.id.txtBatteryOvervoltage);
        popup_txtBatteryUnderVoltage = (TextView) dialog2.findViewById(R.id.txtBatteryUnderVoltage);
        popup_txtBatteryCurrentDeviation = (TextView) dialog2.findViewById(R.id.txtBatteryCurrentDeviation);
        popup_txtHighBatteryTemperatury = (TextView) dialog2.findViewById(R.id.txtHighBatteryTemperatury);
        popup_txtBatteryVoltageDeviation = (TextView) dialog2.findViewById(R.id.txtBatteryVoltageDeviation);
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
                if (ConnectionStatus.getBooleanValue())
                    carModelTop.setText("Nissan Leaf (Połączono)");
                else carModelTop.setText("Nissan Leaf (Brak połączenia)");
            }
        });

        btnBTOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

        btnPostTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUARTStopMessage();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchUARTConnection();
            }
        });

        generateNotificationChannel();
        generateNotificationChannel2();

        battery20 = getBitmap(R.drawable.ic_battery_charging_20_black_24dp);
        battery30 = getBitmap(R.drawable.ic_battery_charging_30_black_24dp);
        battery50 = getBitmap(R.drawable.ic_battery_charging_50_black_24dp);
        battery60 = getBitmap(R.drawable.ic_battery_charging_60_black_24dp);
        battery80 = getBitmap(R.drawable.ic_battery_charging_80_black_24dp);
        battery90 = getBitmap(R.drawable.ic_battery_charging_90_black_24dp);
        batteryFull = getBitmap(R.drawable.ic_battery_charging_full_black_24dp);

        return myView;
    }


    //TODO ten bottom navigartion size w historii ładowania na 50dp to tak średnio


    public double getMoneyFromKwh(double kwH) {
        return energyCost * kwH;
    }

    public String getCurrencyFromNumber(double money) {
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

    private void Logout() {
        if (bSocket != null) {
            try {
                bSocket.close();
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

    private void animateItem(Object layoutAsObject, Animation animation) {
        if (layoutAsObject instanceof LinearLayout) {
            LinearLayout linearLayout = (LinearLayout) layoutAsObject;
            linearLayout.startAnimation(animation);
            linearLayout.setVisibility(View.VISIBLE);
        }

        if (layoutAsObject instanceof CardView) {
            CardView cardViewLayout = (CardView) layoutAsObject;
            cardViewLayout.startAnimation(animation);
            cardViewLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (uartThread != null && uartThread.isAlive())
            uartThread.interrupt(); // sprawdz czy thread dziala - problem z niewylaczeniem go miedzy fragmentami
    }

    public void sendPostMessage() {
        float averagePowerTakenFromList = 0f;
        double averagePowerTakenFromListAsDouble = 0.0;
        for (float temp : averagePowerList) averagePowerTakenFromList += temp;


        averagePowerTakenFromList /= averagePowerList.size();
        averagePowerTakenFromListAsDouble = round(averagePowerTakenFromList / 1000, 2).doubleValue();

        double elapsTime = (Math.floor(chargerData.getChargingTime() / 60));


        chargingOperation.setInitialCapacity(initialCapacity);
        chargingOperation.setAveragePower(round(averagePowerFromUART, 2).doubleValue());
        chargingOperation.setCapacityCharged(round(chargerData.getActualBatteryCapacity(), 2).doubleValue());
        chargingOperation.setCarModel("nissan leaf");
        chargingOperation.setCost(round(getMoneyFromKwh(chargerData.getKwh()), 2).doubleValue());
        chargingOperation.setElapsedTime(elapsTime);


        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date now = Calendar.getInstance().getTime();

        chargingOperation.setDateAndTime(dateFormat.format(now));

        asyncPost asyncpostt = new asyncPost()
        {
            @Override
            protected void onPostExecute(String httpResponseString) {
                generateNotification(chargingOperation, httpResponseString);
                Log.d("testhttpresponse", httpResponseString);
            }
        };
        asyncHelper asyncHelp = new asyncHelper(getActivity(), chargingOperation);
        asyncpostt.execute(asyncHelp);



    }

    public void sendUARTStopMessage() {
        OutputStream outputStream = null;
        if (bSocket != null && isChargingActive) //isChargingActive
        {
            try {
                outputStream = bSocket.getOutputStream();
                msg("Wyslano Wiadomość UART");
                outputStream.write("1".toString().getBytes());
            } catch (IOException e) {
                msg("Błąd");
            }
        }
    }

    private void changeTileBackground(LinearLayout linearLayouts[], BitsByIndexRetriever bitsByIndexRetriever) {
        int index = 0;
        for (LinearLayout linearLayout : linearLayouts) {
            if (bitsByIndexRetriever.getBitByIndex(index++))
                linearLayout.setBackground(getResources().getDrawable(R.drawable.conn_status));

            else linearLayout.setBackground(getResources().getDrawable(R.drawable.conn_status_off));
        }
    }

    public void setNavigationBar(BottomNavigationView navigation) {
        this.navigation = navigation;
    }

    public void disconnectBT() {
        Logout();
    }

    private void launchUARTConnection() {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopUartThread = false;
        bufPos = 0;
        buffer = new byte[1024];
        try {
            socketInputStream = bSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        uartThread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopUartThread) {
                    try {

                        int bytesAvailable = socketInputStream.available();
                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            socketInputStream.read(packetBytes);
                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[bufPos];
                                    System.arraycopy(buffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII").replaceAll("\0", "").replaceAll("inf", "1000");
                                    final int testowo = 1;
                                    bufPos = 0;

                                    handler.post(new Runnable() {
                                        public void run() {

                                            Log.d("logowanie", data);


                                            if (data != null && data.length() != 0 && data.charAt(0) == '#') { //data.length()!=0

                                                dataFromUart = Tools.retrieveArrayFromRawUartMessage(data, ";");
                                                setChargerData(dataFromUart);
                                                gaugeView.setTargetValue(chargerData.getAmps());
                                                gaugeView2.setTargetValue(chargerData.getVoltage());
                                                txtActualBatteryCapacity.setText(String.valueOf(chargerData.getActualBatteryCapacity()) + " kWh");
                                                int temp = calculateRemainingTime(chargerData.getRemainingTime());
                                                txtRemainingTime.setText(String.valueOf((int) (chargerData.getRemainingTime()) + " m ") + temp + " s");
                                                txtPower.setText(String.valueOf(chargerData.getPower() / 1000) + " kW");
                                                setCarData(dataFromUart);
                                                double money = getMoneyFromKwh(chargerData.getKwh());
                                                txtCost.setText(getCurrencyFromNumber(money));


                                                txtMaxChargingVoltage.setText(String.valueOf(carData.getMaxChargingVoltage()) + " V");
                                                txtTotalBatteryCapacity.setText(String.valueOf(carData.getTotalBatteryCapacity() + " kWh"));
                                                isChargingActive = Integer.parseInt(dataFromUart.get(15)) > 0 ? true : false;
                                                averagePowerFromUART = Double.parseDouble(dataFromUart.get(11));

                                                if (isChargingActive) {
                                                    txtIsChargingActive.setText("Ładowanie w toku");
                                                } else {
                                                    txtIsChargingActive.setText("Ładowanie nieaktywne");
                                                }

                                                initialCapacity = round((chargerData.getActualBatteryCapacity() - chargerData.getKwh()), 2).doubleValue();
                                                txtInitialBatteryCapacity.setText(String.valueOf(initialCapacity + " kWh"));
                                                if (!wasChargingActive && isChargingActive)
                                                    wasChargingActive = true;

                                                if (!isChargingActive && wasChargingActive && money > 0.01) {
                                                    wasChargingActive = false;
                                                    sendPostMessage();
                                                    updateNotificationOnChargingEnd();
                                                }

                                                if (chargerData.getPower() > 0)
                                                    averagePowerList.add(chargerData.getPower());

                                                if (isChargingActive) {
                                                    txtIsChargingActive.setBackground(getResources().getDrawable(R.drawable.conn_status));
                                                } else {
                                                    txtIsChargingActive.setBackground(getResources().getDrawable(R.drawable.conn_status_off));
                                                }
                                                Log.d("carStatus", Integer.toBinaryString(carData.getCarStatus()));

                                                txtKwhours.setText(String.valueOf(round(chargerData.getKwh(), 2) + " kWh"));
                                                txtMaxChargingCurrent.setText(String.valueOf(chargerData.getAvailableCurrent() + " A")); //bez dodania do struktury

                                                evState = Tools.retrieveEvState(dataFromUart.get(12));
                                                txtEvState.setText(evState.toString());
                                            }


                                            carStatusMessage = new CarStatusMessage(Tools.retrieveBitsFromByte(carData.getCarStatus()));

                                            TextView[] popupTextViews = new TextView[]{popup_txtVehicleChargingEnabled, popup_txtVehicleShiftLeverPosition, popup_txtChargingSystemFault,
                                                    popup_txtVehicleConnectorStatus, popup_txtNormalStopRequestBefCharging};
                                            setPopupTextViewsText(popupTextViews, Tools.retrieveBitsFromByte(carData.getCarStatus()));

                                            LinearLayout[] linearLayoutsStatus = new LinearLayout[]{linear_tile_1, linear_tile_2, linear_tile_3, linear_tile_4, linear_tile_5};
                                            changeTileBackground(linearLayoutsStatus, carStatusMessage);
                                            carFaultsMessage = new CarFaultsMessage(Tools.retrieveBitsFromByte(carData.getFaults()));

                                            TextView[] popup2TextViews = new TextView[]{popup_txtBatteryOvervoltage, popup_txtBatteryUnderVoltage, popup_txtBatteryCurrentDeviation,
                                                    popup_txtHighBatteryTemperatury, popup_txtBatteryVoltageDeviation};
                                            setPopupTextViewsText(popup2TextViews, Tools.retrieveBitsFromByte(carData.getFaults()));

                                            LinearLayout[] linearLayoutsFaults = new LinearLayout[]{linear_tile_2_1, linear_tile_2_2, linear_tile_2_3, linear_tile_2_4, linear_tile_2_5};
                                            changeTileBackground(linearLayoutsFaults, carFaultsMessage);

                                            int batteryPercentCharged =(int) (chargerData.getActualBatteryCapacity() / carData.getTotalBatteryCapacity()*100);
                                            Log.d("bateria", String.valueOf(batteryPercentCharged));
                                            if (isChargingActive) {
                                                if (builder2 == null)
                                                    generateRealtimeNotifications(chargerData, carData);
                                                updatenotification(chargerData, carData, batteryPercentCharged);
                                            }
                                        }
                                    });
                                } else {
                                    buffer[bufPos++] = b;
                                }
                            }
                        }
                    } catch (IOException ex) {
                        stopUartThread = true;
//                                msg("Exception");
                    }
                }
            }
        });

        uartThread.start();
    }

    private void setChargerData(List<String> values)
    {
        chargerData.setAmps(Integer.parseInt(values.get(0)));
        chargerData.setVoltage(Integer.parseInt(values.get(1)));
        chargerData.setKwh(round(Float.parseFloat(values.get(2)), 4).floatValue());
        chargerData.setRemainingTime(Float.parseFloat(values.get(3)));
        chargerData.setActualBatteryCapacity(round(Float.parseFloat(values.get(4)), 2).floatValue());
        chargerData.setPower(chargerData.getAmps() * chargerData.getVoltage());
        chargerData.setAvailableCurrent(Integer.parseInt(values.get(6)));
        chargerData.setAvailableVoltage(Integer.parseInt(values.get(7)));
        chargerData.setChargingTime(Integer.parseInt(dataFromUart.get(10)) / 1000);
    }

    private void setCarData(List<String> values)
    {
        carData.setMaxChargingCurrent(Integer.parseInt(values.get(6)));
        carData.setMaxChargingVoltage(Integer.parseInt(values.get(8)));
        carData.setTotalBatteryCapacity(Float.parseFloat(values.get(9)) * 0.11);
        carData.setCarStatus(Integer.parseInt(values.get(13)));
        carData.setFaults(Integer.parseInt(values.get(14)));
    }

    public void setPopupTextViewsText(TextView[] textViewArray, boolean[] arrayOfBits)
    {
        int iterator = 0;
        for(TextView textView : textViewArray) textView.setText(String.valueOf(arrayOfBits[iterator++]));
    }

    public void generateNotificationChannel() {
        notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notChan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Charging completion", NotificationManager.IMPORTANCE_DEFAULT);
            notChan.setDescription("Charging completion");
            notChan.enableLights(true);
            notChan.setLightColor(Color.BLUE);
            notChan.enableVibration(true);
            notificationManager.createNotificationChannel(notChan);

        }
    }

    public void generateNotificationChannel2() {
        notificationManager2 = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notChan2 = new NotificationChannel(NOTIFICATION_CHANNEL_ID_2, "Realtime charging parameters", NotificationManager.IMPORTANCE_DEFAULT);
            notChan2.setDescription("Realtime charging parameters");
            notChan2.enableLights(true);
            notChan2.setLightColor(Color.BLUE);
            notChan2.enableVibration(false);
            notificationManager2.createNotificationChannel(notChan2);
        }
    }

    public void generateNotification(ChargingOperation chargingOperation, String httpResponseString) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), NOTIFICATION_CHANNEL_ID)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOnlyAlertOnce(true) //@@@@@@@@@@@@@@@@@@@@@@@@@
                .setAutoCancel(true)
                .setContentTitle("Zakończono ładowanie pojazdu: " + chargingOperation.getCarModel())
                .setContentText(chargingOperation.getDateAndTime() + " | " + chargingOperation.getCost() + " zł")
                //   .setLargeIcon(image)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(chargingOperation.getDateAndTime() + " | " + chargingOperation.getCost() + " zł\n\n" +
                                "Średnia moc ład.: " + chargingOperation.getAveragePower() + " kWh \nDostarczona pojemność: " + chargingOperation.getCapacityCharged() + " kWh\n" +
                                "Czas ładowania: " + chargingOperation.getElapsedTime() + " m \nPojemność początkowa: " + chargingOperation.getInitialCapacity() + " kWh"));


        Intent intent = new Intent(getContext(), MainBottom.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("transaction_id", "test");


        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void generateRealtimeNotifications(ChargerData chargerData, CarData cardata) {
        int temp = calculateRemainingTime(chargerData.getRemainingTime());
        builder2 = new NotificationCompat.Builder(getContext(), NOTIFICATION_CHANNEL_ID_2)
                .setLargeIcon(getBitmap(R.drawable.ic_battery_charging_50_black_24dp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Ładowanie aktywne")
                .setVibrate(new long[]{0L})
                .setContentText("Prąd: " + chargerData.getAmps() + "A | Napięcie: " + chargerData.getVoltage() + "V | " + "Pozostało: " + String.valueOf((int) (chargerData.getRemainingTime()) + " m ") + temp + " s");

        notificationManager2.notify(NOTIFICATION_ID_2, builder2.build());
    }

    private int calculateRemainingTime(float rem) {
        return (int) ((rem - (int) (rem)) * 60);
    }

    public void updatenotification(ChargerData chargerData, CarData carData, int batteryPercentCharged)
    {
        Bitmap battery = chooseBitmap(batteryPercentCharged);
        int temp = (int) ((chargerData.getRemainingTime() - (int) (chargerData.getRemainingTime())) * 60);
        builder2.setContentTitle("Ładowanie aktywne   |   "+ batteryPercentCharged+"%");
        builder2.setContentText("Prąd: " + chargerData.getAmps() + "A | Napięcie: " + chargerData.getVoltage() + "V | " + "Pozostało: " + String.valueOf((int) (chargerData.getRemainingTime()) + " m ") + temp + " s");
        builder2.setLargeIcon(battery);
        notificationManager2.notify(NOTIFICATION_ID_2, builder2.build());
    }

    public void updateNotificationOnChargingEnd() {

        notificationManager2.cancel(NOTIFICATION_ID_2);
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
                if (bSocket == null || !ConnectionStatus.getBooleanValue()) {
                    bAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = bAdapter.getRemoteDevice(address);
                    bSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    bSocket.connect();
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
                SecondFragment secondFragment = new SecondFragment();
                secondFragment.setNavigationBar(navigation);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container5, secondFragment).commit();
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container5, new SecondFragment()).commit();
                //android.support.v4.app.FragmentManager fm = getFragmentManager();
                //  fm.beginTransaction().replace(R.id.mainNavigation, new SecondFragment()).commit();

                navigation.getMenu().getItem(0).setChecked(true);
            } else {
                msg("Połączono");
                ConnectionStatus.setBooleanValue(true);

                animateItem(linearIsChargingActive, animFadeIn);
                animateItem(linearEvState, animFadeInFromRight);
                animateItem(gaugesView, animFadeIn);
                animateItem(linearButtonsBottom, animFadeInFromRight);
                animateItem(cardView, animFadeIn);
                animateItem(cardView2, animFadeInFromRight);

                launchUARTConnection();

               // handler = new Handler();
              //  handler.postDelayed(runnable, 0);



            }
            progress.dismiss();
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            if (isChargingActive)
//            {
//            if (builder2 == null)
//            generateRealtimeNotifications(chargerData, carData);
//            updatenotification(chargerData, carData);
//            }
//            handler.postDelayed(this, 200); // reschedule the handler
        }
    };


    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private Bitmap chooseBitmap(int batteryPercentCharged)
    {
        if(batteryPercentCharged >= 0  && batteryPercentCharged < 29) return battery20;
        if(batteryPercentCharged >= 29 && batteryPercentCharged <49) return battery30;
        if(batteryPercentCharged >= 49 && batteryPercentCharged < 59) return battery50;
        if(batteryPercentCharged >= 59 && batteryPercentCharged < 79) return battery60;
        if(batteryPercentCharged >= 79 && batteryPercentCharged < 89) return battery80;
        if(batteryPercentCharged >= 89 && batteryPercentCharged <= 99) return battery90;
        else return batteryFull;
    }
}


