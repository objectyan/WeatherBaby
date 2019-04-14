package me.objectyan.weatherbaby.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.adapter.AddCityAdapter;
import me.objectyan.weatherbaby.adapter.CitySearchAdapter;
import me.objectyan.weatherbaby.common.BaseActivity;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.common.WeatherBabyConstants;
import me.objectyan.weatherbaby.entities.CityInfo;
import me.objectyan.weatherbaby.entities.database.CityBase;
import me.objectyan.weatherbaby.entities.database.CityBaseDao;
import me.objectyan.weatherbaby.entities.heweather.BasicEntity;
import me.objectyan.weatherbaby.services.CityManageService;
import me.objectyan.weatherbaby.services.HeWeatherApiService;

public class AddCityActivity extends BaseActivity implements View.OnClickListener {

    private static final String LOG_TAG = "AddCityActivity";

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
    @BindView(R.id.all_city)
    RelativeLayout allCity;
    @BindView(R.id.no_matched_city_tv)
    TextView noMatchedCityTv;
    @BindView(R.id.lv_search_city)
    ListView lvSearchCity;

    private AddCityAdapter addCityAdapter;

    private CitySearchAdapter citySearchAdapter;

    private int currentType = 0;

    private boolean isOpenWeather = true;

    private String mSelectedProvince;

    private String mSelectedCity;

    private CityBaseDao cityBaseDao;

