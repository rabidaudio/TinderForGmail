package com.rabidaudio.dev.tinderforgmail;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by charles on 11/8/14.
 */
public class MailService extends Service {

    private final IBinder mBinder = new LocalBinder();

    private Mailbox mailbox = null;

    public class LocalBinder extends Binder {
        MailService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MailService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate(){
        mailbox = new Mailbox(Mailbox.GMAIL_ALLMAIL);
    }

}
