package com.example.musicstats.awards;

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

public class AwardsAdapter extends RecyclerView.Adapter<AwardsAdapter.ViewHolder>
{
    List<String> awards;
    List<Integer> icons;
    LayoutInflater inflater;
    Context ctx;
    int mode, img;

    public AwardsAdapter(Context ctx, List<String> award, List<Integer> icon, int mode)
    {
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.awards = award;
        this.icons = icon;
        this.mode = mode;
        if (mode == 0)
            this.img = R.layout.awards_grid_layout;
        else
            this.img = R.layout.charts_grid_layout;
    }

    @NonNull
    @Override
    public AwardsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(img, parent, false);
        return new AwardsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AwardsAdapter.ViewHolder holder, int position)
    {
        holder.award.setText(awards.get(position));
        holder.icon.setImageResource(icons.get(position));
    }

    @Override
    public int getItemCount()
    {
        return awards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView award;
        ImageView icon;
        private final Context context;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            if (mode == 0)
            {
                award = itemView.findViewById(R.id.gridAward);
                icon = itemView.findViewById(R.id.gridIcon);
            }
            else
            {
                award = itemView.findViewById(R.id.gridCountry);
                icon = itemView.findViewById(R.id.gridFlag);
            }
            context = itemView.getContext();

        }
        @Override
        public void onClick(View view)
        {
            Intent intent = new Intent(context, AwardItemActivity.class);
            intent.putExtra("name", awards.get(getAdapterPosition()));
            context.startActivity(intent);
        }
    }
}
