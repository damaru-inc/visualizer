package com.damaru.visualizer;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VisualizerApplication {

	public static void main(String[] args) {
		Application.launch(ChartApplication.class, args);
//		SpringApplication.run(VisualizerApplication.class, args);
	}

}
