package v1.f1nd.com.f1nd_newfeatures;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity{


    Fragment currentFragment = null;
    android.support.v4.app.FragmentTransaction ft;
    static Resources res;
    SharedPreferences prefs = null;
    BottomNavigationView navigation;
    static String dbPath,dbName;
    int exitCount = 0;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //Toast.makeText(getApplicationContext(),"" + getSupportFragmentManager().getBackStackEntryCount(),Toast.LENGTH_SHORT).show();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ft = getSupportFragmentManager().beginTransaction();
                    currentFragment = new home();
                    ft.replace(R.id.content, currentFragment,"home").addToBackStack("home");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                    exitCount = 0;
                    return true;
                case R.id.navigation_favorites:
                    ft = getSupportFragmentManager().beginTransaction();
                    currentFragment = new favorites();
                    ft.replace(R.id.content, currentFragment,"favorites").addToBackStack("favorites");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                    exitCount = 0;
                    return true;
                case R.id.navigation_wod:
                    ft = getSupportFragmentManager().beginTransaction();
                    currentFragment = new wod();
                    ft.replace(R.id.content, currentFragment,"wod").addToBackStack("wod");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                    exitCount = 0;
                    return true;
                case R.id.navigation_settings:
                    ft = getSupportFragmentManager().beginTransaction();
                    currentFragment = new settings();
                    ft.replace(R.id.content, currentFragment,"settings").addToBackStack("settings");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                    exitCount = 0;
                    return true;
                case R.id.navigation_help:
//                    ft = getSupportFragmentManager().beginTransaction();
//                    currentFragment = new settings();
//                    ft.replace(R.id.content, currentFragment);
//                    ft.commit();
                    exitCount = 0;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        res = getResources();

        CharSequence receivedProcessText = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        getDelegate().setHandleNativeActionModesEnabled(false);

        if(receivedProcessText != null){

            //From Process text...
            Log.d("F1nd_MainActivity", "From processed text...");
            String processedText = receivedProcessText.toString();
            ft = getSupportFragmentManager().beginTransaction();
            currentFragment = new meaning();
            ft.replace(R.id.content, currentFragment,"home").addToBackStack("home");
            ft.commit();

        }else{

            //From UI
            Log.d("F1nd_MainActivity", "From UI...");
            prefs = getSharedPreferences("f1nd.initial.bharath.newUI", MODE_PRIVATE);


            if(prefs.getBoolean("firstrun", true)){
                prefs.edit().putString("wodWord", "Brb").commit();
                prefs.edit().putString("wodWordType", "is").commit();
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                prefs.edit().putBoolean("firstrun", false).commit();
            }else{
                setActionBar("F1nd");
                ft = getSupportFragmentManager().beginTransaction();
                currentFragment = new home();
                ft.replace(R.id.content, currentFragment,"home").addToBackStack("home");
                ft.commit();
            }

        }






    }


    public void setActionBar(String heading) {
        // TODO Auto-generated method stub

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle(heading);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6d4c41")));
        actionBar.hide();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),"Permisions granted :-)",Toast.LENGTH_SHORT).show();
                    dbPath = getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
                    dbName = "dict";
                    try {
                        copyDataBase();
                        getPermissions();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
        }
    }


    static void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput;
        myInput = res.openRawResource(R.raw.dict);
        String outFileName = dbPath + dbName;
        Log.d("F1nd_MainActivity", "" + outFileName);
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }


    public void getPermissions(){

        if(Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {

                try{
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 1234);
                }catch (ActivityNotFoundException e){

                    Log.d("F1nd_MainActivity", "Exception" + e );
                }

            }


        }

    }

    @Override
    public void onBackPressed() {

        exitCount++;
        if(exitCount == 1){
            Toast.makeText(getApplicationContext(), "Press back once again to exit", Toast.LENGTH_SHORT).show();
        }else{
            finish();
        }

    }

}
