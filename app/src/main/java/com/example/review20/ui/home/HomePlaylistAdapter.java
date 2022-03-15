package com.example.review20.ui.home;

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
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.review20.MainActivity;
import com.example.review20.R;
import com.example.review20.ui.awards.AwardItem;
import com.example.review20.ui.awards.AwardNominationFragment;
import com.example.review20.ui.charts.ChartTrackFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class HomePlaylistAdapter extends RecyclerView.Adapter<HomePlaylistAdapter.ViewHolder>
{
    List<HomeItem> items;
    Context ctx;
    //NavController navController;
    HomeFragment fragment;

    public HomePlaylistAdapter(Context ctx, List<HomeItem> items, HomeFragment fragment)//, NavController navController)
    {
        this.ctx = ctx;
        this.items = items;
        //this.navController = navController;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public HomePlaylistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_card, parent, false);
        return new HomePlaylistAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePlaylistAdapter.ViewHolder holder, int position)
    {
        if (items != null && position < items.size())
        {
            HomeItem item = items.get(position);

            Resources res = ctx.getResources();
            int resId = res.getIdentifier(item.getImage(), "drawable", ctx.getPackageName());
            Drawable image = res.getDrawable(resId);

            holder.ivImage.setImageDrawable(image);
        }
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        MaterialCardView card;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_playlist_card_image);
            card = itemView.findViewById(R.id.playlist_card);

            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            HomeItem item = items.get(getAdapterPosition());

            Bundle bundle = new Bundle();
            bundle.putString("BUNDLE_URI", item.getUri());
            bundle.putString("BUNDLE_TITLE", item.getTitle());
            bundle.putString("BUNDLE_IMAGE", item.getImage());

            Fragment f = new ChartTrackFragment();
            f.setArguments(bundle);

            ViewCompat.setTransitionName(card, item.getTitle());
            fragment.createPlaylistTransition(f, card);

            //navController.navigate(R.id.action_home_playlist, bundle);
        }
    }
}
