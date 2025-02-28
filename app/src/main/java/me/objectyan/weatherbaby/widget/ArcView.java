package me.objectyan.weatherbaby.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import me.objectyan.weatherbaby.R;

/**
 * 弧形框
 */
public class ArcView extends View {
    private float density;
    private float attr_max = 100;
    private float attr_min = 0;
    private String attr_title;
    private String attr_value;
    private String attr_subValue;
    private Boolean attr_isPercentage = false;
    private int attt_progressColor = 0xFFFFFFFF;
    private int attt_backgroundColor = 0x55DADADA;
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private RectF rectF = new RectF();

    public ArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = context.getResources().getDisplayMetrics().density;
        textPaint.setTextAlign(Paint.Align.CENTER);
        if (isInEditMode()) {
            return;
        }
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ArcView);
            attr_max = a.getFloat(R.styleable.ArcView_max, attr_max);
            attr_min = a.getFloat(R.styleable.ArcView_min, attr_min);
            attr_title = a.getString(R.styleable.ArcView_title);
            attr_value = a.getString(R.styleable.ArcView_value);
            attr_subValue = a.getString(R.styleable.ArcView_subValue);
            attt_progressColor = a.getColor(R.styleable.ArcView_progressColor, attt_progressColor);
            attt_backgroundColor = a.getColor(R.styleable.ArcView_backgroundColor, attt_backgroundColor);
            attr_isPercentage = a.getBoolean(R.styleable.ArcView_isPercentage, attr_isPercentage);
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final float w = getWidth();
        final float h = getHeight();
        if (w <= 0 || h <= 0) {
            return;
        }
        final float lineSize = h / 10f;// 大约是12dp
        float currAqiPercent = -1f;
        try {
            // TODO
            currAqiPercent = Float.valueOf(density) / attr_max;
            currAqiPercent = Math.min(currAqiPercent, 1f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        float aqiArcRadius = lineSize * 4f;
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(lineSize / 1.5f);
        textPaint.setColor(attt_backgroundColor);
        rectF.set(-aqiArcRadius, -aqiArcRadius, aqiArcRadius, aqiArcRadius);
        final int saveCount = canvas.save();
        canvas.translate(w / 2f, h / 1.6f);
        // draw aqi restPercent arc
        final float startAngle = -225f;
        final float sweepAngle = 270f;
        canvas.drawArc(rectF, startAngle + sweepAngle * currAqiPercent, sweepAngle * (1f - currAqiPercent), false,
                textPaint);
        if (currAqiPercent >= 0f) {

            // draw aqi aqiPercent arc
            textPaint.setColor(attt_progressColor);
            canvas.drawArc(rectF, startAngle, sweepAngle * currAqiPercent, false, textPaint);

            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextSize(lineSize * 0.8f);
            textPaint.setColor(getResources().getColor(R.color.colorTemp));
            try {
                // TODO
                canvas.drawText(attr_title, 0, -lineSize * 5f, textPaint);
            } catch (Exception e) {
            }
            if (!(TextUtils.isEmpty(attr_value) && TextUtils.isEmpty(attr_subValue))) {
                // draw aqi number and text
                textPaint.setStyle(Paint.Style.FILL);
                textPaint.setTextSize(lineSize * 1.5f);
                textPaint.setColor(getResources().getColor(R.color.colorTemp));
                try {
                    // TODO
                    canvas.drawText(attr_value, 0, lineSize, textPaint);
                } catch (Exception e) {
                }
                textPaint.setTextSize(lineSize * 0.8f);
                textPaint.setColor(getResources().getColor(R.color.colorTemp));
                try {
                    // TODO
                    canvas.drawText(attr_subValue, 0, lineSize * -0.5f, textPaint);
                } catch (Exception e) {
                }
            } else {
                textPaint.setStyle(Paint.Style.FILL);
                textPaint.setTextSize(lineSize * 1.5f);
                textPaint.setColor(getResources().getColor(R.color.colorTemp));
                try {
                    // TODO
                    canvas.drawText(TextUtils.isEmpty(attr_value) ? (TextUtils.isEmpty(attr_subValue) ? "-" : attr_subValue) : attr_value, 0, lineSize * .25f, textPaint);
                } catch (Exception e) {
                }
            }
            // draw max and min
            textPaint.setTextSize(lineSize * 0.75f);
            textPaint.setColor(getResources().getColor(R.color.colorTips));
            try {
                // TODO
                canvas.drawText(String.valueOf(attr_min), -lineSize * 2.5f, lineSize * 3.6f, textPaint);
            } catch (Exception e) {
            }
            textPaint.setTextSize(lineSize * 0.75f);
            textPaint.setColor(getResources().getColor(R.color.colorTips));
            try {
                // TODO
                canvas.drawText(String.valueOf(attr_max), lineSize * 2.5f, lineSize * 3.6f, textPaint);
            } catch (Exception e) {
            }

            // draw the aqi line
            canvas.rotate(startAngle + sweepAngle * currAqiPercent - 180f);
            textPaint.setStyle(Paint.Style.STROKE);
            textPaint.setColor(0xffffffff);
        }
        canvas.restoreToCount(saveCount);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setDensity(Double density) {
        this.density = density == null ? 0 : density.floatValue();
        if (this.attr_isPercentage)
            this.attr_value = String.format("%.1f", (this.density / this.attr_max * 100)) + "%";
        invalidate();
    }

    public void setValue(String value) {
        if (!this.attr_isPercentage) {
            this.attr_value = value;
            invalidate();
        }
    }

    public void setSubValue(String subValue) {
        if (!this.attr_isPercentage) {
            this.attr_subValue = subValue;
            invalidate();
        }
    }

}