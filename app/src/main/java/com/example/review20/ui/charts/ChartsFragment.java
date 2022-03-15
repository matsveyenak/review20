package com.example.review20.ui.charts;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.review20.MainActivity;
import com.example.review20.R;
import com.example.review20.ui.artists.ArtistItem;
import com.example.review20.ui.artists.ArtistListAdapter;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.transition.Hold;
import com.google.android.material.transition.MaterialContainerTransform;

import java.util.ArrayList;

public class ChartsFragment extends Fragment
{
    private RecyclerView rvCharts;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_charts, container, false);

        if (MainActivity.playerViewModel.click > 0 )
        {
            MainActivity.playerCard.card.setVisibility(View.VISIBLE);
        }

        MainActivity.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        MainActivity.tvToolbar.setText("charts");

        rvCharts = root.findViewById(R.id.rv_charts);

        return root;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        ArrayList<ChartItem> charts = new ArrayList<>();

        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
        Cursor c = db.query("charts", null, null, null, null, null, null);
        if (c.moveToFirst())
        {
            int nameColIndex = c.getColumnIndex("title");
            int imgColIndex = c.getColumnIndex("img");
            int uriColIndex = c.getColumnIndex("uri");
            do
            {
                String title = c.getString(nameColIndex);
                String img = c.getString(imgColIndex);
                String uri = c.getString(uriColIndex);

                charts.add(new ChartItem(title, img, uri));
            }
            while (c.moveToNext());
        }
        c.close();

        rvCharts.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        ChartListAdapter adapter = new ChartListAdapter(getActivity(), charts, ChartsFragment.this);//, Navigation.findNavController(getView()));
        rvCharts.setAdapter(adapter);
    }

    public void createTransition(Fragment fragment, View view)
    {
        MaterialContainerTransform transform = new MaterialContainerTransform();
        transform.setFadeMode(MaterialContainerTransform.FADE_MODE_CROSS);
        transform.setContainerColor(getResources().getColor(R.color.white));

        Hold hold = new Hold();
        hold.addTarget(R.id.charts_fragment);
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
