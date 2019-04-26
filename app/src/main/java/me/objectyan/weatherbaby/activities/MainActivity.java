package me.objectyan.weatherbaby.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.adapter.WeatherPagerAdapter;
import me.objectyan.weatherbaby.common.BaseActivity;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.common.WeatherBabyConstants;
import me.objectyan.weatherbaby.entities.CityInfo;
import me.objectyan.weatherbaby.entities.database.CityBase;
import me.objectyan.weatherbaby.entities.database.CityBaseDao;
import me.objectyan.weatherbaby.fragment.WeatherFragment;
import me.objectyan.weatherbaby.services.AutoLocationService;
import me.objectyan.weatherbaby.services.AutoUpdateService;
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

    private UpdateWeatherReceiver updateWeatherReceiver;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_header, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting_update_interval:
                showSettingUpdateIntervalDialog();
                break;
            case R.id.menu_setting_temp_unit:
                showSettingTempUnitDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(updateWeatherReceiver);
        super.onDestroy();
    }

    /**
     * 弹出更新时间设置
     */
    private void showSettingUpdateIntervalDialog() {
        Integer[] updateInterval = new Integer[]{1, 2, 6, 12};
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        singleChoiceDialog.setTitle(R.string.toolbar_menu_settings_update_interval);
        singleChoiceDialog.setSingleChoiceItems(R.array.settings_update_interval,
                Arrays.binarySearch(updateInterval, Util.getSettingsUpdateInterval()),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Util.setSettingsUpdateInterval(updateInterval[which]);
                        dialog.dismiss();
                        weatherPagerAdapter.refreshSettingForData();
                        startService(new Intent(getApplicationContext(), AutoUpdateService.class));
                    }
                });
        singleChoiceDialog.show();
    }

    /**
     * 弹出温度单位设置
     */
    private void showSettingTempUnitDialog() {
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        String[] tempUnits = getResources().getStringArray(R.array.settings_temp_unit);
        singleChoiceDialog.setTitle(R.string.toolbar_menu_settings_temp_unit);
        singleChoiceDialog.setSingleChoiceItems(tempUnits,
                Arrays.binarySearch(tempUnits, Util.getSettingsTempUnit()),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Util.setSettingsTempUnit(tempUnits[which]);
                        dialog.dismiss();
                        weatherPagerAdapter.refreshSettingForData();
                    }
                });
        singleChoiceDialog.show();
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
        // 各城市天气适配器
        weatherPagerAdapter = new WeatherPagerAdapter(fm);
        wpafViewpager.setAdapter(weatherPagerAdapter);
        wpafIndicator.setViewPager(wpafViewpager);
        weatherPagerAdapter.registerDataSetObserver(wpafIndicator.getDataSetObserver());
        cityBaseDao = BaseApplication.getDaoSession().getCityBaseDao();
        wpafViewpager.setCurrentItem(weatherPagerAdapter.getDefaultItem(Util.getDefaultCityID()));
        updateWeatherReceiver = new UpdateWeatherReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WeatherBabyConstants.RECEIVER_UPDATE_WEATHER);
        registerReceiver(updateWeatherReceiver, intentFilter);
        startService(new Intent(this, AutoUpdateService.class));
        startService(new Intent(this, AutoLocationService.class));
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
                    CityInfo cityInfo = ((WeatherFragment) weatherPagerAdapter.getItem(position)).getCityInfo();
                    setHeaderTitle(cityInfo.getId());
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

    private void setHeaderTitle(long cityID) {
        imageLocation.setVisibility(View.GONE);
        headerTitle.setText(R.string.loading);
        CityBase cityBase = cityBaseDao.load(cityID);
        if (cityBase != null) {
            imageLocation.setVisibility(cityBase.getIsLocation() ? View.VISIBLE : View.GONE);
            headerTitle.setText(cityBase.getLocation());
        }
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

    public class UpdateWeatherReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //拿到进度，更新UI
            int type = intent.getIntExtra("Type", 0);
            long cityID = intent.getLongExtra("CityID", 0);
            switch (type) {
                case WeatherBabyConstants.RECEIVE_UPDATE_TYPE_LOCATION:
                    weatherPagerAdapter.refreshSettingForCityID(cityID);
                    break;
                case WeatherBabyConstants.RECEIVE_UPDATE_TYPE_CITY_NAMR:
                    int position = intent.getIntExtra("CurrentPosition", 0);
                    if (position == wpafViewpager.getCurrentItem())
                        setHeaderTitle(cityID);
                    break;
            }
        }

    }
}
