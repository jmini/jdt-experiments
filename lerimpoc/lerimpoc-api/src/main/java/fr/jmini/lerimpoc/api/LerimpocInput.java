package fr.jmini.lerimpoc.api;

import java.nio.file.Path;

public class LerimpocInput {

    private String projectName;
    private Path outputDir;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Path getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(Path outputDir) {
        this.outputDir = outputDir;
    }

    @Override
    public String toString() {
        return "LerimpocInput [projectName=" + projectName + ", outputDir=" + outputDir + "]";
    }
}
