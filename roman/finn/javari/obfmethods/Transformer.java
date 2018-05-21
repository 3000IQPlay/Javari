package roman.finn.javari.obfmethods;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Created by Roman on 15.03.2017.
 */

public class Transformer extends Thread implements Opcodes {
	Random rand = ThreadLocalRandom.current();
	MethodNode mn;
	ClassNode parent;

	protected Transformer(MethodNode _mn, ClassNode _parent) {
		mn = _mn;
		parent = _parent;
	}

	public ArrayList<ClassNode> obfuscate(ArrayList<ClassNode> classes) throws Throwable {
		return classes;
	}
}
