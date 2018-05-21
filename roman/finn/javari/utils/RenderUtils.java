package roman.finn.javari.utils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class RenderUtils {

	public static void renderDialog(StackPane stackpane, String header, String text, String Button) {
		JFXDialogLayout content = new JFXDialogLayout();
		content.setHeading(new Text(header));
		content.setBody(new Text(text));
		stackpane.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
		content.setStyle("-fx-text-fill:WHITE;-fx-background-color:#ecf0f1;-fx-font-size:14px;-fx-font-family:system;");
		JFXDialog dialog = new JFXDialog(stackpane, content, JFXDialog.DialogTransition.CENTER);
		JFXButton closeButton = new JFXButton(Button);
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dialog.close();
			}
		});
		content.setActions(closeButton);
		dialog.show();
	}

	public static void renderDialogWithoutButton(StackPane stackpane, String header, String text, String Button) {
		JFXDialogLayout content = new JFXDialogLayout();
		content.setHeading(new Text(header));
		content.setBody(new Text(text));
		stackpane.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
		content.setStyle("-fx-text-fill:WHITE;-fx-background-color:#ecf0f1;-fx-font-size:14px;-fx-font-family:system;");
		JFXDialog dialog = new JFXDialog(stackpane, content, JFXDialog.DialogTransition.CENTER);
		dialog.show();
	}

	public static void loadFXML(Pane pane, Node node) {
		pane.getChildren().clear();
		pane.getChildren().add(node);
	}
}
