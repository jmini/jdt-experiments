package fr.jmini.lerimpoc.api;

public class LerimpocInput {

    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "LerimpocInput [projectName=" + projectName + "]";
    }
}
