package com.star.eagleme.ui.widgets.animview;

import android.animation.TypeEvaluator;
import android.graphics.Point;

;

/**
 * Created by star on 2017/10/10.
 *
 * @description:
 */
public class PointSinEvaluator implements TypeEvaluator
{
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue)
    {
        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;
        int x = (int) (startPoint.x + fraction * (endPoint.x - startPoint.x));

        int y = (int) (Math.cos(x * Math.PI / 180) * 100) + endPoint.y / 2;
        Point point = new Point(x, y);
        return point;
    }

}
