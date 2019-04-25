package me.objectyan.weatherbaby.adapter;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.view.Gravity;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.entities.database.CityBase;
import me.objectyan.weatherbaby.entities.database.CityBaseDao;
import me.objectyan.weatherbaby.fragment.WeatherFragment;

public class WeatherPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private CityBaseDao cityBaseDao;

    public WeatherPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        cityBaseDao = BaseApplication.getDaoSession().getCityBaseDao();
        this.setFragmentPositionMapForUpdate();
    }

    private void setFragmentPositionMapForUpdate() {
        fragmentList.clear();
        for (CityBase item :
                cityBaseDao.queryBuilder().orderDesc(CityBaseDao.Properties.IsDefault, CityBaseDao.Properties.Sort).build().list()) {
            fragmentList.add(WeatherFragment.newInstance(item.getId()));
        }
    }

    public void refreshData() {
        setFragmentPositionMapForUpdate();
        notifyDataSetChanged();
    }

    public void refreshSettingForData() {
        for (Fragment fragment : fragmentList) {
            WeatherFragment weatherFragment = (WeatherFragment) fragment;
            weatherFragment.refreshData();
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    public int getDefaultItem(Long cityID) {
        try {
            List<Long> cityIds = fragmentList.stream().map(fragment -> {
                return ((WeatherFragment) fragment).getCityID();
            }).collect(Collectors.toList());
            if (cityID == null || cityID == 0) {
                Long defCityID = fragmentList.stream().filter(fragment -> ((WeatherFragment) fragment).getCityInfo().isDefault())
                        .map(fragment -> ((WeatherFragment) fragment).getCityID()).collect(Collectors.toList()).get(0);
                Long locCityID = fragmentList.stream().filter(fragment -> ((WeatherFragment) fragment).getCityInfo().isLocation())
                        .map(fragment -> ((WeatherFragment) fragment).getCityID()).collect(Collectors.toList()).get(0);
                if (defCityID != locCityID) {
                    return Arrays.binarySearch(cityIds.toArray(), locCityID);
                }
                return Arrays.binarySearch(cityIds.toArray(), defCityID);
            } else {
                Long findCityID = fragmentList.stream().filter(fragment -> ((WeatherFragment) fragment).getCityID() == cityID)
                        .map(fragment -> ((WeatherFragment) fragment).getCityID()).collect(Collectors.toList()).get(0);
                return Arrays.binarySearch(cityIds.toArray(), findCityID);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
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