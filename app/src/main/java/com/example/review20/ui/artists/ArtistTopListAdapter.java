package com.example.review20.ui.artists;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.review20.ui.charts.ChartTrackFragment;
import com.example.review20.ui.home.HomeItem;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ArtistTopListAdapter extends RecyclerView.Adapter<ArtistTopListAdapter.ViewHolder>
{
    List<ArtistItem> items;
    Context ctx;
    ArtistsFragment fragment;
    //NavController navController;

    public ArtistTopListAdapter(Context ctx, List<ArtistItem> items, ArtistsFragment fragment)//, NavController navController)
    {
        this.ctx = ctx;
        this.items = items;
      //  this.navController = navController;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ArtistTopListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wide_button, parent, false);
        return new ArtistTopListAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistTopListAdapter.ViewHolder holder, int position)
    {
        if (items != null && position < items.size())
        {
            ArtistItem artist = items.get(position);
            holder.tvTitle.setText(artist.getName());

            Resources res = ctx.getResources();
            int resId = res.getIdentifier(artist.getImage(), "drawable", ctx.getPackageName());
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
        TextView tvTitle;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_wide_button_title);
            ivImage = itemView.findViewById(R.id.iv_wide_button_image);
            card = itemView.findViewById(R.id.wide_button_card);

            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            String name = items.get(getAdapterPosition()).getName();
            Bundle bundle = new Bundle();
            bundle.putString("BUNDLE_NAME", name);

            Fragment f = new ArtistPageFragment();
            f.setArguments(bundle);

            ViewCompat.setTransitionName(card, name);
            fragment.createTransition(f, card);

           // navController.navigate(R.id.action_artist_page, bundle);
        }
    }
}

