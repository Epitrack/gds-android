package com.epitrack.guardioes.helper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Igor Morais
 */
public final class ViewUtility {

    private ViewUtility () {

    }

    public static int toPixel(final Context context, final float densityPixel) {

        final float density = context.getResources().getDisplayMetrics().density;

        return (int) (densityPixel * density);
    }

    public static float toDensity(final Context context, final int pixel) {

        final float density = context.getResources().getDisplayMetrics().density;

        return pixel / density;
    }

    public static void setPadding(final View view, final int value) {
        setPadding(view, value, value, value, value);
    }

    public static void setPaddingLeft(final View view, final int left) {
        setPadding(view, left, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setPaddingTop(final View view, final int top) {
        setPadding(view, view.getPaddingLeft(), top, view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setPaddingRight(final View view, final int right) {
        setPadding(view, view.getPaddingLeft(), view.getPaddingTop(), right, view.getPaddingBottom());
    }

    public static void setPaddingBottom(final View view, final int bottom) {
        setPadding(view, view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), bottom);
    }

    private static void setPadding(final View view, final int left, final int top, final int right, final int bottom) {
        view.setPadding(left, top, right, bottom);
    }

    public static void setMargin(final View view, final int value) {
        setMargin(view, value, value, value, value);
    }

    public static void setMarginLeft(final View view, final int left) {

        final ViewGroup.MarginLayoutParams layoutParam = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        setMargin(view, left, layoutParam.topMargin, layoutParam.rightMargin, layoutParam.bottomMargin);
    }

    public static void setMarginTop(final View view, final int top) {

        final ViewGroup.MarginLayoutParams layoutParam = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        setMargin(view, layoutParam.leftMargin, top, layoutParam.rightMargin, layoutParam.bottomMargin);
    }

    public static void setMarginRight(final View view, final int right) {

        final ViewGroup.MarginLayoutParams layoutParam = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        setMargin(view, layoutParam.leftMargin, layoutParam.topMargin, right, layoutParam.bottomMargin);
    }

    public static void setMarginBottom(final View view, final int bottom) {

        final ViewGroup.MarginLayoutParams layoutParam = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        setMargin(view, layoutParam.leftMargin, layoutParam.topMargin, layoutParam.rightMargin, bottom);
    }

    private static void setMargin(final View view, final int left, final int top, final int right, final int bottom) {

        final ViewGroup.MarginLayoutParams layoutParam = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        layoutParam.setMargins(left, top, right, bottom);

        view.setLayoutParams(layoutParam);
    }
}
