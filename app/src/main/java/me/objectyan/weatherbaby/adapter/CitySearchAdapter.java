package me.objectyan.weatherbaby.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.entities.CityInfo;

public class CitySearchAdapter extends ArrayAdapter<CityInfo> {

    private int mResource;

    public CitySearchAdapter(@NonNull Context context, int resource, @NonNull List<CityInfo> objects) {
        super(context, resource, objects);
        mResource = resource;
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
        CityInfo cityInfo = getItem(position);
        viewHolder.cityName.setText(cityInfo.getCityName());
        return view;
    }

    class ViewHolder {
        @BindView(R.id.search_item)
        TextView cityName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}