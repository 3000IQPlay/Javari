package roman.finn.javari.obfmethods;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import roman.finn.javari.utils.AccessHelper;

import java.util.ArrayList;

/**
 * Created by Roman on 28.02.2017.
 */
public class BadTryCatch extends Transformer {
	public BadTryCatch(MethodNode _mn) {
		super(_mn, null);
	}

	FieldNode fn;

	public BadTryCatch(FieldNode _fn) {
		super(null, null);
		fn = _fn;
	}

	public BadTryCatch() {
		super(null, null);
	}

	@Override
	public void run() {
		addTryCatch(mn, "java/lang/Exception", null);
	}

	@Override
	public ArrayList<ClassNode> obfuscate(ArrayList<ClassNode> classes) {
		for (int i = 0; i < classes.size(); i++) {
			ClassNode cn = classes.get(i);

			for (MethodNode mn : cn.methods)
				new BadTryCatch(mn).start();

			classes.set(i, cn);
		}

		return classes;
	}

	public static void addTryCatch(MethodNode mn, String catchType, String handleType) {
		if (mn.name.startsWith("<") || AccessHelper.isAbstract(mn.access)) {
			return;
		}
		LabelNode start = new LabelNode();
		LabelNode handler = new LabelNode();
		LabelNode end = new LabelNode();
		if (mn.localVariables == null) {
			mn.localVariables = new ArrayList<LocalVariableNode>(5);
		}
		int index = mn.localVariables.size();
		mn.instructions.insert(start);
		mn.instructions.add(end);
		mn.instructions.add(handler);
		mn.instructions.add(new InsnNode(Opcodes.ACONST_NULL));
		mn.instructions.add(new InsnNode(Opcodes.ATHROW));

		LocalVariableNode exVar = new LocalVariableNode("excptn", "L" + catchType + ";", null, start, handler, index);
		TryCatchBlockNode tryBlock = new TryCatchBlockNode(start, end, handler,
				handleType == null ? null : ("L" + handleType + ";"));
		mn.localVariables.add(exVar);
		mn.tryCatchBlocks.add(tryBlock);
		mn.exceptions.add(catchType);
	}

}
