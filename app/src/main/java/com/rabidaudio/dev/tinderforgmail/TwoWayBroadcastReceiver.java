package com.rabidaudio.dev.tinderforgmail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by Julian on 8/11/2014.
 *
 * Abstract BroadcastReceiver for Inter-Context messaging. Uses LocalBroadcastManager to send
 * intents, auto-registering itself on construction. Be sure to release() in parent's onDestroy()
 *
 * It parses intents in a standard way and passes the found data to a new onReceive(). Implementations
 * should use this method to receive data.
 *
 * Implementations define a String FILTER which identifies them (which intents they listen too).
 * They also use this value to send out to other TwoWayBroadcastReceivers
 */
public abstract class TwoWayBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = TwoWayBroadcastReceiver.class.getSimpleName();

    //Keys for interface messaging
    private static final String MESSAGE = BuildConfig.PACKAGE_NAME+".MESSAGE";
    private static final String BUNDLE = BuildConfig.PACKAGE_NAME+".BUNDLE";

    private Context context;

    public TwoWayBroadcastReceiver(Context c, String filter){
        this.context = c;
        LocalBroadcastManager.getInstance(context).registerReceiver(this, new IntentFilter(filter));
    }

    protected void sendMessage(String destination, String message, Bundle b){
        Log.d(TAG, "Sending " + message + " to " + destination + (b == null ? "" : " (with data)"));
        Intent i = new Intent(destination);
        i.putExtra(MESSAGE, message);
        if(b!=null) i.putExtra(BUNDLE, b);
        LocalBroadcastManager.getInstance(context).sendBroadcast(i);
    }

    @Override
    public void onReceive(Context context, Intent intent){
        String message = intent.getStringExtra(MESSAGE);
        Bundle bundle = intent.getBundleExtra(BUNDLE);
        Log.d(TAG, "Got message "+message+(bundle==null?"":" (with data)"));
        onReceive(message, bundle);
    }

    public abstract void onReceive(String message, Bundle b);

    public void release(){
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    }
}