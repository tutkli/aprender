package com.example.clara.aprender;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.clara.aprender.Modelos.Nivel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MenuNivel extends AppCompatActivity {

    GridView gridView;
    ArrayList<Nivel> list;
    MenuNivelAdapter adapter = null;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_nivel);

        init();
    }

    //CONVERTIDOR DE IMAGEN A BYTE
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //INICIALIZACION DE ITEMS
    private void init(){
        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new MenuNivelAdapter(this, R.layout.nivel_item, list);
        imageView = (ImageView) findViewById(R.id.imageView);
        gridView.setAdapter(adapter);

        final String titulos[] = new String[999];

        list.clear();
        for(int i=0;i<5;i++) {
            list.add(new Nivel("Nivel "+i, 10, "0", 34, i));
            titulos[i] = "Nivel "+i;
        }
        adapter.notifyDataSetChanged();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Toast.makeText(MenuNivel.this, titulos[position], Toast.LENGTH_SHORT).show();

            }
        });
    }
}
