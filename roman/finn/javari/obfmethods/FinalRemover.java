package roman.finn.javari.obfmethods;

import java.util.ArrayList;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

/**
 * Created by Roman on 26.02.2017.
 */

public class FinalRemover extends Transformer {
	public FinalRemover() {
		super(null, null);
	}

	@Override
	public ArrayList<ClassNode> obfuscate(ArrayList<ClassNode> classes) {
		for(int i = 0; i < classes.size(); i++) {
			ClassNode cn = classes.get(i);

			for(int i2 = 0; i2 < cn.fields.size(); i2++) {
				FieldNode fn = cn.fields.get(i2);

				if ((fn.access | ACC_FINAL) != 0)
					fn.access &= ~ACC_FINAL;

				cn.fields.set(i2, fn);
			}

			classes.set(i, cn);
		}

		return classes;
	}
}
