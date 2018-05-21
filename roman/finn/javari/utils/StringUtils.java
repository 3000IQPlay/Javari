package roman.finn.javari.utils;

/**
 * Created by Roman on 27.02.2017.
 */
public class StringUtils {

	public static String winrarFucker = "\0\0\0\0\0\0\0\0\0\0\0\0\0";

	public static String unicodes = "\u1616\u1717\u1616\u1919\u2020\u1616\u1616\u1919\u1919\u1717\u1919\u2020\u1818\u2020\u1818\u2020\u1919\u1919\u1919\u1717\u2020\u2020\u1616\u1919\u1919\u1616\u1818\u1818\u1818\u1717 \u1717\u1818\u1919\u1717\u1717\u1919";

	public static String createLongString(String s) {
		StringBuffer sb = new StringBuffer();
		while (sb.length() < 100 - 1) {
			sb.append(s);
		}
		return sb.toString();
	}
}
