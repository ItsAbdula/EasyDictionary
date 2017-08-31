package com.example.woohyung_choi.mpproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by WooHyung_Choi on 2017-05-14.
 */

public class DBManager extends SQLiteOpenHelper {
    private static DBManager mInstance;

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE WORD_LIST(_id INTEGER PRIMARY KEY AUTOINCREMENT,word TEXT,meaning TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}


    public void insert(String _word, String _meaning) {
        if(isReduplicate(_word)){
            Log.d("DBManager.insert", "이미 저장된 단어 입니다.");
            //return;
        }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word", _word);
        values.put("meaning", _meaning);

        db.insert("WORD_LIST", null, values);
        db.close();
        Log.d("DBManager.insert", "INSERT " + _word);
    }

    public void delete(int _id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("WORD_LIST", "_id=?", new String[]{String.valueOf(_id)});
        db.close();
    }

    public void delete(String _word) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("WORD_LIST", "WORD=?", new String[]{_word});
        db.close();
    }

    public String getPath(){
        SQLiteDatabase db = getWritableDatabase();
        String path = db.getPath();
        db.close();

        return path;
    }

    public int getSize(){
        SQLiteDatabase db = getReadableDatabase();
        long size = DatabaseUtils.queryNumEntries(db, "WORD_LIST");
        db.close();
        return (int)size;
    }

    public String getWord(int _id){
        Log.d(getClass().getName(), String.valueOf(_id));

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        String word = "";
        cursor = db.query("WORD_LIST", new String[]{"_id", "word"}, "_id=?", new String[]{"3"}, null, null, null, null);

        if(cursor != null){
            Log.d(getClass().getName(), String.valueOf(cursor.getColumnIndex("word")));
            Log.d(getClass().getName() + "Position", String.valueOf(cursor.getPosition()));
            cursor.getString(cursor.getColumnIndex("word"));
            cursor.close();
        }
        db.close();
        return word;
    }

    public String getMeaning(int _id){
        Log.d(getClass().getName(), String.valueOf(_id));

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        String meaning = "";
        try {
            cursor = db.query("WORD_LIST", new String[]{"_id", "meaning"}, "_id=?", new String[]{String.valueOf(_id)}, null, null, null, null);
            if(cursor != null){
                meaning = cursor.getString(cursor.getColumnIndex("meaning"));
                cursor.close();
            }
        }
        catch(Exception e){
            Log.d(getClass().getName(), "MEANING_ERROR");
        }
        return meaning;
    }

    public boolean isReduplicate(String _word){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        try {
            cursor = db.query("WORD_LIST", new String[]{"_id","word"}, "word=?",
                    new String[]{_word}, null, null, null, null);
            if(cursor == null){
                Log.d("DBManager.isReduplicate", "cursor is null");
                return false;
            }
        }
        catch(Exception e){
            db.close();
            return false;
        }
        db.close();
        cursor.close();
        return true;
    }
}
