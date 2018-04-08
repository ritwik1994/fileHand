package com.example.root.recyclertutorial;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Abhishek on 20-03-2015.
 */
public class SubActivityAdapter extends RecyclerView.Adapter<SubActivityAdapter.SubActivityViewHolder> {

    private final LayoutInflater inflater;
    List<SubActivityData> subActivityData = Collections.EMPTY_LIST;

    public SubActivityAdapter(Context context, List<SubActivityData> subActivityData) {
        inflater = LayoutInflater.from(context);
        this.subActivityData = subActivityData;
    }


    @Override
    public SubActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custom_card, parent, false);
        SubActivityViewHolder subActivityViewHolder = new SubActivityViewHolder(view);
        return subActivityViewHolder;
    }

    @Override
    public void onBindViewHolder(SubActivityViewHolder holder, int position) {
        int positionInList = position % subActivityData.size();
        SubActivityData currentCard = subActivityData.get(positionInList);
        holder.title.setText(currentCard.cardTitle);
        holder.image.setImageResource(currentCard.cardImage);

    }


    // Takes maximum space.
    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class SubActivityViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView image;

        public SubActivityViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.card_text);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
