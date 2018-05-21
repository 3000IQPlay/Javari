package roman.finn.javari.obfmethods;

import java.util.ArrayList;
import java.util.HashMap;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

import roman.finn.javari.Config;

/**
 * Created by Roman on 26.02.2017.
 */

public class LocalFieldRenamer extends Transformer {
	HashMap<String, Boolean> hm = new HashMap<String, Boolean>();

	public LocalFieldRenamer(MethodNode _mn) {
		super(_mn, null);
	}

	public LocalFieldRenamer() {
		super(null, null);
	}

	@Override
	public void run() {
		if (mn.localVariables == null)
			return;
		for (int i = 0; i < mn.localVariables.size(); i++) {
			LocalVariableNode lvn = mn.localVariables.get(i);

			mn.localVariables.set(i, new LocalVariableNode(StringEncryption.genKey(Config.strength), lvn.desc, null,
					lvn.start, lvn.end, i));
			hm.put(lvn.desc, Boolean.TRUE);
		}
	}

	@Override
	public ArrayList<ClassNode> obfuscate(ArrayList<ClassNode> classes) {
		for (int i = 0; i < classes.size(); i++) {
			ClassNode cn = classes.get(i);

			for (MethodNode mn : cn.methods)
				new LocalFieldRenamer(mn).start();

			classes.set(i, cn);
		}

		return classes;
	}
}
