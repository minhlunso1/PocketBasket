package minhna.android.giodicho.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.View;

public class DimenUtils {
    public static int dptopx(Context context, int dp) {
        if (context != null)
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        else
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static float acos(double value) {
        return (float) Math.toDegrees(Math.acos(value));
    }

    public static float asin(double value) {
        return (float) Math.toDegrees(Math.asin(value));
    }

    public static float centerX(View view) {
        return ViewCompat.getX(view) + view.getMeasuredWidth() / 2;
    }

    public static float centerY(View view) {
        return ViewCompat.getY(view) + view.getMeasuredHeight() / 2;
    }

    public static float cos(double degree) {
        return (float) Math.cos(Math.toRadians(degree));
    }

    public static float sin(double degree) {
        return (float) Math.sin(Math.toRadians(degree));
    }
}