    @Override
    public void initView() {
        setContentView(R.layout.activity_add_city);
        ButterKnife.bind(this);
        setSupportActionBar(headerToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        addCityAdapter = new AddCityAdapter(this, R.layout.activity_add_city_item, new ArrayList<>());
        citySearchAdapter = new CitySearchAdapter(this, R.layout.activity_add_city_search, new ArrayList());
        searchResult.setAdapter(addCityAdapter);
        lvSearchCity.setAdapter(citySearchAdapter);
        cityBaseDao = BaseApplication.getDaoSession().getCityBaseDao();
    }

    @Override
    public void initData() {
        initRecommendCities();
    }

    @Override
    public void initListener() {
        moreCityAndReturnBtn.setOnClickListener(this);
        searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityInfo cityInfo = (CityInfo) parent.getItemAtPosition(position);
                if (isOpenWeather) {
                    startWeatherActivity(cityInfo);
                } else {
                    currentType += 1;
                    if (currentType == 1) {
                        mSelectedProvince = cityInfo.getCityName();
                    } else if (currentType == 2) {
                        mSelectedCity = cityInfo.getCityName();
                    }
                    queryCityByType(currentType, cityInfo.getCityName());
                }
            }
        });
        searchCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("NewApi")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cityName = s.toString();
                if (TextUtils.isEmpty(cityName)) {
                    allCity.setVisibility(View.VISIBLE);
                    noMatchedCityTv.setVisibility(View.GONE);
                    lvSearchCity.setVisibility(View.GONE);
                } else {
                    allCity.setVisibility(View.GONE);
                    lvSearchCity.setVisibility(View.VISIBLE);
                    noMatchedCityTv.setVisibility(View.GONE);
                    searchCity(cityName);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        lvSearchCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityInfo cityInfo = citySearchAdapter.getItem(position);
                startWeatherActivity(cityInfo);
            }
        });
    }

    private void startWeatherActivity(CityInfo cityInfo) {
        if (cityInfo.isLocation() && CityManageService.hasLocation()) {
            Util.showShort(R.string.city_already_exists_location);
            return;
        }
        if (CityManageService.isDisabled(cityInfo.getCityName())) {
            Util.showShort(getString(R.string.city_already_exists, cityInfo.getCityName()));
            return;
        }

        CityBase cityBase = Util.cityInfoToBase(cityInfo);
        if (cityInfo.isLocation()) {
            Location location = Util.getCurrentLocation();
            if (location == null) {
                Util.showLong(R.string.no_location);
            } else {
                cityBase.setLatitude(location.getLatitude());
                cityBase.setLongitude(location.getLongitude());
            }
            cityBase.setLocation(getString(R.string.current_location_addresss));
            cityBase.setIsLocation(true);
        }
        cityBase.setId(null);
        cityBase.setSort(0);
        cityBase.setIsDefault(cityBaseDao.loadAll().

                isEmpty());
        cityBaseDao.insert(cityBase);
        Log.d(LOG_TAG, "Inserted new CityBase, ID: " + cityBase.getId());
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(

                getApplicationContext(), MainActivity.class);
        Util.setDefaultCityID(cityBase.getId());

        startActivity(intent);

    }

    /**
     * 获取推荐城市
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.N)
    private void initRecommendCities() {
        List<CityInfo> recommendCities = new ArrayList<>();
        recommendCities.add(new CityInfo(CityInfo.CityType.Location.getKey()));
        Observable.create(new ObservableOnSubscribe<List<BasicEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BasicEntity>> emitter) throws Exception {
                if (Util.isNetworkConnected()) {
                    HeWeatherApiService.getInstance().fetchTopCityInfo().onErrorResumeNext(throwable -> {
                        return observer -> {
                            observer.onNext(getLocalTopBaseCity());
                            observer.onComplete();
                        };
                    }).doOnNext(basicEntities -> {
                        emitter.onNext(basicEntities);
                    }).subscribe();
                } else {
                    emitter.onNext(getLocalTopBaseCity());
                }
            }
        }).doOnNext(basicEntities -> {
            basicEntities.forEach(basicEntity -> {
                CityInfo cityInfo = new CityInfo();
                cityInfo.setCityName(basicEntity.Name);
                cityInfo.setLatitude(basicEntity.latitude);
                cityInfo.setLongitude(basicEntity.longitude);
                cityInfo.setWeatherCode(basicEntity.code);
                recommendCities.add(cityInfo);
            });
        }).doAfterNext(basicEntities -> {
            searchBarText.setText(R.string.recommend_city);
            addCityAdapter.clear();
            addCityAdapter.addAll(recommendCities);
            moreCityAndReturnBtnTv.setText(R.string.more_city);
        }).subscribe();
    }

    private List<BasicEntity> getLocalTopBaseCity() {
        List<BasicEntity> baseWeatherEntities = new ArrayList<>();
        XmlPullParser parser = getResources().getXml(R.xml.city_china);
        try {
            int eventType = parser.getEventType();
            int depth = 0;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if ("county".equals(parser.getName()) && depth == 0) {
                        depth = parser.getDepth();
                        BasicEntity basicEntity = new BasicEntity();
                        basicEntity.Name = parser.getAttributeValue(null, "name");
                        basicEntity.latitude = parser.getAttributeValue(null, "latitude");
                        basicEntity.longitude = parser.getAttributeValue(null, "longitude");
                        basicEntity.code = parser.getAttributeValue(null, "weatherCode");
                        baseWeatherEntities.add(basicEntity);
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if ("province".equals(parser.getName()))
                        depth = 0;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            Log.d(LOG_TAG, "initRecommendCities(): " + e.toString());
        }
        return baseWeatherEntities;
    }

    /**
     * 查询所有城市
     *
     * @param type 0 省 1 市 2 区
     */
    private void queryCityByType(int type, String val) {
        List<CityInfo> recommendCities = new ArrayList<>();
        XmlPullParser parser = getResources().getXml(R.xml.city_china);
        String typeName = null, pType = null, pVal = null;
        switch (type) {
            case 0:
                typeName = "province";
                isOpenWeather = false;
                break;
            case 1:
                typeName = "city";
                pType = "province";
                isOpenWeather = false;
                pVal = mSelectedProvince;
                break;
            case 2:
                typeName = "county";
                pType = "city";
                isOpenWeather = true;
                pVal = mSelectedCity;
                break;
        }
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (pVal == null && val == null && parser.getName().equals("country")) {
                        pVal = parser.getAttributeValue(null, "name");
                        val = pVal;
                    } else if (pType != null && parser.getName().equals(pType)) {
                        pVal = parser.getAttributeValue(null, "name");
                    }
                    if (parser.getName().equals(typeName) && pVal.equals(val)) {
                        CityInfo cityInfo = new CityInfo();
                        cityInfo.setCityName(parser.getAttributeValue(null, "name"));
                        cityInfo.setLatitude(parser.getAttributeValue(null, "latitude"));
                        cityInfo.setLongitude(parser.getAttributeValue(null, "longitude"));
                        cityInfo.setSpell(parser.getAttributeValue(null, "spell"));
                        cityInfo.setWeatherCode(parser.getAttributeValue(null, "weatherCode"));
                        recommendCities.add(cityInfo);
                    }
                }
                eventType = parser.next();
            }
            searchBarText.setText(val);
            addCityAdapter.clear();
            addCityAdapter.addAll(recommendCities);
            moreCityAndReturnBtnTv.setText(R.string.city_back);
        } catch (Exception e) {
            Log.d(LOG_TAG, "queryCityByType(): " + e.toString());
        }
    }

    /**
     * 搜索城市
     *
     * @param query
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void searchCity(String query) {
        List<CityInfo> recommendCities = new ArrayList<>();
        XmlPullParser parser = getResources().getXml(R.xml.city_china);
        try {
            int eventType = parser.getEventType();
            String country = null, province = null, city = null, county = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    switch (name) {
                        case "country":
                            country = parser.getAttributeValue(null, "name");
                            break;
                        case "province":
                            province = parser.getAttributeValue(null, "name");
                            break;
                        case "city":
                            city = parser.getAttributeValue(null, "name");
                            break;
                        case "county":
                            county = parser.getAttributeValue(null, "name");
                            CityInfo cityInfo = new CityInfo();
                            cityInfo.setCityName(county);
                            cityInfo.setLatitude(parser.getAttributeValue(null, "latitude"));
                            cityInfo.setLongitude(parser.getAttributeValue(null, "longitude"));
                            cityInfo.setSpell(parser.getAttributeValue(null, "spell"));
                            cityInfo.setWeatherCode(parser.getAttributeValue(null, "weatherCode"));
                            List<String> fullPath = new ArrayList<>();
                            fullPath.add(country);
                            if (!fullPath.contains(province)) fullPath.add(province);
                            if (!fullPath.contains(city)) fullPath.add(city);
                            if (!fullPath.contains(county)) fullPath.add(county);
                            cityInfo.setFullPath(fullPath);
                            recommendCities.add(cityInfo);
                            break;
                    }
                }
                eventType = parser.next();
            }
            @SuppressLint({"NewApi", "LocalSuppress"})
            List<CityInfo> result = recommendCities.stream().filter((CityInfo s) -> s.getCityName().contains(query) || s.getSpell().contains(query)).collect(Collectors.toList());
            if (result.isEmpty()) {
                lvSearchCity.setVisibility(View.GONE);
                noMatchedCityTv.setVisibility(View.VISIBLE);
            } else {
                lvSearchCity.setVisibility(View.VISIBLE);
                noMatchedCityTv.setVisibility(View.GONE);
                citySearchAdapter.clear();
                citySearchAdapter.addAll(result);
            }
        } catch (Exception e) {
            Log.d(LOG_TAG, "initRecommendCities(): " + e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_city_and_return_btn:
                if (isOpenWeather && currentType == 0) {
                    queryCityByType(0, null);
                } else if (currentType <= 3) {
                    currentType -= 1;
                    if (!isOpenWeather && currentType < 0) {
                        isOpenWeather = true;
                        currentType = 0;
                        initRecommendCities();
                    } else {
                        String val = null;
                        if (currentType == 1) {
                            val = mSelectedProvince;
                        }
                        queryCityByType(currentType, val);
                    }
                }
                break;
        }
    }
}
