package com.example.clara.aprender;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        init();

        /* CARGAR UN GIF
        try {
            InputStream inputStream = getAssets().open("splash_bg.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
        } catch (IOException ex) {
            Log.d("SplashScreen: ", "ERROR");
        }

        */

        new Cargar(this).execute();
    }

    public void init() {
        setFlags();
    }

    public void setFlags() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
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
