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
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.ViewHolder>
{
    private List<ArtistItem> artists;
    private Context ctx;
    ArtistsFragment fragment;
   // NavController navController;

    public ArtistListAdapter(Context ctx, List<ArtistItem> artists, ArtistsFragment fragment)//, NavController navController)
    {
        this.ctx = ctx;
        this.artists = artists;
        //this.navController = navController;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ArtistListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.small_card, parent, false);
        return new ArtistListAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistListAdapter.ViewHolder holder, int position)
    {
        if (artists != null && position < artists.size())
        {
            ArtistItem artist = artists.get(position);
            holder.tvName.setText(artist.getName());

            Resources res = ctx.getResources();
            int resId = res.getIdentifier(artist.getImage(), "drawable", ctx.getPackageName());
            Drawable image = res.getDrawable(resId);
            holder.ivImage.setImageDrawable(image);
        }
    }

    @Override
    public int getItemCount()
    {
        return artists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        MaterialCardView card;
        TextView tvName;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_small_card_title);
            ivImage = itemView.findViewById(R.id.iv_small_card_image);
            card = itemView.findViewById(R.id.small_card);

            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            String name = artists.get(getAdapterPosition()).getName();
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
