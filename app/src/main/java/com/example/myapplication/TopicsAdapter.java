package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsViewHolder> {

    ArrayList<CategoriesReference> items;
    LayoutInflater inflater;
    OnViewClickInterface onViewClickInterface;

    public TopicsAdapter(Context context, ArrayList<CategoriesReference> items, OnViewClickInterface onViewClickInterface) {
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        this.onViewClickInterface = onViewClickInterface;
    }

    @NonNull
    @Override
    public TopicsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View output =inflater.inflate(R.layout.topics_view_holder,viewGroup,false);
        return new TopicsViewHolder(output);
    }

    @Override
    public void onBindViewHolder(@NonNull final TopicsViewHolder topicsViewHolder, int i) {
        topicsViewHolder.textView.setText(items.get(i).cat_name);
        topicsViewHolder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewClickInterface.onViewClick(v,topicsViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
