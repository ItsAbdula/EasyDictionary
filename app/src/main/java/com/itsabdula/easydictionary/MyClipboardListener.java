package com.itsabdula.easydictionary;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

/**
 * Created by WooHyung_Choi on 2017-09-01.
 */

public class MyClipboardListener implements ClipboardManager.OnPrimaryClipChangedListener {
    private final Context context;
    String previous = "";
    ClipboardManager clipboard;

    public MyClipboardListener(Context context1) {
        context = context1;
        clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public void onPrimaryClipChanged() {
        if (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == false || clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML) == false) {
            return;
        }

        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
        if (item == null) return;

        String input = item.getText().toString();
        input = input.replaceAll(" ", "");
        if (previous.equals(input)) return;

        previous = input;

        Intent intent = new Intent(context, GetFromNDic.class);
        intent.putExtra("Word", input);

        context.startService(intent);
    }
}
