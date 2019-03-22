package me.objectyan.weatherbaby.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.view.Gravity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import me.objectyan.weatherbaby.fragment.WeatherFragment;

public class WeatherPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public WeatherPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        fragmentList.add(new WeatherFragment());
        fragmentList.add(new WeatherFragment());
        fragmentList.add(new WeatherFragment());
        fragmentList.add(new WeatherFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}