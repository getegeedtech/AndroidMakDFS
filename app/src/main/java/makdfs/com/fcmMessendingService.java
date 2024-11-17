package makdfs.com;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import io.github.muddz.styleabletoast.StyleableToast;

public class fcmMessendingService extends FirebaseMessagingService {
    Bitmap bitmap;
    User user;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        user = new User(getApplicationContext());
        String heding = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        Uri imgurl = remoteMessage.getNotification().getImageUrl();
        String topic = remoteMessage.getData().get("topic");
        String weburl = remoteMessage.getData().get("weburl");
        if(imgurl!=null) {bitmap = getBitmapfromUrl(imgurl.toString());}
        else {bitmap=null;}

        System.out.println("Hello");
        System.out.println(remoteMessage.getData());
        System.out.println(remoteMessage.getNotification());

        notificationDialog(heding,message,bitmap,topic,weburl);
    }

    private void notificationDialog(String heding, String message,Bitmap image, String topic, String weburl) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "tutorialspoint_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "MyNotification", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            Uri uri = Uri.parse("android.resource://"+this.getPackageName()+"/" + R.raw.notify);
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            notificationChannel.setSound(uri,att);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Tutorialspoint")
                .setContentTitle(heding)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentInfo("Information");

        if (image!=null) {
            notificationBuilder.setLargeIcon(image);
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));
        }

        Intent intendt = new Intent(getApplicationContext(), LiveActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intendt, PendingIntent.FLAG_IMMUTABLE);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationManager.notify(1, notificationBuilder.build());
    }


    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
