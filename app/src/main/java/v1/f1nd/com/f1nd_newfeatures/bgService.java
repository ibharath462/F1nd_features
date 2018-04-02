package v1.f1nd.com.f1nd_newfeatures;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class bgService extends Service {

    wodReceiver alarmReceiver = new wodReceiver();
    public bgService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Notification.Builder builder = new Notification.Builder(this, "1234")
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Hello")
                    .setAutoCancel(true);

            Notification notification = builder.build();
            Toast.makeText(getApplicationContext(),"Oreo / higher",Toast.LENGTH_SHORT).show();
            startForeground(1234, notification);

        } else {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Hello")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            Notification notification = builder.build();

            startForeground(1234, notification);
        }
        alarmReceiver.setAlarm(this);
        Toast.makeText(getApplicationContext(),"F1nd service started",Toast.LENGTH_SHORT).show();
        return START_NOT_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }
}
