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

import org.greenrobot.greendao.query.Query;

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

public class CityManageAdapter extends ArrayAdapter<CityInfo> {

    private int mResource;

    private boolean mIsEdit = false;

    private CityInfo cityInfoByAdd = new CityInfo(CityInfo.CityType.Add.getKey());

    private CityBaseDao cityBaseDao;

    public CityManageAdapter(@NonNull Context context, int resource, @NonNull List<CityInfo> objects) {
        super(context, resource, objects);
        mResource = resource;
        cityBaseDao = BaseApplication.getDaoSession().getCityBaseDao();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(this.getContext()).inflate(mResource, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if (this.mIsEdit)
            viewHolder.cityDelete.setVisibility(View.VISIBLE);
        else
            viewHolder.cityDelete.setVisibility(View.GONE);

        CityInfo cityInfo = getItem(position);

        if ((cityInfo.getType() & CityInfo.CityType.Add.getKey()) == CityInfo.CityType.Add.getKey()) {
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

        viewHolder.cityDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(cityInfo);
                cityManageItemClick.removeCity(cityInfo);
            }
        });

        viewHolder.setDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < getCount(); i++) {
                    CityInfo item = getItem(i);
                    item.removeType(CityInfo.CityType.Default.getKey());
                }
                cityInfo.setType(CityInfo.CityType.Default.getKey());
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
            }
        });

        viewHolder.cityAddLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityManageItemClick.addCity();
            }
        });

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
    }

    private onCityManageItemClick cityManageItemClick;

    public void setOnItemSelectListener(onCityManageItemClick listener) {

        this.cityManageItemClick = listener;
    }


    class ViewHolder {
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


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
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
