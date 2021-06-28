package com.itsabdula.easydictionary;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.Nullable;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Random;

public class GetFromNDic extends IntentService {

    private String word;
    Document doc;

    public GetFromNDic() {
        super("GetFromNDic");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            word = intent.getStringExtra("Word");

            String url = new String("http://m.endic.naver.com/search.nhn?searchOption=entryIdiom&query=" + word);
            String toNotification_meaning = new String("");
            String toNotification_word = new String("");

            doc = Jsoup.connect(url).get();
            Elements meanings = doc.select("ul.desc_lst");
            Elements words = doc.select("strong.target");
            Element meaning;
            Element word;

            if (meanings.size() == 0 || words.size() == 0) {
                //Toast.makeText(getApplicationContext(), "검색결과가 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                meaning = meanings.get(0);
                word = words.get(0);
                toNotification_meaning = meaning.text();
                toNotification_word = word.text();
            }
            sendNotification(toNotification_word, toNotification_meaning);
        } catch (IOException e) {
            Log.e("IO", "Something Wrong with IO");
        }
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

