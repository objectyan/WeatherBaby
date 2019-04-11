package me.objectyan.weatherbaby.adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.view.Gravity;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private Query<CityBase> cityBaseQuery;

    public WeatherPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        cityBaseDao = BaseApplication.getDaoSession().getCityBaseDao();
        for (CityBase item :
                cityBaseDao.queryBuilder().orderDesc(CityBaseDao.Properties.IsDefault, CityBaseDao.Properties.Sort).build().list())
            fragmentList.add(WeatherFragment.newInstance(item.getId()));
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