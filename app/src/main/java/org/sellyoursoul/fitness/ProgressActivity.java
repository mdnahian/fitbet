package org.sellyoursoul.fitness;

import android.app.Activity;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.deepakbaliga.beautifulgraph.Plotter;

import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mdnah on 1/20/2018.
 */

public class ProgressActivity extends Activity {

    private Plotter plotter;
    private List<Integer> plots =  new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        plotter = findViewById(R.id.plotter_view);
        plotter.setRowCol(10,10);
        plotter.setPlots(plots);


    }


    plot = (XYPlot)

    findViewById(R.id.plot) {
    final Number[] domainLabels = {1, 2, 3, 6, 7, 8, 9, 10, 13, 14};
    Number[] series1Numbers = {1, 4, 2, 8, 4, 16, 8, 32, 16, 64};
    Number[] series2Numbers = {5, 2, 10, 5, 20, 10, 40, 20, 80, 40};

    // turn the above arrays into XYSeries':
    // (Y_VALS_ONLY means use the element index as the x value)
    XYSeries series1 = new SimpleXYSeries(
            Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1");
    XYSeries series2 = new SimpleXYSeries(
            Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2");

    // create formatters to use for drawing a series using LineAndPointRenderer
    // and configure them from xml:
    LineAndPointFormatter series1Format =
            new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels);

    LineAndPointFormatter series2Format =
            new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_2);

    // add an "dash" effect to the series2 line:
        series2Format.getLinePaint().setPathEffect(new DashPathEffect(new float[] {

        // always use DP when specifying pixel sizes, to keep things consistent across devices:
        PixelUtils.dpToPix(20),
                PixelUtils.dpToPix(15)}, 0));

    // just for fun, add some smoothing to the lines:
    // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        series2Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

    /* add a new series' to the xyplot: */
        plot.addSeries(series1, series1Format);
        plot.addSeries(series2, series2Format);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            int i = Math.round(((Number) obj).floatValue());
            return toAppendTo.append(domainLabels[i]);
        }
        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }
    });
}
}
