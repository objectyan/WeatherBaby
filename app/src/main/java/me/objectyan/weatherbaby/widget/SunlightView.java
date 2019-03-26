package me.objectyan.weatherbaby.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.core.view.ViewCompat;

public class SunlightView extends View {

    private int width, height;
    private final float density;
    private final DashPathEffect dashPathEffect;
    private Path sunPath = new Path();
    private RectF sunRectF = new RectF();
    private final TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private float paintTextOffset;
    final float offsetDegree = 15f;
    private float sunArcHeight, sunArcRadius;
    private Rect visibleRect = new Rect();

    private String attr_beginForamt = "%s";
    private String attr_endForamt = "%s";
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
            canvas.drawText(String.format(attr_beginForamt, getDateStr(attr_beginDate, "HH:mm")), textLeft, textSize * 10.5f + paintTextOffset, paint);
            canvas.drawText(String.format(attr_endForamt, getDateStr(attr_endDate, "HH:mm")), width - textLeft, textSize * 10.5f + paintTextOffset, paint);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        try {
            // draw the sun
            Integer[] sr = new Integer[]{attr_beginDate.getHours(), attr_beginDate.getMinutes()};// 日出
            int srTime = Integer.valueOf(sr[0]) * 60 * 60 + Integer.valueOf(sr[1]) * 60 + 0;// 精确到秒
            Integer[] ss = new Integer[]{attr_endDate.getHours(), attr_endDate.getMinutes()};// 日落
            int ssTime = Integer.valueOf(ss[0]) * 60 * 60 + Integer.valueOf(ss[1]) * 60 + 0;
            Calendar c = Calendar.getInstance();

            int curTime = c.get(Calendar.HOUR_OF_DAY) * 60 * 60 + c.get(Calendar.MINUTE) * 60 + c.get(Calendar.MINUTE);
            if (curTime >= srTime && curTime <= ssTime) {
                canvas.save();
                canvas.translate(width / 2f, sunArcRadius + textSize);// 先平移到圆心
                float percent = (curTime - srTime) / ((float) (ssTime - srTime));
                float degree = 15f + 150f * percent;
                final float sunRadius = density * 4f;
                canvas.rotate(degree);// 旋转到太阳的角度
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(density * 1.333f);// 宽度是2对应半径是6
                canvas.translate(-sunArcRadius, 0);// 平移到太阳应该在的位置
                canvas.rotate(-degree);// 转正方向。。。
                canvas.drawCircle(0, 0, sunRadius, paint);
                paint.setStyle(Paint.Style.STROKE);
                final int light_count = 8;
                for (int i = 0; i < light_count; i++) {// 画刻度
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
        // 间距1 图8.5行 间距0.5 字1行 间距1 = 12;
        // 9.5 10 11 12
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
            sunPath.addArc(sunRectF, -165, +150);// 圆形的最右端点为0，顺时针sweepAngle
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

}
