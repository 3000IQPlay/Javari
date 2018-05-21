package roman.finn.javari.obfmethods;

import java.util.ArrayList;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import roman.finn.javari.Config;

/*
In dieser Classe Teste ich eigene Modules, bevor ich sie dann zu richtigen Modules mache.
 */

public class Test extends Transformer {

	@SuppressWarnings("unused")
	private final String keepPackage = Config.keepPackage.replace(".", "/") + "/";

	public Test(MethodNode _mn) {
		super(_mn, null);
	}

	FieldNode fn;

	public Test(FieldNode _fn) {
		super(null, null);
		fn = _fn;
	}

	public Test() {
		super(null, null);
	}

	@Override
	public void run() {
	}

	@Override
	public ArrayList<ClassNode> obfuscate(ArrayList<ClassNode> classes) {
		for (int i = 0; i < classes.size(); i++) {
			ClassNode cn = classes.get(i);

			for (FieldNode fn : cn.fields) {
				new Test(fn).start();
			}

			classes.set(i, cn);
		}

		return classes;
	}

}
