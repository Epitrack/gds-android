package com.epitrack.guardioes.utility;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.diary.DiaryActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.List;

/**
 * @author Miqueias Lopes
 */
public class MySelectorDecoratorBad implements DayViewDecorator {

    private Drawable drawable = null;
    private List<Integer> days;

    public MySelectorDecoratorBad(DiaryActivity context, List<Integer> days) {
        drawable = context.getResources().getDrawable(R.drawable.img_donut_25_calendar);
        this.days = days;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        if (!days.isEmpty()) {
            for (int i = 0; i < days.size(); i++) {
                if (days.get(i) == day.getDay()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.BLACK));
        view.setSelectionDrawable(drawable);
    }
}
