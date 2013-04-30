package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import util.Logger;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{
		System.out.println(getClass().getResource("/layout.fxml"));
		Parent root = FXMLLoader.load(getClass().getResource("/layout.fxml"));  
		
		Scene s = new Scene(root, 800, 600);
		primaryStage.initStyle(StageStyle.DECORATED);
		primaryStage.setScene(s);
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
				{
					@Override
					public void handle(WindowEvent arg0) {
						System.exit(0);
					}
			
				});
	}

	public static void main(String[] args) {
		Logger.startLogging("spectro.log");
		launch(args);
	}
}
