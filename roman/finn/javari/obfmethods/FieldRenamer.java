package roman.finn.javari.obfmethods;

import java.util.*;

import org.objectweb.asm.tree.*;

import roman.finn.javari.Config;

/**
 * Created by Roman on 26.02.2017.
 */
public class FieldRenamer extends Transformer {
	
	private final String keepPackage = Config.keepPackage.replace(".", "/") + "/";
	public FieldRenamer(MethodNode _mn) {
		super(_mn, null);
	}

	FieldNode fn;

	public FieldRenamer(FieldNode _fn) {
		super(null, null);
		fn = _fn;
	}

	public FieldRenamer() {
		super(null, null);
	}

	@Override
	public void run() {

	}

	@Override
	public ArrayList<ClassNode> obfuscate(ArrayList<ClassNode> classes) {
		for (int i = 0; i < classes.size(); i++) {
			ClassNode cn = classes.get(i);

			if(!cn.toString().startsWith(keepPackage)) {
			for (FieldNode fn : cn.fields) {
				FieldNode f = (FieldNode) fn;
				String newName = StringEncryption.genKey(Config.strength);
				renameFieldNode(classes, cn.name, f.name, f.desc, null, newName, null);
				f.name = newName;
				
			}
		}
			
			classes.set(i, cn);
	}

		return classes;
	}

	public static void renameFieldNode(ArrayList<ClassNode> classes, String originalParentName, String originalFieldName, String originalFieldDesc,
			String newFieldParent, String newFieldName, String newFieldDesc) {
		for (int i = 0; i < classes.size(); i++) {
			ClassNode cn = classes.get(i);
				for (Object o : cn.methods.toArray()) {
					MethodNode m = (MethodNode) o;
					for (AbstractInsnNode i1 : m.instructions.toArray()) {
						if (i1 instanceof FieldInsnNode) {
							FieldInsnNode field = (FieldInsnNode) i1;

							if (field.owner.equals(originalParentName) && field.name.equals(originalFieldName)
									&& field.desc.equals(originalFieldDesc)) {
								if (newFieldParent != null)
									field.owner = newFieldParent;
								if (newFieldName != null)
									field.name = newFieldName;
								if (newFieldDesc != null)
									field.desc = newFieldDesc;
							}
						}
					}
				}
		}
	}

}
