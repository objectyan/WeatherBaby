package me.objectyan.weatherbaby.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.annotations.Until;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.activities.AddCityActivity;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.entities.CityInfo;
import me.objectyan.weatherbaby.entities.database.CityBase;
import me.objectyan.weatherbaby.entities.database.CityBaseDao;
import me.objectyan.weatherbaby.entities.database.CityDailyForecastDao;
import me.objectyan.weatherbaby.entities.database.CityHourlyForecastDao;
import me.objectyan.weatherbaby.entities.database.CityLifestyleForecastDao;
import me.objectyan.weatherbaby.services.CityManageService;

public class CityManageAdapter extends ArrayAdapter<CityInfo> {

    private int mResource;

    private boolean mIsEdit = false;

    private CityInfo cityInfoByAdd = new CityInfo(CityInfo.CityType.Add.getKey());

    private CityBaseDao cityBaseDao;
    private CityDailyForecastDao cityDailyForecastDao;
    private CityLifestyleForecastDao cityLifestyleForecastDao;
    private CityHourlyForecastDao cityHourlyForecastDao;

    public CityManageAdapter(@NonNull Context context, int resource, @NonNull List<CityInfo> objects) {
        super(context, resource, objects);
        mResource = resource;
        cityBaseDao = BaseApplication.getDaoSession().getCityBaseDao();
        cityDailyForecastDao = BaseApplication.getDaoSession().getCityDailyForecastDao();
        cityLifestyleForecastDao = BaseApplication.getDaoSession().getCityLifestyleForecastDao();
        cityHourlyForecastDao = BaseApplication.getDaoSession().getCityHourlyForecastDao();
        add(cityInfoByAdd);
    }

    public void setIsEdit(boolean isShow) {
        mIsEdit = isShow;
        this.setRomoveAdd(isShow);
        this.notifyDataSetChanged();
    }

    public void setRomoveAdd(boolean isRemove) {
        if (isRemove) {
            remove(cityInfoByAdd);
        } else {
            add(cityInfoByAdd);
        }
    }

