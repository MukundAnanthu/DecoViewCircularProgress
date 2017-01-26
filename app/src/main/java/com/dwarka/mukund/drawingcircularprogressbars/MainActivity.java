package com.dwarka.mukund.drawingcircularprogressbars;

/*
* This project is done in order to understand how to use Deco View library. The tutorial I am referring
* to can be found at: https://android-arsenal.com/details/1/2329
*
* */

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DecoView arcView = (DecoView)findViewById(R.id.dynamicArcView);

        // Create background track
        // the 218 for rgb is the grey color of the background
        arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0, 100, 100)
                .setInitialVisibility(false)
                .setLineWidth(32f)
                .build());

        //Create data series track
        // The color set is the color of the arc representing progress
        final SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0, 100, 0)
                .setLineWidth(32f)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_OUTER, Color.parseColor("#22000000"), 0.4f)) // Adds the edge to the ring
                //.setSeriesLabel(new SeriesLabel.Builder("Percent %.0f%%").build()) // Adds that Toast kinda thing along the arc, to dispay the percentage
                .setInterpolator(new BounceInterpolator()) // The progress overshoots its mark and then comes back
                .setCapRounded(false) // makes the progress arc have flat ends
                //.setInset(new PointF(32f, 32f)) // draws the progress arc inside the track, instead of on the track
                //.setDrawAsPoint(true) // results in just a small stick indicating the progress. It's movement instead of smear
                //.setSpinDuration(6000) // Time taken for the progress to do a 360
                .build();

        //To move the current position of a data series create a new DecoEvent and pass it to the DecoView:
        //decoView.addEvent(new DecoEvent.Builder(50).setIndex(mSeriesIndex).build());


        seriesItem1.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            String format = "%.0f%%";
            TextView percentageChargeTextView = (TextView) findViewById(R.id.percentage_display_tv);
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                if (format.contains("%%")) {
                    float percentFilled = ((currentPosition - seriesItem1.getMinValue()) / (seriesItem1.getMaxValue() - seriesItem1.getMinValue()));
                    percentageChargeTextView.setText(String.format(format, percentFilled * 100f));
                } else {
                    percentageChargeTextView.setText(String.format(format, currentPosition));
                }
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {
                // I think this gives the progress of the animation, for instance the grey circle
                // expanding to it's size is an event, and it's progress is listened to in this function. Not sure though
            }
        });


        /*
        Code showing how we could configure the seriesItem

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .setLineWidth(32f)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_OUTER, Color.parseColor("#22000000"), 0.4f))
                .setSeriesLabel(new SeriesLabel.Builder("Percent %.0f%%").build())
                .setInterpolator(new OvershootInterpolator())
                .setShowPointWhenEmpty(false)
                .setCapRounded(false)
                .setInset(new PointF(32f, 32f))
                .setDrawAsPoint(false)
                .setSpinClockwise(true)
                .setSpinDuration(6000)
                .setChartStyle(SeriesItem.ChartStyle.STYLE_DONUT)
                .build();*/


        final int series1Index = arcView.addSeries(seriesItem1);

        // Add events to animate the data series
        arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
        //arcView.addEvent(new DecoEvent.Builder(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                //.setDelay(1)
                .setDuration(1000)  // it's the time required to display the grey ring. This includes the animation of the ring expanding to it's actual size
                .build());

        //To move the current position of a data series create a new DecoEvent and pass it to the DecoView:
        // That's what the lines in the below block of code indicate
        //decoView.addEvent(new DecoEvent.Builder(50).setIndex(mSeriesIndex).build());
        //If you want to execute the event at a future time you can add the delay to the DecoEvent through the DecoEvent.Builder.setDelay(long ms) function.
        // decoView.addEvent(new DecoEvent.Builder(50).setIndex(mSeriesIndex).setDelay(8000).build());

        // setDelay implies the amount of milliseconds before the event/animation is triggered
        arcView.addEvent(new DecoEvent.Builder(25).setIndex(series1Index).setDelay(1000).build());
        //arcView.addEvent(new DecoEvent.Builder(100).setIndex(series1Index).setDelay(8000).build());
        //arcView.addEvent(new DecoEvent.Builder(10).setIndex(series1Index).setDelay(12000).build());

        /*In the above example some important points to note are:

        The argument '50' passed to the builder function is the position in relation to the range of data initialized with the SeriesItem.Builder().setRange(min, max, initial) function call
        The index that is passed was returned from the DecoView.addSeries(...) function call
        All durations are specified in milliseconds*/

        /*
        Working code...
        final int[] progress = {0};
        final Handler handler = new Handler();
        handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        arcView.addEvent(new DecoEvent.Builder(progress[0]).setIndex(series1Index).setDelay(1000).build());
                        progress[0]++;
                        if (progress[0] <= 100 )
                            handler.postDelayed(this, 1000);
                    }
                }
        );*/

    }
}
