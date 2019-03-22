package me.objectyan.weatherbaby.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.adapter.WeatherPagerAdapter;
import me.relex.circleindicator.CircleIndicator;

public class WeatherPagerAdapterFragment extends Fragment {

    @BindView(R.id.wpaf_indicator)
    CircleIndicator circleIndicator;

    @BindView(R.id.wpaf_viewpager)
    ViewPager viewPager;

    private WeatherPagerAdapter weatherPagerAdapter;

    public WeatherPagerAdapterFragment() {
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
        weatherPagerAdapter = new WeatherPagerAdapter();
        viewPager.setAdapter(weatherPagerAdapter);
        circleIndicator.setViewPager(viewPager);
        weatherPagerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
    }
}
