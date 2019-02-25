package com.example.feverfinder;

import android.util.Log;
import android.util.SparseArray;

import com.example.feverfinder.questions.Question;
import com.example.feverfinder.questions.Section;

import org.json.JSONException;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SendSurveyThread extends Thread {
    private String strToSend;

    public SendSurveyThread(String s) {
        strToSend = s;
    }

    public void run() {
        try {
            // Set up streams for sending:
            URL url = new URL("http://13.95.172.26:8000/api/people/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            Log.d("API", "Sending");
            // Send
            writer.write(strToSend);
            Log.d("API", "Trying to send this:");
            Log.d("API", strToSend);

            // Close things
            writer.flush();
            writer.close();
            os.close();

            BufferedReader responseReader;
            if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 299) {
                responseReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                responseReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            while ((line = responseReader.readLine()) != null) {
                Log.d("API_RESPONSE", line);
            }
            responseReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
