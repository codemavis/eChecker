package com.sliit.mad.echecker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

public class third extends AppCompatActivity {

    TextView txtQuestion = null;
    TextView txtQuestionCode = null;
    EditText txtAnswer = null;
    int nQuestCode = 1;
    String cAnswer;
    String cKeywords;
    String cStudAnswer;
    EditText edtAns;
    int cMaxQuestId = 100;
    String cRele;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        txtQuestionCode = (TextView) findViewById(R.id.txtQuestionCode);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        edtAns = (EditText) findViewById(R.id.edtAnswer);

        addListenerOnExeBtn();

        String link = "http://192.168.43.116:80/echecker/displayquestions.php?nQuestCode=" + nQuestCode;
        updateData upd = new updateData();
        upd.execute(link);
       // Toast.makeText(third.this, "Executed", Toast.LENGTH_LONG).show();
    }

    public void addListenerOnExeBtn() {
        Button btnNext = (Button) findViewById(R.id.btnNext);
        Button btnCheck = (Button) findViewById(R.id.btnCheck);

        btnNext.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (nQuestCode <= cMaxQuestId) {
                            String link = "http://192.168.43.116:80/echecker/displayquestions.php?nQuestCode=" + nQuestCode;
                            updateData upd = new updateData();
                            upd.execute(link);
                            //Toast.makeText(third.this, "Executed", Toast.LENGTH_LONG).show();
                        } else {
                            Intent int2 = new Intent(third.this, forth.class);
                            startActivity(int2);
                        }
                    }
                }
        );

        btnCheck.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cStudAnswer = edtAns.getText().toString().trim();
                        String link = "http://192.168.43.116:80/echecker/relavancy.php?cStudAnswer=" + cStudAnswer;
                        //10.0.2.2:80
                        // String link = "http://192.168.43.116/echecker/relavancy.php?cStudAnswer=" + cStudAnswer;

                        checkRelavancy show = new checkRelavancy();
                        show.execute(link);
                        //Toast.makeText(third.this, "Executed", Toast.LENGTH_LONG).show();


                    }
                }
        );
    }

    public class updateData extends AsyncTask<String, String, String> {

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
                Toast.makeText(third.this, "Failed", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(third.this, "Failed", Toast.LENGTH_LONG).show();
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

            String[] cSeperated = s.split(": ");

            String cQuestCode = cSeperated[0].trim();
            String cQuestion = cSeperated[1].trim();
            cAnswer = cSeperated[2].trim();
            cKeywords = cSeperated[3].trim();
            cMaxQuestId = Integer.parseInt(cSeperated[4].trim());

            txtQuestionCode.setText("Q" + cQuestCode);
            txtQuestion.setText(cQuestion);  //cKeywords
            nQuestCode = nQuestCode + 1;
        }
    }

    public class showMark extends AsyncTask<String, String, String> {

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
                Toast.makeText(third.this, "Failed", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(third.this, "Failed", Toast.LENGTH_LONG).show();
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

            String[] cSeperated = s.split(": ");

            String cMark = cSeperated[0].trim();

            Toast.makeText(getApplicationContext(), "Mark: " + cMark + " Correct Answer: " + cAnswer, Toast.LENGTH_SHORT).show();
        }
    }

    //checkRelavancy
    public class checkRelavancy extends AsyncTask<String, String, String> {

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
                Toast.makeText(third.this, "Failed", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(third.this, "Failed", Toast.LENGTH_LONG).show();
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

            cRele = s;
            Toast.makeText(third.this, s, Toast.LENGTH_LONG).show();
            if (s.equals("NO")) {
                Toast.makeText(getApplicationContext(), "Not Relevant To The Subject", Toast.LENGTH_SHORT).show();
                //"Not Relevant To The Subject"
            }
            else
            if (s.equals("YES")){

                String cRealm = cStudAnswer + "-" + cKeywords;

                String link1 = "http://192.168.43.116:80/echecker/keywordScore.php?cRealm=" + cRealm;
                showMark show1 = new showMark();
                show1.execute(link1);
                //Toast.makeText(third.this, "Executed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
