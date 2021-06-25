package com.itsabdula.easydictionary;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import java.util.Random;

public class GetFromNDic extends IntentService {


    public GetFromNDic() {
        super("GetFromNDic");
    }

    @Override
    protected void onHandleIntent(@org.jetbrains.annotations.NotNull Intent intent) {

        String word = intent.getStringExtra("Word");
        String url = "http://m.endic.naver.com/search.nhn?searchOption=entryIdiom&query=" + word;
    }

    private void sendNotification(String _word, String _meaning) {
        int notificationID = new Random().nextInt(Integer.MAX_VALUE - 1);

        PendingIntent toMainActivity = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(_word)
                .setContentText(_meaning)
                .setTicker(_word + _meaning)
                .setStyle(new Notification.BigTextStyle().bigText(_meaning))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{0L})
                .setContentIntent(toMainActivity);

        Notification notification = builder.build();
        notificationManager.notify(notificationID, notification);
    }
}

