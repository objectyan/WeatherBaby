package me.objectyan.weatherbaby;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CityActivity extends BaseActivity {

    @Override
    public void initView() {
        setContentView(R.layout.activity_city);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.hide();
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
