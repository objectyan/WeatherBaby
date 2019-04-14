package me.objectyan.weatherbaby.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.adapter.WeatherPagerAdapter;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.common.WeatherBabyConstants;
import me.objectyan.weatherbaby.entities.CityInfo;
import me.objectyan.weatherbaby.entities.database.CityBaseDao;
import me.objectyan.weatherbaby.fragment.WeatherFragment;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends BaseActivity {

    @BindView(R.id.header_toolbar)
    Toolbar toolbar;
    @BindView(R.id.header_title)
    TextView headerTitle;
    @BindView(R.id.wpaf_viewpager)
    ViewPager wpafViewpager;
    @BindView(R.id.wpaf_indicator)
    CircleIndicator wpafIndicator;
    @BindView(R.id.image_location)
    AppCompatImageView imageLocation;

    private CityBaseDao cityBaseDao;
    private WeatherPagerAdapter weatherPagerAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_header, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_main_settings) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_header);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_city_manage);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        FragmentManager fm = getSupportFragmentManager();
        weatherPagerAdapter = new WeatherPagerAdapter(fm);
        wpafViewpager.setAdapter(weatherPagerAdapter);
        wpafIndicator.setViewPager(wpafViewpager);
        weatherPagerAdapter.registerDataSetObserver(wpafIndicator.getDataSetObserver());
        cityBaseDao = BaseApplication.getDaoSession().getCityBaseDao();
        wpafViewpager.setCurrentItem(weatherPagerAdapter.getDefaultItem(Util.getDefaultCityID()));
    }

    @Override
    public void initData() {
        if (cityBaseDao.queryBuilder().buildCount().count() == 0) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClass(this, CityManageActivity.class);
            startActivityForResult(intent, WeatherBabyConstants.MAIN_REQUEST_CITY_MANAGE);
        }
    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), CityManageActivity.class), WeatherBabyConstants.MAIN_REQUEST_CITY_MANAGE);
            }
        });
        wpafViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset == 0) {
                    imageLocation.setVisibility(View.GONE);
                    headerTitle.setText(R.string.loading);
                    CityInfo cityInfo = ((WeatherFragment) weatherPagerAdapter.getItem(position)).getCityInfo();
                    if (cityInfo != null) {
                        imageLocation.setVisibility(cityInfo.isLocation() ? View.VISIBLE : View.GONE);
                        headerTitle.setText(cityInfo.getCityName());
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        weatherPagerAdapter.refreshData();
        // 城市管理关闭回调
        if (requestCode == WeatherBabyConstants.MAIN_REQUEST_CITY_MANAGE) {
            wpafViewpager.setCurrentItem(weatherPagerAdapter.getDefaultItem(Util.getDefaultCityID()));
        }
    }
}
