package com.example.woohyung_choi.mpproject;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ClipboardManager clipboard;
    MyClipBoardListener myClipBoardListener;
    public static DBManager dbManager;
    ListView listview;
    Cursor cursor;
    DBCursorAdapter dbCursorAdapter;
    List<Integer> itemids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DBManager(MainActivity.this, "WORD_LIST.db", null, 1);

        listview = (ListView) findViewById(R.id.listview);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        itemids = new ArrayList<>();

        /*
        * Listview에 MultiChoiceModeListener추가 Longtouch 했을 때의 조작
        * */
        listview.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedItem = listview.getCheckedItemCount();
                mode.setTitle(checkedItem + " Selected");
                if(checked){
                    itemids.add(position);
                } else{
                    itemids.remove(itemids.indexOf(position));
                }

                for(int i = 0; i < itemids.size(); i++) {Log.d("Selecteditemid", itemids.get(i).toString());}
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.setTitle("Select Word To Delete");
                mode.getMenuInflater().inflate(R.menu.cab_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                menu.findItem(R.id.action_delete_all).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_delete:
                        for(int i = 0; i < itemids.size(); i++) {
                            String selectedWord = (String)((TextView) dbCursorAdapter.getView(itemids.get(i), null, listview).findViewById(R.id.tv_word)).getText();
                            Log.d("MainActivity.onCreate", "ItemLongClick");

                            dbManager.delete(selectedWord);
                            Toast.makeText(getApplicationContext(), selectedWord + ", Deleted", Toast.LENGTH_SHORT).show();
                        }
                        cursor = dbManager.getReadableDatabase().rawQuery("SELECT * FROM WORD_LIST;", null);
                        if(cursor.getCount() >= 0){
                            dbCursorAdapter = new DBCursorAdapter(MainActivity.this, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                            dbCursorAdapter.notifyDataSetChanged();
                            listview.setAdapter(dbCursorAdapter);
                        }

                        itemids.clear();
                        mode.finish();
                        break;

                    case R.id.action_delete_all:
                        deleteDatabase(dbManager.getPath());
                        cursor = dbManager.getReadableDatabase().rawQuery("SELECT * FROM WORD_LIST;", null);
                        if(cursor.getCount() > 0){
                            dbCursorAdapter = new DBCursorAdapter(MainActivity.this, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                            listview.setAdapter(dbCursorAdapter);
                            dbCursorAdapter.notifyDataSetChanged();

                        }
                        mode.finish();
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                itemids.clear();
            }
        });

        cursor = dbManager.getReadableDatabase().rawQuery("SELECT * FROM WORD_LIST;", null);
        if(cursor.getCount() > 0){
            dbCursorAdapter = new DBCursorAdapter(this, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            listview.setAdapter(dbCursorAdapter);
            dbCursorAdapter.notifyDataSetChanged();
        }

        clipboard  = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        myClipBoardListener = new MyClipBoardListener();
        clipboard.addPrimaryClipChangedListener(myClipBoardListener);
    }
    @Override
    protected void onResume() {
        super.onResume();

        dbManager = new DBManager(MainActivity.this, "WORD_LIST.db", null, 1);
        cursor = dbManager.getReadableDatabase().rawQuery("SELECT * FROM WORD_LIST;", null);
        if(cursor.getCount() > 0){
            dbCursorAdapter = new DBCursorAdapter(this, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            listview.setAdapter(dbCursorAdapter);
            dbCursorAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 0, Menu.NONE, "Quiz");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case 0:
                int id_1 = new Random().nextInt(dbManager.getSize() - 1);
                int id_2 = new Random().nextInt(dbManager.getSize() - 1);
                int id_3 = new Random().nextInt(dbManager.getSize() - 1);
                int id_4 = new Random().nextInt(dbManager.getSize() - 1);

                Intent intent = new Intent(this, Quiz.class);

                String[] words = new String[]{dbManager.getWord(id_1), dbManager.getWord(id_2), dbManager.getWord(id_3), dbManager.getWord(id_4)};
                String[] meanings = new String[]{dbManager.getMeaning(id_1), dbManager.getMeaning(id_2), dbManager.getMeaning(id_3), dbManager.getMeaning(id_4)};

                intent.putExtra("words", words);
                intent.putExtra("meanings", meanings);
                startActivity(intent);

                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private final class MyClipBoardListener implements ClipboardManager.OnPrimaryClipChangedListener {
        String mPreviousText = "";

        @Override
        public void onPrimaryClipChanged() {
            // Clipboard에 들어온 것이 MIMETYPE_TEXT_PLAIN과 MIMETYPE_TEXT_HTML이 아닐 때  && !clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML)
            if (!clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                Log.e(this.getClass().getName(), "It's not MImeType_TEXT");
                Log.d(this.getClass().getName(), clipboard.getPrimaryClipDescription().toString());
                Log.d(this.getClass().getName(), clipboard.getPrimaryClip().getItemAt(0).getText().toString());

                return;
            }

            // Clipboard에 들어온 것이 MIMETYPE_TEXT_PLAIN과 MIMETYPE_TEXT_HTML일 때
            else {
                try {
                    ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                    if(item == null) throw new Throwable("NULL_CLIPDATA");
                    String clipDataWord = item.getText().toString();

                    // 앞의 단어와 같은 경우 return;
                    if(mPreviousText.equals(clipDataWord)) throw new Throwable("Duplicated");
                    else {
                        mPreviousText = clipDataWord;

                        Intent intent = new Intent(MainActivity.this, GetFromNDic.class);
                        intent.putExtra("Word", clipDataWord);
                        startService(intent);
                    }
                } catch (NullPointerException e) {
                    Log.e(this.getClass().getName(), "NullptrException");
                } catch(Throwable t){
                    Log.e(this.getClass().getName(), "Duplicated");
                }
            }
        }
    }

}

