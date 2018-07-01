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

    String word="confers",meaning="have a conference in order to talk something over",wordType="verb";
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

        res = context.getResources();
        prefs = context.getSharedPreferences("f1nd.initial.bharath.newUI", Context.MODE_PRIVATE);

        boolean isLaW = prefs.getBoolean("isLaWEnabled",false);

        boolean isGRE = prefs.getBoolean("isGREEnabled",false);

        if(isLaW){

            //Getting a random word...
            databaseHandler dbHandler = new databaseHandler(context);
            JSONObject wod = null;
            if(isGRE){
                wod = dbHandler.getGREwordOfTheDay();
            }else {
                wod = dbHandler.getWordOfTheDay();
            }
            try {
                    word = wod.getString("word");
                    meaning = wod.getString("meaning");
                    wordType = wod.getString("wordtype");

                meaning = meaning.replaceAll("^ +| +$|( )+", " ");
                meaning = meaning.replace("\n", "").replace("\r", "");
                Log.d("F1nd_BCreceiver ","" +wod.toString());


            } catch (Exception e) {
                e.printStackTrace();
            }

            //Adding to WOD shared preference...
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
                        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                        .setChannelId(CHANNEL_ID)
                        .setStyle(style)
                        .setOngoing(true)
                        .build();

                PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                        new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

                notification.contentIntent = contentIntent;

                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.createNotificationChannel(mChannel);

                mNotificationManager.notify(notifyID,notification);

            } else {

                int notifyID = 1;
                CharSequence name = "WOD";// The user-visible name of the channel.
                Notification.Style style = new Notification.BigTextStyle().bigText(meaning);
                Notification notification = new Notification.Builder(context)
                        .setContentTitle("" + word)
                        .setContentText("" + meaning)
                        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                        .setStyle(style)
                        .setOngoing(true)
                        .build();

                PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                        new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

                notification.contentIntent = contentIntent;

                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                mNotificationManager.notify(notifyID,notification);
            }

        }


    }

    public void setAlarm(Context context)
    {
        prefs = context.getSharedPreferences("f1nd.initial.bharath.newUI", Context.MODE_PRIVATE);
        Long wodInterval = Long.parseLong(prefs.getString("wodInterval", "5"));
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, wodReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * wodInterval, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, wodReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}
