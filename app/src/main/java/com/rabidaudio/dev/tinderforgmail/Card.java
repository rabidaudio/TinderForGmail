package com.rabidaudio.dev.tinderforgmail;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Card extends LinearLayout {
    private static final String TAG = Card.class.getSimpleName();

    private VEmail email;

    private Paint senderTextPaint = new Paint();
    private Paint headerBox = new Paint();

    private Drawable shadow;

    private TextView sender;
    private TextView subject;
    private TextView body;

    private static final int SHADOW_RADIUS = 8*3;

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

    private void init(Context context, AttributeSet attrs, int defStyle) {
        senderTextPaint.setTextSize(50f);
        shadow = getResources().getDrawable(R.drawable.shadow);
        setBackgroundDrawable(shadow);
        headerBox.setColor(getResources().getColor(R.color.lightblue));

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.card, this, true);

        sender = (TextView) findViewById(R.id.sender);
        subject = (TextView) findViewById(R.id.subject);
        body = (TextView) findViewById(R.id.body);

        findViewById(R.id.header_container).setMinimumHeight(Math.round(0.4f*getMeasuredHeight()));

        setBody("Hello World");

    }

    public void setEmail(VEmail e){
        this.email = e;
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
        invalidate();
    }

    @Override
    protected void onDraw(Canvas c){
        int side = Math.min(getMeasuredWidth(), getMeasuredHeight());
        c.drawRect(SHADOW_RADIUS, SHADOW_RADIUS, side-SHADOW_RADIUS, 0.4f*(side-SHADOW_RADIUS), headerBox);
//        c.drawText(email.getSenderEmail(), 100, 100, senderTextPaint);

        super.onDraw(c);
    }
}
