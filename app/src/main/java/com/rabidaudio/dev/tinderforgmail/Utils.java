package com.rabidaudio.dev.tinderforgmail;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by charles on 11/8/14.
 */
public class Utils {

    public static void Toaster(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
