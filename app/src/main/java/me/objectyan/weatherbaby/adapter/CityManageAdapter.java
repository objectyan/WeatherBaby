package me.objectyan.weatherbaby.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.entities.CityInfo;

public class CityManageAdapter extends ArrayAdapter<CityInfo> {

    private int mResource;

    private boolean mIsEdit = false;

    private List<CityInfo> mData;

    private CityInfo cityInfoByAdd = new CityInfo(CityInfo.CityType.Add.getKey());

    public CityManageAdapter(@NonNull Context context, int resource, @NonNull List<CityInfo> objects) {
        super(context, resource, objects);
        mResource = resource;
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
        }

        if ((cityInfo.getType() & CityInfo.CityType.Default.getKey()) == CityInfo.CityType.Default.getKey()) {
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
            }
        });

        return view;
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


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
