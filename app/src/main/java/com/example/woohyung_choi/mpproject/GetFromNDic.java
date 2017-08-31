package com.example.woohyung_choi.mpproject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

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
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.e(this.getClass().getName(), "Service Started.");
        try{
            word = intent.getStringExtra("Word");
        } catch(NullPointerException e){
            Log.e(this.getClass().getName(), "I don't get anything");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try{
            String url = new String("http://m.endic.naver.com/search.nhn?searchOption=entryIdiom&query=" + word);
            String toNotification_meaning = new String("");
            String toNotification_word = new String("");

            doc = Jsoup.connect(url).get();
            Elements meanings = doc.select("ul.desc_lst");
            Elements words = doc.select("strong.target");
            Element meaning;
            Element word;

            if(meanings.size() == 0 || words.size() == 0){
                //Toast.makeText(getApplicationContext(), "검색결과가 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                meaning = meanings.get(0);
                word = words.get(0);
                toNotification_meaning = meaning.text();
                toNotification_word = word.text();
            }
            sendNotification(toNotification_word, toNotification_meaning);
        } catch(IOException e){
            Log.e("IO", "Something Wrong with IO");
        }
    }

    private void sendNotification(String _word, String _meaning){
        int notificationID = new Random().nextInt(Integer.MAX_VALUE - 1);

        /* inIntentService로 보낼, Intent와 PendingIntent 생성
         Intent에 word와 string을 putExtra로 같이 붙임*/
        Intent saveIntent = new Intent(this, DBIntentService.class);
        saveIntent.putExtra("word", _word)
                .putExtra("meaning", _meaning)
                .putExtra("notificationID", notificationID)
                .setAction("ACTION_SAVE");
        PendingIntent piSave = PendingIntent.getService(this, 0, saveIntent, PendingIntent.FLAG_ONE_SHOT);

        /* DBIntentService로 보낼, Intent와 PendingIntent 생성
        * 사용자가 SAVE를 선택하지 않았을 경우 생성*/
        Intent notsaveIntent = new Intent(this, DBIntentService.class);
        notsaveIntent.setAction("ACTION_NOT_SAVE")
                    .putExtra("notificationID", notificationID);
        PendingIntent piNotSave = PendingIntent.getService(this, 0, notsaveIntent, PendingIntent.FLAG_ONE_SHOT);

        PendingIntent toMainActivity = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        /*Notification Manager AND Builder*/
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(_word)
                .setContentText(_meaning)
                .setTicker(_word + _meaning)
                .setStyle(new Notification.BigTextStyle().bigText(_meaning))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(toMainActivity)
                .addAction(R.drawable.ic_stat_name, "SAVE", piSave)
                .addAction(R.drawable.ic_stat_name, "NOT SAVE", piNotSave);

        Notification notification = builder.build();
        notificationManager.notify(notificationID, notification);
    }
}

