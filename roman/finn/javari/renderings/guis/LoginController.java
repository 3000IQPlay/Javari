package roman.finn.javari.renderings.guis;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Pane loginPane;

	@FXML
	private StackPane stackpane;

	@FXML
	private JFXPasswordField pwbox;

	@FXML
	private JFXTextField userbox;

	@FXML
	private JFXCheckBox checkbox;

	@FXML
	private Label tosLabel;

	@FXML
	private JFXButton login;

	final Tooltip tipp = new Tooltip();

	@FXML
	void renderlabel(MouseEvent event) {
		tipp.setText("Agree");
		checkbox.setTooltip(tipp);
	}

	@FXML
	void openTOS(MouseEvent event) {
		// RenderUtils.renderDialog(stackpane, "Terms of Service",
		// Security.downloadString("http://cheap-alts.com/Javari/TOS.txt"),
		// "Agree");
	}

	public static Parent root;

	@FXML
	void checkLogin(ActionEvent event) throws IOException {
		new MainGui();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		new MainGui();
	}

}
