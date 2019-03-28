package me.objectyan.weatherbaby.activities;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.objectyan.weatherbaby.R;

public class CityManageActivity extends BaseActivity {

    @BindView(R.id.header_toolbar)
    Toolbar headerToolbar;
    @BindView(R.id.gv_city_manage)
    GridView gvCityManage;

    MenuItem menuCityManageEdit;
    MenuItem menuCityManageConfirm;
    MenuItem menuCityManageRefresh;


    @Override
    public void initView() {
        setContentView(R.layout.activity_city_manage);
        ButterKnife.bind(this);
        setSupportActionBar(headerToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                break;
            case R.id.menu_city_manage_refresh:
                menuCityManageRefresh.setIcon(R.drawable.ic_disabled);
                break;
            case R.id.menu_city_manage_confirm:
                menuCityManageEdit.setVisible(true);
                menuCityManageConfirm.setVisible(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
