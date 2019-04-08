package me.objectyan.weatherbaby.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.entities.heweather.WeatherApi;
import me.objectyan.weatherbaby.services.HeWeatherApiService;

public class MainActivity extends BaseActivity {

    @BindView(R.id.header_toolbar)
    Toolbar toolbar;

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
    }

    @Override
    public void initData() {
        HeWeatherApiService.getInstance().fetchWeather("上海市");
    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CityManageActivity.class));
            }
        });
    }
}
