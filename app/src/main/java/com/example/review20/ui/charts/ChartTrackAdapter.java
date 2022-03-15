package com.example.review20.ui.charts;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.review20.MainActivity;
import com.example.review20.R;
import com.example.review20.ui.player.PlayerFragment;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChartTrackAdapter extends RecyclerView.Adapter<ChartTrackAdapter.ViewHolder>
{
    Context ctx;
    List<ChartTrack> items;
    int mode = 0;
    //NavController navController;

    public ChartTrackAdapter(Context ctx, List<ChartTrack> items, int mode)//, NavController navController)
    {
        this.ctx = ctx;
        this.items = items;
        this.mode = mode;
        //this.navController = navController;
    }

    @NonNull
    @Override
    public ChartTrackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_card, parent, false);
        return new ChartTrackAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChartTrackAdapter.ViewHolder holder, int position)
    {
        if (items != null && position < items.size())
        {
            if (position != 0)
                holder.vDivider.setVisibility(View.GONE);
            ChartTrack chart = items.get(position);

            holder.tvId.setText(String.valueOf(chart.getID()));
            holder.tvTitle.setText(chart.getTitle());

            holder.tvArtist.setText("");
            int size = chart.getArtists().size();

            for (int i = 0; i < size; i++)
            {
                if (i != size - 1)
                    holder.tvArtist.append(items.get(position).getArtists().get(i) + ", ");
                else
                    holder.tvArtist.append(items.get(position).getArtists().get(i));
            }

            Picasso.get().load(chart.getCover()).into(holder.ivImage);
        }
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView tvId;
        TextView tvArtist;
        TextView tvTitle;
        View vDivider;
        ImageView ivImage;
        ImageButton btnMore;
        MaterialCardView card;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_track_card_id);
            tvTitle = itemView.findViewById(R.id.tv_track_card_title);
            tvArtist = itemView.findViewById(R.id.tv_track_card_artist);
            vDivider = itemView.findViewById(R.id.divider_track_card);
            ivImage = itemView.findViewById(R.id.iv_track_card_image);
            card = itemView.findViewById(R.id.track_card);
            btnMore = itemView.findViewById(R.id.btn_track_card_more);

            card.setOnClickListener(this::onClick);
            btnMore.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view)
        {
            MainActivity.playerViewModel.click += 1;
            MainActivity.playerCard.card.setVisibility(View.VISIBLE);

            if (view.getId() == R.id.btn_track_card_more
                    && MainActivity.playerViewModel.getUri().getValue().equals(items.get(getAdapterPosition()).getUri()))
            {

                MainActivity activity = (MainActivity) view.getContext();
                Fragment f = new PlayerFragment();
                activity.replaceFragment(f);
                //navController.navigate(R.id.action_player);
            }
            else
            {
                if (mode == 0)
                    MainActivity.spotifyAppRemote.getPlayerApi().skipToIndex("spotify:playlist:" + ChartTrackFragment.playlistUri,
                            getAdapterPosition());
                else if (mode == 1)
                    MainActivity.spotifyAppRemote.getPlayerApi().play(items.get(getAdapterPosition()).getUri());
                else if (mode == 2)
                    MainActivity.spotifyAppRemote.getPlayerApi().play(items.get(getAdapterPosition()).getUri());

                if (view.getId() == R.id.btn_track_card_more)
                {

                    MainActivity activity = (MainActivity) view.getContext();
                    Fragment f = new PlayerFragment();
                    activity.replaceFragment(f);
                    //navController.navigate(R.id.action_player);
                }
            }
//            MainActivity.playerViewModel.setUri(items.get(getAdapterPosition()).getUri());
//            MainActivity.playerViewModel.setSong(name.getText().toString());
//            MainActivity.playerViewModel.setArtists(artist.getText().toString());
//            MainActivity.playerViewModel.setCover(items.get(getAdapterPosition()).getCover());

//            Resources res = ctx.getResources();
//            int fullFavId = res.getIdentifier("ic_baseline_favorite_24", "drawable", ctx.getPackageName());
//            int emptyFavId = res.getIdentifier("ic_baseline_favorite_border_24", "drawable", ctx.getPackageName());
//            Drawable fullFavImage = res.getDrawable(fullFavId);
//            Drawable emptyFavImage = res.getDrawable(emptyFavId);
//
//            SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
//            Cursor c = db.rawQuery("SELECT * FROM favourite WHERE uri = '" +
//                                        MainActivity.card.uri + "'", null);
//            if (c.getCount() > 0)
//            {
//                Log.d("est'", "zapis");
//                MainActivity.playerViewModel.setFav(fullFavImage);
//            }
//            else
//            {
//                MainActivity.playerViewModel.setFav(emptyFavImage);
//            }
//            c.close();
//
//            int playId = res.getIdentifier("ic_baseline_play_arrow_24", "drawable", ctx.getPackageName());
//            int pauseId = res.getIdentifier("ic_baseline_pause_24", "drawable", ctx.getPackageName());
//            Drawable playImage = res.getDrawable(playId);
//            Drawable pauseImage = res.getDrawable(pauseId);
//
//            MainActivity.playerViewModel.setPlay(pauseImage);


//            if (view.getId() == R.id.chartItemFavourite)
//            {
//                if (mode == 1)
//                {
//                    String uri = items.get(getAdapterPosition()).getUri();
//                    SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
//                    db.delete("favourite", "uri=?", new String[]{uri});
//                    items.remove(getAdapterPosition());
//                    int t = getAdapterPosition();
//                    FavouriteFragment.adapter.notifyItemRemoved(getAdapterPosition());
//
//                    for (int i = t; i < items.size(); i++ )
//                    {
//                        ChartTrack tmp = items.get(i);
//                        tmp.setId(tmp.getID() - 1);
//                        Log.d("new id of item #",  tmp.getName() + " " + tmp.getID());
//                        items.set(i, tmp);
//                    }
//
//                    FavouriteFragment.adapter.notifyDataSetChanged();
//                }
//            }
        }
    }
}
