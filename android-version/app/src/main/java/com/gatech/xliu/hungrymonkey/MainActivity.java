package com.gatech.xliu.hungrymonkey;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gatech.xliu.entity.GetUserCallback;
import com.gatech.xliu.entity.User;
import com.gatech.xliu.entity.UserLocalStore;
import com.gatech.xliu.web.WebService;

public class MainActivity extends AppCompatActivity {
    private Button signin, signup;
    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signin = (Button) findViewById(R.id.signin);
        signup = (Button) findViewById(R.id.signup);
        signin.setOnClickListener(new ButtonListener());
        signup.setOnClickListener(new ButtonListener());
        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);
    }

    class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            Intent intent = new Intent();

            switch (v.getId()) {
                case R.id.signin:
                    if (!checkNetwork()) {
                        Toast toast = Toast.makeText(MainActivity.this, "Network failed", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        break;
                    }
                    User user = new User(username.getText().toString(), password.getText().toString());
                    authenticate(user);
                    break;
                case R.id.signup:
                    intent.setClass(MainActivity.this, RegisterActivity.class);
                    MainActivity.this.startActivity(intent);
                    break;
            }
        }

        private boolean checkNetwork() {
            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connManager.getActiveNetworkInfo() != null)
                return connManager.getActiveNetworkInfo().isAvailable();
            return false;
        }

        private void authenticate(User user) {
            WebService wb=new WebService(MainActivity.this);
            wb.fetchUserDataInBackground(user, new GetUserCallback() {
                @Override
                public void done(User user) {
                    System.out.println(user.username + ":::::::" + user.password);
                    if (user.username.equals("") || user.password.equals("")) {
                        showErrorMessage();
                    }
                    else{
                        logIn(user);
                    }
                }
            });
        }
        private void showErrorMessage() {
            AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(MainActivity.this);
            dialogBuilder.setMessage("User Details Incorrect");
            dialogBuilder.setPositiveButton("OK",null);
            dialogBuilder.show();
        }
        private void logIn(User user) {
            UserLocalStore userLocalStore=new UserLocalStore(MainActivity.this);
            userLocalStore.storeUserData(user);
            userLocalStore.setUserLoggedIn(true);
            Intent intent=new Intent(getApplicationContext(),RecommandActivity.class);
            startActivity(intent);
        }
    }
}
