package com.damaru.visualizer;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan({"com.solace", "com.damaru"})
public class VisualizerApplication {

	public static void main(String[] args) {
		Application.launch(ChartApplication.class, args);
	}

}
