package me.objectyan.weatherbaby.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import butterknife.ButterKnife;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.entities.CityInfo;

public class AddCityAdapter extends ArrayAdapter<CityInfo> {

    private int mResource;

    private CityInfo cityInfoByLocation = new CityInfo(CityInfo.CityType.Location.getKey());

    public AddCityAdapter(@NonNull Context context, int resource, @NonNull List<CityInfo> objects) {
        super(context, resource, objects);
        mResource = resource;
        add(cityInfoByLocation);
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

        return view;
    }

    public class ViewHolder {

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
