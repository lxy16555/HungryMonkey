package com.gatech.xliu.hungrymonkey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gatech.xliu.entity.GetUserCallback;
import com.gatech.xliu.entity.User;
import com.gatech.xliu.entity.UserLocalStore;
import com.gatech.xliu.web.WebService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by lyleliu on 3/5/16.
 */
public class RegisterActivity extends Activity {
    private Button signup, signin;
    private EditText username, password, confirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);
        signup = (Button) findViewById(R.id.signup2);
        signin = (Button) findViewById(R.id.signin2);
        signin.setOnClickListener(new ButtonListener());
        signup.setOnClickListener(new ButtonListener());
        username = (EditText) findViewById(R.id.username2);
        password = (EditText) findViewById(R.id.password2);
        confirmpassword = (EditText) findViewById(R.id.password3);
    }

    class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            Intent intent = new Intent();

            switch (v.getId()) {
                case R.id.signup2:
                    if (!checkNetwork()) {
                        Toast toast = Toast.makeText(RegisterActivity.this, "Network failed", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        break;
                    }
                    String uName = username.getText().toString();
                    String pWord = password.getText().toString();
                    String cPWord = confirmpassword.getText().toString();
                    if (uName.equals("") || pWord.equals("") || cPWord.equals("")) {
                        showErrorMessage();
                        break;
                    }
                    if (!pWord.equals(cPWord)) {
                        AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(RegisterActivity.this);
                        dialogBuilder.setMessage("Different Passwords");
                        dialogBuilder.setPositiveButton("OK",null);
                        dialogBuilder.show();
                        break;
                    }
                    User user = new User(uName,pWord);
                    registerUser(user);
                    break;
                case R.id.signin2:
                    intent.setClass(RegisterActivity.this, MainActivity.class);
                    RegisterActivity.this.startActivity(intent);
                    break;
            }
        }

        private boolean checkNetwork() {
            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connManager.getActiveNetworkInfo() != null)
                return connManager.getActiveNetworkInfo().isAvailable();
            return false;
        }

        public void registerUser(User user)
        {
            WebService serverRequest=new WebService(RegisterActivity.this);
            serverRequest.storeUserDataInBackground(user, new GetUserCallback() {
                @Override
                public void done(User user) {
                    System.out.println(user.username + ":::::::" + user.password);
                    if (user.password.equals(""))
                        showErrorMessage();
                    else
                        logIn(user);
                }
            });
        }

//        private void authenticate(User user) {
//            WebService wb=new WebService(RegisterActivity.this);
//            wb.fetchUserDataInBackground(user, new GetUserCallback() {
//                @Override
//                public void done(User user) {
//                    System.out.println(user.username + ":::::::" + user.password);
//                    if (user.username.equals("") || user.password.equals("")) {
//                        showErrorMessage();
//                    }
//                    else{
//                        logIn(user);
//                    }
//                }
//            });
//        }
        private void showErrorMessage() {
            AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(RegisterActivity.this);
            dialogBuilder.setMessage("User Details Incorrect");
            dialogBuilder.setPositiveButton("OK",null);
            dialogBuilder.show();
        }
        private void logIn(User user) {
            UserLocalStore userLocalStore=new UserLocalStore(RegisterActivity.this);
            userLocalStore.storeUserData(user);
            userLocalStore.setUserLoggedIn(true);
            Intent intent=new Intent(getApplicationContext(),RecommandActivity.class);
            startActivity(intent);
        }
    }
}
