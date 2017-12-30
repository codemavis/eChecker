package com.sliit.mad.echecker;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnExeBtn();
    }

    public void addListenerOnExeBtn() {
        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText cRUserName = (EditText) findViewById(R.id.ed_UserName);
                        String cUserName = cRUserName.getText().toString();
                        EditText cRPassword = (EditText) findViewById(R.id.ed_Password);
                        String cPassword = cRPassword.getText().toString();

                        String RegStr = cUserName+"-"+cPassword;

                        String link = "http://192.168.43.116:80/echecker/login.php?RegStr=" + RegStr;
                        MainActivity.getLogin upd = new MainActivity.getLogin();
                        upd.execute(link);
                        //Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_LONG).show();


                    }
                });

        exit();
    }

    public class getLogin extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection conn = null;

            try {
                URL url = new URL(params[0].trim());
                conn = (HttpURLConnection) url.openConnection();
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    InputStream is = conn.getInputStream();

                    String result = null;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + " ");
                    }

                    result = sb.toString().trim();

                    JSONArray jArray = new JSONArray();
                    jArray.put(result);

                    String stime = null;

                    for (int i = 0; i < jArray.length(); i++) {
                        stime = jArray.getString(0);
                    }

                    return stime;
                } else {
                    InputStream err = conn.getErrorStream();
                }
                return "Done";
            } catch (MalformedURLException e) {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data" + e.toString());
                Toast.makeText(getApplicationContext(), "JsonArray fail", Toast.LENGTH_SHORT).show();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("Done")) {
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                Intent int3 = new Intent(MainActivity.this, first.class);
                startActivity(int3);
            }else
            {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void exit()
    {
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int4 = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(int4);
            }
        });
    }
}
