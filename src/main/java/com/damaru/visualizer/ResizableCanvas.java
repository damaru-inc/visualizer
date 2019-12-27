package com.damaru.visualizer;

import com.solace.temperature.TemperatureDataChannel;
import com.solace.temperature.TemperatureMessage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class ResizableCanvas extends Canvas {
	private static final Logger log = LoggerFactory.getLogger(ResizableCanvas.class);
	private int numGraphs = 2;
	private static final int UNITS_SPACING = 2;
	private static final int UNITS_WIDTH = 3;
	private static final int BORDER_WIDTH = 1;
	private static final int TWICE_BORDER_WIDTH = BORDER_WIDTH * 2;
	static final double TEMP_LOW = 20.0;
	static final double TEMP_HIGH = 35.0;
	static final double TEMP_RANGE = TEMP_HIGH - TEMP_LOW;

	private double[] values;


	Color backGround = Color.color(0.1, 0.1, 0.2);
	Color hotColour = Color.RED; // Color.color(0.5, 0.15, 0.15);
	Color coldColour = Color.BLUE; //Color.color(0.15, 0.15, 0.5);
	Color borderColor = Color.PURPLE;
	LinearGradient linearGradient;

	public ResizableCanvas(double v, double v1, int numGraphs) {
		super(v, v1);
		this.numGraphs = numGraphs;
		values = new double[numGraphs];
		for (int i = 0; i < numGraphs; i++) {
			values[i] = TEMP_LOW;
		}
		init();
	}

	private void init() {
		// Redraw canvas when size changes.
		widthProperty().addListener(evt -> draw());
		heightProperty().addListener(evt -> draw());
		Stop stops[] = new Stop[] { new Stop(1.0, hotColour), new Stop(0.0, coldColour) };
		linearGradient = new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE, stops);
		draw();
	}

	public void draw() {
		double width = getWidth();
		double height = getHeight();
		int totalUnits = ((numGraphs + 1) * UNITS_SPACING) + (numGraphs * UNITS_WIDTH);
		int pixelsPerUnit = (int) (width / totalUnits);

		// set background
		GraphicsContext gc = getGraphicsContext2D();
		gc.setLineWidth(TWICE_BORDER_WIDTH);
		gc.setFill(backGround);
		gc.fillRect(0, 0, width, height);
		gc.setTextAlign(TextAlignment.CENTER);
		Font font = Font.font(height * 0.08);
		gc.setFont(font);

		double y = height * 0.2;
		double h = height * 0.6;
		double w = pixelsPerUnit * UNITS_WIDTH;

		for (int i = 0; i < numGraphs; i++) {
			int unitsForX = UNITS_SPACING + i * (UNITS_WIDTH + UNITS_SPACING);
			double x = unitsForX * pixelsPerUnit;
			drawBarMeter(gc, x, y, w, h, values[i]);
			gc.setFill(Color.LIGHTBLUE);
			double fontx = x + UNITS_WIDTH * 3.0;
			gc.fillText("" + i, fontx, height * 0.2);
			gc.fillText("" + values[i], fontx, height * 0.9);
		}

	}

	private void drawBarMeter(GraphicsContext gc, double x, double y, double w, double h, double value) {
		double percent = (value - TEMP_LOW) / TEMP_RANGE;
		gc.setFill(linearGradient);
		gc.setStroke(borderColor);
		gc.setLineWidth(BORDER_WIDTH);
		gc.strokeRect(x, y, w, h);

		gc.fillRect(x + BORDER_WIDTH, y+BORDER_WIDTH,  w - TWICE_BORDER_WIDTH, h - TWICE_BORDER_WIDTH);

		gc.setFill(Color.BLACK);
		h = h * (1.0 - percent);
		//log.info(String.format("width: %f height: %f x: %f y: %f w: %f h: %f", width, height, x, y, w, h));
		gc.fillRect(x + BORDER_WIDTH, y+BORDER_WIDTH,  w - TWICE_BORDER_WIDTH, h - TWICE_BORDER_WIDTH);
	}

	@Override
	public boolean isResizable() {
		return true;
	}

	@Override
	public double prefWidth(double height) {
		return getWidth();
	}

	@Override
	public double prefHeight(double width) {
		return getHeight();
	}

	public void setValues(double[] values) {
		this.values = values;
	}
}

