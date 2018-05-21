package roman.finn.javari.renderings.guis;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Login extends Application {

	public static Parent root;

	@Override
	public void start(Stage stage) throws IOException {
		Platform.runLater(new Runnable() {
			public void run() {
				try {
					root = FXMLLoader.load(getClass().getResource("LoginWindow.fxml"));

					Scene scene = new Scene(root);

					stage.setTitle("Login");
					stage.setResizable(false);
					stage.setScene(scene);
					stage.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void load(String[] args) {
		launch(args);
	}
}
