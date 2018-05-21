package roman.finn.javari.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class StringCheck {

	@SuppressWarnings("deprecation")
	public static String downloadString(String theURL) {
		InputStream is = null;
		final StringBuffer sb = new StringBuffer();
		try {
			final URL u = new URL(theURL);
			is = u.openStream();
			final DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
			String s;
			while ((s = dis.readLine()) != null) {
				sb.append(String.valueOf(s) + "\n");
			}
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException ex) {
			}
		}
		try {
			is.close();
		} catch (IOException ex2) {
		}
		return sb.toString();
	}

}
