package roman.finn.javari.obfmethods;

import java.util.ArrayList;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Created by Roman on 22.02.2017.
 */
public class TrashCode extends Transformer {
    public TrashCode(MethodNode _mn) {
        super(_mn, null);
    }

    FieldNode fn;

    public TrashCode(FieldNode _fn) {
        super(null, null);
        fn = _fn;
    }

    public TrashCode() { super(null, null); }

    @Override
    public void run() {
        if(fn == null && mn == null)
            return;

        if(mn == null) {
            if((fn.access & ACC_DEPRECATED) == 0)
                fn.access |= ACC_DEPRECATED;
        } else {
            if((mn.access & ACC_DEPRECATED) == 0)
                mn.access |= ACC_DEPRECATED;
        }
    }

    @Override
    public ArrayList<ClassNode> obfuscate(ArrayList<ClassNode> classes) {
        for (int i = 0; i < classes.size(); i++) {
            ClassNode cn = classes.get(i);

            for (MethodNode mn : cn.methods)
                new TrashCode(mn).start();
            for (FieldNode fn : cn.fields)
                new TrashCode(fn).start();

            classes.set(i, cn);
        }

        return classes;
    }
}
