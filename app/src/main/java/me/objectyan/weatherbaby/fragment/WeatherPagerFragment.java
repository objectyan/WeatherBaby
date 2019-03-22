package me.objectyan.weatherbaby.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.adapter.WeatherPagerAdapter;
import me.relex.circleindicator.CircleIndicator;

public class WeatherPagerFragment extends Fragment {

    @BindView(R.id.wpaf_indicator)
    CircleIndicator circleIndicator;

    @BindView(R.id.wpaf_viewpager)
    ViewPager viewPager;

    private WeatherPagerAdapter weatherPagerAdapter;

    public WeatherPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_pager_adapter, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FragmentManager fm = getFragmentManager();
        weatherPagerAdapter = new WeatherPagerAdapter(fm);
        viewPager.setAdapter(weatherPagerAdapter);
        circleIndicator.setViewPager(viewPager);
        weatherPagerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
    }
}
