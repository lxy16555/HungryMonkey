package com.gatech.xliu.hungrymonkey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button signin, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signin = (Button) findViewById(R.id.signin);
        signup = (Button) findViewById(R.id.signup);
        signin.setOnClickListener(new ButtonListener());
        signup.setOnClickListener(new ButtonListener());
    }

    class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.signin:
                    intent.setClass(MainActivity.this, RecommandActivity.class);
                    break;
                case R.id.signup:
                    intent.setClass(MainActivity.this, RegisterActivity.class);
                    break;
            }
            MainActivity.this.startActivity(intent);
        }
    }
}
