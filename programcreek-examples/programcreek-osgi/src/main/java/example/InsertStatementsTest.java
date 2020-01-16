package example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * Insert/Add Statements to Java Source Code by using Eclipse JDT ASTRewrite
 * @see https://www.programcreek.com/2012/06/insertadd-statements-to-java-source-code-by-using-eclipse-jdt-astrewrite/
 *
 */
public class InsertStatementsTest {

	private final Bundle bundle = FrameworkUtil.getBundle(this.getClass());

	@Test
	public void test() throws Exception {
		assertNotNull("This test must be run inside an OSGi framework", bundle);

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("testAddStatements");
		if(!project.exists()) {
			project.create(null);
		}
		project.open(null);
		 
		//set the Java nature
		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		 
		//create the project
		project.setDescription(description, null);
		IJavaProject javaProject = JavaCore.create(project);
		
		//set the build path
		IClasspathEntry[] buildPath = {
				JavaCore.newSourceEntry(project.getFullPath().append("src"))
		};
		 
		javaProject.setRawClasspath(buildPath, project.getFullPath().append("bin"), null);
		 
		//create folder by using resources package
		IFolder folder = project.getFolder("src");
		if(!folder.exists()) {
			folder.create(true, true, null);
		}
		 
		//Add folder to Java element
		IPackageFragmentRoot srcFolder = javaProject.getPackageFragmentRoot(folder);
		 
		//create package fragment
		IPackageFragment package1 = srcFolder.createPackageFragment("org.example", true, null);

		// compilation unit
		String str = ""
				+ "package org.example;\n"
				+ "public class Main {\n"
				+ "	public static void main(String[] args) {\n"
				+ "	}\n"
				+ "	public static void add() {\n"
				+ "		int i = 1;\n"
				+ "		System.out.println(i);\n"
				+ "	}\n"
				+"}";
		ICompilationUnit unit = package1.createCompilationUnit("Main.java", str, true, null);
		
		// parse compilation unit
		CompilationUnit astRoot = parse(unit);

		// create a ASTRewrite
		AST ast = astRoot.getAST();
		ASTRewrite rewriter = ASTRewrite.create(ast);

		// for getting insertion position
		TypeDeclaration typeDecl = (TypeDeclaration) astRoot.types().get(0);
		MethodDeclaration methodDecl = typeDecl.getMethods()[0];
		Block block = methodDecl.getBody();

		// create new statements for insertion
		MethodInvocation newInvocation = ast.newMethodInvocation();
		newInvocation.setName(ast.newSimpleName("add"));
		Statement newStatement = ast.newExpressionStatement(newInvocation);

		// create ListRewrite
		ListRewrite listRewrite = rewriter.getListRewrite(block, Block.STATEMENTS_PROPERTY);
		listRewrite.insertFirst(newStatement, null);

		TextEdit edits = rewriter.rewriteAST();

		// apply the text edits to the compilation unit
		Document document = new Document(unit.getSource());

		edits.apply(document);

		// this is the code for adding statements
		unit.getBuffer().setContents(document.get());
		
		String expected = ""
				+ "package org.example;\n"
				+ "public class Main {\n"
				+ "	public static void main(String[] args) {\n"
				+ "		add();\n"
				+ "	}\n"
				+ "	public static void add() {\n"
				+ "		int i = 1;\n"
				+ "		System.out.println(i);\n"
				+ "	}\n"
				+"}";
		assertEquals(expected, document.get());
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
}
