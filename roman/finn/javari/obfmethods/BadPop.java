package roman.finn.javari.obfmethods;

import java.util.ArrayList;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import roman.finn.javari.Config;
import roman.finn.javari.JavariInfo;
import roman.finn.javari.utils.AccessHelper;

/**
 * Created by Roman on 26.02.2017.
 */
public class BadPop extends Transformer {
	public static final String s = getMassive();
	private final String keepPackage = Config.keepPackage.replace(".", "/") + "/";

	public BadPop(MethodNode _mn) {
		super(_mn, null);
	}

	FieldNode fn;

	public BadPop(FieldNode _fn) {
		super(null, null);
		fn = _fn;
	}

	public BadPop() {
		super(null, null);
	}

	@Override
	public void run() {
		if (AccessHelper.isAbstract(mn.access)) {
			return;
		}
		for (@SuppressWarnings("unused")
		AbstractInsnNode ain : mn.instructions.toArray()) {
			if (mn.name.contains("<") || AccessHelper.isAbstract(mn.access)) {
				return;
			}
			for (int i = 0; i < 3; i++) {
				mn.instructions.insert(new InsnNode(Opcodes.POP2));
				mn.instructions.insert(new LdcInsnNode("PROTECTION MADE BY " + JavariInfo.getDeveloperRoman() + " AND "
						+ JavariInfo.getDeveloperFinn()));
				mn.instructions.insert(new InsnNode(Opcodes.POP));
				mn.instructions.insert(new InsnNode(Opcodes.SWAP));
				mn.instructions.insert(new InsnNode(Opcodes.POP));
				mn.instructions.insert(new LdcInsnNode(s));
				mn.instructions.insert(new LdcInsnNode(s));
				mn.instructions.insert(new LdcInsnNode("Protected by " + JavariInfo.getTitle() + " Obfuscator"));
			}
		}
	}

	@Override
	public ArrayList<ClassNode> obfuscate(ArrayList<ClassNode> classes) {
		for (int i = 0; i < classes.size(); i++) {
			ClassNode cn = classes.get(i);

			if (!cn.toString().startsWith(keepPackage)) {
				for (MethodNode mn : cn.methods)
					new BadPop(mn).start();
			}
			classes.set(i, cn);
		}

		return classes;
	}

	private static String getMassive() {
		StringBuffer sb = new StringBuffer();
		while (sb.length() < 65536 - 1) {
			sb.append(" ");
		}
		return sb.toString();
	}
}
