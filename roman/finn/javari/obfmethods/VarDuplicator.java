package roman.finn.javari.obfmethods;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import roman.finn.javari.utils.AccessHelper;

import java.util.ArrayList;

/**
 * Created by Roman on 26.02.2017.
 */
public class VarDuplicator extends Transformer {
    public VarDuplicator(MethodNode _mn) {
        super(_mn, null);
    }

    FieldNode fn;

    public VarDuplicator(FieldNode _fn) {
        super(null, null);
        fn = _fn;
    }

    public VarDuplicator() { super(null, null); }

    @Override
    public void run() {
        if (AccessHelper.isAbstract(mn.access)) {
            return;
        }
        for (AbstractInsnNode ain : mn.instructions.toArray()) {
            if (ain.getType() == AbstractInsnNode.VAR_INSN) {
                VarInsnNode vin = (VarInsnNode) ain;
                if (vin.getOpcode() == Opcodes.ASTORE) {
                    mn.instructions.insertBefore(vin, new InsnNode(Opcodes.DUP));
                    mn.instructions.insertBefore(vin, new InsnNode(Opcodes.ACONST_NULL));
                    mn.instructions.insertBefore(vin, new InsnNode(Opcodes.SWAP));
                    AbstractInsnNode next = vin.getNext();

                    mn.instructions.insertBefore(next, new InsnNode(Opcodes.POP));
                    mn.instructions.insertBefore(next, new VarInsnNode(Opcodes.ASTORE, vin.var));
                }
            }
        }
    }

    @Override
    public ArrayList<ClassNode> obfuscate(ArrayList<ClassNode> classes) {
        for (int i = 0; i < classes.size(); i++) {
            ClassNode cn = classes.get(i);

            for (MethodNode mn : cn.methods)
                new VarDuplicator(mn).start();

            classes.set(i, cn);
        }

        return classes;
    }

}