package com.example.review20.ui.home;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Matrix;
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
import com.example.review20.ui.artists.ArtistsFragment;
import com.example.review20.ui.awards.AwardItem;
import com.example.review20.ui.awards.AwardsFragment;
import com.example.review20.ui.charts.ChartTrackFragment;
import com.example.review20.ui.charts.ChartsFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.PlaylistsPager;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Query;
import retrofit.http.QueryMap;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder>
{
    List<HomeItem> items;
    Context ctx;
    HomeFragment fragment;
   // NavController navController;
    int mode;

    public HomeListAdapter(Context ctx, List<HomeItem> items, int mode, HomeFragment fragment)//, NavController navController, int mode)
    {
        this.ctx = ctx;
        this.items = items;
        //this.navController = navController;
        this.mode = mode;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public HomeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wide_button, parent, false);
        return new HomeListAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeListAdapter.ViewHolder holder, int position)
    {
        if (items != null && position < items.size())
        {
            HomeItem artist = items.get(position);
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
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView tvTitle;
        ImageView ivImage;
        MaterialCardView card;

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
            if (mode == 0)
            {
                MainActivity activity = (MainActivity) view.getContext();
                Fragment fr;
                switch (getAdapterPosition())
                {
                    case 0:
                        fr = new ChartsFragment();
                        fragment.createTransition(fr);
                       // navController.navigate(R.id.action_main_charts);
                        break;
                    case 1:
                        fr = new ArtistsFragment();
                        fragment.createTransition(fr);
                        //navController.navigate(R.id.action_main_artists);
                        break;
                    case 2:
                        fr = new AwardsFragment();
                        fragment.createTransition(fr);
                        //navController.navigate(R.id.action_main_awards);
                        break;
                }
            }
            else
            {
                Bundle bundle = new Bundle();
                MainActivity.spotifyService.getMe(new Callback<UserPrivate>()
                {
                    @Override
                    public void success(UserPrivate userPrivate, Response response)
                    {
                        bundle.putString("BUNDLE_TITLE", userPrivate.display_name);
                    }

                    @Override
                    public void failure(RetrofitError error)
                    {
                        Log.e("HomeListAdapter", error.getMessage());
                    }
                });
;
                bundle.putString("BUNDLE_IMAGE", "yourtop");

                Map<String, Object> options = new HashMap<>();
                options.put(SpotifyService.LIMIT, 1);

                MainActivity.spotifyService.searchPlaylists("your top 2020", options, new SpotifyCallback<PlaylistsPager>()
                {
                    @Override
                    public void success(PlaylistsPager playlistsPager, Response response)
                    {
                        String result = playlistsPager.playlists.items.get(0).uri;
                        String uri = result.replace("spotify:playlist:", "");

                        bundle.putString("BUNDLE_URI", uri);

                        Fragment f = new ChartTrackFragment();
                        f.setArguments(bundle);
                        //tag

                        ViewCompat.setTransitionName(card, bundle.getString("BUNDLE_TITLE"));
                        fragment.createPlaylistTransition(f, card);

                        //navController.navigate(R.id.action_home_playlist, bundle);
                    }

                    @Override
                    public void failure(SpotifyError error)
                    {
                        Log.e("HomeListAdapter", error.getMessage());
                    }
                });
            }
        }
    }
}
