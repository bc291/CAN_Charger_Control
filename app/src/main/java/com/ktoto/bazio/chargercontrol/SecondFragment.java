package com.ktoto.bazio.chargercontrol;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ktoto.bazio.chargercontrol.Fragments.StatisticsFragment;
import com.ktoto.bazio.chargercontrol.asynce.asyncHelper;
import com.ktoto.bazio.chargercontrol.asynce.asyncPost;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class SecondFragment extends Fragment {
    ChargingOperation chargingOperation;
    Animation animationFromLeft, animationFromLeftDelayBy200, animationFromLeftDelayBy400;
    LinearLayout linearFirstTile, linearSecondTile, linearThirdTile;
    Fragment fragment;
    private BottomNavigationView navigation;
    Button button4;
    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";
    NotificationCompat.Builder builder;
    NotificationManager notificationManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View myView = inflater.inflate(R.layout.secondfragment,  null);
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
                if(!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent , 0);

                }

                if(BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                    fragment = new Connect();
                    Connect connect = ((Connect)fragment);
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
               if(builder==null) generateNotification();
               else updatenotification();
                Snackbar snack = Snackbar.make(myView.findViewById(R.id.relativetest4),
                        "Your message", Snackbar.LENGTH_LONG);
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                        snack.getView().getLayoutParams();
                params.setMargins(0, 0,0, navigation.getHeight());
                snack.getView().setLayoutParams(params);
                snack.show();
            }
        });



        return myView;
    }

    private boolean loadFragment(Fragment fragment, int index)
    {
        if(fragment!=null)
        {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container5, fragment).commit();
            navigation.getMenu().getItem(index).setChecked(true);
            return true;
        }
        return false;
    }

public void setNavigationBar(BottomNavigationView navigation)
{
    this.navigation=navigation;
}


public void generateNotification()
{
    notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

        // Configure the notification channel.
        notificationChannel.setDescription("Channel description");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.BLUE);
        //  notificationChannel.setVibrationPattern(new long[]{500});
        notificationChannel.enableVibration(true);
        notificationManager.createNotificationChannel(notificationChannel);
    }


    String title = "Zakończono ładowanie pojazdu: "+"nissan leaf";
    String text = "21.05.2018 16:12:56"+" | "+"5.54"+" zł";


    Rect rect = new Rect(0, 0, 1, 1);

// You then create a Bitmap and get a canvas to draw into it
    Bitmap image = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(image);

//You can get an int value representing an argb color by doing so. Put 1 as alpha by default
    int color = Color.argb(32, 2,2,125);

//Paint holds information about how to draw shapes
    Paint paint = new Paint();
    paint.setColor(color);

// Then draw your shape
    canvas.drawRect(rect, paint);

    builder = new NotificationCompat.Builder(getContext(), NOTIFICATION_CHANNEL_ID)
            // .setVibrate(new long[]{0, 50})
            //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
           // .setDefaults(Notification.DEFAULT_SOUND)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(text)
            .setLargeIcon(image)
            .setOnlyAlertOnce(true)
            .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("21.05.2018 16:12:56"+" | "+"5.54"+" zł\n\n" +
                            "Średnia moc ład.: 34.32 kWh \nDostarczona pojemność: 22.23 kWh\n" +
                            "Czas ładowania: 34.23 m \nPojemność początkowa: 34.23 kWh"));

    notificationManager.notify(NOTIFICATION_ID, builder.build());
}

private int indexer=0;
public void updatenotification()
{
    builder.setContentTitle("Updated "+indexer++);
    notificationManager.notify(NOTIFICATION_ID, builder.build());
}

}
