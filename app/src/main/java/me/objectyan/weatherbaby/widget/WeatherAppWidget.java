package me.objectyan.weatherbaby.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.google.gson.annotations.Until;

import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.common.WeatherBabyConstants;
import me.objectyan.weatherbaby.services.UpdateWeatherAppWidgetService;

public class WeatherAppWidget extends AppWidgetProvider {

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Intent stopUpdateIntent = new Intent(context, UpdateWeatherAppWidgetService.class);
        context.stopService(stopUpdateIntent);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Intent startUpdateIntent = new Intent(context, UpdateWeatherAppWidgetService.class);
        context.startService(startUpdateIntent);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Intent startUpdateIntent = new Intent(context, UpdateWeatherAppWidgetService.class);
        context.startService(startUpdateIntent);
    }
}

