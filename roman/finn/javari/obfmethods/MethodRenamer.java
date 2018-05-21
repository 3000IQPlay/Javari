package roman.finn.javari.obfmethods;

import java.util.*;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import roman.finn.javari.Config;

/**
 * Created by Roman on 10.03.2017.
 */

public class MethodRenamer extends Transformer {
	
	private final String keepPackage = Config.keepPackage.replace(".", "/") + "/";
    public MethodRenamer(MethodNode _mn) {
        super(_mn, null);
    }

    FieldNode fn;

    public MethodRenamer(FieldNode _fn) {
        super(null, null);
        fn = _fn;
    }

    public MethodRenamer() { super(null, null); }

    @Override
    public void run() {
	
    }

    @Override
    public ArrayList<ClassNode> obfuscate(ArrayList<ClassNode> classes) {
    	for (int i = 0; i < classes.size(); i++) {
			ClassNode cn = classes.get(i);

			if(!cn.toString().startsWith(keepPackage)) {
						
				for (MethodNode mn : cn.methods) {				
						if (mn.access != Opcodes.ACC_ABSTRACT
								&& mn.access != Opcodes.ACC_ABSTRACT
										+ Opcodes.ACC_STATIC
								&& mn.access != Opcodes.ACC_ABSTRACT
										+ Opcodes.ACC_STATIC + Opcodes.ACC_PUBLIC
								&& mn.access != Opcodes.ACC_ABSTRACT
										+ Opcodes.ACC_STATIC + Opcodes.ACC_PRIVATE
								&& mn.access != Opcodes.ACC_ABSTRACT
										+ Opcodes.ACC_STATIC + Opcodes.ACC_PROTECTED
								&& mn.access != Opcodes.ACC_ABSTRACT
										+ Opcodes.ACC_PUBLIC
								&& mn.access != Opcodes.ACC_ABSTRACT
										+ Opcodes.ACC_PRIVATE
								&& mn.access != Opcodes.ACC_ABSTRACT
										+ Opcodes.ACC_PROTECTED) {
							
							if (!mn.desc.equals("([Ljava/lang/String;)V") && !mn.name.equals("main") && !mn.name.contains("<") && !mn.name.contains("yam") &&
									!mn.name.equals("<init>") &&
									!mn.name.equals("<clinit>") &&
									!mn.name.equals("actionPerformed") &&
									!mn.name.equals("paint") &&
									!mn.name.equals("mouseClicked") &&
									!mn.name.equals("mouseEntered") &&
									!mn.name.equals("mouseExited") &&
									!mn.name.equals("mousePressed") &&	
									!mn.name.equals("mouseReleased") &&	
									!mn.name.equals("mouseDragged") &&	
									!mn.name.equals("mouseMoved") &&
									!mn.name.equals("contains") &&	
									!mn.name.equals("toString") &&	
									!mn.name.equals("equals") &&	
									!mn.name.equals("clone") &&
									!mn.name.equals("start") &&
									!mn.name.equals("onEvent") &&
									!mn.name.equals("onDisable") &&
									!mn.name.equals("onEnable") &&
									!mn.name.equals("onToggle") &&
									!mn.name.equals("func_180510_a") &&
									!mn.name.equals("setFolderType") &&
									!mn.name.equals("load") &&
									!mn.name.equals("add") &&
									!mn.name.equals("toggle") &&
									!mn.name.equals("setup") &&
									!mn.name.equals("register") &&
									!mn.name.equals("get") &&
									!mn.name.equals("reset") &&
									!mn.name.equals("set") &&
									!mn.name.equals("options") &&
									!mn.name.equals("drawScreen") &&
									!mn.name.equals("initGui") &&
									!mn.name.equals("updateScreen") &&
									!mn.name.equals("keyTyped") &&
									!mn.name.equals("loadFromString") &&
									!mn.name.equals("run")) {
								
								String newName = StringEncryption.genKey(Config.strength);
								renameMethodNode(classes, cn.name, mn.name, mn.desc, null, newName, null);
							}
						}
			}

			classes.set(i, cn);
	}
}

	return classes;
  }
    
    public static void renameMethodNode(ArrayList<ClassNode> classes, String originalParentName, String originalMethodName, String originalMethodDesc, String newParent, String newName, String newDesc) {
    	for (int i = 0; i < classes.size(); i++) {
		ClassNode cn = classes.get(i);
			for (Object o : cn.methods.toArray()) {
				MethodNode m = (MethodNode) o;
				for (AbstractInsnNode i1 : m.instructions.toArray()) {
					if (i1 instanceof MethodInsnNode) {
						MethodInsnNode mi = (MethodInsnNode) i1;
						if (mi.owner.equals(originalParentName) && mi.name.equals(originalMethodName) && mi.desc.equals(originalMethodDesc)) {
							if (newName != null)
								mi.name = newName;
						}
					} else {
						// System.out.println(i.getOpcode()+":"+c.name+":"+m.name);
					}
				}

				if (m.signature != null) {
					if (newName != null)
						m.signature = m.signature.replace(originalMethodName, newName);
					if (newParent != null)
						m.signature = m.signature.replace(originalParentName, newParent);
				}

				if (m.name.equals(originalMethodName) && m.desc.equals(originalMethodDesc) && cn.name.equals(originalParentName)) {
					if (newName != null)
						m.name = newName;
					if (newDesc != null)
						m.desc = newDesc;
				}
			}
		}

	}

}
