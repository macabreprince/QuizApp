package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class TopicsViewHolder extends RecyclerView.ViewHolder {
    View itemView;
    TextView textView;
    public TopicsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        textView = itemView.findViewById(R.id.textViewTopic);
    }
}
