package com.ktoto.bazio.chargercontrol;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ktoto.bazio.chargercontrol.Fragments.Connect;
import com.ktoto.bazio.chargercontrol.Fragments.SecondFragment;
import com.ktoto.bazio.chargercontrol.Fragments.chargerOperationsList;

public class MainNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

  //  Button btnBluetooth, sendTest, button3;
   // TextView txtview1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       // btnBluetooth = (Button)findViewById(R.id.button);
       // sendTest = (Button)findViewById(R.id.sendTest);
       // button3 = (Button)findViewById(R.id.button3);
      //  txtview1 = (TextView)findViewById(R.id.textView);

//        btnBluetooth.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent i = new Intent(MainNavigation.this, Connect.class);
//                startActivity(i);
//            }
//        });
//
//        sendTest.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                ChargingOperation chargingOperation = new ChargingOperation();
//                chargingOperation.setAveragePower(2.3);
//                chargingOperation.setCapacityCharged(2);
//                chargingOperation.setCarModel("auto5");
//                chargingOperation.setCost(2323.2323);
//                chargingOperation.setElapsedTime(2323.4343);
//
//                Gson gsonTest = new Gson();
//
//                asyncPost asyncpostt = new asyncPost();
//                asyncpostt.execute(chargingOperation);
//            }
//        });
//
//        button3.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent i = new Intent(MainNavigation.this, chargerOperationsList.class);
//                startActivity(i);
//            }
//        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.main_fragment_menu) {
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.mainNavigation, new SecondFragment()).commit();
        } else if (id == R.id.second_fragment_menu) {
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.mainNavigation, new chargerOperationsList()).commit();
        } else if (id == R.id.bt_connect_fragment_menu) {
            if(!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent , 0);

            }

            if(BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.mainNavigation, new Connect()).commit();

            }


        }  else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.mainNavigation, new Connect()).commit();
        }
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Nie da rady", Toast.LENGTH_SHORT).show();
        }
    }
}

