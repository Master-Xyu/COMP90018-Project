package com.example.goodgame;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mDescription = new ArrayList<>();
    private ArrayList<String> mDistance = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> mName, ArrayList<String> mDescription, ArrayList<String> mDistance, Context mContext) {
        this.mName = mName;
        this.mDescription = mDescription;
        this.mDistance = mDistance;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.temp,parent,false);
        ViewHolder holder= new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(mName.get(position));
        holder.description.setText(mDescription.get(position));
        holder.distance.setText(mDistance.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView description;
        TextView distance;
        LinearLayout parentLayout;

        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.stop_name);
            description = v.findViewById(R.id.stop_description);
            distance = v.findViewById(R.id.distance);
            parentLayout = v.findViewById(R.id.parent_layout);
        }
    }
}
