package com.example.clara.aprender.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.clara.aprender.Base_datos.Base_datos_Aprender;
import com.example.clara.aprender.LoadGame;
import com.example.clara.aprender.Modelos.Nivel;
import com.example.clara.aprender.Modelos.Solucion;
import com.example.clara.aprender.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class puntuacion_adapter extends RecyclerView.Adapter<puntuacion_adapter.MyViewHolder> {

    private Context mContext ;
    private List<Nivel> mData ;
    Solucion sol = new Solucion();


    public puntuacion_adapter(Context mContext, List<Nivel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        sol = new Solucion();
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.puntuacion_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("usuarios");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        GoogleSignInAccount googleUser = GoogleSignIn.getLastSignedInAccount(mContext);

        String nombre = mData.get(position).getTitulo();
        holder.nombreNivel.setText(nombre);
        final int id_nivel = mData.get(position).getIdNivel();

        if(googleUser != null || firebaseUser != null) {
            if (googleUser != null) {
                myRef.child(googleUser.getId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int puntuacion = 0;
                        if (dataSnapshot.exists()) {
                            puntuacion = Integer.parseInt(dataSnapshot.child("/puntuaciones/lvl" + id_nivel).getValue().toString());

                            if(puntuacion==3){
                                holder.star1.setImageResource(R.drawable.star);
                                holder.star2.setImageResource(R.drawable.star);
                                holder.star3.setImageResource(R.drawable.star);
                            }
                            if(puntuacion==2){
                                holder.star1.setImageResource(R.drawable.star);
                                holder.star2.setImageResource(R.drawable.star);
                                holder.star3.setImageResource(R.drawable.empty_star);
                            }
                            if(puntuacion==1){
                                holder.star1.setImageResource(R.drawable.star);
                                holder.star2.setImageResource(R.drawable.empty_star);
                                holder.star3.setImageResource(R.drawable.empty_star);
                            }
                            if(puntuacion==0){
                                holder.star1.setImageResource(R.drawable.empty_star);
                                holder.star2.setImageResource(R.drawable.empty_star);
                                holder.star3.setImageResource(R.drawable.empty_star);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            if (firebaseUser != null) {
                myRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int puntuacion = 0;
                        if (dataSnapshot.exists()) {
                            puntuacion = Integer.parseInt(dataSnapshot.child("/puntuaciones/lvl" + id_nivel).getValue().toString());

                            if(puntuacion==3){
                                holder.star1.setImageResource(R.drawable.star);
                                holder.star2.setImageResource(R.drawable.star);
                                holder.star3.setImageResource(R.drawable.star);
                            }
                            if(puntuacion==2){
                                holder.star1.setImageResource(R.drawable.star);
                                holder.star2.setImageResource(R.drawable.star);
                                holder.star3.setImageResource(R.drawable.empty_star);
                            }
                            if(puntuacion==1){
                                holder.star1.setImageResource(R.drawable.star);
                                holder.star2.setImageResource(R.drawable.empty_star);
                                holder.star3.setImageResource(R.drawable.empty_star);
                            }
                            if(puntuacion==0){
                                holder.star1.setImageResource(R.drawable.empty_star);
                                holder.star2.setImageResource(R.drawable.empty_star);
                                holder.star3.setImageResource(R.drawable.empty_star);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }else {
            Base_datos_Aprender BDAprender = Room.databaseBuilder(mContext, Base_datos_Aprender.class, "base_datos_aprender").allowMainThreadQueries().build();
            sol = BDAprender.getSolucionDAO().getSolucionPorID(id_nivel);
            Log.i("punt", sol + "");
            int puntuacion = 0;
            if (sol != null) {
                puntuacion = sol.getPuntuacion();
            }
            if(puntuacion==3){
                holder.star1.setImageResource(R.drawable.star);
                holder.star2.setImageResource(R.drawable.star);
                holder.star3.setImageResource(R.drawable.star);
            }
            if(puntuacion==2){
                holder.star1.setImageResource(R.drawable.star);
                holder.star2.setImageResource(R.drawable.star);
                holder.star3.setImageResource(R.drawable.empty_star);
            }
            if(puntuacion==1){
                holder.star1.setImageResource(R.drawable.star);
                holder.star2.setImageResource(R.drawable.empty_star);
                holder.star3.setImageResource(R.drawable.empty_star);
            }
            if(puntuacion==0){
                holder.star1.setImageResource(R.drawable.empty_star);
                holder.star2.setImageResource(R.drawable.empty_star);
                holder.star3.setImageResource(R.drawable.empty_star);
            }
        }


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nombreNivel;
        ImageView star1;
        ImageView star2;
        ImageView star3;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);
            nombreNivel =  itemView.findViewById(R.id.nombre_nivel);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }
    }
}
