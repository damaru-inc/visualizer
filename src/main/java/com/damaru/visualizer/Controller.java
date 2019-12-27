package com.damaru.visualizer;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


//@Component
public class Controller {

	private static final Logger log = LoggerFactory.getLogger(Controller.class);

	@FXML
	public Canvas canvas;

	@FXML
	public void initialize() {
		final GraphicsContext gc = canvas.getGraphicsContext2D();
		drawLines(gc);
		doDrawing(gc);
		final Parent parent = canvas.getParent();
		Bounds bounds = parent.getBoundsInParent();
		log.info(String.format("parent: %s w: %f h: %f w: %f h: %f", parent.toString(), bounds.getWidth(), bounds.getHeight(), canvas.getWidth(), canvas.getHeight()));
		canvas.setHeight(bounds.getHeight());
		canvas.setWidth(bounds.getWidth());

	}

	private void drawLines(GraphicsContext gc) {

		gc.beginPath();
		gc.moveTo(30.5, 30.5);
		gc.lineTo(150.5, 30.5);
		gc.lineTo(150.5, 150.5);
		gc.lineTo(30.5, 30.5);
		gc.stroke();
	}

	private void doDrawing(GraphicsContext gc) {

		gc.setStroke(Color.FORESTGREEN.brighter());
		gc.setLineWidth(5);
		gc.strokeOval(30, 30, 80, 80);
		gc.setFill(Color.FORESTGREEN);
		gc.fillOval(130, 30, 80, 80);
	}

}