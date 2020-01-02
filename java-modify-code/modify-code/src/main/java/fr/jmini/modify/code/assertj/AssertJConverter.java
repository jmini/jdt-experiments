package fr.jmini.modify.code.assertj;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.BadLocationException;

import fr.jmini.modify.code.ICompilationUnitModifier;

public class AssertJConverter implements ICompilationUnitModifier {

    @Override
    public void modifyCompilationUnit(CompilationUnit astRoot, IProgressMonitor monitor) throws JavaModelException, CoreException, BadLocationException {
        // TODO Auto-generated method stub
    }

}
