package com.rabidaudio.dev.tinderforgmail.views;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rabidaudio.dev.tinderforgmail.R;
import com.rabidaudio.dev.tinderforgmail.VEmail;

import java.util.ArrayList;
import java.util.Random;

import javax.mail.MessagingException;


public class Card extends CardView {
    private static final String TAG = Card.class.getSimpleName();

    public static final double DISTANCE_THRESH = 0.25;

    private static final int WEST  = 0;
    private static final int NORTH = 1;
    private static final int EAST  = 2;
    private static final int SOUTH = 3;


    private VEmail email;

    private Paint headerBox = new Paint();

    private TextView sender;
    private TextView subject;
    private TextView body;

    private static final int SHADOW_RADIUS = 4*3;
    private static final float HEADER_SIZE = 0.4f;

    public Card(Context context) {
        super(context);
        init(context,null, 0);
    }

    public Card(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public Card(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(final Context context, AttributeSet attrs, int defStyle) {
        int[] colors = {R.color.pink, R.color.blue, R.color.green};
        headerBox.setColor(getResources().getColor(colors[(int)(Math.random() % 3)]));

        //build layout (so internal views work)
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.card, this, true);

        sender = (TextView) findViewById(R.id.sender);
        subject = (TextView) findViewById(R.id.subject);
        body = (TextView) findViewById(R.id.body);

        //set header container size
        findViewById(R.id.header_container).setMinimumHeight(Math.round(HEADER_SIZE*getMeasuredHeight()));

        //create drag listeners+handlers for the card
        setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent event) {
                Card v = (Card) view;
//                Log.v(TAG, "DRAG-EVENT: " + event.getAction());
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        v.setVisibility(View.INVISIBLE);
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        //set inital positions
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:

                        int dx = Math.round(event.getX()) - v.getCenterX();
                        int dy = Math.round(event.getY()) - v.getCenterY();
                        Log.w(TAG, event.getY()+"::"+v.getCenterY()+"////"+v.getTop()+","+v.getBottom()+","+v.getHeight());
                        if(distanceFormula(dx, dy) > DISTANCE_THRESH*v.getWidth()){
                            //pulled far enough
                            switch (findDirection(dx, dy)){
                                case WEST:
                                    Log.w(TAG, "MARK READ");
                                    break;
                                case NORTH:
                                    Log.w(TAG, "DELETE");
                                    break;
                                case EAST:
                                    Log.w(TAG, "SAVE FOR LATER");
                                    break;
                                case SOUTH:
                                    Log.w(TAG, "ARCHIVE");
                                    break;
                            }
                        }
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        //threshold reached
                    case DragEvent.ACTION_DROP:
                        // threshold not reached.put back in place + reassign View to ViewGroup
                        resetView(event);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
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
                ((RelativeLayout) ((Activity)context).findViewById(R.id.main_container)).addView(newView);
                newView.setVisibility(View.VISIBLE);
            }
            private double distanceFormula(int a, int b){
                return Math.sqrt(Math.pow(a,2) + Math.pow(b,2));
            }
            //return the proper cardinal direction based on drag offsets 0:1:2:3<=>W:N:E:S
            private int findDirection(int dx, int dy){
                Log.d(TAG, dx+"::"+dy);
                if(Math.abs(dy) - Math.abs(dx) >= 0){
                    return (dy > 0 ? SOUTH : NORTH);
                }else{
                    return (dx > 0 ? EAST : WEST);
                }
            }
        });
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("label", "text"); //todo email content
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);

                    v.startDrag(data, shadowBuilder, v, 0);
                    return true;
                }
                return false;
            }
        });
    }

    public int getCenterX(){
        return  (getLeft() + getRight()) / 2;
    }
    public int getCenterY(){
        return (getTop() + getBottom()) / 2;
//        return getBottom() - getHeight()/2;
    }

    public void setEmail(VEmail email){
        this.email = email;
        try {
            setBody(email.getBody());
            setSender("From: " + email.getSenderName() + "<" + email.getSenderEmail() + ">");
            setSubject(email.getSubject());
            invalidate();
        }catch (MessagingException ex){
            Log.e(TAG, "Couldn't populate card", ex);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //make it square but to fit
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int size = Math.min(widthSize, heightSize);
        setMeasuredDimension(size, size);
    }

    private void setBody(String content){
        body.setText(content);
    }
    private void setSender(String content){
        sender.setText(content);
    }
    private void setSubject(String content){
        subject.setText(content);
    }

    @Override
    protected void onDraw(Canvas c){
        int side = Math.min(getMeasuredWidth(), getMeasuredHeight());
        c.drawRect(SHADOW_RADIUS, SHADOW_RADIUS, side-SHADOW_RADIUS, HEADER_SIZE*(side-SHADOW_RADIUS), headerBox);
        super.onDraw(c);
    }
}
