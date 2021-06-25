package com.itsabdula.easydictionary;

import android.content.ClipboardManager;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    ClipboardManager clipboard;
    MyClipboardListener myClipBoardListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        myClipBoardListener = new MyClipboardListener(this);
        clipboard.addPrimaryClipChangedListener(myClipBoardListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 0, Menu.NONE, "설정");
        return super.onCreateOptionsMenu(menu);
    }

}

