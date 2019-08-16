package com.example.tyrone.lowsignalwarning;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Tyrone on 8/15/19.
 */

public class Utils {

    static void showToast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
