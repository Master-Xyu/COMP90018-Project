package com.example.goodgame.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodgame.R;
import com.example.goodgame.models.ModelComment;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.MyHolder>{

    Context context;
    List<ModelComment> commentList;

    public AdapterComments(Context context, List<ModelComment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //bind the row_comment.xml
        View view= LayoutInflater.from(context).inflate(R.layout.row_comments,parent,false);







        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get the data
        String uid=commentList.get(position).getUid();
        String name=commentList.get(position).getuName();
        String email=commentList.get(position).getuEmail();
        String image=commentList.get(position).getuDp();
        String cid=commentList.get(position).getcId();
        String comment=commentList.get(position).getComment();
        String timestamp=commentList.get(position).getTimestamp();

        //convert timestamp to dd/mm/yyy hh:mm am/pm
        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String pTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();




        //set the data
        holder.nameTv.setText(name);
        holder.commentTv.setText(comment);
        holder.timeTv.setText(pTime);

        //set user dp
        try{
            Picasso.get().load(image).placeholder(R.drawable.default_avatar).into(holder.avatarIv);

        }catch (Exception e){


        }



    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        //declare views from row_comments.xml
        ImageView avatarIv;
        TextView nameTv,commentTv,timeTv;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            avatarIv=itemView.findViewById(R.id.avatarIv);
            nameTv=itemView.findViewById(R.id.nameTv);
            commentTv=itemView.findViewById(R.id.commentTv);
            timeTv=itemView.findViewById(R.id.timeTv);


        }
    }
}
