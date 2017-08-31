package com.example.woohyung_choi.mpproject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.util.Log;

public class DBIntentService extends IntentService {
    public static final String ACTION_SAVE = "ACTION_SAVE";
    public static final String ACTION_NOT_SAVE = "ACTION_NOT_SAVE";
    public static final String DB_NAME = "WORD_LIST.db";

    DBManager dbManager;
    NotificationManager notificationManager;

    public DBIntentService() {
        super("DBIntentService");
        dbManager = new DBManager(this, DB_NAME, null, 1);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_SAVE.equals(action)) {
                Log.d("DBIntentService", "Save");
                dbManager.insert(intent.getStringExtra("word"), intent.getStringExtra("meaning"));
                notificationManager.cancel(intent.getIntExtra("notificationID", -1));
            } else if (ACTION_NOT_SAVE.equals(action)) {
                Log.d("DBIntentService", "NotSave");
                notificationManager.cancel(intent.getIntExtra("notificationID", -1));
            }
        }
    }
}
