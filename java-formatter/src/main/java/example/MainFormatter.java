/*******************************************************************************
 * Copyright (C) 2017 Jeremie Bresson
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package example;

import java.util.Properties;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

public class MainFormatter {

  public static void main(String[] args) {
    String result;

    String javaCode = "public class MyClass{ "
                        + "public static void main(String[] args) { "
                        + "System.out.println(\"Hello World\");"
                        + " }"
                        + " }";

    Properties prefs = new Properties();
    prefs.setProperty(JavaCore.COMPILER_SOURCE, CompilerOptions.VERSION_1_8);
    prefs.setProperty(JavaCore.COMPILER_COMPLIANCE, CompilerOptions.VERSION_1_8);
    prefs.setProperty(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, CompilerOptions.VERSION_1_8);

    CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(prefs);
    IDocument doc = new Document(javaCode);
    try {
      TextEdit edit = codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT | CodeFormatter.F_INCLUDE_COMMENTS,
                                             javaCode, 0, javaCode.length(), 0, null);
      if (edit != null) {
        edit.apply(doc);
        result = doc.get();
      }
      else {
        result = javaCode;
      }
    }
    catch (BadLocationException e) {
      throw new RuntimeException(e);
    }

    System.out.println(result);
  }
}