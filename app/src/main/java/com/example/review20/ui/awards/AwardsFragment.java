package com.example.review20.ui.awards;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.review20.MainActivity;
import com.example.review20.R;
import com.example.review20.ui.charts.ChartItem;
import com.example.review20.ui.charts.ChartListAdapter;
import com.google.android.material.transition.Hold;
import com.google.android.material.transition.MaterialContainerTransform;

import java.util.ArrayList;

public class AwardsFragment extends Fragment
{
    RecyclerView rvAwards;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_awards, container, false);

        if (MainActivity.playerViewModel.click > 0 )
        {
            MainActivity.playerCard.card.setVisibility(View.VISIBLE);
        }

        MainActivity.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        MainActivity.tvToolbar.setText("awards");

        rvAwards = root.findViewById(R.id.rv_awards);

        return root;
    }


    @Override
    public void onStart()
    {
        super.onStart();

        ArrayList<AwardItem> awards = new ArrayList<>();

        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
        Cursor c = db.query("awards", null, null, null, null, null, null);

        int amount = 0;
        if (c.moveToPosition(amount))
        {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int imgColIndex = c.getColumnIndex("img");
            int amountColIndex = c.getColumnIndex("amount");

            do
            {
                int id = c.getInt(idColIndex);
                amount = c.getInt(amountColIndex) + id - 1;

                String title = c.getString(nameColIndex);
                String img = c.getString(imgColIndex);
                awards.add(new AwardItem(title, img));
            }
            while (c.moveToPosition(amount));
        }
        c.close();

        rvAwards.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        AwardListAdapter adapter = new AwardListAdapter(getActivity(), awards, this);//, Navigation.findNavController(getView()));
        rvAwards.setAdapter(adapter);
    }

    public void createTransition(Fragment fragment, View view)
    {
        MaterialContainerTransform transform = new MaterialContainerTransform();
        transform.setFadeMode(MaterialContainerTransform.FADE_MODE_CROSS);
        transform.setContainerColor(getResources().getColor(R.color.white));

        Hold hold = new Hold();
        hold.addTarget(R.id.awards_fragment);
        hold.setDuration(transform.getDuration());
        setExitTransition(hold);

        fragment.setSharedElementEnterTransition(transform);

        getParentFragmentManager()
                .beginTransaction()
                .addSharedElement(view, ViewCompat.getTransitionName(view))
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

}
