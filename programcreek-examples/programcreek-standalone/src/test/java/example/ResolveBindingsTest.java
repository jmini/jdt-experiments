package example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.junit.Test;


/**
 * How to Resolve Bindings When Using Eclipse JDT ASTParser?
 * @see https://www.programcreek.com/2014/01/how-to-resolve-bindings-when-using-eclipse-jdt-astparser/
 *
 */
public class ResolveBindingsTest {

	@Test
	public void test() throws Exception {
		String mavenHome = System.getProperty("user.home") + "/.m2";
		assertTrue("The mavenHome folder is not found", Files.isDirectory(Paths.get(mavenHome)));
		String jarPath = mavenHome + "/repository/junit/junit/4.12/junit-4.12.jar";
		assertTrue("The junit file is not present where expected", Files.isRegularFile(Paths.get(jarPath)));

		String str = ""
				+ "import org.junit.Assert;\n"
				+ "import org.junit.Test;\n"
				+ "\n"
				+ "public class ATest {"
				+ "	@Test\n"
				+ "	public void test() throws Exception {\n"
				+ "		Assert.assertTrue(1 == 1);"
				+ "}\n";
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
 
		parser.setBindingsRecovery(true);
 
		Map<String, String> options = JavaCore.getOptions();
		parser.setCompilerOptions(options);
 
		String unitName = "ATest.java";
		parser.setUnitName(unitName);
 
		String[] sources = { }; 
		String[] classpath = {jarPath};
 
		parser.setEnvironment(classpath, sources, new String[] {}, true);
		parser.setSource(str.toCharArray());
 
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		
		assertTrue("Binding activated.", cu.getAST().hasBindingsRecovery());
 
		Set<String> names = new HashSet<>();

		cu.accept(new ASTVisitor() {

			@Override
			public boolean visit(MethodInvocation node) {
				SimpleName name = node.getName();
				System.out.println("> node: " + node);
				if ("assertTrue".contains(name.getIdentifier())) {
					IMethodBinding methodBinding = node.resolveMethodBinding();
					System.out.println("> methodBinding: " + methodBinding);

					String declaringClassQualifiedName = methodBinding.getDeclaringClass().getQualifiedName();
					names.add(declaringClassQualifiedName);
				}
				return true;
			}
		});

		assertEquals(1, names.size());
		assertTrue(names.contains("org.junit.Assert"));
	}
}
