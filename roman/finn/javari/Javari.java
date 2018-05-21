package roman.finn.javari;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.CheckClassAdapter;

import roman.finn.javari.obfmethods.BadPop;
import roman.finn.javari.obfmethods.BadTryCatch;
import roman.finn.javari.obfmethods.ClassRemapper;
import roman.finn.javari.obfmethods.FieldRenamer;
import roman.finn.javari.obfmethods.FinalRemover;
import roman.finn.javari.obfmethods.LineNumberObfuscation;
import roman.finn.javari.obfmethods.LocalFieldRenamer;
import roman.finn.javari.obfmethods.MethodRenamer;
import roman.finn.javari.obfmethods.StringEncryption;
import roman.finn.javari.obfmethods.Transformer;
import roman.finn.javari.obfmethods.TrashCode;
import roman.finn.javari.obfmethods.VarDuplicator;
import roman.finn.javari.utils.CustomClassWriter;
import roman.finn.javari.utils.DirWalker;
import roman.finn.javari.utils.MyFile;
import roman.finn.javari.utils.NoClassInPathException;
import roman.finn.javari.utils.Obfuscator;

public class Javari extends Thread implements Opcodes {

	private static Javari instance;
	public File inF, outF;
	public ArrayList<ClassNode> classes = new ArrayList<ClassNode>();
	public ArrayList<MyFile> files = new ArrayList<MyFile>();

	public HashMap<String, ClassNode> hmSCN = new HashMap<String, ClassNode>();

	public HashMap<ClassNode, String> hmSCN2 = new HashMap<ClassNode, String>();
	public ArrayList<File> paths = new ArrayList<File>();
	public ArrayList<Transformer> transformers = new ArrayList<Transformer>();
	public HashMap<String, Integer> pkgLens = new HashMap<String, Integer>();
	public String[] args;

	public static void loadJavari(String[] args) throws Throwable {
		(instance = new Javari(args)).start();
	}

	public static Javari getInstance() {
		return instance;
	}

	private void checkArgs() throws Throwable {
		if (args.length < 4) {
			System.out.println("something is wrong");
			throw new Throwable();
		}

		for (int i = 0; i < args.length; i++)
			if (parseArg(args[i], i, args)) {
				checkArgs();
				break;
			}
	}

	public boolean parseArg(String arg, int index, String[] args) throws Throwable {
		File f;
		switch (index) {
		case 0:
			if (!(f = new File(arg)).exists())
				throw new Throwable("Jar to obfuscate not found :L");
			inF = f;

			break;
		case 1:
			if ((f = new File(arg)).exists()) {

			}
			outF = f;

			break;
		case 2:
			if (!(f = new File(arg)).exists() && !arg.equalsIgnoreCase("null"))
				throw new Throwable(
						".JAR/.class/folder with libraries must exists! (it can be empty folder or \'null\')");

			break;
		case 3:
			// Sowas wie ein ModuleManager xD
			if (Config.stringEncryption)
				transformers.add(new StringEncryption());
			if (Config.fieldRenamer)
				transformers.add(new FieldRenamer());
			if (Config.methodRenamer)
				transformers.add(new MethodRenamer());
			if (Config.localFieldRenamer)
				transformers.add(new LocalFieldRenamer());
			if (Config.varDuplicator)
				transformers.add(new VarDuplicator());
			if (Config.finalRemover)
				transformers.add(new FinalRemover());
			if (Config.badTryCatch)
				transformers.add(new BadTryCatch());
			if (Config.lineNumberObfuscation)
				transformers.add(new LineNumberObfuscation());
			// if(Config.classHider)
			// transformers.add(new ClassHider());
			if (Config.trashCode)
				transformers.add(new TrashCode());
			if (Config.badPop)
				transformers.add(new BadPop());

			break;
		default:
			System.out.println("Arg " + index + " is excess.");
			break;
		}

		return false;
	}

	public static boolean isEmpty(MethodNode mn) {
		return mn.instructions.getFirst() != null;
	}

