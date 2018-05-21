package roman.finn.javari.obfmethods;

import org.objectweb.asm.commons.*;
import org.objectweb.asm.tree.*;

import roman.finn.javari.Config;
import roman.finn.javari.utils.Processor;
import roman.finn.javari.utils.StringUtils;
import roman.finn.javari.utils.UniqueString;

import java.util.*;

public class ClassRemapper implements Processor {

	private final String basePackage;
	private final List<String> skip;
	private final String keepPackage = Config.keepPackage.replace(".", "/") + "/";

	public ClassRemapper(String basePackage, String... skip) {
		this.basePackage = basePackage.replace('.', '/');
		for (int i = 0; i < skip.length; i++) {
			skip[i] = skip[i].replace('.', '/');
		}
		this.skip = Arrays.asList(skip);
	}

	@Override
	public void process(Map<String, ClassNode> classMap) {
		UniqueString.reset();
		Map<String, String> remap = new HashMap<>();
		List<String> keys = new ArrayList<>(classMap.keySet());
		Collections.shuffle(keys);
		for (String key : keys) {
			ClassNode cn = classMap.get(key);
			String name = cn.name;
				if (!name.startsWith(keepPackage) && !skip.contains(name)) {
					name = UniqueString.next();
					name = basePackage + name + StringUtils.createLongString(StringEncryption.genKey(Config.strength));
				}
			remap.put(cn.name, name);
		}
		SimpleRemapper remapper = new SimpleRemapper(remap);
		for (ClassNode node : new ArrayList<>(classMap.values())) {
			ClassNode copy = new ClassNode();
			RemappingClassAdapter adapter = new RemappingClassAdapter(copy, remapper);
			node.accept(adapter);
			classMap.put(node.name, copy);
		}
		for (Object o : remap.entrySet()) {
			System.out.println("Class Remapping: " + o);
		}
		UniqueString.reset();
	}

}
