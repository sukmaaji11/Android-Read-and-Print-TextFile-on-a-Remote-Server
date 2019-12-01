package com.android.network;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView fileContent;
    private static final String PATH_TO_SERVER = "https://drive.google.com/uc?authuser=0&id=1MOzT-Yp49z5Jrt1_VFNWKWlGTvbrXa1H&export=download";
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileContent = (TextView)findViewById(R.id.content_from_server);
        Button btn  = (Button)findViewById(R.id.load_file_from_server);
        btn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DownloadFileTask downloadFileTask = new DownloadFileTask();
                downloadFileTask.execute();
            }
        }));
    }

    private class DownloadFileTask extends AsyncTask<URL, Void, String> {
        protected String doInBackground(URL...urls) {
            return downloadRemoteTextFileContent();
        }
        protected void onPostExecute(String result) {
            if(!TextUtils.isEmpty(result)) {
                fileContent.setText(result);
            } else {
                fileContent.setText("No Connection");
            }
        }
    }

    private String downloadRemoteTextFileContent() {
        URL url = null;
        String content = "";

        try {
            url = new URL(PATH_TO_SERVER);
        }

        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            assert url != null;
            URLConnection connection = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while((line = br.readLine()) != null) {
                content += line;
            }
            br.close();
        }

        catch(IOException e) {
            e.printStackTrace();
        }

        return content;
    }
}
