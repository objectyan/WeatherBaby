package me.objectyan.weatherbaby.activities;

import android.content.DialogInterface;
import android.content.Intent;
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

    /**
     * 弹出更新时间设置
     */
    private void showSettingUpdateIntervalDialog() {
        Integer[] updateInterval = new Integer[]{1, 2, 6, 12};
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(this);
        singleChoiceDialog.setTitle(R.string.toolbar_menu_settings_update_interval);
        singleChoiceDialog.setSingleChoiceItems(R.array.settings_update_interval,
                Arrays.binarySearch(updateInterval, Util.getSettingsUpdateInterval()),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Util.setSettingsUpdateInterval(updateInterval[which]);
                        dialog.dismiss();
                        weatherPagerAdapter.refreshSettingForData();
                    }
                });
        singleChoiceDialog.show();
    }

    /**
     * 弹出温度单位设置
     */
    private void showSettingTempUnitDialog() {
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(this);
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
