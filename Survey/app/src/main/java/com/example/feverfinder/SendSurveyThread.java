package com.example.feverfinder;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendSurveyThread extends Thread {
    private String strToSend;

    public SendSurveyThread(String s) {
        strToSend = s;
    }

    public void run() {
        try {
            // Set up streams for sending (make sure URL ends with '/')
            URL url = new URL("http://13.95.172.26:8000/api/people/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            // Send the string
            writer.write(strToSend);

            // Close buffers and streams
            writer.flush();
            writer.close();
            os.close();

            // Check response (for debugging)
            BufferedReader responseReader;
            if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 299) {
                // If successful API call, read from the input stream
                responseReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                // Otherwise read from the error stream
                responseReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line;
            while ((line = responseReader.readLine()) != null) {
                Log.d("API_RESPONSE", line);
            }

            responseReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
