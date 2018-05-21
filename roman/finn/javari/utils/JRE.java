package roman.finn.javari.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

public class JRE {

	private static JRE jre;

	private final Map<String, ClassNode> classMap = new HashMap<>();

	public JRE() {
		String[] libraries = System.getProperty("java.class.path").split(";");
		for (String library : libraries) {
			if (!library.endsWith(".jar")) {
				continue;
			}
			try {
				@SuppressWarnings("resource")
				JarFile jar = new JarFile(library);
				Enumeration<JarEntry> entries = jar.entries();
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					try (InputStream in = jar.getInputStream(entry)) {
						if (!entry.getName().endsWith(".class")) {
							continue;
						}
						byte[] bytes = IO.read(in);
						ClassNode c = new ClassNode();
						new ClassReader(bytes).accept(c, 7);
						classMap.put(c.name, c);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static JRE getJRE() {
		if (jre == null)
			jre = new JRE();
		return jre;
	}

	public Map<String, ClassNode> getClassMap() {
		return classMap;
	}

}
