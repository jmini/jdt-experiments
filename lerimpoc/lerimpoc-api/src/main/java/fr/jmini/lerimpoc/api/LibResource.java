package fr.jmini.lerimpoc.api;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Class that represents a binary resource (i.e. a directory with .class files or .jar file) file and its (optional) corresponding sources (i.e. a directory with .java files or a .jar file).
 */
public class LibResource {

    private final File binaryFile;
    private final Optional<File> sourceFile;

    public LibResource(File binaryFile) {
        this.binaryFile = binaryFile;
        sourceFile = Optional.empty();
    }

    public LibResource(File binaryFile, File sourceFile) {
        this.binaryFile = binaryFile;
        this.sourceFile = Optional.of(sourceFile);
    }

    public File getBinaryFile() {
        return binaryFile;
    }

    public boolean hasSourceFile() {
        return sourceFile.isPresent();
    }

    public File getSourceFile() {
        return sourceFile.orElseThrow(() -> new NoSuchElementException("Source file is not defined"));
    }

    public File getSourceFileOrElse(File defaultValue) {
        return sourceFile.orElse(defaultValue);
    }

    @Override
    public String toString() {
        return "LibResource [binaryFile= " + binaryFile + ", sourceFile=" + (hasSourceFile() ? getSourceFile() : "<none>") + "]";
    }

}
