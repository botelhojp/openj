package openjade.trust.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Hashtable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;

public class MonitorChart {

	private static ChartFrame chartFrame;
	private static JFreeChart chart;
	private static Hashtable<String, XYSeries> series;
	private static XYSeriesCollection xyDataset;

	private static MonitorChart gui = new MonitorChart();

	private MonitorChart() {
		series = new Hashtable<String, XYSeries>();
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		xyDataset = new XYSeriesCollection();
		chart = ChartFactory.createXYLineChart(" ", "Time", "Utility (UG)", xyDataset, PlotOrientation.VERTICAL, true, true, false);
		chartFrame = new ChartFrame("", chart);
		chartFrame.setBounds(0, 0, (int) dimension.getWidth() / 2, (int) dimension.getHeight() / 2);
		chartFrame.setVisible(true);
		plotLabels(100.0, 365.0);
	}

	public static MonitorChart getInstance() {
		return gui;
	}
	
	public static MonitorChart getInstance(String string, double max, double clicks) {
		MonitorChart.setTitle(string);
		MonitorChart.plotLabels(max, clicks);
		return gui;
	}


	private void addSerie(String _serie) {
		XYSeries serie = new XYSeries(_serie);
		series.put(_serie, serie );
		xyDataset.addSeries(serie);
	}

	public void addValue(String _serie, int click, double value) {
		if (series.containsKey(_serie)) {
			series.get(_serie).add(click, value);
		} else {
			addSerie(_serie);
			addValue(_serie, click, value);
		}
	}

	public static void setTitle(String title) {
		chartFrame.setTitle(title);
	}
	
	public static void plotLabels(double maxRange, double maxDomain) {
		XYPlot xyPlot = chart.getXYPlot();
		XYItemRenderer renderer = xyPlot.getRenderer();
		renderer.setBaseItemLabelsVisible(true);
		
		XYItemRenderer xyitemrenderer = xyPlot.getRenderer();

		xyitemrenderer.setSeriesPaint(0, Color.blue);
		xyitemrenderer.setBaseItemLabelsVisible(true);
		xyitemrenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER));
		
		NumberAxis numberaxis1 = (NumberAxis) xyPlot.getRangeAxis();
		numberaxis1.setRange(0.0D, maxRange);
		
		NumberAxis numberaxis = (NumberAxis) xyPlot.getDomainAxis();
		numberaxis.setRange(0.0D, maxDomain);
	}
}
