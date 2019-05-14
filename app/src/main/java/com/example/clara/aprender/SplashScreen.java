package com.example.clara.aprender;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import static android.view.View.VISIBLE;

public class SplashScreen extends AppCompatActivity {

    GifImageView gifImageView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        gifImageView = (GifImageView) findViewById(R.id.gifImageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(VISIBLE);

        try {
            InputStream inputStream = getAssets().open("splash_bg.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
        } catch (IOException ex) {
            Log.d("SplashScreen: ", "ERROR");
        }

        new Cargar(this).execute();
    }

    public static class Cargar extends AsyncTask<Void, Void, Boolean> {

        private final WeakReference<Activity> weakReference;

        private Cargar(Activity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Thread.sleep(3000);
            }catch(InterruptedException e) {
                Log.d("SplashActivity:", "ERROR");
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            Activity activity = weakReference.get();
            Intent i = new Intent(activity, MainActivity.class);
            activity.startActivity(i);
            activity.finish();
        }
    }
}
