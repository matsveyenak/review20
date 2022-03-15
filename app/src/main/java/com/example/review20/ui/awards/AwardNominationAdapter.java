package com.example.review20.ui.awards;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.review20.MainActivity;
import com.example.review20.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class AwardNominationAdapter extends RecyclerView.Adapter<AwardNominationAdapter.ViewHolder>
{
    List<AwardNominationItem> nominations;
    Context ctx;
    AwardNominationFragment fragment;
    //NavController navController;

    public AwardNominationAdapter(Context ctx, List<AwardNominationItem> nominations, AwardNominationFragment fragment)//, NavController navController)
    {
        this.ctx = ctx;
        this.nominations = nominations;
        this.fragment = fragment;
        //this.navController = navController;
    }

    @NonNull
    @Override
    public AwardNominationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.award_card, parent, false);
        return new AwardNominationAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull AwardNominationAdapter.ViewHolder holder, int position)
    {
        if (nominations != null && position < nominations.size())
        {
            if (position != 0)
                holder.vDivider.setVisibility(View.GONE);

            AwardNominationItem artist = nominations.get(position);
            holder.tvTitle.setText(artist.getNomination());
        }
    }

    @Override
    public int getItemCount()
    {
        return nominations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        MaterialCardView card;
        TextView tvTitle;
        View vDivider;
        ImageButton btnMore;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_award_card_title);
            card = itemView.findViewById(R.id.award_card);
            vDivider = itemView.findViewById(R.id.divider_award_card);
            btnMore = itemView.findViewById(R.id.btn_award_card_more);

            card.setOnClickListener(this);
            btnMore.setOnClickListener(this);

        }

        @Override
        public void onClick(View view)
        {
            AwardNominationItem item = nominations.get(getAdapterPosition());

            Bundle bundle = new Bundle();
            bundle.putString("BUNDLE_NOMINATION", item.getNomination());
            bundle.putString("BUNDLE_WINNER", item.getWinner());
            bundle.putString("BUNDLE_NOMINEE", item.getNominee());

            Fragment fr = new AwardNomineeFragment();
            fr.setArguments(bundle);
            fragment.createTransition(fr);

           // navController.navigate(R.id.action_nomination_nominees, bundle);
        }
    }
}

