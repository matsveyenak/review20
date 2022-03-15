package com.example.review20.ui.awards;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.review20.MainActivity;
import com.example.review20.R;
import com.example.review20.ui.player.PlayerFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class AwardListAdapter extends RecyclerView.Adapter<AwardListAdapter.ViewHolder>
{
    List<AwardItem> awards;
    Context ctx;
    AwardsFragment fragment;
    //NavController navController;

    public AwardListAdapter(Context ctx, List<AwardItem> awards, AwardsFragment fragment)//, NavController navController)
    {
        this.ctx = ctx;
        this.awards = awards;
        //this.navController = navController;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public AwardListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.small_card, parent, false);
        return new AwardListAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull AwardListAdapter.ViewHolder holder, int position)
    {
        if (awards != null && position < awards.size())
        {
            AwardItem artist = awards.get(position);
            holder.tvTitle.setText(artist.getTitle());

            Resources res = ctx.getResources();
            int resId = res.getIdentifier(artist.getImage(), "drawable", ctx.getPackageName());
            Drawable image = res.getDrawable(resId);
            holder.ivImage.setImageDrawable(image);
        }
    }

    @Override
    public int getItemCount()
    {
        return awards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        MaterialCardView card;
        TextView tvTitle;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_small_card_title);
            ivImage = itemView.findViewById(R.id.iv_small_card_image);
            card = itemView.findViewById(R.id.small_card);
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            AwardItem item = awards.get(getAdapterPosition());

            Bundle bundle = new Bundle();
            bundle.putString("BUNDLE_TITLE", item.getTitle());
            bundle.putString("BUNDLE_IMAGE", item.getImage());

            Fragment f = new AwardNominationFragment();
            f.setArguments(bundle);

            ViewCompat.setTransitionName(card, item.getTitle());
            fragment.createTransition(f, card);

            //navController.navigate(R.id.action_award_nominations, bundle);
        }
    }
}