	@Override
	public void run() {
		// SocketClient sc = new SocketClient();;
		// sc.sendServer("hat die Obfuscation gestartet");
		JavariInfo.printWatermark();
		if (Config.doClassRemapp == true) {
			try {
				Obfuscator obfuscator = new Obfuscator();
				obfuscator.supply(new JarFile(Config.in));
				obfuscator.apply(new ClassRemapper(Config.newPackageName, Config.keepMainClass));
				try (JarOutputStream out = new JarOutputStream(new FileOutputStream(Config.out + "-temp.jar"))) {
					obfuscator.write(out);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}

		try {
			try {
				checkArgs();
			} catch (Throwable t) {
				String msg = t.getMessage();
				if (msg != null)
					System.out.println(msg);

				return;
			}

			System.out.println("Loading java APIs...");
			new DirWalker(new File(System.getProperty("java.home") + File.separatorChar + "lib"),
					ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES, false);
			if (!args[2].equalsIgnoreCase("null")) {
				System.out.println("Loading user APIs...");
				new DirWalker(new File(args[2]),
						ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES, false);
			}
			System.out.println("Loading input file...");
			new DirWalker(inF, ClassReader.SKIP_FRAMES, true);
			System.out.println("All APIs loaded!");

			System.out.println("--------------------------------------------------");

			ArrayList<ClassNode> modClasses = classes;

			System.out.println("Starting Obfuscation.... ");

			System.out.println("--------------------------------------------------");

			for (Transformer transformer : transformers) {
				String name = transformer.getClass().getName();

				System.out.println("Started Obfuscation with " + name.substring(name.lastIndexOf('.') + 1));

				modClasses = transformer.obfuscate(modClasses);

				System.out.println("Obfuscation completed with " + name.substring(name.lastIndexOf('.') + 1));
			}

			System.out.println("--------------------------------------------------");
			System.out.println("Obfuscating all classes...  ");
			for (ClassNode cn : modClasses)
				dump(cn, true);
			System.out.println("--------------------------------------------------");
			System.out.println("All classes obfuscated! ");
			System.out.println("Saving all classes...");
			saveAll();
			// Die Temp jar löschen
			if (Config.doClassRemapp == true) {
				System.out.println("Deleting temp jar...");
				File file = new File(Config.out + "-temp.jar");
				file.delete();
			}
			System.out.println("Done!");
			// sc.sendServer("ist fertig mit der Obfuscation");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public Javari(String[] args) {
		this.args = args;
	}

	public int classesInPackageOfClass(String className) {
		return classesInPackage(getPackageByClass(className));
	}

	public int classesInPackage(String pkg) {
		if (pkgLens.containsKey(pkg))
			return pkgLens.get(pkg);

		pkg = pkg + ".";
		int len = 0;

		for (ClassNode cn : classes) {
			if (cn.name.startsWith(pkg)) {
				String className = cn.name.substring(pkg.length());

				if (className.indexOf('.') == -1)
					len++;
			}
		}

		pkgLens.put(pkg.substring(0, pkg.length() - 1), len);

		return len;
	}

	public static String getPackageByClass(String className) {
		return className.substring(0, className.lastIndexOf('.'));
	}

	public byte[] dump(ClassNode node, boolean autoAdd) {
		if (node.innerClasses != null) {
			node.innerClasses.stream().filter(in -> in.innerName != null).forEach(in -> {
				if (in.innerName.indexOf('/') != -1) {
					in.innerName = in.innerName.substring(in.innerName.lastIndexOf('/') + 1); // Stringer
				}
			});
		}
		ClassWriter writer = new CustomClassWriter(ClassWriter.COMPUTE_FRAMES);
		try {
			try {
				node.accept(writer);
			} catch (RuntimeException e) {
				if (e instanceof NoClassInPathException) {
					NoClassInPathException ex = (NoClassInPathException) e;
					System.out.println("Error: " + ex.getMessage() + " could not be found while writing " + node.name
							+ ". Using COMPUTE_MAXS");
					writer = new CustomClassWriter(ClassWriter.COMPUTE_MAXS);
					node.accept(writer);
				} else if (e.getMessage() != null) {
					if (e.getMessage().contains("JSR/RET")) {
						System.out.println("ClassNode contained JSR/RET so COMPUTE_MAXS instead");
						writer = new CustomClassWriter(ClassWriter.COMPUTE_MAXS);
						node.accept(writer);
					} else {
						throw e;
					}
				} else {
					throw e;
				}
			}
			byte[] classBytes = writer.toByteArray();

			ClassReader cr = new ClassReader(classBytes);
			try {
				cr.accept(new CheckClassAdapter(new ClassWriter(0)), 0);
			} catch (Throwable t) {
				//// System.out.println("Error: " + node.name + " failed
				//// verification");
				// t.printStackTrace(System.out);
			}

			if (autoAdd)
				files.add(new MyFile(node.name.replaceAll("\\.", "/") + ".class", classBytes));

			return classBytes;
		} catch (Throwable t) {
			System.out.println("Error while writing " + node.name);
			t.printStackTrace(System.out);
		}
		return null;
	}

	public void saveAll() throws Throwable {
		outF.createNewFile();
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outF));
		/* Start of combination of jar's */
		ZipFile zf = new ZipFile(inF);
		Enumeration<? extends ZipEntry> in = zf.entries();

		byte[] data;

		while (in.hasMoreElements()) {
			ZipEntry ze = in.nextElement();
			boolean finded = false;

			for (MyFile mc : files)
				if (mc != null && ze != null && mc.name != null && ze.getName() != null
						&& mc.name.equals(ze.getName())) {
					finded = true;
					break;
				}

			if (zf != null && ze != null && !finded) {
				DataInputStream dis = new DataInputStream(zf.getInputStream(ze));
				data = new byte[(int) ze.getSize()];
				dis.readFully(data);
				dis.close();

				ze = modifyEntry(new ZipEntry(ze.getName()));

				out.putNextEntry(ze);
				out.write(data, 0, data.length);
				out.closeEntry();
			}
		}
		zf.close();
		for (MyFile mc : files)
			try {
				data = mc.bytecode;
				ZipEntry ze = modifyEntry(new ZipEntry(mc.name));

				out.putNextEntry(ze);
				out.write(data, 0, data.length);
				out.closeEntry();
			} catch (Throwable t) {
			}

		out.setComment("Developer's: Roman & Finn");
		out.setLevel(9);
		out.close();
	}

	public static ZipEntry modifyEntry(ZipEntry ze) {
		final long time = 0;

		ze.setTime(time);
		ze = ze.setCreationTime(FileTime.fromMillis(time)).setLastAccessTime(FileTime.fromMillis(time))
				.setLastModifiedTime(FileTime.fromMillis(time));

		return ze;
	}
}
