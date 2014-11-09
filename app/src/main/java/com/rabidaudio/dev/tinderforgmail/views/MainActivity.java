package com.rabidaudio.dev.tinderforgmail.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rabidaudio.dev.tinderforgmail.Email;
import com.rabidaudio.dev.tinderforgmail.Mailbox;
import com.rabidaudio.dev.tinderforgmail.R;
import com.rabidaudio.dev.tinderforgmail.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;


public class MainActivity extends Activity {
    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String PREFS_EMAIL = MainActivity.class.getPackage().getName()+".PREFS_EMAIL";
    public static final String PREFS_PASS = MainActivity.class.getPackage().getName()+".PREFS_PASS";
    public static final String PREFS_FOLDER = MainActivity.class.getPackage().getName()+".PREFS_FOLDER";

    private Mailbox mService = null;

    String username;
    String password;

    AlertDialog alert;

    int index = 0;
    List<Email> emails;
    Card card;
    TextView count;

    Mailbox m = new Mailbox("[Gmail]/All Mail");//"INBOX");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getPreferences(MODE_PRIVATE);

        Log.d(TAG, "settings contains? "+settings.contains(PREFS_EMAIL));

        if(!settings.contains(PREFS_EMAIL)){
            startActivityForResult(new Intent(this, SigninActivity.class), 1);
        }

        card = (Card) findViewById(R.id.c);
        count = (TextView) findViewById(R.id.count);
        //create drag listeners+handlers for the card
        card.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent event) {
                Card v = (Card) view;
//                Log.v(TAG, "DRAG-EVENT: " + event.getAction());
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
//                        v.setVisibility(View.INVISIBLE);
                        v.hideText();
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        //set inital positions
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        if(v.start_y == -1){
                            v.start_y = Math.round(event.getY());
                            break;
                        }
                        v.drag_x = Math.round(event.getX()) - v.getCenterX();
//                        int dy = Math.round(event.getY()) - v.getCenterY();
                        v.drag_y = Math.round(event.getY()) - v.start_y;
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        //threshold reached
                        break;
                    case DragEvent.ACTION_DROP:
                        // threshold not reached.put back in place + reassign View to ViewGroup
                        v.start_y = -1;
                        resetView(event);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.d(TAG, v.drag_x + ":"+v.drag_y);

//                        if(distanceFormula(dx, dy) > Card.DISTANCE_THRESH*v.getWidth()){
                            //pulled far enough
                            switch (findDirection(v.drag_x, v.drag_y)){
                                case Card.WEST:
                                    Log.w(TAG, "MARK READ");
                                    Utils.Toaster(MainActivity.this, "Marked as read");
                                    new MarkRead().execute(v.getEmail());
                                    break;
                                case Card.NORTH:
                                    Log.w(TAG, "DELETE");
                                    Utils.Toaster(MainActivity.this, "Deleted");
                                    break;
                                case Card.EAST:
                                    Utils.Toaster(MainActivity.this, "Sent to the back");
                                    Log.w(TAG, "SKIP");
                                    break;
                                case Card.SOUTH:
                                    Log.w(TAG, "ARCHIVE");
                                    Utils.Toaster(MainActivity.this, "Archived");
                                    break;
                            }
                            setNextEmail(); //prepare the lower one

//                        }
                        break;
                    default:
                        break;
                }
                return true;

            }

            public void resetView(DragEvent event){
                View newView = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) newView.getParent();
                owner.removeView(newView);
                ((RelativeLayout) findViewById(R.id.main_container)).addView(newView);
//                newView.setVisibility(View.VISIBLE);
                ((Card) newView).showText();
            }
            private double distanceFormula(int a, int b){
                return Math.sqrt(Math.pow(a,2) + Math.pow(b,2));
            }
            //return the proper cardinal direction based on drag offsets 0:1:2:3<=>W:N:E:S
            private int findDirection(int dx, int dy){
                if(Math.abs(dy) - Math.abs(dx) >= 0){
                    return (dy > 0 ? Card.SOUTH : Card.NORTH);
                }else{
                    return (dx > 0 ? Card.EAST : Card.WEST);
                }
            }
        });

        //testing code
