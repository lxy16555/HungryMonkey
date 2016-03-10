package com.gatech.xliu.web;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.gatech.xliu.entity.GetUserCallback;
import com.gatech.xliu.entity.User;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by lyleliu on 3/8/16.
 */
public class WebService {
    ProgressDialog progressDialog;

    public WebService(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait....");
        System.out.println("--------------------------");
    }

    public void storeUserDataInBackground(User user, GetUserCallback userCallback) {
        progressDialog.show();
        new StoreUserDataAsyncTack(user,userCallback).execute();
    }

    public class StoreUserDataAsyncTack extends AsyncTask<Void,Void,User> {
        User user;
        GetUserCallback userCallback;
        public  StoreUserDataAsyncTack(User user,GetUserCallback userCallback) {
            this.user=user;
            this.userCallback=userCallback;
        }

        @Override
        protected User doInBackground(Void... params) {
            ArrayList<String> param=new ArrayList<>();
            param.add(new String(user.username));
            param.add(new String(user.password));
            try {
//                URL url = new URL("http://192.168.0.106/~lyleliu/register.php");
                URL url = new URL("http://143.215.56.250/~lyleliu/register.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(param));
                writer.flush();
                writer.close();
                os.close();
                urlConnection.connect();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String result = convertInputStreamToString(in);
                System.out.println("--------------------------"+result);
                JSONObject jsonObject = new JSONObject(new JSONTokener(result));
                String username = jsonObject.getString("username");
                String password = jsonObject.getString("password");
                user = new User(username, password);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return user;
        }

        @Override
        protected void onPostExecute(User user)
        {   progressDialog.dismiss();
            userCallback.done(user);
            super.onPostExecute(user);
        }
    }



    public void fetchUserDataInBackground(User user,GetUserCallback userCallback) {
        progressDialog.show();
        new FetchUserDataAsyncTack(user,userCallback).execute();
    }
    public class FetchUserDataAsyncTack extends AsyncTask<Void,Void,User> {
        User user;
        GetUserCallback userCallback;
        public  FetchUserDataAsyncTack(User user,GetUserCallback userCallback) {
            this.user=user;
            this.userCallback=userCallback;
        }

        @Override
        protected User doInBackground(Void... params) {
            ArrayList<String> param=new ArrayList<>();
            param.add(new String(user.username));
            param.add(new String(user.password));
            try {
//                URL url = new URL("http://192.168.0.106/~lyleliu/login.php");
                URL url = new URL("http://143.215.56.250/~lyleliu/login.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(param));
                writer.flush();
                writer.close();
                os.close();
                urlConnection.connect();
                InputStream in       = new BufferedInputStream(urlConnection.getInputStream());
                String result        = convertInputStreamToString(in);
                if (result.length() > 2) {
                    JSONObject jsonObject = new JSONObject(new JSONTokener(result));
                    String username = jsonObject.getString("username");
                    String password = jsonObject.getString("password");
                    user = new User(username, password);
                } else
                    user = new User("", "");
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            return user;
        }
        @Override
        protected void onPostExecute(User user)
        {   progressDialog.dismiss();
            userCallback.done(user);
            super.onPostExecute(user);
        }
    }




    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    private static String getQuery(ArrayList<String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (int i=0; i<params.size(); i++) {
            if (first)
                first = false;
            else
                result.append("&");

            if (i == 0)
                result.append(URLEncoder.encode("username", "UTF-8"));
            else
                result.append(URLEncoder.encode("password", "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(params.get(i), "UTF-8"));
        }

        return result.toString();
    }
}
