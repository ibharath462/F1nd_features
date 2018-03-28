package v1.f1nd.com.f1nd_newfeatures;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {


    Fragment currentFragment = null;
    android.support.v4.app.FragmentTransaction ft;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                    ft = getSupportFragmentManager().beginTransaction();
                    currentFragment = new home();
                    ft.replace(R.id.content, currentFragment);
                    ft.commit();
                    return true;
                case R.id.navigation_favorites:
                    //Toast.makeText(getApplicationContext(),"Favorites",Toast.LENGTH_SHORT).show();
                    ft = getSupportFragmentManager().beginTransaction();
                    currentFragment = new favorites();
                    ft.replace(R.id.content, currentFragment);
                    ft.commit();
                    return true;
                case R.id.navigation_wod:
                    //Toast.makeText(getApplicationContext(),"WOD",Toast.LENGTH_SHORT).show();
                    ft = getSupportFragmentManager().beginTransaction();
                    currentFragment = new wod();
                    ft.replace(R.id.content, currentFragment);
                    ft.commit();
                    return true;
                case R.id.navigation_settings:
                    ft = getSupportFragmentManager().beginTransaction();
                    currentFragment = new settings();
                    ft.replace(R.id.content, currentFragment);
                    ft.commit();
                    //Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ft = getSupportFragmentManager().beginTransaction();
        currentFragment = new home();
        ft.replace(R.id.content, currentFragment);
        ft.commit();


    }


}
