package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.junit.jupiter.api.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class GetJavaProjectTest {

	private final Bundle bundle = FrameworkUtil.getBundle(this.getClass());

	@Test
	public void test() throws Exception {
		assertNotNull(bundle, "This test must be run inside an OSGi framework");

		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("test-project");
		if (!project.exists()) {
			project.create(null);
		}
		project.open(null);

		// set the Java nature
		final IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });

		// create the project
		project.setDescription(description, null);
		final IJavaProject javaProject = JavaCore.create(project);
		javaProject.setOption(CompilerOptions.OPTION_Source, "1.8");
		javaProject.setOption(CompilerOptions.OPTION_Compliance, "1.8");

		// set the build path
		final IClasspathEntry[] buildPath = { JavaCore.newSourceEntry(project.getFullPath().append("src")) };

		javaProject.setRawClasspath(buildPath, project.getFullPath().append("bin"), null);

		// create folder by using resources package
		final IFolder folder = project.getFolder("src");
		if (!folder.exists()) {
			folder.create(true, true, null);
		}

		// Add folder to Java element
		final IPackageFragmentRoot srcFolder = javaProject.getPackageFragmentRoot(folder);

		// create package fragment
		final IPackageFragment package1 = srcFolder.createPackageFragment("p", true, null);

		// compilation unit
		final String str1 = ""
				+ "package p;\n"
				+ "\n"
				+ "public class OuterClass {\n"
				+ "	public enum InnerEnum {\n"
				+ "		A\n"
				+ "	}\n"
				+ "}\n"
				+ "";
		final ICompilationUnit unit1 = package1.createCompilationUnit("OuterClass.java", str1, true, null);

		final String str2 = ""
				+ "package p;\n"
				+ "\n"
				+ "import p.OuterClass.InnerEnum;\n"
				+ "\n"
				+ "public class Snippet {\n"
				+ "	public static OuterClass.InnerEnum doSomething1() {\n"
				+ "		return null;\n"
				+ "	}\n"
				+ "\n"
				+ "	public static InnerEnum doSomething2() {\n"
				+ "		return null;\n"
				+ "	}\n"
				+ "}";
		final ICompilationUnit unit2 = package1.createCompilationUnit("Snippet.java", str2, true, null);

		// parse compilation unit
		final CompilationUnit astRoot = parse(unit2);

		final ASTVisitor v = new Visitor();
		astRoot.accept(v);
	}

	/**
	 * Reads a ICompilationUnit and creates the AST DOM for manipulating the Java
	 * source file
	 *
	 * @param unit
	 * @return
	 */
	private static CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}

	public static class Visitor extends ASTVisitor {
		@Override
		public boolean visit(final MethodDeclaration node) {
			assertNotNull(node, "node (MethodDeclaration)");

			final IMethodBinding methodBinding = node.resolveBinding();

			assertNotNull(methodBinding, "methodBinding");
			assertNotNull(methodBinding.getDeclaringClass(), "methodBinding > DeclaringClass");
			assertNotNull(methodBinding.getJavaElement(), "methodBinding > JavaElement");

			return true;
		}
	}
}
