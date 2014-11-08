package com.rabidaudio.dev.tinderforgmail;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;


public class MainActivity extends Activity {
    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String PREFS_EMAIL = MainActivity.class.getPackage().getName()+".PREFS_EMAIL";
    public static final String PREFS_PASS = MainActivity.class.getPackage().getName()+".PREFS_PASS";
    public static final String PREFS_FOLDER = MainActivity.class.getPackage().getName()+".PREFS_FOLDER";

    private Mailbox mService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getPreferences(MODE_PRIVATE);

        Log.d(TAG, "settings contains? "+settings.contains(PREFS_EMAIL));

        if(!settings.contains(PREFS_EMAIL)){
            startActivityForResult(new Intent(this, SigninActivity.class), 1);
        }

//        new GetMail().execute((Integer) null);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String u = data.getStringExtra(PREFS_EMAIL);
        String p = data.getStringExtra(PREFS_PASS);

        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor e = settings.edit();
        e.putString(PREFS_EMAIL, u);
        e.putString(PREFS_PASS, p);
        e.commit();
        startActivity(new Intent(this, MainActivity.class)); //relaunch
    }

    @Override
    public void onStart(){
        super.onStart();
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        final String u = settings.getString(PREFS_EMAIL, null);
        final String p = settings.getString(PREFS_PASS, null);
        Utils.Toaster(this, u+"+"+p);
        //launch MailService
        Intent i = new Intent(this, Mailbox.class);
        i.putExtra(PREFS_EMAIL, u);
        i.putExtra(PREFS_PASS, p);
        i.putExtra(PREFS_FOLDER, Mailbox.GMAIL_ALLMAIL);
        i.setAction("CONNECT");
        startService(i);

    }

//    @Override
//    public void onStop(){
//        super.onStop();
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startMain(){
        //now connections are ready and we can start drawing
        //TODO make cards, etc
        try {
            List<Email> results = mService.getUnreadMail(10);
            for (Email e : results) {
                if (e.isRead()) {
                    Log.d(TAG, e.getSubject());
                    e.markAsUnread();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "open issue", e);
        }
    }
//    class GetMail extends AsyncTask<Integer, Void, Void>{
//        @Override
//        protected Void doInBackground(Integer... params){
//
//            Mailbox m = new Mailbox("[Gmail]/All Mail");//"INBOX");
//
//            try {
//                m.connect();
//                List<Email> results = m.getUnreadMail(10);
//                for(Email e : results){
////                    Log.d(TAG, e.getSubject());
//                    if(e.isRead()){
//                        Log.d(TAG, e.getSubject());
////                        m.markImportantEmail(e);
////                        m.starEmail(e);
////                        m.deleteEmail(e);
////                        m.markReadEmail(e);
////                        m.unstarEmail(e);
////                        m.markSpam(e);
////                        m.markUnreadEmail(e);
//
//
//
////                        Log.d(TAG, e.getSubject());
////                        e.delete();
//                    }
//                }
//            } catch (MessagingException e) {
//                Log.e(TAG, "", e);
//            }finally {
//                try {
//                    m.disconnect();
//                } catch (MessagingException e) {
//                    Log.e(TAG, "", e);
//                }
//            }
//
//            return null;
//        }
//    }
}
