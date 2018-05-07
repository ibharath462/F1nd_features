package v1.f1nd.com.f1nd_newfeatures;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class clipboardService extends Service {

    ClipboardManager clipBoard ;

    ClipboardManager.OnPrimaryClipChangedListener mPrimaryClipChangedListener;

    static Resources res;
    SharedPreferences prefs = null;




    public clipboardService() {
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

        boolean isCopyListenerNeeded = prefs.getBoolean("isCopyListener",false);

        clipBoard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        Toast.makeText(getApplicationContext(),"Listening to your clipboard...",Toast.LENGTH_SHORT).show();

        if(isCopyListenerNeeded){

            mPrimaryClipChangedListener = new ClipboardManager.OnPrimaryClipChangedListener() {
                public void onPrimaryClipChanged() {
                    if (clipBoard.hasPrimaryClip())
                    {
                        // Put your paste code here
                        ClipData clipData = clipBoard.getPrimaryClip();

                        String copiedWord = clipData.getItemAt(0).coerceToText(getApplicationContext()).toString();
                        copiedWord = copiedWord.trim();
                        copiedWord = copiedWord.replaceAll("\\d","");
                        copiedWord = copiedWord.replaceAll("[^a-zA-Z]"," ");
                        copiedWord = copiedWord.replaceAll("^ +| +$|( )+", " ");
                        copiedWord = copiedWord.replace("\n", "").replace("\r", "");

                        int copiedWordLength = copiedWord.split(" " ).length;

                        if(copiedWordLength > 1){
                            //Toast.makeText(getApplicationContext(),"Parser needed",Toast.LENGTH_SHORT).show();
                            prefs.edit().putString("meaningSearchWord", "" + copiedWord).commit();
                            Intent popupMeaning = new Intent(clipboardService.this, v1.f1nd.com.f1nd_newfeatures.parserActivity.class);
                            startActivity(popupMeaning);
                        }else{
                            //Toast.makeText(getApplicationContext(),"NO parser is needed",Toast.LENGTH_SHORT).show();
                            prefs.edit().putString("meaningSearchWord", "" + copiedWord).commit();
                            Intent popupMeaning = new Intent(clipboardService.this, v1.f1nd.com.f1nd_newfeatures.popupMeaning.class);
                            startActivity(popupMeaning);
                        }

                        Log.d("F1nd_clipboardService", copiedWord );
                    }
                }
            };

            clipBoard.addPrimaryClipChangedListener(mPrimaryClipChangedListener);

            createNotification();


        }


        return START_NOT_STICKY;

    }

    public void createNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String CHANNEL_ID = "my_channel_01";// The id of the channel.
            CharSequence name = "WOD";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            Notification.Style style = new Notification.BigTextStyle().bigText("F1nd is listening to your clipboard");
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("F1nd")
                    .setContentText("F1nd is listening to your clipboard")
                    .setSmallIcon(R.drawable.heart_on)
                    .setChannelId(CHANNEL_ID)
                    .setStyle(style)
                    .setOngoing(true)
                    .build();




            startForeground(2, notification);

        } else {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle("F1nd")
                    .setContentText("F1nd is listening to your clipboard")
                    .setSmallIcon(R.drawable.heart_on)
                    .setOngoing(true);

            Notification notification = builder.build();

            startForeground(2, notification);
        }
    }

    @Override
    public void onDestroy() {
        clipBoard.removePrimaryClipChangedListener(mPrimaryClipChangedListener);
    }
}
