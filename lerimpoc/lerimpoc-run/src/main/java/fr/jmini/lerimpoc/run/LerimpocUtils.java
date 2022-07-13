package fr.jmini.lerimpoc.run;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Optional;
import java.util.function.Function;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LerimpocUtils {
    private static final String MANIFEST_FILE_KEY = "META-INF/MANIFEST.MF";
    private static final Logger LOG = LoggerFactory.getLogger(LerimpocUtils.class);

    public static <T> Optional<T> readEntryInJar(Path file, String name, Function<InputStream, T> reader) {
        try (JarFile jar = new JarFile(file.toFile())) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName()
                        .equals(name)) {
                    return Optional.of(reader.apply(jar.getInputStream(entry)));
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error while reading the jar '" + file + "' to find entry '" + name + "'", ex);
        }
        LOG.info("Could not find entry '{}' in file '{}'", name, file);
        return Optional.empty();
    }

    public static Optional<BundleHolder> toBundleHolder(Path file) {
        return readEntryInJar(file, MANIFEST_FILE_KEY, is -> readManifest(file, is));
    }

    private static BundleHolder readManifest(Path file, InputStream is) {
        try {
            Manifest manifest = new Manifest(is);
            Attributes attributes = manifest.getMainAttributes();
            String bundleSymbolicName = attributes.getValue(Constants.BUNDLE_SYMBOLICNAME);
            if (bundleSymbolicName != null) {
                int i = bundleSymbolicName.indexOf(";");
                if (i > -1) {
                    bundleSymbolicName = bundleSymbolicName.substring(0, i);
                }
            }
            String bundleVersion = attributes.getValue(Constants.BUNDLE_VERSION);
            String exportPackage = attributes.getValue(Constants.EXPORT_PACKAGE);
            String fragmentHost = attributes.getValue(Constants.FRAGMENT_HOST);
            return new BundleHolder(file, bundleSymbolicName, bundleVersion, exportPackage, fragmentHost);
        } catch (IOException ex) {
            throw new RuntimeException("Error while reading the MANIFEST.MF entry in " + file, ex);
        }
    }

    private LerimpocUtils() {
    }
}
