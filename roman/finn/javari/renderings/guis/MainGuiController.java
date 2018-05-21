package roman.finn.javari.renderings.guis;

import java.io.File;
import java.io.OutputStream;
import java.util.jar.JarFile;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import roman.finn.javari.Config;
import roman.finn.javari.utils.RenderUtils;

public class MainGuiController {

	private JFileChooser chooserDialog;

	@FXML
	private JFXCheckBox badTryCatchCheck;

	@FXML
	private JFXCheckBox varDuplicatorCheck;

	@FXML
	private JFXTextField libBox;

	@FXML
	private JFXCheckBox methodRenamerCheck;

	@FXML
	private JFXTextField newPackageTxt;

	@FXML
	private JFXTextField outBox;

	@FXML
	private JFXCheckBox lineNumberObfCheck;

	@FXML
	private JFXCheckBox badPopCheck;

	@FXML
	private JFXButton inputButton;

	@FXML
	private JFXCheckBox stringEncryptionCheck;

	@FXML
	private Tab mainTab;

	@FXML
	private JFXButton outputButton;

	@FXML
	private JFXCheckBox classRenamerCheck;

	@FXML
	private JFXCheckBox classHiderCheck;

	@FXML
	private JFXCheckBox finalRenamerCheck;

	@FXML
	private JFXCheckBox fieldRenamerCheck;

	@FXML
	private JFXSlider obfStrengthSlider;

	@FXML
	private JFXCheckBox localFieldRenamerCheck;

	@FXML
	private JFXTextField keepMainTxt;

	@FXML
	private JFXButton ObfuscateButton;

	@FXML
	private JFXCheckBox trashCodeCheck;

	@FXML
	private JFXTextField inputBox;

	@FXML
	private JFXCheckBox keepPackageCheck;

	@FXML
	private JFXButton libButton;

	@FXML
	private JFXTextField keepPackageTxt;

	@FXML
	private Tab accountTab;

	@FXML
	private Tab settingsTab;

	@FXML
	private TabPane tabPane;

	@FXML
	private StackPane stackPane;

	@FXML
	private AnchorPane mainAnchorPane;

	final Tooltip tipp = new Tooltip();

	@FXML
	void setInput(ActionEvent event) {
		inputBox.setText(showFileSelectDialog(true).getPath());
	}

	@FXML
	void setOutput(ActionEvent event) {
		outBox.setText(showFileSelectDialog(true).getPath());
	}

	@FXML
	void setLibs(ActionEvent event) {
		libBox.setText(showFileSelectDialog2(true).getPath());
	}

