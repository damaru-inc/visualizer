import com.damaru.visualizer.ChartApplication;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageInitializer implements ApplicationListener<ChartApplication.StageReadyEvent> {
	@Override
	public void onApplicationEvent(ChartApplication.StageReadyEvent event) {
		Stage stage = event.getStage();
	}
}
