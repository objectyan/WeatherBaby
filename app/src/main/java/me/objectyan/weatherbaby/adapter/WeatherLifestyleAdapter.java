package me.objectyan.weatherbaby.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.entities.database.CityLifestyleForecast;
import me.objectyan.weatherbaby.entities.database.CityLifestyleForecastDao;
import me.objectyan.weatherbaby.services.AutoUpdateService;

public class WeatherLifestyleAdapter extends ArrayAdapter<CityLifestyleForecast> {

    private int mResource;

    private CityLifestyleForecastDao cityLifestyleForecastDao;

    private Long mCityID;

    public WeatherLifestyleAdapter(@NonNull Context context, int resource, Long mCityID) {
        super(context, resource, new ArrayList<>());
        mResource = resource;
        this.mCityID = mCityID;
        cityLifestyleForecastDao = BaseApplication.getDaoSession().getCityLifestyleForecastDao();
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityLifestyleForecast cityLifestyleForecast = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(this.getContext()).inflate(mResource, parent, false);
            viewHolder = new ViewHolder(view, cityLifestyleForecast);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.lifestyleBrief.setText(cityLifestyleForecast.getBriefIntroduction());
        viewHolder.lifestyleName.setText(Util.getLifestyleName(cityLifestyleForecast.getType()));
        viewHolder.lifestyleIcon.setImageResource(Util.getLifestyleIcon(cityLifestyleForecast.getType()));
        return view;
    }

    public void updateData() {
        clear();
        addAll(cityLifestyleForecastDao.queryBuilder().where(CityLifestyleForecastDao.Properties.CityID.eq(mCityID)).list());
        notifyDataSetChanged();
    }


    class ViewHolder implements View.OnClickListener {
        @BindView(R.id.lifestyle_icon)
        AppCompatImageView lifestyleIcon;
        @BindView(R.id.lifestyle_brief)
        TextView lifestyleBrief;
        @BindView(R.id.lifestyle_name)
        TextView lifestyleName;
        @BindView(R.id.lifestyle_layout)
        LinearLayout lifestyleLayout;

        CityLifestyleForecast mCityLifestyleForecast;

        View view;

        ViewHolder(View view, CityLifestyleForecast mCityLifestyleForecast) {
            ButterKnife.bind(this, view);
            this.view = view;
            this.mCityLifestyleForecast = mCityLifestyleForecast;
            lifestyleLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder singleChoiceDialog =
                    new AlertDialog.Builder(view.getContext());
            singleChoiceDialog.setTitle(Util.getLifestyleName(mCityLifestyleForecast.getType()));
            singleChoiceDialog.setMessage(mCityLifestyleForecast.getDescription());
            singleChoiceDialog.show();
        }
    }
}
