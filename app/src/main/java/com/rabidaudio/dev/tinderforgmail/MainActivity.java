package com.rabidaudio.dev.tinderforgmail;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sun.mail.gimap.GmailMessage;
import com.sun.mail.imap.IMAPMessage;

import java.util.List;
import java.util.Random;

import javax.mail.Message;
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

        Card2 c = (Card2) findViewById(R.id.c);
//        c.setEmail(new VEmail(null));
        c.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent event) {
//                int center_x = (v.getLeft() + v.getRight()) / 2;
//                int center_y = (v.getTop() + v.getBottom()) / 2;
//                float touch_x = event.getX();
//                float touch_y = event.getY();
//                Log.v(TAG, "DRAG: "+center_x+","+center_y+";"+touch_x+","+touch_y);

                Card2 v = (Card2) view;

                Log.v(TAG, "DRAG-EVENT: "+event.getAction());
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        v.setVisibility(View.INVISIBLE);
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
//                        v.drag_x = event.getX();
//                        v.drag_y = event.getY();
//                        v.drag_time = System.currentTimeMillis();
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:
//                        float dx = (event.getX() - v.drag_x)/v.drag_time;
//                        float dy = (event.getY() - v.drag_y)/v.drag_time;
//                        Log.d(TAG, "dx: "+dx+"  dy: "+dy);
//                        v.drag_x = event.getX();
//                        v.drag_y = event.getY();
//                        v.drag_time = System.currentTimeMillis();
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DROP:
                        // Dropped, reassign View to ViewGroup
                        View newView = (View) event.getLocalState();
                        ViewGroup owner = (ViewGroup) newView.getParent();
                        owner.removeView(newView);
                        ((RelativeLayout) findViewById(R.id.main_container)).addView(newView);
                        newView.setVisibility(View.VISIBLE);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        c.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("label", "text"); //todo email content
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(data, shadowBuilder, v, 0);
//                    v.setVisibility(View.INVISIBLE);
                    return true;
//                }else if(event.getAction() == MotionEvent.ACTION_UP){
////                    v.setVisibility(View.VISIBLE);
//
//                    return true;
                }
                return false;
            }
        });


//        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
//             @Override
//             public void onReceive(Context context, Intent intent) {
//                 String action = intent.getAction();
//                 if(action.equals("CONNECTED")){
//                     Log.d(TAG, "received connected msg");
//                     Intent i = new Intent(MainActivity.this, Mailbox.class);
//                     i.setAction("GET_MAIL");
//                     startService(i);
//                 }
//             }
//         }, new IntentFilter());

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
//        startService(i);

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

//    private void startMain(){
//        //now connections are ready and we can start drawing
//        //TODO make cards, etc
//        try {
//            List<Email> results = mService.getUnreadMail(10);
//            for (Email e : results) {
//                if (e.isRead()) {
//                    Log.d(TAG, e.getSubject());
//                    e.markAsUnread();
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            Log.e(TAG, "open issue", e);
//        }
//    }

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
