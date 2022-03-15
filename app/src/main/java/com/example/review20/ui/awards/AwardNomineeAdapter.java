package com.example.review20.ui.awards;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.review20.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class AwardNomineeAdapter extends RecyclerView.Adapter<AwardNomineeAdapter.ViewHolder>
{
    List<String> nominations;
    Context ctx;

    public AwardNomineeAdapter(Context ctx, List<String> nominations)
    {
        this.ctx = ctx;
        this.nominations = nominations;
    }

    @NonNull
    @Override
    public AwardNomineeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nominee_card, parent, false);
        return new AwardNomineeAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull AwardNomineeAdapter.ViewHolder holder, int position)
    {
        if (nominations != null && position < nominations.size())
        {
            if (position != 0)
                holder.vDivider.setVisibility(View.GONE);

            String artist = nominations.get(position);
            holder.tvTitle.setText(artist);
        }
    }

    @Override
    public int getItemCount()
    {
        return nominations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        MaterialCardView card;
        TextView tvTitle;
        View vDivider;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_nominee_card);
            card = itemView.findViewById(R.id.nominee_card);
            vDivider = itemView.findViewById(R.id.divider_nominee_card);
        }
    }
}

