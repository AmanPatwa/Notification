package patwa.aman.com.internalnotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MyMessagingService extends FirebaseMessagingService {

    DatabaseReference notiData;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        notiData = (DatabaseReference) FirebaseDatabase.getInstance().getReference("notiData");
        if (remoteMessage.getData().isEmpty()) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData().get("deviceToken"));
//            System.out.println(remoteMessage.getNotification().getClickAction().toString());
        } else {
            showNotification(remoteMessage.getData());
//            System.out.println(remoteMessage.getNotification().getClickAction().toString());
        }


    }

    private void showNotification(String title, String body, String deviceToken) {

        System.out.println("Title:" + title);
        System.out.println("Body:" + body);
        System.out.println("DeviceToken:" + deviceToken);
//        System.out.println("ImageUrl"+imageUrl);


//        Intent i=new Intent(this , MainActivity.class);
//        i.putExtra("phoneno",title);
//        i.putExtra("text",body);
//        i.putExtra("imageUrl",imageUrl);


        Uri uri = Uri.parse("https://wa.me/<" + title + ">/?text=" + body);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        i.setPackage("com.whatsapp");
//        i.putExtra("phoneno", title);
//        i.putExtra("text", body);
//        i.putExtra("deviceToken", deviceToken);

//        addToDb(title,body,deviceToken);
//        startActivity(Intent.createChooser(i, "Share with"));
        PendingIntent pendingIntent = (PendingIntent) PendingIntent.getActivity(this, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "patwa.aman.com.coutloot.test";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Coutloot");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);


        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setOngoing(true)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setContentInfo("Info");

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());

//        startActivity(Intent.createChooser(i, "Share with"));

    }


    public void showNotification(Map<String, String> data) {
        String title = data.get("title").toString();
        String body = data.get("body").toString();
        String deviceToken = data.get("deviceToken").toString();
//        String imageUrl = data.get("image").toString();
//        Uri imageUrl = Uri.parse(data.get("imageUrl").toString());

        System.out.println("Title:" + title);
        System.out.println("Body:" + body);
        System.out.println("DeviceToken:" + deviceToken);
//        System.out.println("ImageUrl"+imageUrl);

//        Intent i=new Intent(this , MainActivity.class);

//        i.putExtra("imageUrl",imageUrl);

        Uri uri = Uri.parse("https://wa.me/<" + title + ">/?text=" + body);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        i.setPackage("com.whatsapp");
//        i.putExtra("phoneno", title);
//        i.putExtra("text", body);
//        i.putExtra("deviceToken", deviceToken);

//        addToDb(title,body,deviceToken);
//        startActivity(Intent.createChooser(i, "Share with"));
        PendingIntent pendingIntent = (PendingIntent) PendingIntent.getActivity(this, 1, i, PendingIntent.FLAG_CANCEL_CURRENT);

        System.out.println("Title:" + title);
        System.out.println("Body:" + body);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "patwa.aman.com.coutloot.test";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Coutloot");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setOngoing(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setContentInfo("Info");

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
//        startActivity(Intent.createChooser(i, "Share with"));

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);


        Log.d("TOKENFIREBASE", s);
    }

    private void addToDb(String phoneNo, String text, String deviceToken) {
        if (phoneNo != null && text != null && deviceToken != null) {
            final HashMap<String, String> data = new HashMap<>();
            data.put("number", phoneNo);
            data.put("mess", text);


            notiData.child(deviceToken).push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.v("Successfully added db", data + "");
                }
            });
        }

        //    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
//    }
//
//    public void showNotification(String title, String message){
//        PendingIntent pi = PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),0);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNotifications")
//                .setContentTitle(title)
//                .setAutoCancel(true)
//                .setContentIntent(pi)
//                .setContentText(message);
//
//        NotificationManagerCompat managerCompat= NotificationManagerCompat.from(this);
//        managerCompat.notify(999,builder.build());
//    }
    }
}
//notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});