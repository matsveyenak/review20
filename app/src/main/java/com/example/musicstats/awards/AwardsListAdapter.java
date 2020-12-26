package com.example.musicstats.awards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicstats.R;

import java.util.List;

public class AwardsListAdapter extends RecyclerView.Adapter<AwardsListAdapter.Adapter>
{
    List<AwardItem> awards;

    public AwardsListAdapter(List<AwardItem> awards)
    {
        this.awards = awards;
    }

    @NonNull
    @Override
    public Adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nominee_item, parent, false);
        return new Adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter holder, int position)
    {
        AwardItem item = awards.get(position);
        holder.name.setText(awards.get(position).getNomination());
        holder.winner.setText(awards.get(position).getWinner());
        holder.others.setText(awards.get(position).getNominee());

        boolean isEx = awards.get(position).isExpanded();
        holder.expLayout.setVisibility(isEx ? View.VISIBLE: View.GONE);
    }

    @Override
    public int getItemCount()
    {
        return awards.size();
    }

    public class Adapter extends RecyclerView.ViewHolder
    {
        TextView name, winner, others;
        ConstraintLayout expLayout;

        public Adapter(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.awardName);
            winner = itemView.findViewById(R.id.awardWinner);
            others = itemView.findViewById(R.id.awardOther);
            expLayout = itemView.findViewById(R.id.expLayout);

            name.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AwardItem item = awards.get(getAdapterPosition());
                    item.setExpanded(!item.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
