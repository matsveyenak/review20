package com.example.musicstats.charts;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicstats.R;
import com.example.musicstats.launch.MainActivity;

import java.util.List;

public class ChartsAdapter extends RecyclerView.Adapter<ChartsAdapter.ViewHolder>
{
    List<Item> countries;
    LayoutInflater inflater;
    Context ctx;


    public ChartsAdapter(Context ctx, List<Item> country)
    {
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.countries = country;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.charts_grid_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.title.setText(countries.get(position).getTitle());
        holder.flag.setImageResource(countries.get(position).getImg());
    }

    @Override
    public int getItemCount()
    {
        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView title;
        ImageView flag;
        private final Context context;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.gridCountry);
            flag = itemView.findViewById(R.id.gridFlag);
            context = itemView.getContext();

        }
        @Override
        public void onClick(View view)
        {
            Intent intent =  new Intent(context, ChartItemActivity.class);

            SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
            Cursor c = db.query("charts", null, null, null, null, null, null);
            if (c.moveToFirst())
            {
                c.moveToPosition(getAdapterPosition());
                int uriColIndex = c.getColumnIndex("uri");
                String uri = c.getString(uriColIndex);
                intent.putExtra("uri", uri);
            }
            c.close();

            context.startActivity(intent);
        }
    }
}
