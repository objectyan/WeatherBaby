package me.objectyan.weatherbaby.widget;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.view.ViewCompat;

public class WindView extends View {

    private int width, height;
    private final float density;
    private Path fanPath = new Path();// 旋转的风扇的扇叶
    private Path fanPillarPath = new Path();// 旋转的风扇的柱子
    private final TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private float curRotate;// 旋转的风扇的角度
    private Rect visibleRect = new Rect();

    public WindView(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = context.getResources().getDisplayMetrics().density;
        paint.setColor(Color.WHITE);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(density);
        paint.setTextAlign(Align.CENTER);
        if (isInEditMode()) {
            return;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.WHITE);
        float textSize = paint.getTextSize();
        try {
            paint.setTextSize(textSize);
            paint.setStrokeWidth(density);
            paint.setStyle(Style.STROKE);

            for (int i = 1; i <= 5; i++) {
                int tmp = i == 1 ? 1 : 2;
                int saveCount = canvas.save();
                float tmpHeight = height / 12f * 4f;
                float tmpDX = width / 2f;
                float tmpDY = tmpHeight * tmp;
                switch (i) {
                    case 2:
                    case 4:
                        tmpDX -= tmpHeight / 2;
                        if (i == 2)
                            tmpDY /= 2.5;
                        break;
                    case 3:
                    case 5:
                        tmpDX += tmpHeight / 2;
                        if (i == 3)
                            tmpDY /= 2.5;
                        break;
                }
                canvas.translate(tmpDX, tmpDY);

                float textSize2 = height / 12f;
                float fanPillerHeight = 0f;
                paint.setStyle(Style.STROKE);
                fanPillarPath.reset();
                final float fanPillarSize = textSize2 * 0.25f;// 柱子的宽度
                fanPillarPath.moveTo(0, 0);
                fanPillerHeight = textSize2 * 4f / tmp;// 柱子的宽度
                fanPillarPath.lineTo(fanPillarSize, fanPillerHeight);
                fanPillarPath.lineTo(-fanPillarSize, fanPillerHeight);
                fanPillarPath.close();
                canvas.drawPath(fanPillarPath, paint);
                canvas.rotate(curRotate * 360f);
                float speed = 0f;
                try {
                    speed = Float.valueOf(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                speed = Math.max(speed, 0.75f);
                curRotate += 0.001f * speed;
                if (curRotate > 1f) {
                    curRotate = 0f;
                }
                paint.setStyle(Style.FILL);
                fanPath.reset();
                final float fanSize = textSize2 * 0.2f / tmp;// 风扇底部半圆的半径
                final float fanHeight = textSize2 * 2f / tmp;
                final float fanCenterOffsetY = fanSize * 1.6f;
                fanPath.addArc(new RectF(-fanSize, -fanSize - fanCenterOffsetY, fanSize, fanSize - fanCenterOffsetY), 0,
                        180);
                fanPath.quadTo(-fanSize * 1f, -fanHeight * 0.5f - fanCenterOffsetY, 0, -fanHeight - fanCenterOffsetY);
                fanPath.quadTo(fanSize * 1f, -fanHeight * 0.5f - fanCenterOffsetY, fanSize, -fanCenterOffsetY);
                fanPath.close();
                canvas.drawPath(fanPath, paint);
                canvas.rotate(120f);
                canvas.drawPath(fanPath, paint);
                canvas.rotate(120f);
                canvas.drawPath(fanPath, paint);
                canvas.restoreToCount(saveCount);
            }


        } catch (Exception e1) {
            e1.printStackTrace();
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
    }

}
