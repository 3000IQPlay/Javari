package roman.finn.javari;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import roman.finn.javari.renderings.guis.Login;

public class Main {

	public static void main(String[] args) throws UnsupportedLookAndFeelException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		Login.load(args);
	}

}
