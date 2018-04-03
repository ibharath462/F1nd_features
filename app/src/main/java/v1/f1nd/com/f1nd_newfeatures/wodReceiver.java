package v1.f1nd.com.f1nd_newfeatures;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class wodReceiver extends BroadcastReceiver {

    String word,meaning,wordType;
    static Resources res;
    SharedPreferences prefs = null;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        // Put here YOUR code.

        wl.release();

        //Getting a random word...
        databaseHandler dbHandler = new databaseHandler(context);
        JSONObject wod = dbHandler.getWordOfTheDay();
        try {
            word = wod.getString("word");
            meaning = wod.getString("meaning");
            wordType = wod.getString("wordtype");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("F1nd_BCreceiver ","" +wod.toString());

        //Adding to WOD shared preference...
        res = context.getResources();
        prefs = context.getSharedPreferences("f1nd.initial.bharath.newUI", Context.MODE_PRIVATE);
        prefs.edit().putString("wodWord", word).commit();
        prefs.edit().putString("wodWordType",wordType).commit();

        //Creating notification...
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int notifyID = 1;
            String CHANNEL_ID = "my_channel_01";// The id of the channel.
            CharSequence name = "WOD";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            Notification.Style style = new Notification.BigTextStyle().bigText(meaning);
            Notification notification = new Notification.Builder(context)
                    .setContentTitle("" + word)
                    .setContentText("" + meaning)
                    .setSmallIcon(R.drawable.heart_on)
                    .setContentText("" + meaning)
                    .setChannelId(CHANNEL_ID)
                    .setStyle(style)
                    .setOngoing(true)
                    .build();

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);

            mNotificationManager.notify(notifyID,notification);

        } else {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentTitle("WOD_check")
                    .setContentText("Hello WOD")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            Notification notification = builder.build();
            //notification.notify();
        }
    }

    public void setAlarm(Context context)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, wodReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 1, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, wodReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}
