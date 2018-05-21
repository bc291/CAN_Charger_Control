package com.ktoto.bazio.chargercontrol;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ktoto.bazio.chargercontrol.Fragments.StatisticsFragment;

import java.lang.reflect.Field;

public class MainBottom extends AppCompatActivity {

    private TextView mTextMessage;
    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new SecondFragment();
                    ((SecondFragment)fragment).setNavigationBar(navigation);
                   Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container5);
                    if(currentFragment instanceof Connect)
                    {
                        ((Connect) currentFragment).disconnectBT();
                    }
                    break;
                case R.id.navigation_dashboard:
                    fragment = new chargerOperationsList();
                    currentFragment = getSupportFragmentManager().findFragmentById(R.id.container5);
                    if(currentFragment instanceof Connect)
                    {
                        ((Connect) currentFragment).disconnectBT();
                    }
                    break;
                case R.id.navigation_notifications:
                    if(!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent , 0);

                    }
                    currentFragment = getSupportFragmentManager().findFragmentById(R.id.container5);
                    if(currentFragment instanceof Connect)
                    {
                        ((Connect) currentFragment).disconnectBT();
                    }
                    if(BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                        fragment = new Connect();
                        Connect connect = ((Connect)(fragment));
                        connect.setNavigationBar(navigation);

                    }
                    break;
                case R.id.navigation_details:
                    fragment = new StatisticsFragment();
                    currentFragment = getSupportFragmentManager().findFragmentById(R.id.container5);
                    if(currentFragment instanceof Connect)
                    {
                        ((Connect) currentFragment).disconnectBT();
                    }
                    break;
            }
            return loadFragment(fragment);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bottom);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        new BottomNavigationViewHelper().disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SecondFragment secondFragment = new SecondFragment();
        secondFragment.setNavigationBar(navigation);
        loadFragment(secondFragment);
      //  loadFragment(new SecondFragment());
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    //    getSupportActionBar().setLogo(R.drawable.my_logo);
      //  getSupportActionBar().setDisplayUseLogoEnabled(true);
    }


    private boolean loadFragment(Fragment fragment)
    {
        if(fragment!=null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container5, fragment).commit();
            return true;
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Connect connect = new Connect();
            connect.setNavigationBar(navigation);
            loadFragment(connect);
//loadFragment(new Connect());
            navigation.getMenu().getItem(3).setChecked(true);
        }
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Bluetooth jest konieczny do poprawnego działania aplikacji", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainBottom.this, SettingsActivity.class);
            i.putExtra( PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment.class.getName() );
            i.putExtra( PreferenceActivity.EXTRA_NO_HEADERS, true );
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        public void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
              //  Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
               // Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }
    }


}


