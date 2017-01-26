package com.dwarka.mukund.drawingcircularprogressbars;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
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
        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0, 100, 0)
                .setLineWidth(32f)
                .build();

        final int series1Index = arcView.addSeries(seriesItem1);

        // Add events to animate the data series
        arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                //.setDelay(1)
                .setDuration(1000)  // it's the time required to display the grey ring. This includes the animation of the ring expanding to it's actual size
                .build());


        // setDelay implies the amount of milliseconds before the event/animation is triggered
        arcView.addEvent(new DecoEvent.Builder(25).setIndex(series1Index).setDelay(8000).build());
        //arcView.addEvent(new DecoEvent.Builder(100).setIndex(series1Index).setDelay(8000).build());
        //arcView.addEvent(new DecoEvent.Builder(10).setIndex(series1Index).setDelay(12000).build());

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
        );

        /*for ( int i = 0; i <= 100; i++ ) {
            arcView.addEvent(new DecoEvent.Builder(i).setIndex(series1Index).setDelay(4000).build());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }
}
