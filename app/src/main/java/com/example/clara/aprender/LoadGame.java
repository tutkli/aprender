package com.example.clara.aprender;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.clara.aprender.Juego.Juego;
import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class LoadGame extends AppCompatActivity {

    GifImageView gifImageView;
    public static int id_nivel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);

        init();

        new Cargar(this).execute();
    }

    public void init() {
        id_nivel = getIntent().getExtras().getInt("id");
        gifImageView = (GifImageView) findViewById(R.id.gifImageView);

        try {
            InputStream inputStream = getAssets().open("launch-rocket-animation.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
        } catch (IOException ex) {
            Log.d("LoadGame: ", "ERROR");
        }

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
            } catch (InterruptedException e) {
                Log.d("SplashActivity:", "ERROR");
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            Activity activity = weakReference.get();
            Intent i = new Intent(activity, Juego.class);
            i.putExtra("id",LoadGame.id_nivel);
            activity.startActivity(i);
            activity.finish();
        }
    }
}
