package me.objectyan.weatherbaby.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.adapter.AddCityAdapter;
import me.objectyan.weatherbaby.entities.CityInfo;

public class AddCityActivity extends BaseActivity {

    @BindView(R.id.header_toolbar)
    Toolbar headerToolbar;
    @BindView(R.id.search_city)
    EditText searchCity;
    @BindView(R.id.search_bar_text)
    TextView searchBarText;
    @BindView(R.id.search_result)
    GridView searchResult;
    @BindView(R.id.more_city_and_return_btn_tv)
    TextView moreCityAndReturnBtnTv;
    @BindView(R.id.more_city_and_return_btn)
    LinearLayout moreCityAndReturnBtn;

    AddCityAdapter addCityAdapter;

    @Override
    public void initView() {
        setContentView(R.layout.activity_add_city);
        ButterKnife.bind(this);
        setSupportActionBar(headerToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        addCityAdapter = new AddCityAdapter(this, R.layout.activity_add_city_item, new ArrayList<>());
        searchResult.setAdapter(addCityAdapter);
    }

    @Override
    public void initData() {
        for (int i = 0; i <= 100; i++) {
            addCityAdapter.add(new CityInfo("" + i));
        }
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
