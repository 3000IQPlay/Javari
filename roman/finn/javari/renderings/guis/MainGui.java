package roman.finn.javari.renderings.guis;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import roman.finn.javari.JavariInfo;

public class MainGui {

	public MainGui() {
		Platform.runLater(new Runnable() {
			public void run() {
				try {
					Login.root.getScene().getWindow().hide();

					FXMLLoader fxmlLoader = new FXMLLoader();
					fxmlLoader.setLocation(getClass().getResource("MainGuiWindow.fxml"));

					Scene scene = new Scene(fxmlLoader.load());
					Stage stage = new Stage();
					stage.setTitle(JavariInfo.getTitle());
					stage.setScene(scene);
					stage.setResizable(false);
					stage.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// private static void Protection() {
	// try{
	// URL url = new URL("http://cheap-alts.com/Javari/status.txt");
	// BufferedReader in;
	// in = new BufferedReader(new InputStreamReader(url.openStream()));
	// if (in.readLine().equals("true")){
	// while (true) {
	// Object[] o = null;
	// o = new Object[] {o};
	// System.exit(44);
	// }
	// }
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// }
}
