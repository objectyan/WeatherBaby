package me.objectyan.weatherbaby.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.core.view.ViewCompat;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.common.BaseApplication;

public class SunlightView extends View {

    private int width, height;
    private final float density;
    private final DashPathEffect dashPathEffect;
    private Path sunPath = new Path();
    private RectF sunRectF = new RectF();
    private Path sunPathCurr = new Path();
    private final TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private float paintTextOffset;
    final float offsetDegree = 15f;
    private float sunArcHeight, sunArcRadius;
    private Rect visibleRect = new Rect();

    private String attr_beginForamt = "%s";
    private String attr_endForamt = "%s";
    private String attr_title = BaseApplication.getAppContext().getString(R.string.Alternates);
    private Date attr_beginDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-12-14 08:00:00");
    private Date attr_endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-12-14 17:00:00");

    public SunlightView(Context context, AttributeSet attrs) throws ParseException {
        super(context, attrs);
        density = context.getResources().getDisplayMetrics().density;
        dashPathEffect = new DashPathEffect(new float[]{density * 5, density * 5}, 2);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(density);
        paint.setTextAlign(Paint.Align.CENTER);
        if (isInEditMode()) {
            return;
        }
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SunlightView);
            attr_beginForamt = TextUtils.isEmpty(a.getString(R.styleable.SunlightView_beginForamt)) ? attr_beginForamt : a.getString(R.styleable.SunlightView_beginForamt);
            attr_endForamt = TextUtils.isEmpty(a.getString(R.styleable.SunlightView_endForamt)) ? attr_endForamt : a.getString(R.styleable.SunlightView_endForamt);
            attr_title = TextUtils.isEmpty(a.getString(R.styleable.SunlightView_title)) ? attr_title : a.getString(R.styleable.SunlightView_title);
            attr_beginDate = TextUtils.isEmpty(a.getString(R.styleable.SunlightView_beginDate)) ? attr_beginDate : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(a.getString(R.styleable.SunlightView_beginDate));
            attr_endDate = TextUtils.isEmpty(a.getString(R.styleable.SunlightView_endDate)) ? attr_endDate : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(a.getString(R.styleable.SunlightView_endDate));
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.WHITE);
        float textSize = paint.getTextSize();
        try {
            paint.setStrokeWidth(density);
            paint.setStyle(Paint.Style.STROKE);
            // draw sun path
            paint.setColor(Color.WHITE);
            paint.setPathEffect(dashPathEffect);
            canvas.drawPath(sunPath, paint);
            paint.setPathEffect(null);
            paint.setColor(Color.WHITE);

            int saveCount = canvas.save();
            canvas.translate(width / 2f, textSize + sunArcHeight);
            canvas.restoreToCount(saveCount);

            // draw bottom line
            paint.setStyle(Paint.Style.STROKE);
            final float lineLeft = width / 2f - sunArcRadius * 1.43f;
            canvas.drawLine(lineLeft, sunArcHeight + textSize, width - lineLeft, sunArcHeight + textSize, paint);

            // draw astor info
            paint.setStyle(Paint.Style.FILL);
            final float textLeft = width / 2f - sunArcRadius;// sunArcSize;
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(attr_title, width / 2f, textSize * 8f + paintTextOffset, paint);
            canvas.drawText(String.format(attr_beginForamt, getDateStr(attr_beginDate, "HH:mm")), textLeft, textSize * 10.5f + paintTextOffset, paint);
            canvas.drawText(String.format(attr_endForamt, getDateStr(attr_endDate, "HH:mm")), width - textLeft, textSize * 10.5f + paintTextOffset, paint);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        try {
            // draw the sun
            Integer[] sr = new Integer[]{attr_beginDate.getHours(), attr_beginDate.getMinutes()};
            int srTime = Integer.valueOf(sr[0]) * 60 * 60 + Integer.valueOf(sr[1]) * 60 + 0;
            Integer[] ss = new Integer[]{attr_endDate.getHours(), attr_endDate.getMinutes()};
            int ssTime = Integer.valueOf(ss[0]) * 60 * 60 + Integer.valueOf(ss[1]) * 60 + 0;
            Calendar c = Calendar.getInstance();

            int curTime = c.get(Calendar.HOUR_OF_DAY) * 60 * 60 + c.get(Calendar.MINUTE) * 60 + c.get(Calendar.MINUTE);
            if (curTime >= srTime && curTime <= ssTime) {
                float percent = (curTime - srTime) / ((float) (ssTime - srTime));
                sunPathCurr.reset();
                sunPathCurr.addArc(sunRectF, -165, 150f * percent);
                paint.setStrokeWidth(density);
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.YELLOW);
                paint.setPathEffect(dashPathEffect);
                canvas.drawPath(sunPathCurr, paint);
                paint.setPathEffect(null);
                canvas.save();
                canvas.translate(width / 2f, sunArcRadius + textSize);
                float degree = 15f + 150f * percent;
                final float sunRadius = density * 4f;
                canvas.rotate(degree);
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(density * 1.333f);
                canvas.translate(-sunArcRadius, 0);
                canvas.rotate(-degree);
                canvas.drawCircle(0, 0, sunRadius, paint);
                paint.setStyle(Paint.Style.STROKE);
                final int light_count = 8;
                for (int i = 0; i < light_count; i++) {
                    double radians = Math.toRadians(i * (360 / light_count));
                    float x1 = (float) (Math.cos(radians) * sunRadius * 1.6f);
                    float y1 = (float) (Math.sin(radians) * sunRadius * 1.6f);
                    float x2 = x1 * (1f + 0.4f * 1f);
                    float y2 = y1 * (1f + 0.4f * 1f);
                    canvas.drawLine(0 + x1, y1, 0 + x2, y2, paint);
                }


                canvas.restore();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        getGlobalVisibleRect(visibleRect);
        if (!visibleRect.isEmpty()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        try {
            final float textSize = height / 12f;
            paint.setTextSize(textSize);
            paintTextOffset = getTextPaintOffset(paint);
            sunPath.reset();
            sunArcHeight = textSize * 8.5f;
            sunArcRadius = (float) (sunArcHeight / (1f - Math.sin(Math.toRadians(offsetDegree))));
            final float sunArcLeft = width / 2f - sunArcRadius;
            sunRectF.left = sunArcLeft;
            sunRectF.top = textSize;
            sunRectF.right = width - sunArcLeft;
            sunRectF.bottom = sunArcRadius * 2f + textSize;
            sunPath.addArc(sunRectF, -165, +150);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float getTextPaintOffset(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return -(fontMetrics.bottom - fontMetrics.top) / 2f - fontMetrics.top;
    }

    private static String getDateStr(Date date, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public void setSunRise(Date sunRise) {
        this.sunRise = sunRise;
    }

    public void setSunSet(Date sunSet) {
        this.sunSet = sunSet;
    }

    public void setMonthlyRise(Date monthlyRise) {
        this.monthlyRise = monthlyRise;
    }

    public void setMonthlySet(Date monthlySet) {
        this.monthlySet = monthlySet;
    }

    private Date sunRise;
    private Date sunSet;
    private Date monthlyRise;
    private Date monthlySet;

    public void updateData() {
        Date currentDate = new Date();
        if (sunRise.getTime() < sunSet.getTime()) {
            Calendar c = Calendar.getInstance();
            c.setTime(sunSet);
            c.add(Calendar.DAY_OF_MONTH, 1);
            sunSet = c.getTime();
        }
        if (monthlyRise.getTime() < monthlySet.getTime()) {
            Calendar c = Calendar.getInstance();
            c.setTime(monthlySet);
            c.add(Calendar.DAY_OF_MONTH, 1);
            monthlySet = c.getTime();
        }
        if (currentDate.getTime() >= sunRise.getTime() && currentDate.getTime() <= sunSet.getTime()) {
            // 日出
            this.attr_title = BaseApplication.getAppContext().getString(R.string.Sunlight);
            this.attr_beginDate = sunRise;
            this.attr_endDate = sunSet;
        } else if (currentDate.getTime() >= monthlyRise.getTime() && currentDate.getTime() <= monthlySet.getTime()) {
            // 月出
            this.attr_beginDate = monthlyRise;
            this.attr_endDate = monthlySet;
            this.attr_title = BaseApplication.getAppContext().getString(R.string.Monthly);
        } else {
            // 昼夜交替中
            this.attr_title = BaseApplication.getAppContext().getString(R.string.Alternates);
        }
        invalidate();
    }
}
