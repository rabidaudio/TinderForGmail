package com.rabidaudio.dev.tinderforgmail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by charles on 11/8/14.
 */
public class SigninActivity extends Activity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Button submit = (Button) findViewById(R.id.submit);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra(MainActivity.PREFS_EMAIL, email.getText().toString());
                i.putExtra(MainActivity.PREFS_PASS, password.getText().toString());
                setResult(RESULT_OK, i);
                finish();
            }
        });

    }

}
