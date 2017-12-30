package com.sliit.mad.echecker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamCorruptedException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        addListenerOnExeBtn();
    }

    public void addListenerOnExeBtn() {
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        Button btnExit = (Button) findViewById(R.id.btnExit);

        btnRegister.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText cRUsername = (EditText) findViewById(R.id.ed_UserName);
                        String cUsername = cRUsername.getText().toString();

                        EditText cREmail = (EditText) findViewById(R.id.ed_Email);
                        String cEmail = cREmail.getText().toString();

                        EditText cRPassword = (EditText) findViewById(R.id.ed_Password);
                        String cPassword = cRPassword.getText().toString();

                        String RegStr = cUsername+"-"+cEmail+"-"+cPassword;

                        String link = "http://192.168.43.116:80/echecker/register_student.php?RegStr=" + RegStr;
                        RegisterActivity.registerData upd = new RegisterActivity.registerData();
                        upd.execute(link);
                        //Toast.makeText(RegisterActivity.this, RegStr , Toast.LENGTH_LONG).show();
                    }
                });

        btnExit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent int2 = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(int2);
                    }
                }
        );
    }

    public class registerData extends AsyncTask<String, String, String> {

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
                Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_LONG).show();
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

            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }
    }
}