	@FXML
	void startObf(ActionEvent event) {
		// JFXSnackbar snackbar = new JFXSnackbar(mainAnchorPane);
		// snackbar.show("Please Wait...", 2000);
		mainTab.setDisable(true);
		settingsTab.setDisable(true);
		accountTab.setDisable(true);
		RenderUtils.renderDialogWithoutButton(stackPane, "Wait", "Obfuscating", "");
		Config.in = inputBox.getText();
		Config.out = outBox.getText();
		Config.libs = libBox.getText();
		Config.newPackageName = newPackageTxt.getText();
		Config.keepMainClass = keepMainTxt.getText();
		Config.keepPackage = keepPackageTxt.getText();
		Config.strength = new Integer((int) obfStrengthSlider.getValue());
		new Thread() {
			@Override
			public void run() {
				try {
					if (classRenamerCheck.isSelected()) {
						Config.doClassRemapp = true;
						roman.finn.javari.Javari.loadJavari(new String[] { outBox.getText() + "-temp.jar",
								outBox.getText(), libBox.getText(), ";", });
					} else if (!classRenamerCheck.isSelected()) {
						roman.finn.javari.Javari.loadJavari(
								new String[] { inputBox.getText(), outBox.getText(), libBox.getText(), ";", });
					}
					roman.finn.javari.Javari.getInstance().join();

					mainTab.setDisable(false);
					settingsTab.setDisable(false);
					accountTab.setDisable(false);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@FXML
	void setFieldRenamer(ActionEvent event) {
		if (fieldRenamerCheck.isSelected()) {
			Config.fieldRenamer = true;
		} else {
			Config.fieldRenamer = false;
		}
	}

	@FXML
	void setMethodRenamer(ActionEvent event) {
		if (methodRenamerCheck.isSelected()) {
			Config.methodRenamer = true;
		} else {
			Config.methodRenamer = false;
		}
	}

	@FXML
	void setLocalFieldRenamer(ActionEvent event) {
		if (localFieldRenamerCheck.isSelected()) {
			Config.localFieldRenamer = true;
		} else {
			Config.localFieldRenamer = false;
		}
	}

	@FXML
	void setLineNumberObf(ActionEvent event) {
		if (lineNumberObfCheck.isSelected()) {
			Config.lineNumberObfuscation = true;
		} else {
			Config.lineNumberObfuscation = false;
		}
	}

	@FXML
	void setStringEncryption(ActionEvent event) {
		if (stringEncryptionCheck.isSelected()) {
			Config.stringEncryption = true;
		} else {
			Config.stringEncryption = false;
		}
	}

	@FXML
	void setVarDuplicator(ActionEvent event) {
		if (varDuplicatorCheck.isSelected()) {
			Config.varDuplicator = true;
		} else {
			Config.varDuplicator = false;
		}
	}

	@FXML
	void setClassHider(ActionEvent event) {
		if (classHiderCheck.isSelected()) {
			Config.classHider = true;
		} else {
			Config.classHider = true;
		}
	}

	@FXML
	void setBadPop(ActionEvent event) {
		if (badPopCheck.isSelected()) {
			Config.badPop = true;
		} else {
			Config.badPop = false;
		}
	}

	@FXML
	void setBadTryCatch(ActionEvent event) {
		if (badTryCatchCheck.isSelected()) {
			Config.badTryCatch = true;
		} else {
			Config.badTryCatch = false;
		}
	}

	@FXML
	void setFinalRenamer(ActionEvent event) {
		if (finalRenamerCheck.isSelected()) {
			Config.finalRemover = true;
		} else {
			Config.finalRemover = false;
		}
	}

	@FXML
	void setTrashCode(ActionEvent event) {
		if (trashCodeCheck.isSelected()) {
			Config.trashCode = true;
		} else {
			Config.trashCode = false;
		}
	}

	@FXML
	void setKeepPackage(ActionEvent event) {

	}

	@FXML
	void setClassRenamer(ActionEvent event) {
		try {
			@SuppressWarnings("resource")
			String jarMain = new JarFile(inputBox.getText()).getManifest().getMainAttributes().getValue("Main-class");

			keepMainTxt.setText(jarMain);
		} catch (Exception e) {
			keepMainTxt.setText("Main class not found");
		}
	}

	@FXML
	void loadUserData(Event event) {
		if (accountTab.isSelected()) {
			System.out.println("user data");
		}
	}

	@FXML
	void showSettings(Event event) {

	}

	@FXML
	void getFieldRenamerDesc(MouseEvent event) {
		tipp.setText("Renames Fields in random characters");
		fieldRenamerCheck.setTooltip(tipp);
	}

	@FXML
	void getMethodRenamerDesc(MouseEvent event) {
		tipp.setText("Renames Methods in random characters");
		methodRenamerCheck.setTooltip(tipp);
	}

	@FXML
	void getLocalFieldRenamerDesc(MouseEvent event) {
		tipp.setText("Renames in Methods Fields");
		localFieldRenamerCheck.setTooltip(tipp);
	}

	@FXML
	void getLineNumberObfDesc(MouseEvent event) {
		tipp.setText("Changes the Line-Numbers and let crash jdcore");
		lineNumberObfCheck.setTooltip(tipp);
	}

	@FXML
	void getStringEncryptionDesc(MouseEvent event) {
		tipp.setText("Replaced Strings to unreadable characters");
		stringEncryptionCheck.setTooltip(tipp);
	}

	@FXML
	void getVarDuplicatorDesc(MouseEvent event) {
		// tipp.setText("Setting Variables to zero, and adds new to make the
		// code more confusing");
	}

	@FXML
	void getClassHiderDesc(MouseEvent event) {
		tipp.setText("Hides all Methods etc. in Classes");
		classHiderCheck.setTooltip(tipp);
	}

	@FXML
	void getBadPopDesc(MouseEvent event) {
		tipp.setText("Fernflower Method-Crash exploit");
		badPopCheck.setTooltip(tipp);
	}

	@FXML
	void getBadTryCatchDesc(MouseEvent event) {
		tipp.setText("Add try catch to all Methods");
		badTryCatchCheck.setTooltip(tipp);
	}

	@FXML
	void getFinalRenamerDesc(MouseEvent event) {
		tipp.setText("Removes the Final modifier");
		finalRenamerCheck.setTooltip(tipp);
	}

	@FXML
	void getTrashCodeDesc(MouseEvent event) {
		tipp.setText("Add deprecated modifier to all Methods and Fields");
		trashCodeCheck.setTooltip(tipp);
	}

	@FXML
	void getKeepPackageDesc(MouseEvent event) {
		tipp.setText("Keeps Package");
		keepPackageCheck.setTooltip(tipp);
	}

	@FXML
	void getClassRenamerDesc(MouseEvent event) {
		tipp.setText("Renames Classes in random characters");
		classRenamerCheck.setTooltip(tipp);
	}

	@FXML
	void getGetStrengthSliderDesc(MouseEvent event) {
		tipp.setText("Obfuscation strength");
		obfStrengthSlider.setTooltip(tipp);
	}

	private File showFileSelectDialog(boolean isJAR) {
		chooserDialog = new JFileChooser();
		chooserDialog.setCurrentDirectory(new File("."));

		if (isJAR)
			chooserDialog.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.getName().endsWith(".jar") || f.isDirectory();
				}

				@Override
				public String getDescription() {
					return "Only (.jar) files!";
				}
			});
		else
			chooserDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		chooserDialog.setAcceptAllFileFilterUsed(true);
		int state = chooserDialog.showDialog(null, "Select Jar");
		if (state == JFileChooser.APPROVE_OPTION) {
			File in = chooserDialog.getSelectedFile();
			if (isJAR && !in.getName().endsWith(".jar"))
				return null;

			return in;
		}

		return null;
	}

	private File showFileSelectDialog2(boolean isJAR) {
		chooserDialog = new JFileChooser();
		chooserDialog.setCurrentDirectory(new File("."));

		if (isJAR)
			chooserDialog.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.getName().endsWith("") || f.isDirectory();
				}

				@Override
				public String getDescription() {
					return "Only Library-Folders!";
				}
			});
		else
			chooserDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		chooserDialog.setAcceptAllFileFilterUsed(true);
		int state = chooserDialog.showDialog(null, "Select Library");
		if (state == JFileChooser.APPROVE_OPTION) {
			File in = chooserDialog.getSelectedFile();
			if (isJAR && !in.getName().endsWith(""))
				return null;

			return in;
		}

		return null;
	}

	protected class JTextAreaPrintStream extends OutputStream {
		private final JFXTextArea textArea;

		public JTextAreaPrintStream(JFXTextArea logBox) {
			this.textArea = logBox;
		}

		@Override
		public void write(int b) throws java.io.IOException {
			textArea.appendText(new String(new char[] { (char) b }));
		}

		@Override
		public void write(byte b[], int off, int len) throws java.io.IOException {
			String mess = new String(b, off, len);
			if (textArea != null)
				textArea.appendText(mess);
		}
	}
}
