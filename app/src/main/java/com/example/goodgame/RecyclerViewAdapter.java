package com.example.goodgame;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable{

    private List<StopDetails> mStop;
    private List<StopDetails> stopFull;
    private Context mContext;


    public RecyclerViewAdapter(ArrayList<StopDetails>mStop,  Context mContext) {
        this.mStop = mStop;
        this.mContext = mContext;
        stopFull = new ArrayList<>(mStop);

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
        holder.name.setText(mStop.get(position).getName());
        holder.description.setText(mStop.get(position).getDescription());
        holder.distance.setText(mStop.get(position).getDistance());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("stopID",mStop.get(position).getId());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mStop.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<StopDetails> filteredStop = new ArrayList<>();

            if (charSequence.toString().isEmpty()){
                filteredStop.addAll(stopFull);

            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (StopDetails stop: stopFull){
                    if (stop.getName().toLowerCase().contains(filterPattern) || stop.getDescription().toLowerCase().contains(filterPattern)){
                        filteredStop.add(stop);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredStop;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mStop.clear();
            mStop.addAll((Collection<? extends StopDetails>) filterResults.values);
            notifyDataSetChanged();
        }
    };

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