    public void updateCityInfo(Long cityID) {
        List<CityInfo> cityInfos = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            CityInfo cityInfo = getItem(i);
            if (!cityInfo.isAdd()) {
                if (cityInfo.getId().equals(cityID)) {
                    cityInfo = Util.cityBaseToInfo(cityBaseDao.load(cityID));
                }
                cityInfos.add(cityInfo);
            }
        }
        clear();
        addAll(cityInfos);
    }

    public void setRefreshCity(List<Long> refreshCity) {
        this.refreshCity = refreshCity;
        notifyDataSetChanged();
    }

    private List<Long> refreshCity = new ArrayList<>();

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityInfo cityInfo = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(this.getContext()).inflate(mResource, parent, false);
            viewHolder = new ViewHolder(view, cityInfo);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if (this.mIsEdit)
            viewHolder.cityDelete.setVisibility(View.VISIBLE);
        else
            viewHolder.cityDelete.setVisibility(View.GONE);

        if (cityInfo.isAdd()) {
            viewHolder.cityAddLayout.setVisibility(View.VISIBLE);
            viewHolder.cityWeatherLayout.setVisibility(View.GONE);
        } else {
            viewHolder.cityAddLayout.setVisibility(View.GONE);
            viewHolder.cityWeatherLayout.setVisibility(View.VISIBLE);
            viewHolder.cityName.setText(cityInfo.getCityName());
            viewHolder.cityWeatherIcon.setImageDrawable(Util.getHeWeatherIcon(cityInfo.getWeatherType()));
            viewHolder.cityWeather.setText(cityInfo.getWeatherTypeTxt());
            viewHolder.cityWeatherMax.setText(String.valueOf(cityInfo.getTempHigh()));
            viewHolder.cityWeatherMin.setText(String.valueOf(cityInfo.getTempLow()));

            if (Arrays.binarySearch(refreshCity.toArray(), cityInfo.getId()) >= 0) {
                viewHolder.cityWeatherLayout.setVisibility(View.INVISIBLE);
                viewHolder.cityLoadingLayout.setVisibility(View.VISIBLE);
            } else {
                viewHolder.cityWeatherLayout.setVisibility(View.VISIBLE);
                viewHolder.cityLoadingLayout.setVisibility(View.GONE);
            }
        }

        if (cityInfo.isDefault()) {
            viewHolder.setDefault.setText(R.string.city_default);
            viewHolder.setDefaultLayout.setBackgroundResource(R.color.icon_enabled);
            viewHolder.setDefault.setTextColor(ContextCompat.getColor(this.getContext(), R.color.icon_disabled));
            viewHolder.setDefault.setEnabled(false);
        } else {
            viewHolder.setDefault.setText(R.string.city_setting_default);
            viewHolder.setDefaultLayout.setBackgroundResource(R.color.icon_disabled);
            viewHolder.setDefault.setTextColor(ContextCompat.getColor(this.getContext(), R.color.icon_enabled));
            viewHolder.setDefault.setEnabled(true);
        }

        viewHolder.imageLocation.setVisibility(cityInfo.isLocation() ? View.VISIBLE : View.GONE);

        return view;
    }


    /**
     * 接口回调
     */
    public interface onCityManageItemClick {
        /**
         * 添加城市
         */
        void addCity();

        /**
         * 删除城市
         */
        void removeCity(CityInfo cityInfo);

        /**
         * 设置默认
         */
        void settingDefault(CityInfo cityInfo);

        /**
         * 设置默认
         */
        void openHome(CityInfo cityInfo);
    }

    private onCityManageItemClick cityManageItemClick;

    public void setOnItemSelectListener(onCityManageItemClick listener) {
        this.cityManageItemClick = listener;
    }

    class ViewHolder implements View.OnClickListener {
        @BindView(R.id.city_weather_layout)
        LinearLayout cityWeatherLayout;
        @BindView(R.id.city_name)
        TextView cityName;
        @BindView(R.id.city_weather_min)
        TextView cityWeatherMin;
        @BindView(R.id.city_weather_max)
        TextView cityWeatherMax;
        @BindView(R.id.city_weather)
        TextView cityWeather;
        @BindView(R.id.set_default)
        TextView setDefault;
        @BindView(R.id.city_add_layout)
        LinearLayout cityAddLayout;
        @BindView(R.id.city_loading_layout)
        LinearLayout cityLoadingLayout;
        @BindView(R.id.city_delete)
        ImageView cityDelete;
        @BindView(R.id.set_default_layout)
        LinearLayout setDefaultLayout;
        @BindView(R.id.city_weather_icon)
        AppCompatImageView cityWeatherIcon;
        @BindView(R.id.image_location)
        AppCompatImageView imageLocation;


        CityInfo cityInfo;

        ViewHolder(View view, CityInfo mCityInfo) {
            ButterKnife.bind(this, view);
            cityDelete.setOnClickListener(this);
            setDefault.setOnClickListener(this);
            cityAddLayout.setOnClickListener(this);
            view.setOnClickListener(this);
            cityInfo = mCityInfo;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.city_delete:
                    remove(cityInfo);
                    cityBaseDao.deleteByKey(cityInfo.getId());
                    cityDailyForecastDao.queryBuilder().where(CityDailyForecastDao.Properties.CityID.eq(cityInfo.getId()))
                            .buildDelete().executeDeleteWithoutDetachingEntities();
                    cityHourlyForecastDao.queryBuilder().where(CityHourlyForecastDao.Properties.CityID.eq(cityInfo.getId()))
                            .buildDelete().executeDeleteWithoutDetachingEntities();
                    cityLifestyleForecastDao.queryBuilder().where(CityLifestyleForecastDao.Properties.CityID.eq(cityInfo.getId()))
                            .buildDelete().executeDeleteWithoutDetachingEntities();
                    break;
                case R.id.set_default:
                    for (int i = 0; i < getCount(); i++) {
                        CityInfo item = getItem(i);
                        item.removeType(CityInfo.CityType.Default.getKey());
                    }
                    cityInfo.setType(CityInfo.CityType.Default.getKey());
                    Util.setDefaultCityID(cityInfo.getId());
                    notifyDataSetChanged();
                    List<CityBase> cityBaseQuery = cityBaseDao.queryBuilder().where(CityBaseDao.Properties.Id.notEq(cityInfo.getId()), CityBaseDao.Properties.IsDefault.eq(true)).list();
                    for (CityBase item :
                            cityBaseQuery) {
                        item.setIsDefault(false);
                    }
                    cityBaseDao.updateInTx(cityBaseQuery);
                    CityBase cityBase = cityBaseDao.load(cityInfo.getId());
                    if (cityBase != null) {
                        cityBase.setIsDefault(true);
                        cityBaseDao.update(cityBase);
                    }
                    cityManageItemClick.settingDefault(cityInfo);
                    break;
                case R.id.city_add_layout:
                    cityManageItemClick.addCity();
                    break;
                default:
                    cityManageItemClick.openHome(cityInfo);
                    break;
            }
        }
    }

    @Override
    public void addAll(CityInfo... items) {
        remove(cityInfoByAdd);
        super.addAll(items);
        super.add(cityInfoByAdd);
    }

    @Override
    public void addAll(@NonNull Collection<? extends CityInfo> collection) {
        remove(cityInfoByAdd);
        super.addAll(collection);
        super.add(cityInfoByAdd);
    }

    @Override
    public void add(@Nullable CityInfo object) {
        remove(cityInfoByAdd);
        super.add(object);
    }
}
