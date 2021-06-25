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
    /*
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
    */

}

