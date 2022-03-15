package com.example.review20.ui.awards;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.review20.MainActivity;
import com.example.review20.R;
import com.google.android.material.transition.Hold;
import com.google.android.material.transition.MaterialContainerTransform;
import com.google.android.material.transition.MaterialSharedAxis;

import java.util.ArrayList;
import java.util.Arrays;

public class AwardNominationFragment extends Fragment
{
    String name, image;
    RecyclerView rvAwardNominations;
    TextView tvAward;
    ImageView ivAward;
    ViewGroup layoutContainer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_award_nomination, container, false);

        if (MainActivity.playerViewModel.click > 0 )
        {
            MainActivity.playerCard.card.setVisibility(View.VISIBLE);
        }

        layoutContainer  = root.findViewById(R.id.award_nomination_fragment);

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            name = bundle.get("BUNDLE_TITLE").toString();
            image = bundle.get("BUNDLE_IMAGE").toString();
            ViewCompat.setTransitionName(layoutContainer, name);
        }

        MainActivity.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        MainActivity.tvToolbar.setText(name);

        rvAwardNominations = root.findViewById(R.id.rv_award_nominations);
        tvAward = root.findViewById(R.id.tv_award_name);
        ivAward = root.findViewById(R.id.iv_award_nomination_image);

        return root;
    }


    @Override
    public void onStart()
    {
        super.onStart();

        tvAward.setText(name);

        Resources res = getActivity().getResources();
        int resId = res.getIdentifier(image, "drawable", getActivity().getPackageName());
        Drawable image = res.getDrawable(resId);
        ivAward.setImageDrawable(image);

        ArrayList<AwardNominationItem> nominations = new ArrayList<>();

        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
        Cursor k = db.rawQuery("SELECT * FROM awards WHERE name = '" + name + "'", null);
        k.moveToFirst();
        int id = k.getInt(0);
        int amount = k.getInt(3);
        k.close();

        int t = 0;
        Cursor c = db.query("awards", null, null, null, null, null, null);
        if (c.moveToPosition(id-1))
        {
            do
            {
                String nomination = c.getString(4);
                String winner = c.getString(5) ;
                String nominee =  c.getString(6);
                nominations.add(new AwardNominationItem(nomination, winner, nominee));
                t++;
            }
            while (c.moveToNext() && t != amount);
        }
        c.close();

        rvAwardNominations.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        AwardNominationAdapter adapter = new AwardNominationAdapter(getActivity(), nominations, this);//, Navigation.findNavController(getView()));
        rvAwardNominations.setAdapter(adapter);
    }

    public void createTransition(Fragment fragment)
    {
        MaterialSharedAxis exitTransition = new MaterialSharedAxis(MaterialSharedAxis.X, true);
        exitTransition.addTarget(R.id.award_nominee_fragment);
        fragment.setEnterTransition(exitTransition);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}
