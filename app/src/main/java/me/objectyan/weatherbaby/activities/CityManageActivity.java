package me.objectyan.weatherbaby.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.adapter.CityManageAdapter;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.entities.CityInfo;
import me.objectyan.weatherbaby.entities.database.CityBase;
import me.objectyan.weatherbaby.entities.database.CityBaseDao;
import me.objectyan.weatherbaby.services.CityManageService;

public class CityManageActivity extends BaseActivity {

    @BindView(R.id.header_toolbar)
    Toolbar headerToolbar;
    @BindView(R.id.gv_city_manage)
    GridView gvCityManage;

    MenuItem menuCityManageEdit;
    MenuItem menuCityManageConfirm;
    MenuItem menuCityManageRefresh;

    CityManageAdapter cityManageAdapter;

    private CityBaseDao cityBaseDao;
    private Query<CityBase> cityBaseQuery;

    @Override
    public void initView() {
        setContentView(R.layout.activity_city_manage);
        ButterKnife.bind(this);
        setSupportActionBar(headerToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        cityManageAdapter = new CityManageAdapter(this, R.layout.activity_city_manage_item, new ArrayList<>());
        gvCityManage.setAdapter(cityManageAdapter);
        cityBaseDao = BaseApplication.getDaoSession().getCityBaseDao();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_city_manage, menu);
        menuCityManageEdit = menu.findItem(R.id.menu_city_manage_edit);
        menuCityManageConfirm = menu.findItem(R.id.menu_city_manage_confirm);
        menuCityManageRefresh = menu.findItem(R.id.menu_city_manage_refresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_city_manage_edit:
                menuCityManageEdit.setVisible(false);
                menuCityManageConfirm.setVisible(true);
                cityManageAdapter.setIsEdit(true);
                menuCityManageRefresh.setEnabled(false);
                break;
            case R.id.menu_city_manage_refresh:
                menuCityManageRefresh.setIcon(R.drawable.ic_disabled);
                menuCityManageEdit.setEnabled(false);
                cityManageAdapter.setRomoveAdd(true);
                refreshAll();
                break;
            case R.id.menu_city_manage_confirm:
                menuCityManageEdit.setVisible(true);
                menuCityManageConfirm.setVisible(false);
                cityManageAdapter.setIsEdit(false);
                menuCityManageRefresh.setEnabled(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshAll() {
        List<Long> refreshCity = new ArrayList<>();
        for (int i = 0; i < cityManageAdapter.getCount(); i++) {
            refreshCity.add(cityManageAdapter.getItem(i).getId());
        }
        cityManageAdapter.setRefreshCity(refreshCity);
        for (Long cityID : refreshCity) {
            CityManageService.refreshCityInfo(cityID).doOnNext(cid -> {
                refreshCity.remove(cityID);
                cityManageAdapter.updateCityInfo(cityID);
                cityManageAdapter.setRefreshCity(refreshCity);
                if (refreshCity.isEmpty()) {
                    menuCityManageRefresh.setIcon(R.drawable.ic_refresh);
                    menuCityManageEdit.setEnabled(true);
                    cityManageAdapter.setRomoveAdd(false);
                }
            }).subscribe();
        }
    }

    @Override
    public void initData() {
        cityManageAdapter.clear();
        cityBaseQuery = cityBaseDao.queryBuilder().build();
        List<CityInfo> array = new ArrayList<>();
        for (CityBase item : cityBaseQuery.list()) {
            array.add(Util.cityBaseToInfo(item));
        }
        cityManageAdapter.addAll(array);
    }

    @Override
    public void initListener() {
        cityManageAdapter.setOnItemSelectListener(new CityManageAdapter.onCityManageItemClick() {
            @Override
            public void addCity() {
                Intent intent = new Intent(getApplicationContext(), AddCityActivity.class);
                startActivityForResult(intent, 1);
            }

            @Override
            public void removeCity(CityInfo cityInfo) {
            }

            @Override
            public void settingDefault(CityInfo cityInfo) {

            }

            @Override
            public void openHome(CityInfo cityInfo) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(getApplicationContext(), MainActivity.class);
                Util.setDefaultCityID(cityInfo.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        String result = data.getExtras().getString("result");
//        Log.i("11111111111", result);
    }

}
