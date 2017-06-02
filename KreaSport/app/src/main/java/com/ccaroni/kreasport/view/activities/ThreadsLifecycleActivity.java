package com.ccaroni.kreasport.view.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ccaroni.kreasport.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ThreadsLifecycleActivity extends AppCompatActivity {
    // Static so that the thread access the latest attribute
    private static ProgressDialog dialog;
    private static Bitmap downloadBitmap;
    private static Handler handler;
    private ImageView imageView;
    private Thread downloadThread;

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threads_lifecycle);

        // create a handler to update the UI
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                imageView.setImageBitmap(downloadBitmap);
                dialog.dismiss();
            }

        };
        // get the latest imageView after restart of the application
        imageView = (ImageView) findViewById(R.id.imageView1);
        Context context = imageView.getContext();
        System.out.println(context);
        // Did we already download the image?
        if (downloadBitmap != null) {
            imageView.setImageBitmap(downloadBitmap);
        }
        // check if the thread is already running
        downloadThread = (Thread) getLastCustomNonConfigurationInstance();
        if (downloadThread != null && downloadThread.isAlive()) {
            dialog = ProgressDialog.show(this, "Download", "downloading");
        }
    }

    public void resetPicture(View view) {
        if (downloadBitmap != null) {
            downloadBitmap = null;
        }
        imageView.setImageResource(R.drawable.ic_map_black_24dp);
    }

    public void downloadPicture(View view) {
        dialog = ProgressDialog.show(this, "Download", "downloading");
        downloadThread = new MyThread();
        downloadThread.start();
    }

    // save the thread
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return downloadThread;
    }

    // dismiss dialog if activity is destroyed
    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        super.onDestroy();
    }

    // Utiliy method to download image from the internet
    static private Bitmap downloadBitmap(String url) throws IOException {
        HttpUriRequest request = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);

        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            byte[] bytes = EntityUtils.toByteArray(entity);

            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
                    bytes.length);
            return bitmap;
        } else {
            throw new IOException("Download failed, HTTP response code "
                    + statusCode + " - " + statusLine.getReasonPhrase());
        }
    }

    static public class MyThread extends Thread {
        @Override
        public void run() {
            try {
                // Simulate a slow network
                SystemClock.sleep(5000);
                downloadBitmap = downloadBitmap("https://lh3.googleusercontent.com/-C1DcqUMgPxQ/AAAAAAAAAAI/AAAAAAAAoV4/N4XTZn35nEA/photo.jpg?sz=75");
                // Updates the user interface
                handler.sendEmptyMessage(0);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

}