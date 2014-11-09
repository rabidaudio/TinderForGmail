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

import javax.mail.MessagingException;


public class Card extends CardView {
    private static final String TAG = Card.class.getSimpleName();

    public static final double DISTANCE_THRESH = 0.25;

    private VEmail email;

    private Paint senderTextPaint = new Paint();
    private Paint headerBox = new Paint();

//    private Drawable shadow;

    private TextView sender;
    private TextView subject;
    private TextView body;

    public float drag_x = 0;
    public float drag_y = 0;
    public float drag_time = 0;

//    private Animator animator = ViewAnimationUtils.

    private static final int SHADOW_RADIUS = 6*3;

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
        senderTextPaint.setTextSize(50f);
//        shadow = getResources().getDrawable(R.drawable.shadow);
//        setBackgroundDrawable(shadow);
        headerBox.setColor(getResources().getColor(R.color.pink));

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.card, this, true);

        sender = (TextView) findViewById(R.id.sender);
        subject = (TextView) findViewById(R.id.subject);
        body = (TextView) findViewById(R.id.body);

        findViewById(R.id.header_container).setMinimumHeight(Math.round(0.45f*getMeasuredHeight()));

        setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent event) {
//                int center_x = (v.getLeft() + v.getRight()) / 2;
//                int center_y = (v.getTop() + v.getBottom()) / 2;
//                float touch_x = event.getX();
//                float touch_y = event.getY();
//                Log.v(TAG, "DRAG: "+center_x+","+center_y+";"+touch_x+","+touch_y);

                Card v = (Card) view;

                Log.v(TAG, "DRAG-EVENT: " + event.getAction());

//                Log.d(TAG, event.getX()+","+event.getY()+","+((v.getLeft() + v.getRight()) / 2)+","+((v.getTop() + v.getBottom()) / 2));
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        v.setVisibility(View.INVISIBLE);
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        //set inital positions
//                        v.drag_x = (v.getLeft()+v.getRight())/2;
//                        v.drag_y = (v.getLeft()+v.getRight())/2;
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:
//                        double d_x = (event.getX() - v.drag_x) / (double) v.getWidth();
//                        double d_y = (event.getY() - v.drag_y) / (double) v.getWidth();
//                        if (Math.abs(d_x) > DISTANCE_THRESH){// || Math.abs(d_y) > DISTANCE_THRESH) {
//                            Utils.Toaster(context, "HIT");
//                            Log.w(TAG, "HIT");
//                        }


                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        //threshold reached - handle


                        int px = ((v.getLeft() + v.getRight()) / 2) - Math.round(event.getX());
                        int py = ((v.getTop() + v.getBottom()) / 2) - Math.round(event.getY());

                        float dx = (v.drag_x - event.getX()) / (System.currentTimeMillis() - v.drag_time);
                        float dy = (v.drag_y - event.getY()) / (System.currentTimeMillis() - v.drag_time);

                        Log.d(TAG, dx+"\t"+dy+"\t:\t"+px+"\t"+py);
//                        return false;

//                        break;
                    case DragEvent.ACTION_DROP:
                        // threshold not reached.put back in place + reassign View to ViewGroup
                        View newView = (View) event.getLocalState();
                        ViewGroup owner = (ViewGroup) newView.getParent();
                        owner.removeView(newView);
                        ((RelativeLayout) ((Activity)context).findViewById(R.id.main_container)).addView(newView);
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
        setOnTouchListener(new View.OnTouchListener() {
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
        c.drawRect(SHADOW_RADIUS, SHADOW_RADIUS, side-SHADOW_RADIUS, 0.4f*(side-SHADOW_RADIUS), headerBox);
        super.onDraw(c);
    }
}
