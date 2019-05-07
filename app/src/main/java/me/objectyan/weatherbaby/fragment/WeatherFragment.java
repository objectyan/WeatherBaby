package me.objectyan.weatherbaby.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.adapter.CityWeatherAdapter;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.common.WeatherBabyConstants;
import me.objectyan.weatherbaby.entities.CityInfo;
import me.objectyan.weatherbaby.entities.database.CityBase;
import me.objectyan.weatherbaby.entities.database.CityBaseDao;
import me.objectyan.weatherbaby.services.CityManageService;

public class WeatherFragment extends Fragment {
    private static final String TAG = WeatherFragment.class.getSimpleName();
    private static final String ARG_POSITION = "ARG_POSITION";
    private static final String ARG_CITYID = "ARG_CITYID";
    @BindView(R.id.swipe_weather_layout)
    SwipeRefreshLayout swipeWeatherLayout;
    @BindView(R.id.recyclerWeather)
    RecyclerView recyclerWeather;

    private Long mCityID;

    private int mPosition;

    private CityBaseDao cityBaseDao;

    private CityWeatherAdapter cityWeatherAdapter;

    private boolean isLoaded = false;

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

    public static WeatherFragment newInstance(Long cityID, int position) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CITYID, cityID);
        args.putLong(ARG_POSITION, position);
        fragment.setArguments(args);
        fragment.mCityID = cityID;
        fragment.mPosition = position;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, view);
        recyclerWeather.setItemViewCacheSize(200);
        recyclerWeather.setDrawingCacheEnabled(true);
        recyclerWeather.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerWeather.setLayoutManager(new LinearLayoutManager(this.getContext()));
        cityWeatherAdapter = new CityWeatherAdapter(mCityID);
        cityWeatherAdapter.setHasStableIds(true);
        recyclerWeather.setAdapter(cityWeatherAdapter);
        swipeWeatherLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(true);
            }
        });
        isLoaded = true;
        initData(false);
        return view;
    }

    private void initData(Boolean isRefresh) {
        if (isLoaded)
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
                    doOnSubscribe(disposable -> {
                        if (swipeWeatherLayout != null && !swipeWeatherLayout.isRefreshing())
                            swipeWeatherLayout.setRefreshing(true);
                    }).
                    subscribe(data -> {
                                Observable.timer(1, TimeUnit.MICROSECONDS, AndroidSchedulers.mainThread()).doOnNext(aLong -> {
                                    cityWeatherAdapter.refreshing();
                                    if (swipeWeatherLayout != null && swipeWeatherLayout.isRefreshing())
                                        swipeWeatherLayout.setRefreshing(false);
                                }).subscribe();
                                Intent intent = new Intent(WeatherBabyConstants.RECEIVER_UPDATE_WEATHER);
                                intent.putExtra("Type", WeatherBabyConstants.RECEIVE_UPDATE_TYPE_CITY_NAMR);
                                intent.putExtra("CityID", mCityID);
                                intent.putExtra("CurrentPosition", mPosition);
                                getContext().sendBroadcast(intent);
                            },
                            error -> {
                                Util.showLong(error.getLocalizedMessage());
                                Log.e(TAG, error.toString());
                            }
                    );
    }

    public void refreshData() {
        initData(true);
    }

}
