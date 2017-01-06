package com.noelop.p_0721;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.tools.PanListener;
import org.achartengine.tools.ZoomEvent;
import org.achartengine.tools.ZoomListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class AchartengineActivity extends Activity {
    public static final String TYPE = "type";

    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    private XYSeries mCurrentSeries;

    private XYSeriesRenderer mCurrentRenderer;

    private String mDateFormat;

    private Button mNewSeries;

    private Button mAdd;



    private GraphicalView mChartView;



    private int index = 0;



    static double[] x = {};
    static double[] y = {};

    protected Update mUpdateTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achartengine);

        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
        mRenderer.setAxisTitleTextSize(16);
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(15);
        mRenderer.setLegendTextSize(15);
        mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setPointSize(10);
        mRenderer.setXTitle("TIME");
        mRenderer.setYTitle("y");
        mRenderer.setShowGrid(true);
        mRenderer.setBarSpacing(0.5f);




        String seriesTitle = "Series " + (mDataset.getSeriesCount() + 1);
        XYSeries series = new XYSeries(seriesTitle);
        mDataset.addSeries(series);
        mCurrentSeries = series;
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setFillPoints(true);
        mCurrentRenderer = renderer;


        mUpdateTask = new Update();
        mUpdateTask.execute(this);
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            mChartView = ChartFactory.getBarChartView(this, mDataset,
                    mRenderer, BarChart.Type.DEFAULT);
            mRenderer.setClickEnabled(true);
            mRenderer.setSelectableBuffer(100);
            mChartView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeriesSelection seriesSelection = mChartView
                            .getCurrentSeriesAndPoint();
                    double[] xy = mChartView.toRealPoint(0);
                    if (seriesSelection == null) {
                        Toast.makeText(AchartengineActivity.this,
                                "No chart element was clicked",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(
                                AchartengineActivity.this,
                                "Chart element in series index "
                                        + seriesSelection.getSeriesIndex()
                                        + " data point index "
                                        + seriesSelection.getPointIndex()
                                        + " was clicked"
                                        + " closest point value X="
                                        + seriesSelection.getXValue() + ", Y="
                                        + seriesSelection.getValue()
                                        + " clicked point value X="
                                        + (float) xy[0] + ", Y="
                                        + (float) xy[1], Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
            mChartView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    SeriesSelection seriesSelection = mChartView
                            .getCurrentSeriesAndPoint();
                    if (seriesSelection == null) {
                        Toast.makeText(AchartengineActivity.this,
                                "No chart element was long pressed",
                                Toast.LENGTH_SHORT);
                        return false; // no chart element was long pressed, so
                        // let something
                        // else handle the event
                    } else {
                        Toast.makeText(AchartengineActivity.this,
                                "Chart element in series index "
                                        + seriesSelection.getSeriesIndex()
                                        + " data point index "
                                        + seriesSelection.getPointIndex()
                                        + " was long pressed",
                                Toast.LENGTH_SHORT);
                        return true; // the element was long pressed - the event
                        // has been
                        // handled
                    }
                }
            });
            mChartView.addZoomListener(new ZoomListener() {
                public void zoomApplied(ZoomEvent e) {
                    String type = "out";
                    if (e.isZoomIn()) {
                        type = "in";
                    }
                    System.out.println("Zoom " + type + " rate "
                            + e.getZoomRate());
                }

                public void zoomReset() {
                    System.out.println("Reset");
                }
            }, true, true);
            mChartView.addPanListener(new PanListener() {
                public void panApplied() {
                    System.out.println("New X range=["
                            + mRenderer.getXAxisMin() + ", "
                            + mRenderer.getXAxisMax() + "], Y range=["
                            + mRenderer.getYAxisMax() + ", "
                            + mRenderer.getYAxisMax() + "]");
                }
            });
            layout.addView(mChartView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            boolean enabled = mDataset.getSeriesCount() > 0;
        } else {
            mChartView.repaint();
        }
    }

    private int generateX(){
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(10);
        return randomInt;

    }


    private int generateY() {
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(100);
        return randomInt;

    }


    protected class Update extends AsyncTask<Context, Integer, String> {
        int i = generateX();
        int i1 = i;
        int i2 = 0;

        // -- gets called just before thread begins
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Context... params) {
            while (true) {

                try {
                    Thread.sleep(1000);
                    while (i1>=0){
                        x[i2] = i2+1;
                        y[i2] = generateY();
                        i1--;
                        i2++;
                    }

                    //publishProgress(i);

                } catch (Exception e) {

                }
            }
            //return "COMPLETE!";
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);



                //mCurrentSeries.add(,);

            if (mChartView != null) {
                mChartView.repaint();
            }
            Bitmap bitmap = mChartView.toBitmap();
            try {
                File file = new File(Environment.getExternalStorageDirectory(),
                        "test" + index++ + ".png");
                FileOutputStream output = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // -- called if the cancel button is pressed
        @Override
        protected void onCancelled() {
            super.onCancelled();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }
}
