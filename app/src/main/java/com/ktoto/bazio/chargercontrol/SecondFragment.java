package com.ktoto.bazio.chargercontrol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.ktoto.bazio.chargercontrol.Fragments.StatisticsFragment;
import com.ktoto.bazio.chargercontrol.asynce.asyncHelper;
import com.ktoto.bazio.chargercontrol.asynce.asyncPost;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SecondFragment extends Fragment {
    ChargingOperation chargingOperation;
    Animation animationFromLeft, animationFromLeftDelayBy200, animationFromLeftDelayBy400;
    LinearLayout linearFirstTile, linearSecondTile, linearThirdTile;
    Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.secondfragment,  null);
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
                loadFragment(fragment);
            }
        });

        linearSecondTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new StatisticsFragment();
                loadFragment(fragment);
            }
        });

        linearThirdTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new chargerOperationsList();
                loadFragment(fragment);
            }
        });

        return myView;
    }

    private boolean loadFragment(Fragment fragment)
    {
        if(fragment!=null)
        {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container5, fragment).commit();
            return true;
        }
        return false;
    }
}
