package com.rabidaudio.dev.tinderforgmail;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

/**
 * Created by charles on 11/8/14.
 *
 */
public class Card2 extends CardView {
    public Card2(Context context) {
        super(context);
        init();
    }

    public Card2(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public Card2(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
//        setBackgroundDrawable(getResources().getDrawable(R.drawable.shadow));
    }

}
