package com.ktoto.bazio.chargercontrol.Fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ktoto.bazio.chargercontrol.Asynce.asyncHelper;
import com.ktoto.bazio.chargercontrol.Asynce.asyncPost;
import com.ktoto.bazio.chargercontrol.Model.ChargingOperation;
import com.ktoto.bazio.chargercontrol.Activities.MainBottom;
import com.ktoto.bazio.chargercontrol.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class SecondFragment extends Fragment {
    private ChargingOperation chargingOperation;
    private Animation animationFromLeft, animationFromLeftDelayBy200, animationFromLeftDelayBy400;
    private LinearLayout linearFirstTile, linearSecondTile, linearThirdTile;
    private Fragment fragment;
    private BottomNavigationView navigation;
    private Button button4;
    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View myView = inflater.inflate(R.layout.secondfragment, null);
        animationFromLeft = AnimationUtils.loadAnimation(getContext(),
                R.anim.animation_left_to_right);
        animationFromLeftDelayBy200 = AnimationUtils.loadAnimation(getContext(),
                R.anim.animation_left_to_right_delayed_by_200);
        animationFromLeftDelayBy400 = AnimationUtils.loadAnimation(getContext(),
                R.anim.animation_left_to_right_delayed_by_400);
        linearFirstTile = (LinearLayout) myView.findViewById(R.id.linearFirstTile);
        linearFirstTile.setVisibility(View.INVISIBLE);
        linearSecondTile = (LinearLayout) myView.findViewById(R.id.linearSecondTile);
        linearSecondTile.setVisibility(View.INVISIBLE);
        linearThirdTile = (LinearLayout) myView.findViewById(R.id.linearThirdTile);
        linearThirdTile.setVisibility(View.INVISIBLE);

        linearFirstTile.startAnimation(animationFromLeft);
        linearFirstTile.setVisibility(View.VISIBLE);
        linearSecondTile.startAnimation(animationFromLeftDelayBy200);
        linearSecondTile.setVisibility(View.VISIBLE);
        linearThirdTile.startAnimation(animationFromLeftDelayBy400);
        linearThirdTile.setVisibility(View.VISIBLE);

        linearFirstTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new chargerOperationsList();
                loadFragment(fragment, 1);

            }
        });

        linearSecondTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new StatisticsFragment();
                loadFragment(fragment, 2);

            }
        });

        linearThirdTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 0);

                }

                if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                    fragment = new Connect();
                    Connect connect = ((Connect) fragment);
                    connect.setNavigationBar(navigation);
                }

                loadFragment(fragment, 3);
            }
        });


        navigation.getMenu().getItem(0).setChecked(true);

        button4 = (Button) myView.findViewById(R.id.button4);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (builder == null) generateNotification();
//                else updatenotification();
//                Snackbar snack = Snackbar.make(myView.findViewById(R.id.relativetest4),
//                        "Your message", Snackbar.LENGTH_LONG);
//                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
//                        snack.getView().getLayoutParams();
//                params.setMargins(0, 0, 0, navigation.getHeight());
//                snack.getView().setLayoutParams(params);
//                snack.show();
//                DisplayMetrics metrics = new DisplayMetrics();
//                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                float logicalDensity = metrics.density;
//
//
//                int dp = (int) Math.ceil( navigation.getHeight() / logicalDensity);
//                Log.d("wymiary", String.valueOf(dp));
//
//
//
//                int resourceId = getResources().getIdentifier("design_bottom_navigation_height", "dimen", getActivity().getPackageName());
//                int height = 0;
//                if (resourceId > 0) {
//                    height = getResources().getDimensionPixelSize(resourceId);
//                }
//
//                float density = getResources().getDisplayMetrics().density;
//                float dp2 = height / density;
//                Log.d("wymiary", String.valueOf(dp2));


                chargingOperation = new ChargingOperation();
                asyncPost asyncpostt = new asyncPost(){
                    @Override
                    protected void onPostExecute(String s) {
                        generateNotification(chargingOperation, s.replace("added", "").trim());
                        Log.d("testhttpresponse", s.replace("added", "").trim());
                    }
                };




                chargingOperation.setAveragePower(5.0);
                chargingOperation.setCapacityCharged(5.0);
                chargingOperation.setCarModel("nissan leaf http test");
                chargingOperation.setCost(5.0);
                chargingOperation.setDateAndTime("test");
                chargingOperation.setElapsedTime(5.0);
                chargingOperation.setInitialCapacity(5.0);

                asyncHelper asyncHelp = new asyncHelper(getActivity(), chargingOperation);
                asyncpostt.execute(asyncHelp);
            }
        });


        return myView;
    }

    private boolean loadFragment(Fragment fragment, int index) {
        if (fragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container5, fragment).commit();
            navigation.getMenu().getItem(index).setChecked(true);
            return true;
        }
        return false;
    }

    public void setNavigationBar(BottomNavigationView navigation) {
        this.navigation = navigation;
    }


    public void generateNotification(ChargingOperation chargingOperation, String idAsText) {
        notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        String title = "Zakończono ładowanie pojazdu: " + "nissan leaf";
        String text = "21.05.2018 16:12:56" + " | " + "5.54" + " zł";

        Rect rect = new Rect(0, 0, 1, 1);
        Bitmap image = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        int color = Color.argb(32, 2, 2, 125);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rect, paint);

        builder = new NotificationCompat.Builder(getContext(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon(image)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("21.05.2018 16:12:56" + " | " + "5.54" + " zł\n\n" +
                                "Średnia moc ład.: 34.32 kWh \nDostarczona pojemność: 22.23 kWh\n" +
                                "Czas ładowania: 34.23 m \nPojemność początkowa: 34.23 kWh"));

        Intent intent = new Intent(getContext(), MainBottom.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("transaction_id", idAsText);


        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private int indexer = 0;

    public void updatenotification() {
        builder.setContentTitle("Updated " + indexer++);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}
