package com.itsabdula.easydictionary;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by WooHyung_Choi on 2017-09-01.
 */

public class MyClipboardListener implements ClipboardManager.OnPrimaryClipChangedListener {
    private Context context;
    String mPreviousText = "";
    ClipboardManager clipboard;

    public MyClipboardListener(Context context1) {
        context = context1;
        clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public void onPrimaryClipChanged() {
        // Clipboard에 들어온 것이 MIMETYPE_TEXT_PLAIN과 MIMETYPE_TEXT_HTML이 아닐 때  && !clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML)
        if (!clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) && !clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML)) {
            Log.e(this.getClass().getName(), "It's not MImeType_TEXT");
            Log.d(this.getClass().getName(), clipboard.getPrimaryClipDescription().toString());
            Log.d(this.getClass().getName(), clipboard.getPrimaryClip().getItemAt(0).getText().toString());

            return;
        }

        // Clipboard에 들어온 것이 MIMETYPE_TEXT_PLAIN과 MIMETYPE_TEXT_HTML일 때
        else {
            try {
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                if (item == null) throw new Throwable("NULL_CLIPDATA");
                String clipDataWord = item.getText().toString();

                // 앞의 단어와 같은 경우 return;
                if (mPreviousText.equals(clipDataWord)) throw new Throwable("Duplicated");
                else {
                    mPreviousText = clipDataWord;

                    Intent intent = new Intent(context, GetFromNDic.class);
                    intent.putExtra("Word", clipDataWord);
                    context.startService(intent);
                }
            } catch (NullPointerException e) {
                Log.e(this.getClass().getName(), "NullptrException");
            } catch (Throwable t) {
                Log.e(this.getClass().getName(), "Duplicated");
            }
        }
    }
}
