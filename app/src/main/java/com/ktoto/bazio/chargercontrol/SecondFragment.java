package com.ktoto.bazio.chargercontrol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.ktoto.bazio.chargercontrol.asynce.asyncHelper;
import com.ktoto.bazio.chargercontrol.asynce.asyncPost;

public class SecondFragment extends Fragment {
    Button button;
    ChargingOperation chargingOperation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.secondfragment,  null);
        button = (Button) myView.findViewById(R.id.button4);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargingOperation = new ChargingOperation();
                chargingOperation.setInitialCapacity(5.0);
                chargingOperation.setAveragePower(2.0);
                chargingOperation.setCapacityCharged(24.0);
                chargingOperation.setCarModel("nissan leaf555");
                chargingOperation.setCost(232.56);
                chargingOperation.setElapsedTime(23.7);

                Gson gsonTest = new Gson();
                asyncHelper asynchelp = new asyncHelper(getContext(), chargingOperation);


                asyncPost asyncpostt = new asyncPost();
                asyncpostt.execute(asynchelp);
            }
        });




        return myView;
    }


}
