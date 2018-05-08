package v1.f1nd.com.f1nd_newfeatures;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class bgService extends Service {

    wodReceiver alarmReceiver = new wodReceiver();
    static Resources res;
    SharedPreferences prefs = null;
    public bgService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        res = getResources();
        prefs = getApplicationContext().getSharedPreferences("f1nd.initial.bharath.newUI", Context.MODE_PRIVATE);
        String word="",wordType="";
        databaseHandler dbHandler = new databaseHandler(getApplicationContext());
        JSONObject wod = dbHandler.getWordOfTheDay();
        try {
            word = wod.getString("word");
            wordType = wod.getString("wordtype");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("F1nd_bgServie ","Fetching WOD" +wod.toString());
        prefs.edit().putString("wodWord", word).commit();
        prefs.edit().putString("wodWordType",wordType).commit();

        createNotification();

        if(prefs.getBoolean("isLaWEnabled",false)){
            alarmReceiver.setAlarm(this);
            Toast.makeText(getApplicationContext(),"F1nd service started with" +
                    " LaW",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"F1nd service started without LaW",Toast.LENGTH_SHORT).show();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    public void createNotification(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String CHANNEL_ID = "my_channel_01";// The id of the channel.
            CharSequence name = "WOD";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            Notification.Style style = new Notification.BigTextStyle().bigText("You will get new word notifications at your specified time interval");
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("F1nd")
                    .setContentText("You will get new word notifications at your specified time interval")
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setChannelId(CHANNEL_ID)
                    .setStyle(style)
                    .setOngoing(true)
                    .build();


            startForeground(1, notification);

        } else {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle("F1nd")
                    .setContentText("You will get new word notifications at your specified time interval")
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setOngoing(true);


            Notification notification = builder.build();

            startForeground(1, notification);
        }
    }
}