//        ArrayList<VEmail> mails = new ArrayList<VEmail>();
//        mails.add(new VEmail("New message from Sneh Parmar\n", "Sneh Parmar\t\n" +
//                "Sneh Parmar\t9:52pm Nov 8\n" +
//                "Hey are you back?\n" +
//                "\n" +
//                "View Conversation on Facebook\n" +
//                "This message was sent to charles@rabidaudio.com. If you don't want to receive these emails from Facebook in the future, please unsubscribe.\n" +
//                "Facebook, Inc., Attention: Department 415, PO Box 10005, Palo Alto, CA 94303", "notification+oivoih6f@facebookmail.com", "Facebook"));
//        mails.add(new VEmail("Marketplace Black November ® Sale: Wallet $9.99 | Barstool $39.95 | Seiko Watch $59.00", "View this email in your browser. Ensure delivery by adding promo@email.newegg.com to your Address Book.\n" +
//                "\t\n" +
//                "\t\t\t\t\t\t\n" +
//                "BLACKOUT SPECIALS. UP TO 85 PERCENT OFF. SHOP NOW.\n" +
//                "All Deals Expire 11:59PM PT, 11/15/2014\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "Save up to 50%\t \tSave up to 70%\n" +
//                "\n" +
//                "DXRACER Gaming Chairs Sale\n" +
//                "\n" +
//                " \tExplore Even More In Home & Outdoors\t \n" +
//                " \t \t \n" +
//                " \t\n" +
//                "Home Improvement\tHome Living\tOutdoor Entertainment\tAppliances\n" +
//                "Hand & Power Tools\tKitchen\tGarden Center\tFloor Care\n" +
//                "Hardware\tFurniture\tBBQ Grills\tSmall Appliances\n" +
//                "Light Bulbs\tBath\tOutdoor Audio\tHeating & Cooling\n" +
//                "Surveillance\tLighting & Fans\tOutdoor Power\tCooking\n" +
//                " \t \t \t \n" +
//                " \n" +
//                " \t \t \n" +
//                " \t\n" +
//                "Contact Customer Service\tSend Feedback to:\tWrite To Us:\n" +
//                "Newegg.com, 9997 Rose Hills Road, Whittier, CA. 90601\n" +
//                " \tnewsletter@newegg.com\n" +
//                " \n" +
//                " \t \t \n" +
//                " \t\n" +
//                "All offers expire 11:59 PM PT, 11/15/2014. Standard terms and conditions resume upon expiration. Valid only on current stock. Promo codes may not be combined with other promo codes on a single item and can only be used once per registered account. However, unless otherwise noted on the product page , customers may purchase up to five (5) pieces of the same product at the promo code price as long as all pieces are purchased in the same order.Only one promo code will be applied per item per order, even if the item is eligible for other promo codes entered at time of purchase. Newegg.com is not responsible for any typographical errors in this newsletter. \n" +
//                "\n" +
//                "Like our deals but don’t want to receive as many e-mails? Manage your e-mail subscriptions here. Or, you may unsubscribe.\n" +
//                "\n" +
//                " \n" +
//                " \tNewegg.com | Policy and Agreement | Privacy Policy | © 2000-2014 Newegg Inc. All rights reserved.\t", "Promo@email.newegg.com", "Newegg.com"));
//        mails.add(new VEmail("Jackets Win! Kick-off vs. Clemson set for Noon", "If you are unable to view this message correctly, click here.\n" +
//                "\n" +
//                "\n" +
//                "Georgia Tech Yellow Jackets\n" +
//                "\n" +
//                "\n" +
//                "#BuzzRewind: No. 24 Football Stomps State, 56-23\n" +
//                "\n" +
//                "Next home game:\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "GEORGIA TECH vs. CLEMSON\n" +
//                "Noon ET // Saturday, November 15 \n" +
//                "\n" +
//                "Get your Tickets Starting at $75 - Limited Lower Level Seats Available\n" +
//                "\n" +
//                "Copyright 2014, Georgia Tech. The team names, logos and uniform designs are registered trademarks of the teams indicated. No logos, photographs or graphics on this site may be reproduced without written permission. All rights reserved.\n" +
//                "You are receiving this message because you have been in contact with \n" +
//                "Georgia Tech or an affiliate.\n" +
//                "\n" +
//                "To manage your email preferences or unsubscribe, please click here.", "updates@gtathletics.fan-one.com", "Georgia Tech Football"));
////        mails.add(new VEmail("Hello World", "here is a body", "cjk@gatech.edu", "Charles Julian Knight"));

        alert = new AlertDialog.Builder(this).create();
        TextView v = new TextView(this);
//        v.setImageDrawable(getResources().getDrawable(R.drawable.load));
        v.setText("Grabbing your email, just a sec...");
        alert.setView(v);
        alert.show();
        new GetMail().execute();


//        handleEmailList(mails);
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
//        Utils.Toaster(this, u + "+" + p);
        username = u;
        password = p;

        //launch getEmails

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

    public void handleEmailList(List<Email> emails){
        this.emails = emails;
        index = 0;
        card.setEmail(emails.get(index));
        count.setText(String.valueOf(emails.size() - index));
    }

    @Override
    public void onStop(){
        try {
            m.disconnect();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void setNextEmail(){
        index++;
        if(emails.size()-1 < index){
            //todo display no more!
            card.setVisibility(View.INVISIBLE);
            count.setText("No more emails here :)");
            findViewById(R.id.chain).setVisibility(View.INVISIBLE);
        }else{
            card.setEmail(emails.get(index));
            count.setText(String.valueOf(emails.size() - index));
        }
    }

    class GetMail extends AsyncTask<Void, Void, List<Email>> {
        @Override
        protected List<Email> doInBackground(Void... params){

//            Mailbox m = new Mailbox("[Gmail]/All Mail");//"INBOX");
            List<Email> unread = new ArrayList<Email>();
            try {
                m.connect(username, password);
                List<Email> results = m.getUnreadMail(50);

                for(Email e : results){
//                    Log.d(TAG, e.getSubject());
                    if(!e.isRead()){
                        Log.d(TAG, e.getSubject());
                        unread.add(e);
//                        m.markImportantEmail(e);
//                        m.starEmail(e);
//                        m.deleteEmail(e);
//                        m.markReadEmail(e);
//                        m.unstarEmail(e);
//                        m.markSpam(e);
//                        m.markUnreadEmail(e);
                    }
                }
            } catch (MessagingException e) {
                Log.e(TAG, "", e);
//            }finally {
//                try {
//                    m.disconnect();
//                } catch (MessagingException e) {
//                    Log.e(TAG, "", e);
//                }
            }

            return unread;
        }

        @Override
        protected void onPostExecute(List<Email> results){
            alert.hide();
            handleEmailList(results);
        }
    }


    class MarkRead extends AsyncTask<Email, Void, Void> {
        @Override
        protected Void doInBackground(Email... params){


            try {
//                m.connect(username, password);
                for(Email e : params){
                    m.markReadEmail(e);
                }
            } catch (MessagingException e) {
                Log.e(TAG, "", e);
//            }finally {
//                try {
//                    m.disconnect();
//                } catch (MessagingException e) {
//                    Log.e(TAG, "", e);
//                }
            }
            return null;
        }
    }
}
