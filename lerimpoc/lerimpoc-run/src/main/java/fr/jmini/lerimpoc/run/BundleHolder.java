package fr.jmini.lerimpoc.run;

import java.nio.file.Path;

public class BundleHolder {
    private Path jarFile;
    private String bundleSymbolicName;
    private String bundleVersion;
    private String exportPackage;
    private String fragmentHost;

    public BundleHolder(Path jarFile, String bundleSymbolicName, String bundleVersion, String exportPackage, String fragmentHost) {
        this.jarFile = jarFile;
        this.bundleSymbolicName = bundleSymbolicName;
        this.bundleVersion = bundleVersion;
        this.exportPackage = exportPackage;
        this.fragmentHost = fragmentHost;
    }

    public Path getJarFile() {
        return jarFile;
    }

    public String getBundleSymbolicName() {
        return bundleSymbolicName;
    }

    public String getBundleVersion() {
        return bundleVersion;
    }

    public String getExportPackage() {
        return exportPackage;
    }

    public String getFragmentHost() {
        return fragmentHost;
    }

    @Override
    public String toString() {
        return "BundleHolder [jarFile=" + jarFile + ", bundleSymbolicName=" + bundleSymbolicName + ", bundleVersion=" + bundleVersion + ", exportPackage=" + exportPackage + ", fragmentHost=" + fragmentHost + "]";
    }
}