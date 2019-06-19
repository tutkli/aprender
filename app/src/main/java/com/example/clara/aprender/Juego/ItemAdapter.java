/*
 * Copyright 2014 Magnus Woxblom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.clara.aprender.Juego;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.example.clara.aprender.R;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

class ItemAdapter extends DragItemAdapter<Pair<Long, String>, ItemAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;

    ItemAdapter(ArrayList<Pair<Long, String>> list, int layoutId, int grabHandleId, boolean dragOnLongPress) {
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;
        setItemList(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        String text = mItemList.get(position).second;
        holder.mText.setText(text);
        if(text.equals("input") || text.equals("output") || text.equals("bump+") || text.equals("bump-")){
            switch(text) {
                case "input":
                    holder.IV.setImageResource(R.drawable.input);
                    break;
                case "output":
                    holder.IV.setImageResource(R.drawable.output);
                    break;
                case "bump+":
                    holder.IV.setImageResource(R.drawable.bumpmas);
                    break;
                case "bump-":
                    holder.IV.setImageResource(R.drawable.bumpmenos);
                    break;
            }
        }else{
                if(text.contains(" ")){
                    String[] InstruccionesArray = text.split(" ");
                    switch (InstruccionesArray[0]) {
                        case "copyto":
                            holder.IV.setImageResource(R.drawable.copyto);
                            break;
                        case "copyfrom":
                            holder.IV.setImageResource(R.drawable.copyfrom);
                            break;
                        case "sum":
                            holder.IV.setImageResource(R.drawable.sum);
                            break;
                        case "sub":
                            holder.IV.setImageResource(R.drawable.sub);
                            break;
                        case "jump":
                            holder.IV.setImageResource(R.drawable.jump);
                            break;
                }
            }else{
                switch(text) {
                    case "A":
                        holder.IV.setImageResource(R.drawable.jump);
                        break;
                    case "B":
                        holder.IV.setImageResource(R.drawable.jump);
                        break;
                    case "C":
                        holder.IV.setImageResource(R.drawable.jump);
                        break;
                    case "D":
                        holder.IV.setImageResource(R.drawable.jump);
                        break;
                }
            }
        }
        holder.itemView.setTag(mItemList.get(position));
    }
    // Siempre utiliza IDs únicas.
    @Override
    public long getUniqueItemId(int position) {
        return mItemList.get(position).first;
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView mText;
        ImageView IV;

        ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
            mText = (TextView) itemView.findViewById(R.id.text);
            IV = itemView.findViewById(R.id.image);

        }
    }
}
