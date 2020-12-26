package com.example.musicstats.charts;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicstats.R;
import com.example.musicstats.favourite.FavouriteActivity;
import com.example.musicstats.launch.MainActivity;

import java.util.List;

public class ChartItemAdapter extends RecyclerView.Adapter<ChartItemAdapter.ViewHolder>
{
    private String NOTIFICATION_TITLE = "Notification Sample App";
    private String CONTENT_TEXT = "Expand me to see a detailed message!";
    List<ChartItem> items;
    LayoutInflater inflater;
    static Context ctx;
    int mode = 0;

    public ChartItemAdapter(Context ctx, List<ChartItem> items)
    {
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.items = items;
    }
    public ChartItemAdapter(Context ctx, List<ChartItem> items, int mode)
    {
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.items = items;
        this.mode = mode;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.chartitem_grid_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.id.setText(String.valueOf(items.get(position).getID()));
        holder.name.setText(items.get(position).getName());

        holder.artist.setText("");
        int size = items.get(position).getArtists().size();

        for (int i = 0; i < size; i++)
        {
            if (i != size - 1)
                holder.artist.append(items.get(position).getArtists().get(i) + ", ");
            else
                holder.artist.append(items.get(position).getArtists().get(i));
        }
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView id;
        TextView artist;
        TextView name;
        Button play;
        Button favourite;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            id = itemView.findViewById(R.id.chartItemID);
            name = itemView.findViewById(R.id.chartItemSong);
            artist = itemView.findViewById(R.id.chartItemArtist);
            play = itemView.findViewById(R.id.chartItemPlay);
            favourite = itemView.findViewById(R.id.chartItemFavourite);
            play.setOnClickListener(this);
            favourite.setOnClickListener(this);
            if (mode == 1)
                favourite.setBackgroundResource(R.drawable.solid_favor);
        }

        @Override
        public void onClick(View view)
        {
            if (view.getId() == R.id.chartItemPlay)
            {
                String uri = items.get(getAdapterPosition()).getUri();
                Log.d("track:", uri);
                MainActivity.mSpotifyAppRemote.getPlayerApi().play(uri);

                sendNotify(ctx);
            }
            else if (view.getId() == R.id.chartItemFavourite)
            {

                if (mode == 1)
                {
                    String uri = items.get(getAdapterPosition()).getUri();
                    SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
                    db.delete("favourite", "uri=?", new String[]{uri});
                    items.remove(getAdapterPosition());
                    int t = getAdapterPosition();
                    FavouriteActivity.chartItemAdapter.notifyItemRemoved(getAdapterPosition());

                    for (int i = t; i < items.size(); i++ )
                    {
                        ChartItem tmp = items.get(i);
                        tmp.setId(tmp.getID() - 1);
                        Log.d("new id of item #",  tmp.getName() + " " + tmp.getID());
                        items.set(i, tmp);
                    }

                    FavouriteActivity.chartItemAdapter.notifyDataSetChanged();
                }
                else
                {
                    Button fav = view.findViewById(R.id.chartItemFavourite);
                    fav.setBackgroundResource(R.drawable.solid_favor);
                    SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    db.beginTransaction();
                    try
                    {
                        Cursor c = db.rawQuery("SELECT * FROM user WHERE token = '" + MainActivity.token + "'", null);
                        c.moveToFirst();
                        int id = c.getInt(0);
                        c.close();

                        StringBuilder str = new StringBuilder();
                        int size = items.get(getAdapterPosition()).getArtists().size();
                        for (int i = 0; i < size; i++) {
                            if (i != size - 1)
                                str.append(items.get(getAdapterPosition()).getArtists().get(i) + ", ");
                            else
                                str.append(items.get(getAdapterPosition()).getArtists().get(i));
                        }
                        cv.put("artists", str.toString());
                        cv.put("name", items.get(getAdapterPosition()).getName());
                        cv.put("uri", items.get(getAdapterPosition()).getUri());
                        cv.put("user_id", id);
                        db.insert("favourite", null, cv);

                        Log.d("database success", "id = " + id);

                        db.setTransactionSuccessful();
                    }
                    finally
                    {
                        db.endTransaction();
                    }
                }
            }
        }
    }

    public void sendNotify(Context ctx)
    {
        Intent deleteIntent = new Intent(ctx, MainActivity.class);
        deleteIntent.setAction("ru.startandroid.notifications.action_delete");
        PendingIntent deletePendingIntent = PendingIntent.getService(ctx, 0, deleteIntent, 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Title")
                        .setContentText("Notification text")
                        .addAction(android.R.drawable.ic_delete, "Delete", deletePendingIntent)
                        .addAction(android.R.drawable.ic_delete, "Delete", deletePendingIntent);


        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

    }


}


