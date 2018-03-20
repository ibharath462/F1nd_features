package v1.f1nd.com.f1nd_newfeatures;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    SharedPreferences prefs = null;

    static Resources res;

    dbHandler helper;

    static String dbPath,dbName;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        prefs = getSharedPreferences("f1nd.v2.bharath", MODE_PRIVATE);

        res = getResources();

        helper = new dbHandler(MainActivity.this);

        if(prefs.getBoolean("firstrun", true)) {
            //Requesting permissions....
            Log.d("F1nd : MainActivity","Getting permissions");
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            prefs.edit().putBoolean("firstrun", false).commit();
        }else{
            try {
                Log.d("F1nd : MainActivity","Getting meaning");
                JSONArray t = helper.getMeaning("A");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("F1nd : MainActivity","Initiated copy");
                    Toast.makeText(getApplicationContext(),"Permisions granted :-)",Toast.LENGTH_SHORT).show();
                    getPermissions();
                    dbPath = getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
                    dbName = "dict";
                    try {
                        Log.d("F1nd : MainActivity","Initiated copy");
                        copyDataBase();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                return;
            }
        }
    }

    public void getPermissions() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 1234);
                } catch (ActivityNotFoundException e) {
                    Log.d("F1nd : MainActivity", "Exception" + e);
                }

            }


        }
    }

    static void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput;
        myInput = res.openRawResource(R.raw.dict);
        // Path to the just created empty db
        String outFileName = dbPath + dbName;
        Log.d("RestartServiceReceiver", "" + outFileName);
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

}
