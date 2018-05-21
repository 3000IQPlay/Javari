package roman.finn.javari.utils;

import org.objectweb.asm.tree.*;

import java.util.*;

public interface Processor {

	public void process(Map<String, ClassNode> classMap);

}
