package com.example.woohyung_choi.mpproject;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by WooHyung_Choi on 2017-05-14.
 */

public class DBCursorAdapter extends CursorAdapter {
    public DBCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public DBCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView word = (TextView) view.findViewById(R.id.tv_word);
        TextView meaning = (TextView) view.findViewById(R.id.tv_meaning);

        word.setText(cursor.getString(cursor.getColumnIndex("word")));
        meaning.setText(cursor.getString(cursor.getColumnIndex("meaning")));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.word_listview, parent, false);

        return v;
    }
}
