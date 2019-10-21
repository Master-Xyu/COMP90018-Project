/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.goodgame;

//import com.example.android.common.logger.Log;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private static ArrayList<String> mDataSet;
    private static ArrayList<Integer> mDataType;
    private static ArrayList<String> mDataID;
    private static String UID;
    private static FragmentActivity mContext;
    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CustomAdapter adapter;
        private final TextView textView;
        private final Button button;
        private int position;
        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            /*v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
             */


            textView = (TextView) v.findViewById(R.id.textView);
            button = (Button) v.findViewById(R.id.delete);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showNormalDialog(position);
                }
            });
        }

        public TextView getTextView() {
            return textView;
        }

        private void showNormalDialog(int position){
            /* @setIcon Set dialog icon
             * @setTitle Set dialog title
             * @setMessage Set dialog message prompt
             */
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(mContext);
            normalDialog.setTitle("");
            normalDialog.setMessage("Are you sure to delete this comment?");
            normalDialog.setPositiveButton("Continue",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Posts");
                            if(mDataType.get(position) == 1){
                                mDatabase.child(mDataID.get(position)).removeValue();
                            }else{
                                String[] ids = mDataID.get(position).split(" ");
                                mDatabase.child(ids[0]).child("Comments").child(ids[1]).removeValue();
                            }
                            mDataType.remove(position);
                            mDataID.remove(position);
                            mDataSet.remove(position);
                            //RecyclerViewFragment.removeCommentAt(position);
                            adapter.notifyItemRemoved(getAdapterPosition());
                            adapter.notifyItemRangeChanged(getAdapterPosition(),mDataSet.size());
                            adapter.notifyDataSetChanged();
                        }
                    });
            normalDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            // Show
            normalDialog.show();
        }

        public void setPosition(int position){
            this.position = position;
        }
        public void setAdapter(CustomAdapter adapter){
            this.adapter = adapter;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    public void setMcontext(FragmentActivity context){
        this.mContext = context;
    }
    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public CustomAdapter(ArrayList<String> dataSet, ArrayList<Integer> dataType, ArrayList<String> dataID) {
        mDataSet = dataSet;
        mDataID = dataID;
        mDataType = dataType;
        UID = FirebaseAuth.getInstance().getUid();
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comment_row, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getTextView().setText(mDataSet.get(position));
        viewHolder.setPosition(position);
        viewHolder.setAdapter(this);
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
