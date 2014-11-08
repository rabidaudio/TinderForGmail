package com.rabidaudio.dev.tinderforgmail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


public class Card extends View {

    public Card(Context context) {
        super(context);
        init(null, 0);
    }

    public Card(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Card(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        //TODO
    }
}
