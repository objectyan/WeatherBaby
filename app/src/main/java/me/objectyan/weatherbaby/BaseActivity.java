package me.objectyan.weatherbaby;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    // 初始化控件
    public abstract void initView();

    // 初始化疏数据
    public abstract void initData();

    // 初始化监听器
    public abstract void initListener();

}
