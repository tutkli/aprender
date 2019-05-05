package com.example.clara.aprender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.clara.aprender.Modelos.Nivel;

import java.util.ArrayList;

public class MenuNivelAdapter extends BaseAdapter {

    private Context context;
    private  int layout;
    private ArrayList<Nivel> menuNivel;
    public ImageView imageView;

    public MenuNivelAdapter(Context context, int layout, ArrayList<Nivel> menuNivel) {
        this.context = context;
        this.layout = layout;
        this.menuNivel = menuNivel;
    }

    @Override
    public int getCount() {
        return menuNivel.size();
    }

    @Override
    public Object getItem(int position) {
        return menuNivel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.imageView = (ImageView) row.findViewById(R.id.imageView);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        byte[] nivelImage = MenuNivel.imageViewToByte(holder.imageView);
        Bitmap bitmap = BitmapFactory.decodeByteArray(nivelImage, 0, nivelImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}