package com.example.review20.ui.charts;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.recyclerview.widget.RecyclerView;

import com.example.review20.MainActivity;
import com.example.review20.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.transition.Hold;
import com.google.android.material.transition.MaterialArcMotion;
import com.google.android.material.transition.MaterialContainerTransform;
import com.google.android.material.transition.MaterialSharedAxis;

import java.util.List;

public class ChartListAdapter extends RecyclerView.Adapter<ChartListAdapter.ViewHolder>
{
    List<ChartItem> playlists;
    Context ctx;
    ChartsFragment fragment;
    //NavController navController;

    public ChartListAdapter(Context ctx, List<ChartItem> playlists, ChartsFragment fragment)//, NavController navController)
    {
        this.ctx = ctx;
        this.playlists = playlists;
        this.fragment = fragment;

        //this.navController = navController;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.small_card, parent, false);
        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if (playlists != null && position < playlists.size())
        {
            ChartItem chart = playlists.get(position);
            holder.tvTitle.setText(chart.getTitle());

            Resources res = ctx.getResources();
            int resID = res.getIdentifier(chart.getImage(), "drawable", ctx.getPackageName());
            Drawable image = res.getDrawable(resID);
            holder.ivImage.setImageDrawable(image);

            ViewCompat.setTransitionName(holder.container, chart.getTitle());
        }
    }

    @Override
    public int getItemCount()
    {
        return playlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        MaterialCardView card;
        TextView tvTitle;
        ImageView ivImage;
        View container;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            container = itemView.findViewById(R.id.small_card_layout);
            card = itemView.findViewById(R.id.small_card);
            tvTitle = itemView.findViewById(R.id.tv_small_card_title);
            ivImage = itemView.findViewById(R.id.iv_small_card_image);

            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            ChartItem item = playlists.get(getAdapterPosition());

            Bundle bundle = new Bundle();
            bundle.putString("BUNDLE_URI", item.getUri());
            bundle.putString("BUNDLE_TITLE", item.getTitle());
            bundle.putString("BUNDLE_IMAGE", item.getImage());

            Fragment f = new ChartTrackFragment();
            f.setArguments(bundle);

            fragment.createTransition(f, container);

            //navController.navigate(R.id.action_charts_tracks, bundle);
        }
    }
}
