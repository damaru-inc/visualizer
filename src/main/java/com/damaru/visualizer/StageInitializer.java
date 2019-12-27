package com.damaru.visualizer;

import com.solace.temperature.Temperature;
import com.solace.temperature.TemperatureDataChannel;
import com.solace.temperature.TemperatureMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<ChartApplication.StageReadyEvent> {

	private static final Logger log = LoggerFactory.getLogger(StageInitializer.class);
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 960;
	private int numGraphs = 2;
	private double[] values;
	ResizableCanvas canvas;

	@Value("classpath:/fxml/main.fxml")
	private Resource chartResource;

	private ApplicationContext applicationContext;

	@Autowired
	public StageInitializer(ApplicationContext applicationContext) {
		log.info("ctor");
		this.applicationContext = applicationContext;
	}

	@Autowired
	TemperatureDataChannel temperatureDataChannel;

	@Override
	public void onApplicationEvent(ChartApplication.StageReadyEvent event) {
		Stage stage = event.getStage();
		values = new double[numGraphs];

		for (int i = 0; i < numGraphs; i++) {
			values[i] = Math.random();
		}

		//runFromXml(stage);
		runManually(stage);
		try {
			log.info("subscribing....");
			temperatureDataChannel.subscribe(new TemperatureListener(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void runFromXml(Stage stage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(chartResource.getURL());
			fxmlLoader.setControllerFactory(aClass -> applicationContext.getBean(aClass));
			Parent parent = fxmlLoader.load();

			stage.setScene(new Scene(parent, WIDTH, HEIGHT));
			stage.setTitle("Visualizer");
			stage.show();
			log.info("--------- loaded " + chartResource.getURL());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	private void runManually(Stage stage) {
		Pane root = new Pane();

		canvas = new ResizableCanvas(WIDTH, HEIGHT, numGraphs);
		GraphicsContext gc = canvas.getGraphicsContext2D();
//		doDrawing(gc);

		root.getChildren().add(canvas);
		canvas.widthProperty().bind(root.widthProperty());
		canvas.heightProperty().bind(root.heightProperty());

		Scene scene = new Scene(root, WIDTH, HEIGHT, Color.WHITESMOKE);

		stage.setTitle("Stroke and fill");
		stage.setScene(scene);
		stage.show();
	}

	private void doDrawing(GraphicsContext gc) {
		gc.setStroke(Color.FORESTGREEN.brighter());
		gc.setLineWidth(5);
		gc.strokeOval(30, 30, 80, 80);
		gc.setFill(Color.FORESTGREEN);
		gc.fillOval(130, 30, 80, 80);
	}

	//@Scheduled(fixedRate = 1)
	private void updateScene() {
		if (canvas != null) {
			double DELTA = 0.001;
			for (int i = 0; i < numGraphs; i++) {
				double v = values[i];
				double delta = Math.random() * DELTA - DELTA / 2.0;
				v = v + delta;
				v = Math.min(1.0, v);
				v = Math.max(0.0, v);
				values[i] = v;
			}

			canvas.setValues(values);
			canvas.draw();
		}
	}

	public void update(int index, double value) {
		values[index] = value;
		canvas.setValues(values);
		canvas.draw();
	}
}

class TemperatureListener implements TemperatureMessage.SubscribeListener {

	private static final Logger log = LoggerFactory.getLogger(TemperatureListener.class);
	private StageInitializer stageInitializer;

	public TemperatureListener(StageInitializer stageInitializer) {
		this.stageInitializer = stageInitializer;
	}

	@Override
	public void onReceive(TemperatureMessage temperatureMessage) {
		Temperature temperature = temperatureMessage.getPayload();
		log.info("temp: " + temperature.getTemperature());
		stageInitializer.update(temperature.getSensorId(), temperature.getTemperature());
	}

	@Override
	public void handleException(Exception e) {
		log.error("Error: " + e);
	}
}
