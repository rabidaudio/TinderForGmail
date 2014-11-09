package com.rabidaudio.dev.tinderforgmail.views;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.rabidaudio.dev.tinderforgmail.Email;
import com.rabidaudio.dev.tinderforgmail.R;

import javax.mail.MessagingException;


public class Card extends CardView {
    private static final String TAG = Card.class.getSimpleName();

    public static final double DISTANCE_THRESH = 0.25;

    public static final int WEST  = 0;
    public static final int NORTH = 1;
    public static final int EAST  = 2;
    public static final int SOUTH = 3;


    private Email email;

    private Paint headerBox = new Paint();

    private TextView sender;
    private TextView subject;
    private TextView body;

    private static final int SHADOW_RADIUS = 4*3;
    private static final float HEADER_SIZE = 0.4f;

//    int start_x;
    int start_y = -1;
    int drag_x = -1;
    int drag_y = -1;

    public Card(Context context) {
        super(context);
        init(context,null, 0);
    }

    public Card(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public Email getEmail(){
        return email;
    }

    public Card(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(final Context context, AttributeSet attrs, int defStyle) {
        headerBox.setColor(randomColor());

        //build layout (so internal views work)
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.card, this, true);

        sender = (TextView) findViewById(R.id.sender);
        subject = (TextView) findViewById(R.id.subject);
        body = (TextView) findViewById(R.id.body);

        //set header container size
        findViewById(R.id.header_container).setMinimumHeight(Math.round(HEADER_SIZE*getMeasuredHeight()));

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
//        return (getTop() + getBottom()) / 2;
        return getTop() + getWidth()/2;
    }

    public void setEmail(Email email){
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

    public void hideText(){
        setBody("");
        setSender("");
        setSubject("");
        invalidate();
    }
    public void showText(){
        setEmail(email);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //make it square but to fit
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int desired = Math.min(widthSize, heightSize);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desired, widthSize);
        } else {
            width = desired;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desired, heightSize);
        } else {
            height = desired;
        }
        int side = Math.min(width,height);
        setMeasuredDimension(side, side);
    }


    private int randomColor(){
//        int[] colors = {R.color.pink, R.color.blue, R.color.green};
//        return getResources().getColor(colors[(int)(Math.random() % 3)]);
        return getResources().getColor(R.color.blue);
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
