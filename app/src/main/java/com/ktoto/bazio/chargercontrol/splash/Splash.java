package com.ktoto.bazio.chargercontrol.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.ktoto.bazio.chargercontrol.Activities.MainBottom;
import com.ktoto.bazio.chargercontrol.R;

/**
 * Created by bazio on 18.05.2018.
 */

public class Splash extends Activity {
    private ImageView obrazek;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        obrazek = (ImageView) findViewById(R.id.imageView);
        obrazek.setVisibility(View.VISIBLE);

       // Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_left_to_right);
        //obrazek.startAnimation(slide);


        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(Splash.this, MainBottom.class));
                finish();
            }
        }, secondsDelayed * 1000);
    }
}
