package com.epitrack.guardioes.view.diary;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.DiaryRequester;
import com.epitrack.guardioes.request.UserRequester;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.helper.Logger;
import com.epitrack.guardioes.helper.MySelectorDecoratorBad;
import com.epitrack.guardioes.helper.MySelectorDecoratorEquals;
import com.epitrack.guardioes.helper.MySelectorDecoratorGood;
import com.epitrack.guardioes.helper.MySelectorDecoratorOnlyBad;
import com.epitrack.guardioes.helper.MySelectorDecoratorOnlyGood;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.dialog.LoadDialog;
import com.epitrack.guardioes.view.survey.ParentListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.google.android.gms.analytics.HitBuilders;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class DiaryActivity extends BaseAppCompatActivity implements ParentListener, OnDateSelectedListener, OnMonthChangedListener {

    private static final String TAG = DiaryActivity.class.getSimpleName();

    @Bind(R.id.text_view_participation)
    TextView textViewParticipation;

    @Bind(R.id.text_view_good_percentage)
    TextView textViewGoodPercentage;

    @Bind(R.id.text_view_good_report)
    TextView textViewGoodReport;

    @Bind(R.id.text_view_bad_percentage)
    TextView textViewBadPercentage;

    @Bind(R.id.text_view_bad_report)
    TextView textViewBadReport;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.pie_chart_diary)
    PieChart pieChart;

    @Bind(R.id.calendarView)
    MaterialCalendarView materialCalendarView;

    @Bind(R.id.frequency_report)
    TextView textViewFrequencyReport;

    @Bind(R.id.line_chart)
    GraphView lineChart;

    private double totalCount = 0;
    private double goodCount = 0;
    private double badCount = 0;
    private double goodPercent = 0;
    private double badPercent = 0;

    private double goodCountDetail = 0;
    private double badCountDetail = 0;

    private String idSelectedUser = "";

    private final SingleUser singleUser = SingleUser.getInstance();

    private List<Integer> daysGood;
    private List<Integer> daysBad;
    private List<Integer> daysZero;
    private List<Integer> daysOnlyGood;
    private List<Integer> daysOnlyBad;
    private List<Integer> daysEquals;
    private List<Date> daysGoodAndBad;

    private CalendarDay calendarDay = CalendarDay.today();

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.diary);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        textViewFrequencyReport.setText(this.getString(R.string.freq_label) +" "+ CalendarDay.today().getYear());

        // Get all member..
        new UserRequester(this).getAllHousehold(singleUser.getId(), new MemberHandler());

        loadViewPieChart();
        loadViewCalendar();
        loadViewLineChart();

        // Request Pie Chart..
        loadPieChart(null);

        // Calendar..
        loadCalendar(calendarDay);

        // Request line chart..
        requestLineChart();
    }

    private void loadViewPieChart() {

        pieChart.getLegend().setEnabled(false);
        pieChart.setUsePercentValues(false);
        pieChart.setDescription("");
        pieChart.setDrawCenterText(false);
        pieChart.setDrawSliceText(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColorTransparent(false);
        pieChart.setHoleRadius(7);
        pieChart.setTransparentCircleRadius(10);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(false);
        pieChart.setClickable(false);
    }

    private void loadViewCalendar() {

        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
        materialCalendarView.setArrowColor(R.color.blue_light);
        materialCalendarView.setOnDateChangedListener(DiaryActivity.this);
        materialCalendarView.setOnMonthChangedListener(DiaryActivity.this);
        materialCalendarView.setWeekDayLabels(new String[]{"D", "S", "T", "Q", "Q", "S", "S"});
        materialCalendarView.setTitleMonths(new String[]{this.getString(R.string.janeiro),this.getString(R.string.fevereiro),this.getString(R.string.marco),this.getString(R.string.abril), this.getString(R.string.maio),this.getString(R.string.junho),this.getString(R.string.julho),this.getString(R.string.agosto),this.getString(R.string.setembro),this.getString(R.string.outubro),this.getString(R.string.novembro),this.getString(R.string.dezembro) });
    }

    private void loadViewLineChart() {

        lineChart.getGridLabelRenderer().setTextSize(18);
        lineChart.getGridLabelRenderer().reloadStyles();

        lineChart.getGridLabelRenderer().setVerticalLabelsColor(R.color.grey_light);
        lineChart.getGridLabelRenderer().setHorizontalLabelsColor(R.color.grey_light);
        lineChart.setTitleColor(R.color.grey_light);
        lineChart.setTitleTextSize(12f);

        final Viewport viewport = lineChart.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(100);
        viewport.setScrollable(false);

        final StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(lineChart);
        staticLabelsFormatter.setHorizontalLabels(new String[]{this.getString(R.string.jan), this.getString(R.string.fev), this.getString(R.string.mar), this.getString(R.string.abr), this.getString(R.string.mai), this.getString(R.string.jun), this.getString(R.string.jul), this.getString(R.string.ago), this.getString(R.string.set), this.getString(R.string.out), this.getString(R.string.nov), this.getString(R.string.dez)});
        staticLabelsFormatter.setVerticalLabels(new String[]{"0%", "25%", "50%", "75%", "100%"});

        lineChart.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Diary of Health Screen - " + this.getClass().getSimpleName());

        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    private class MemberHandler implements com.epitrack.guardioes.request.base.RequestListener<List<User>> {

        final LoadDialog loadDialog = new LoadDialog();

        @Override
        public void onStart() {
            loadDialog.show(getFragmentManager(), LoadDialog.TAG);
        }

        @Override
        public void onError(final Exception e) {

        }

        @Override
        public void onSuccess(final List<User> parentList) {
            loadDialog.dismiss();

            final User user = new User(singleUser.getNick(),
                    singleUser.getEmail(),
                    singleUser.getId(),
                    singleUser.getDob(),
                    singleUser.getRace(),
                    singleUser.getGender(),
                    singleUser.getImage());

            user.setPath(singleUser.getPath());

            parentList.add(0, user);

            recyclerView.setAdapter(new MemberAdapter(DiaryActivity.this, DiaryActivity.this, parentList));
        }
    }

    private void loadPieChart(final String idHouseHold) {

        final String url = idHouseHold == null || idHouseHold.equals(singleUser.getId()) ? "user/survey/summary" : "household/survey/summary?household_id=" + idHouseHold;
        final String participacoes = this.getString(R.string.participacoes);
        final String bem = this.getString(R.string.bem);
        final String mal = this.getString(R.string.mal);
        final String relatorios = this.getString(R.string.relatorios);

        new DiaryRequester(this).loadPieChart(url, new com.epitrack.guardioes.request.base.RequestListener<String>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(final String result) {

                try {

                    final JSONObject jsonObject = new JSONObject(result);

                    final JSONObject jsonObjectSympton = jsonObject.getJSONObject("data");

                    goodCount = Integer.parseInt(jsonObjectSympton.getString("no_symptom"));
                    badCount = Integer.parseInt(jsonObjectSympton.getString("symptom"));
                    totalCount = Integer.parseInt(jsonObjectSympton.getString("total"));

                    textViewParticipation.setText((int) totalCount + " "+participacoes);

                    goodPercent = totalCount == 0 ? 0 : goodCount / totalCount;

                    String htmlStringGood = "<b>" + (int) (goodPercent * 100) + "%</b> "+bem;
                    textViewGoodPercentage.setText(Html.fromHtml(htmlStringGood));
                    //textViewGoodPercentage.setText((int) (goodPercent * 100) + "% Bem");
                    textViewGoodReport.setText((int) goodCount + " "+R.string.relatorios);

                    badPercent = totalCount == 0 ? 0 : badCount / totalCount;

                    String htmlStringBad = "<b>" + (int) (badPercent * 100) + "%</b> "+mal;
                    textViewBadPercentage.setText(Html.fromHtml(htmlStringBad));
                    //textViewBadPercentage.setText((int) (badPercent * 100) + "% Mal");
                    textViewBadReport.setText((int) badCount + " "+relatorios);

                    setData();

                } catch (Exception e) {
                    Logger.logError(TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    public void onParentSelect(String id) {
        idSelectedUser = id;

        materialCalendarView.removeDecorators();

        loadCalendar(calendarDay);

        loadPieChart(id);

        requestLineChart();
    }

    private void setData() {

        float[] yData = {(int) (badPercent * 100), (int) (goodPercent * 100)};
        String[] xData = { this.getString(R.string.mal), this.getString(R.string.participacoes)};

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < yData.length; i++)
            yVals1.add(new Entry(yData[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xData.length; i++) {
            xVals.add(xData[i]);
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(2);
        dataSet.setSelectionShift(2);

        int colors[] = {Color.parseColor("#FF0000"), Color.parseColor("#CCCC00")};

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setDrawValues(false);
        data.setHighlightEnabled(false);

        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.getLegend().setEnabled(false);
    }

    private void requestLineChart() {

        lineChart.removeAllSeries();

        final String url;

        if (idSelectedUser.equals("") || idSelectedUser.equals(singleUser.getId())) {
            url = "user/calendar/year?year=" + calendarDay.getYear();

        } else {
            url = "household/calendar/year?year=" + calendarDay.getYear() + "&household_id=" + idSelectedUser;
        }
        final String  freq_relatorio = this.getString(R.string.freq_relatorio);
        new DiaryRequester(this).requestLineChart(url, new com.epitrack.guardioes.request.base.RequestListener<String>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(final String result) {

                try {

                    final JSONObject jsonObject = new JSONObject(result);

                    if (!jsonObject.getBoolean("error")) {

                        final Map<Integer, Double> lineMap = new HashMap<>();

                        final JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            final JSONObject jsonMonth = jsonArray.getJSONObject(i);

                            final int month = jsonMonth.getJSONObject("_id").getInt("month");

                            lineMap.put(month, jsonMonth.getDouble("percent"));
                        }

                        final LineGraphSeries<DataPoint> graph = new LineGraphSeries<>();

                        for (int i = 1; i <= 12; i++) {

                            final Double value = lineMap.get(i) == null ? 0.0 : lineMap.get(i);

                            graph.appendData(new DataPoint(i, value), true, 100);
                        }

                        graph.setDrawDataPoints(true);
                        graph.setBackgroundColor(R.color.nice_blue);
                        graph.setDataPointsRadius(5);

                        graph.setOnDataPointTapListener(new OnDataPointTapListener() {

                            @Override
                            public void onTap(Series series, DataPointInterface dataPoint) {
                                Toast.makeText(getApplicationContext(), dataPoint.getY() + "% "+freq_relatorio+".", Toast.LENGTH_SHORT).show();
                            }
                        });

                        lineChart.addSeries(graph);
                    }

                } catch (final Exception e) {
                    Logger.logError(TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    public void onMonthChanged(MaterialCalendarView calendarView, CalendarDay date) {
        calendarDay = date;

        calendarView.removeDecorators();

        loadCalendar(date);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView calendarView, @NonNull CalendarDay date, boolean selected) {

        calendarView.removeDecorators();

        if (!daysGoodAndBad.isEmpty()) {

            for (int i = 0; i < daysGoodAndBad.size(); i++) {

                if (daysGoodAndBad.get(i).equals(date.getDate())) {
                    calendarView.setDateSelected(daysGoodAndBad.get(i), true);
                }
            }
        }

        final String url;

        if (idSelectedUser.equals("") || idSelectedUser.equals(singleUser.getId())) {
            url = "user/calendar/day?day=" + date.getDay() + "&month=" + (date.getMonth() + 1) + "&year=" + date.getYear();

        } else {
            url = "household/calendar/day?day=" + date.getDay() + "&month=" + (date.getMonth() + 1) + "&year=" + date.getYear() + "&household_id=" + idSelectedUser;
        }

        new DiaryRequester(this).requestCalandarDate(url, new RequestListener<String>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(String result) {

                try {

                    JSONObject jsonObject = new JSONObject(result);

                    if (!jsonObject.getBoolean("error")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObjectSymptom = jsonArray.getJSONObject(i);
                            JSONObject jsonObjectDetail = jsonObjectSymptom.getJSONObject("_id");

                            if (jsonObjectDetail.get("no_symptom").toString().equals("N")) {

                                badCountDetail = Double.parseDouble(jsonObjectSymptom.get("count").toString());

                            } else if (jsonObjectDetail.get("no_symptom").toString().equals("Y")) {

                                goodCountDetail = Double.parseDouble(jsonObjectSymptom.get("count").toString());
                            }
                        }
                    }

                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadCalendar(final CalendarDay date) {

        final String url;

        if (idSelectedUser.equals("") || idSelectedUser.equals(singleUser.getId())) {
            url = "user/calendar/month?&month=" + (date.getMonth() + 1) + "&year=" + date.getYear();

        } else {
            url = "household/calendar/month?&month=" + (date.getMonth() + 1) + "&year=" + date.getYear() + "&household_id=" + idSelectedUser;
        }

        new DiaryRequester(this).loadCalendar(url, new com.epitrack.guardioes.request.base.RequestListener<String>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(final String result) {

                int goodCountTotal = 0;
                int badCountTotal = 0;

                daysGood = new ArrayList<>();
                daysBad = new ArrayList<>();
                daysZero = new ArrayList<>();
                daysGoodAndBad = new ArrayList<>();
                daysOnlyGood = new ArrayList<>();
                daysOnlyBad = new ArrayList<>();
                daysEquals = new ArrayList<>();

                try {

                    final JSONObject json = new JSONObject(result);

                    if (!json.getBoolean("error")) {

                        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                        final JSONArray jsonArray = json.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            final JSONObject jsonSymptom = jsonArray.getJSONObject(i);
                            final JSONObject jsonDetail = jsonSymptom.getJSONObject("_id");

                            if (jsonDetail.getString("no_symptom").equals("N")) {

                                badCountTotal = Integer.parseInt(jsonSymptom.getString("count"));

                            } else if (jsonDetail.getString("no_symptom").equals("Y")) {

                                goodCountTotal = Integer.parseInt(jsonSymptom.getString("count"));
                            }

                            final int day = jsonDetail.getInt("day");

                            if (goodCountTotal == 0 && badCountTotal == 0) {
                                daysZero.add(day);

                            } else {

                                if (goodCountTotal == 0 && badCountTotal > 0) {
                                    daysOnlyBad.add(day);

                                } else if (goodCountTotal > 0 && badCountTotal == 0) {
                                    daysOnlyGood.add(day);

                                } else if (goodCountTotal > badCountTotal) {
                                    daysGood.add(day);

                                } else if (goodCountTotal < badCountTotal) {
                                    daysBad.add(day);

                                } else if (goodCountTotal == badCountTotal) {
                                    daysEquals.add(day);
                                }

                                final Date formatted = format.parse(date.getYear() + "-" + (date.getMonth() + 1) + "-" + day);

                                daysGoodAndBad.add(formatted);
                            }
                        }
                    }

                    materialCalendarView.removeDecorators();

                    materialCalendarView.addDecorators(new MySelectorDecoratorGood(DiaryActivity.this, daysGood),
                            new MySelectorDecoratorBad(DiaryActivity.this, daysBad),
                            new DayDisableDecorator(daysZero),
                            new MySelectorDecoratorOnlyGood(DiaryActivity.this, daysOnlyGood),
                            new MySelectorDecoratorOnlyBad(DiaryActivity.this, daysOnlyBad),
                            new MySelectorDecoratorEquals(DiaryActivity.this, daysEquals));

                    materialCalendarView.invalidate();
                    materialCalendarView.requestLayout();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

class DayDisableDecorator implements DayViewDecorator {

    private List<Integer> days;

    public DayDisableDecorator(List<Integer> days) {
        this.days = days;
    }

    @Override
    public boolean shouldDecorate(final CalendarDay calendarDay) {

        if (!days.isEmpty()) {

            for (int day : days) {

                if (day == calendarDay.getDay()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setDaysDisabled(true);
    }
}

