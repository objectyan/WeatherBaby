package me.objectyan.weatherbaby.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import me.objectyan.weatherbaby.R;

public class CityWeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int WEATHER_TODAY = 0;
    private static final int WEATHER_TIMELINE = 1;
    private static final int WEATHER_FORECAST = 2;
    private static final int WEATHER_LIFESTYLE = 3;
    private static final int WEATHER_ATMOSPHERE = 4;
    private static final int WEATHER_COMFORT_DEGREE = 5;
    private static final int WEATHER_WIND = 6;
    private static final int WEATHER_SUNLIGHT = 7;

    private Long mCityID;

    private Context mContext;

    public CityWeatherAdapter(Long mCityID) {
        this.mCityID = mCityID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        switch (viewType) {
            case WEATHER_TODAY:
                return new TodayViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.fragment_weather_today, parent, false));
            case WEATHER_TIMELINE:
                return new TimelineViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.fragment_weather_timeline, parent, false));
            case WEATHER_FORECAST:
                return new ForecastViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.fragment_weather_forecast, parent, false));
            case WEATHER_LIFESTYLE:
                return new LifestyleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_weather_lifestyle, parent, false));
            case WEATHER_ATMOSPHERE:
                return new AtmosphereViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_weather_atmosphere, parent, false));
            case WEATHER_COMFORT_DEGREE:
                return new ComfortDegreeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_weather_comfort_degree, parent, false));
            case WEATHER_WIND:
                return new WindViewholder(LayoutInflater.from(mContext).inflate(R.layout.fragment_weather_wind, parent, false));
            case WEATHER_SUNLIGHT:
                return new SunlightViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_weather_sunlight, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private class TodayViewHolder extends RecyclerView.ViewHolder {
        public TodayViewHolder(View inflate) {
            super(inflate);
        }
    }

    private class TimelineViewHolder extends RecyclerView.ViewHolder {
        public TimelineViewHolder(View inflate) {
            super(inflate);
        }
    }

    private class ForecastViewHolder extends RecyclerView.ViewHolder {
        public ForecastViewHolder(View inflate) {
            super(inflate);
        }
    }

    private class LifestyleViewHolder extends RecyclerView.ViewHolder {
        public LifestyleViewHolder(View inflate) {
            super(inflate);
        }
    }

    private class AtmosphereViewHolder extends RecyclerView.ViewHolder {
        public AtmosphereViewHolder(View inflate) {
            super(inflate);
        }
    }

    private class ComfortDegreeViewHolder extends RecyclerView.ViewHolder {
        public ComfortDegreeViewHolder(View inflate) {
            super(inflate);
        }
    }

    private class WindViewholder extends RecyclerView.ViewHolder {
        public WindViewholder(View inflate) {
            super(inflate);
        }
    }
    
    private class SunlightViewHolder extends RecyclerView.ViewHolder {
        public SunlightViewHolder(View inflate) {
            super(inflate);
        }
    }

}
