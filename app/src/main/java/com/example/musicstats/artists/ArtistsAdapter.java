package com.example.musicstats.artists;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicstats.R;

import java.util.List;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ViewHolder>
{
    List<String> artists;
    List<Integer> images;
    LayoutInflater inflater;
    Context ctx;
    int mode, img;

    public ArtistsAdapter(Context ctx, List<String> artists, List<Integer> images, int mode)
    {
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.artists = artists;
        this.images = images;
        this.mode = mode;
        if (mode == 0)
            this.img = R.layout.artists_grid_layout;
        else
            this.img = R.layout.charts_grid_layout;
    }

    @NonNull
    @Override
    public ArtistsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(img, parent, false);
        return new ArtistsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistsAdapter.ViewHolder holder, int position)
    {
        holder.artist.setText(artists.get(position));
        holder.image.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount()
    {
        return artists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView artist;
        ImageView image;
        private final Context context;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            if (mode == 0)
            {
                artist = itemView.findViewById(R.id.gridArtist);
                image = itemView.findViewById(R.id.gridImage);
            }
            else
            {
                artist = itemView.findViewById(R.id.gridCountry);
                image = itemView.findViewById(R.id.gridFlag);
            }
            context = itemView.getContext();

        }
        @Override
        public void onClick(View view)
        {
            Intent intent = new Intent(context, ArtistItemActivity.class);
            intent.putExtra("name", artists.get(getAdapterPosition()));
            context.startActivity(intent);
        }
    }
}
