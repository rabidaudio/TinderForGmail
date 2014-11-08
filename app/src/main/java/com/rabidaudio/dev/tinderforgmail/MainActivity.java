package com.rabidaudio.dev.tinderforgmail;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import javax.mail.MessagingException;


public class MainActivity extends Activity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GetMail().execute((Integer) null);


    }


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

//    private void toast(String m){
//        Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
//    }

    class GetMail extends AsyncTask<Integer, Void, Void>{
        @Override
        protected Void doInBackground(Integer... params){

            Mailbox m = new Mailbox("[Gmail]/All Mail");//"INBOX");

            try {
                m.connect();
                List<Email> results = m.getUnreadMail(10);
                for(Email e : results){
//                    Log.d(TAG, e.getSubject());
                    if(e.isRead()){
                        Log.d(TAG, e.getSubject());
//                        m.markImportantEmail(e);
//                        m.starEmail(e);
//                        m.deleteEmail(e);
//                        m.markReadEmail(e);
//                        m.unstarEmail(e);
//                        m.markSpam(e);
//                        m.markUnreadEmail(e);



//                        Log.d(TAG, e.getSubject());
//                        e.delete();
                    }
                }
            } catch (MessagingException e) {
                Log.e(TAG, "", e);
            }finally {
                try {
                    m.disconnect();
                } catch (MessagingException e) {
                    Log.e(TAG, "", e);
                }
            }

            return null;
        }
    }
}
