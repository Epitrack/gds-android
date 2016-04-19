package com.epitrack.guardioes.view.diary;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.DiaryRequester;
import com.epitrack.guardioes.request.UserRequester;
import com.epitrack.guardioes.request.base.Method;
import com.epitrack.guardioes.request.old.RequestListener;
import com.epitrack.guardioes.request.old.Requester;
import com.epitrack.guardioes.request.old.SimpleRequester;
import com.epitrack.guardioes.utility.Logger;
import com.epitrack.guardioes.utility.MySelectorDecoratorBad;
import com.epitrack.guardioes.utility.MySelectorDecoratorEquals;
import com.epitrack.guardioes.utility.MySelectorDecoratorGood;
import com.epitrack.guardioes.utility.MySelectorDecoratorOnlyBad;
import com.epitrack.guardioes.utility.MySelectorDecoratorOnlyGood;
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
import java.util.concurrent.Executors;

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

        // Get all member..
        new UserRequester(this).getAllHousehold(singleUser.getId(), new MemberHandler());

        // Request Pie Chart..
        loadPieChart(null);

        // TODO: Need to refactor this..
        // Calendar..
        new AsyncTaskRunner().execute();

        // Request line chart..
        requestLineChart();
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Diary of Health Screen - " + this.getClass().getSimpleName());

        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    private class MemberHandler implements com.epitrack.guardioes.request.base.RequestListener<List<User>> {

        @Override
        public void onStart() {

        }

        @Override
        public void onError(final Exception e) {

        }

        @Override
        public void onSuccess(final List<User> parentList) {

            parentList.add(0, new User(R.drawable.image_avatar_small_8,
                    singleUser.getNick(),
                    singleUser.getEmail(),
                    singleUser.getId(),
                    singleUser.getDob(),
                    singleUser.getRace(),
                    singleUser.getGender(),
                    singleUser.getPicture()));

            recyclerView.setAdapter(new MemberAdapter(DiaryActivity.this, DiaryActivity.this, parentList));
        }
    }

    private void loadPieChart(final String idHouseHold) {

        final String url = idHouseHold == null || idHouseHold.equals(singleUser.getId()) ? "user/survey/summary" : "household/survey/summary?household_id=" + idHouseHold;

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

                    textViewParticipation.setText((int) totalCount + " Participações");

                    goodPercent = totalCount == 0 ? 0 : goodCount / totalCount;

                    String htmlStringGood = "<b>" + (int) (goodPercent * 100) + "%</b> Bem";
                    textViewGoodPercentage.setText(Html.fromHtml(htmlStringGood));
                    //textViewGoodPercentage.setText((int) (goodPercent * 100) + "% Bem");
                    textViewGoodReport.setText((int) goodCount + " Relatórios");

                    badPercent = totalCount == 0 ? 0 : badCount / totalCount;

                    String htmlStringBad = "<b>" + (int) (badPercent * 100) + "%</b> Mal";
                    textViewBadPercentage.setText(Html.fromHtml(htmlStringBad));
                    //textViewBadPercentage.setText((int) (badPercent * 100) + "% Mal");
                    textViewBadReport.setText((int) badCount + " Relatórios");

                    //Pie Char Config
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

                    setData();

                    //Calendar Config
                    materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
                    materialCalendarView.setArrowColor(R.color.blue_light);
                    materialCalendarView.setOnDateChangedListener(DiaryActivity.this);
                    materialCalendarView.setOnMonthChangedListener(DiaryActivity.this);
                    materialCalendarView.setWeekDayLabels(new String[]{"D", "S", "T", "Q", "Q", "S", "S"});
                    materialCalendarView.setTitleMonths(new String[]{"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"});

                } catch (Exception e) {
                    Logger.logError(TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    public void onParentSelect(String id) {
        idSelectedUser = id;

        materialCalendarView.setSelectedDate(CalendarDay.today());

        // TODO: Need to refactor this..
        onDateChanged(materialCalendarView, CalendarDay.today());

        new AsyncTaskRunner().execute();

        loadPieChart(id);

        requestLineChart();
    }

    private void setData() {

        float[] yData = {(int) (badPercent * 100), (int) (goodPercent * 100)};
        String[] xData = {"Mal", "Bem"};

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

        pieChart.getLegend().setEnabled(false);
        textViewFrequencyReport.setText("Frequência de envio de relatório em " + CalendarDay.today().getYear());

        final Map<Integer, Double> mapTotalMonth = new HashMap<>();
        final Map<Integer, Double> mapTotalMonthTemp = new HashMap<>();

        mapTotalMonthTemp.put(1, 0.0);
        mapTotalMonthTemp.put(2, 0.0);
        mapTotalMonthTemp.put(3, 0.0);
        mapTotalMonthTemp.put(4, 0.0);
        mapTotalMonthTemp.put(5, 0.0);
        mapTotalMonthTemp.put(6, 0.0);
        mapTotalMonthTemp.put(7, 0.0);
        mapTotalMonthTemp.put(8, 0.0);
        mapTotalMonthTemp.put(9, 0.0);
        mapTotalMonthTemp.put(10, 0.0);
        mapTotalMonthTemp.put(11, 0.0);
        mapTotalMonthTemp.put(12, 0.0);

        SimpleRequester simpleRequester = new SimpleRequester();

        if (idSelectedUser == singleUser.getId()) {
            simpleRequester.setUrl(Requester.API_URL + "user/calendar/year?year=" + calendarDay.getYear());

        } else if (idSelectedUser.equals("")) {
            simpleRequester.setUrl(Requester.API_URL + "user/calendar/year?year=" + calendarDay.getYear());

        } else {
            simpleRequester.setUrl(Requester.API_URL + "household/calendar/year?year=" + calendarDay.getYear() + "&household_id=" + idSelectedUser);
        }

        simpleRequester.setJsonObject(null);
        simpleRequester.setMethod(Method.GET);

        simpleRequester.execute();

        simpleRequester.setListener(new RequestListener<String>() {

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

                    if (jsonObject.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();

                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectTotalMonth = jsonArray.getJSONObject(i);
                            JSONObject jsonObjectDetail = jsonObjectTotalMonth.getJSONObject("_id");

                            int month = Integer.parseInt(jsonObjectDetail.getString("month"));

                            for (int j = 1; j <= mapTotalMonthTemp.size(); j++) {

                                if (j == month) {
                                    mapTotalMonthTemp.put(j, Double.parseDouble(jsonObjectTotalMonth.get("percent").toString()));
                                }
                            }
                        }

                        lineChart.removeAllSeries();
                        lineChart.getGridLabelRenderer().setTextSize(18);
                        lineChart.getGridLabelRenderer().reloadStyles();

                        //Line Chart
                        mapTotalMonth.put(1, mapTotalMonthTemp.get(1));
                        mapTotalMonth.put(2, mapTotalMonthTemp.get(2));
                        mapTotalMonth.put(3, mapTotalMonthTemp.get(3));
                        mapTotalMonth.put(4, mapTotalMonthTemp.get(4));
                        mapTotalMonth.put(5, mapTotalMonthTemp.get(5));
                        mapTotalMonth.put(6, mapTotalMonthTemp.get(6));
                        mapTotalMonth.put(7, mapTotalMonthTemp.get(7));
                        mapTotalMonth.put(8, mapTotalMonthTemp.get(8));
                        mapTotalMonth.put(9, mapTotalMonthTemp.get(9));
                        mapTotalMonth.put(10, mapTotalMonthTemp.get(10));
                        mapTotalMonth.put(11, mapTotalMonthTemp.get(11));
                        mapTotalMonth.put(12, mapTotalMonthTemp.get(12));

                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

                        for (int k = 1; k <= mapTotalMonth.size(); k++) {
                            series.appendData(new DataPoint(k, mapTotalMonth.get(k)), true, 100);
                        }

                        series.setDrawDataPoints(true);
                        series.setBackgroundColor(R.color.blue_dark);
                        series.setDataPointsRadius(5);

                        series.setOnDataPointTapListener(new OnDataPointTapListener() {

                            @Override
                            public void onTap(Series series, DataPointInterface dataPoint) {
                                Toast.makeText(getApplicationContext(), dataPoint.getY() + "% da frequência no envio de relatórios.", Toast.LENGTH_SHORT).show();
                            }
                        });

                        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(lineChart);
                        staticLabelsFormatter.setHorizontalLabels(new String[]{"jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez"});
                        staticLabelsFormatter.setVerticalLabels(new String[]{"0%", "25%", "50%", "75%", "100%"});

                        lineChart.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

                        lineChart.getGridLabelRenderer().setVerticalLabelsColor(R.color.grey_light);
                        lineChart.getGridLabelRenderer().setHorizontalLabelsColor(R.color.grey_light);
                        lineChart.setTitleColor(R.color.grey_light);
                        lineChart.setTitleTextSize(12f);
                        lineChart.addSeries(series);
                        Viewport viewport = lineChart.getViewport();
                        viewport.setYAxisBoundsManual(true);
                        viewport.setMinY(0);
                        viewport.setMaxY(100);
                        viewport.setScrollable(false);
                    }

                } catch (final Exception e) {
                    Logger.logError(TAG, e.getMessage());
                }
            }
        });
    }

    public void onDateChanged(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
        onDateSelected(widget, date, true);
    }

    @Override
    public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay date) {
        calendarDay = date;

        new AsyncTaskRunner().execute();
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

        widget.invalidateDecorators();

        if (daysGoodAndBad.size() > 0) {
            for (int i = 0; i < daysGoodAndBad.size(); i++) {
                if (daysGoodAndBad.get(i).equals(date.getDate())) {
                    widget.setDateSelected(daysGoodAndBad.get(i), true);
                }
            }
        }

        SimpleRequester simpleRequester = new SimpleRequester();

        if (idSelectedUser == singleUser.getId()) {
            simpleRequester.setUrl(Requester.API_URL + "user/calendar/day?day=" + date.getDay() + "&month=" + (date.getMonth() + 1) + "&year=" + date.getYear());
        } else if (idSelectedUser.equals("")) {
            simpleRequester.setUrl(Requester.API_URL + "user/calendar/day?day=" + date.getDay() + "&month=" + (date.getMonth() + 1) + "&year=" + date.getYear());
        } else {
            simpleRequester.setUrl(Requester.API_URL + "household/calendar/day?day=" + date.getDay() + "&month=" + (date.getMonth() + 1) + "&year=" + date.getYear() + "&household_id=" + idSelectedUser);
        }

        simpleRequester.setJsonObject(null);
        simpleRequester.setMethod(Method.GET);

        String jsonStr;
        goodCountDetail = 0;
        badCountDetail = 0;

        try {
            jsonStr = simpleRequester.execute(simpleRequester).get();

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.get("error").toString() == "true") {
                Toast.makeText(getApplicationContext(), jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
            } else {

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

    private void countTotalGoodAndBad(final CalendarDay date) {

        daysGood = new ArrayList<>();
        daysBad = new ArrayList<>();
        daysZero = new ArrayList<>();
        daysGoodAndBad = new ArrayList<>();
        daysOnlyGood = new ArrayList<>();
        daysOnlyBad = new ArrayList<>();
        daysEquals = new ArrayList<>();

        final SimpleRequester requester = new SimpleRequester();

        if (idSelectedUser.equals(singleUser.getId())) {
            requester.setUrl(Requester.API_URL + "user/calendar/month?&month=" + (date.getMonth() + 1) + "&year=" + date.getYear());
        } else if (idSelectedUser.equals("")) {
            requester.setUrl(Requester.API_URL + "user/calendar/month?&month=" + (date.getMonth() + 1) + "&year=" + date.getYear());
        } else {
            requester.setUrl(Requester.API_URL + "household/calendar/month?&month=" + (date.getMonth() + 1) + "&year=" + date.getYear() + "&household_id=" + idSelectedUser);
        }

        requester.setJsonObject(null);
        requester.setMethod(Method.GET);

        requester.setListener(new RequestListener<String>() {

            final LoadDialog loadDialog = new LoadDialog();

            @Override
            public void onStart() {
                loadDialog.show(getFragmentManager(), LoadDialog.TAG);
            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(final String result) {
                loadDialog.dismiss();

                int goodCountTotal = 0;
                int badCountTotal = 0;

                try {

                    JSONObject jsonObject = new JSONObject(result);

                    if (!jsonObject.getBoolean("error")) {

                        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                        final JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            final JSONObject jsonSympton = jsonArray.getJSONObject(i);
                            final JSONObject jsonDetail = jsonSympton.getJSONObject("_id");

                            if (jsonDetail.getString("no_symptom").equals("N")) {

                                badCountTotal = Integer.parseInt(jsonSympton.getString("count"));

                            } else if (jsonDetail.getString("no_symptom").equals("Y")) {

                                goodCountTotal = Integer.parseInt(jsonSympton.getString("count"));
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

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        requester.execute();
    }

    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            countTotalGoodAndBad(calendarDay);

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {

            // execution of result of Long time consuming operation
            onDateChanged(materialCalendarView, calendarDay);

            materialCalendarView.removeDecorators();

            materialCalendarView.addDecorators(new MySelectorDecoratorGood(DiaryActivity.this, daysGood),
                    new MySelectorDecoratorBad(DiaryActivity.this, daysBad),
                    new DayDisableDecorator(daysZero),
                    new MySelectorDecoratorOnlyGood(DiaryActivity.this, daysOnlyGood),
                    new MySelectorDecoratorOnlyBad(DiaryActivity.this, daysOnlyBad),
                    new MySelectorDecoratorEquals(DiaryActivity.this, daysEquals));
        }

        @Override
        protected void onPreExecute() {

        }
    }
}

class DayDisableDecorator implements DayViewDecorator {

    private List<Integer> days;

    public DayDisableDecorator(List<Integer> days) {
        this.days = days;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        if (days.size() > 0) {
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
        view.setDaysDisabled(true);
    }
}

