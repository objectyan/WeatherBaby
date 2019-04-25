package me.objectyan.weatherbaby.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.adapter.CityWeatherAdapter;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.entities.CityInfo;
import me.objectyan.weatherbaby.entities.database.CityBase;
import me.objectyan.weatherbaby.entities.database.CityBaseDao;
import me.objectyan.weatherbaby.services.CityManageService;

public class WeatherFragment extends Fragment {

    private static String ARG_CITYID = "ARG_CITYID";
    @BindView(R.id.swipe_weather_layout)
    SwipeRefreshLayout swipeWeatherLayout;
    @BindView(R.id.recyclerWeather)
    RecyclerView recyclerWeather;

    private Long mCityID;

    private CityBaseDao cityBaseDao;

    private CityWeatherAdapter cityWeatherAdapter;

    public WeatherFragment() {
        cityBaseDao = BaseApplication.getDaoSession().getCityBaseDao();
    }

    public Long getCityID() {
        return mCityID;
    }

    public CityInfo getCityInfo() {
        if (mCityID != null)
            return Util.cityBaseToInfo(cityBaseDao.load(mCityID));
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCityID = getArguments().getLong(ARG_CITYID);
        }
    }

    public static WeatherFragment newInstance(Long cityID) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CITYID, cityID);
        fragment.setArguments(args);
        fragment.mCityID = cityID;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, view);
        swipeWeatherLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(true);
            }
        });
        recyclerWeather.setLayoutManager(new LinearLayoutManager(this.getContext()));
        cityWeatherAdapter = new CityWeatherAdapter(mCityID);
        recyclerWeather.setAdapter(cityWeatherAdapter);
        initData(false);
        return view;
    }

    private void initData(Boolean isRefresh) {
        Observable.create(new ObservableOnSubscribe<Long>() {
            @SuppressLint("NewApi")
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                CityBase cityBase = cityBaseDao.queryBuilder().where(CityBaseDao.Properties.Id.eq(mCityID)).unique();
                if ((cityBase.getUpdateTime() == null ||
                        cityBase.getUpdateTime().getTime() < new Date().getTime() ||
                        (new Date().getTime() - cityBase.getPublishTime().getTime() > Util.getSettingsUpdateInterval() * 60 * 1000) ||
                        isRefresh) && Util.isNetworkConnected()) {
                    CityManageService.refreshCityInfo(mCityID).doOnNext(cityID -> {
                        emitter.onNext(cityBase.getId());
                    }).subscribe();
                } else {
                    emitter.onNext(cityBase.getId());
                }
            }
        }).
                doOnSubscribe(_a -> {
                    swipeWeatherLayout.setRefreshing(true);
                }).
                doFinally(() -> {
                    cityWeatherAdapter.notifyDataSetChanged();
                    swipeWeatherLayout.setRefreshing(false);
                }).
                subscribe(data -> {
                            cityWeatherAdapter.notifyDataSetChanged();
                            swipeWeatherLayout.setRefreshing(false);
                        },
                        error -> {
                            Util.showLong(error.toString());
                        }
                );
    }

    public void refreshData() {
        initData(true);
    }

    @OnClick(R.id.swipe_weather_layout)
    public void onViewClicked() {
    }
}
