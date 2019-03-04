package com.example.feverfinder;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class SurveyStore {
    protected static void saveSurvey(String contents, Context context) throws EncryptionException, SaveException {
        try {
            //Find a new unique filename
            List<String> fileNames = Arrays.asList(context.getFilesDir().list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.startsWith("response");
                }
            }));

            int i = 0;
            String filename = "response" + String.valueOf(i);
            while (fileNames.contains(filename)) {
                i++;
                filename = "response" + String.valueOf(i);
            }

            //Write out the bytes
            OutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(contents.getBytes(StandardCharsets.UTF_8));
            outputStream.close();
        } catch (Exception e) {
            throw new SaveException(e.getMessage());
        }
    }

    protected static void submitSavedSurveys(Context context) {
        File[] files = context.getFilesDir().listFiles();
        for (File file : files) {
            if (file.getName().startsWith("response")) {
                try {
                    int size = (int) file.length();
                    byte[] bytes = new byte[size];
                    BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
                    in.read(bytes, 0, size);
                    in.close();
                    String data = new String(bytes, 0, size, StandardCharsets.UTF_8);
                    Log.d("STRINGSEND", data);

                    if (isNetworkAvailable(context)) {
                        // Send it by creating a new thread
                        SendSurveyThread sst = new SendSurveyThread(data, context);
                        sst.start();
                        file.delete();
                    } else {
                        report("No Internet - Survey Saved", Toast.LENGTH_LONG, context);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* Test if network is available */
    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static void report(final String message, final int length, final Context context) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, length).show();
            }
        });
    }
}
