package fr.jmini.lerimpoc.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.osgi.launch.EquinoxFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jmini.lerimpoc.api.LerimpocInput;
import fr.jmini.lerimpoc.api.LerimpocService;

public class LerimpocRunner {
    public static final String LERIMPOC_IMPL_BUNDLE_ID = "lerimpoc-impl";

    private static final Logger LOG = LoggerFactory.getLogger(LerimpocRunner.class);

    private Path workingDir;
    private Set<BundleHolder> bundles;
    private LerimpocInput input;

    public LerimpocRunner(Path workingDir, Set<BundleHolder> bundles, LerimpocInput input) {
        this.workingDir = workingDir;
        this.bundles = bundles;
        this.input = input;
    }

    public void runEquinox() throws BundleException, InterruptedException {
        Path lerimpocDir = workingDir
                .resolve("lerimpoc_" + System.currentTimeMillis());
        String workspaceDir = lerimpocDir.resolve("workspace")
                .toString();
        String storageDir = lerimpocDir.resolve("configuration")
                .toString();

        String exportPackage = findLerimpocApiExportPackage();

        Map<String, String> config = new HashMap<>();
        config.put("osgi.instance.area", workspaceDir);
        config.put(Constants.FRAMEWORK_STORAGE, storageDir);
        config.put(Constants.FRAMEWORK_STORAGE_CLEAN, Constants.FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT);
        config.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, exportPackage);

        FrameworkFactory frameworkFactory = new EquinoxFactory();
        Framework framework = frameworkFactory.newFramework(config);
        try {
            framework.init();
            framework.start();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Available bundles: [{}]\n", bundles.stream()
                        .map(BundleHolder::getBundleSymbolicName)
                        .collect(java.util.stream.Collectors.joining(", ")));
            }

            BundleHolder implBundle = findBundle(LERIMPOC_IMPL_BUNDLE_ID);
            List<String> bundleList = LerimpocUtils.readEntryInJar(implBundle.getJarFile(), "start-osgi.txt", LerimpocRunner::toLines)
                    .orElseThrow(() -> new IllegalStateException("Not able to find resource 'start-osgi.txt' in 'lerimpoc-impl' bundle"));

            BundleContext bundleContext = framework.getBundleContext();
            for (String symbolicName : bundleList) {
                startBundle(bundleContext, findBundle(symbolicName));
            }
            printState(bundleContext);

            ServiceReference<LerimpocService> serviceReference = bundleContext.getServiceReference(LerimpocService.class);
            LerimpocService service = bundleContext.getService(serviceReference);
            service.run(input);

        } finally {
            framework.stop();
            framework.waitForStop(0);
        }
    }

    private static List<String> toLines(InputStream in) {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(in))) {
            return r.lines()
                    .map(String::trim)
                    .filter(s -> !s.startsWith("//"))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Could not read lines in 'start-osgi.txt'", e);
        }
    }

    private static void printState(BundleContext bundleContext) {
        LOG.debug("--- print state:");
        for (Bundle b : bundleContext.getBundles()) {
            printBundleState(b);
        }
    }

    private static void printBundleState(Bundle bundle) {
        LOG.debug("({}) {}: {} [{}]", bundle.getBundleId(), bundle.getSymbolicName(), bundle.getLocation(), toState(bundle.getState()));
    }

    private static String toState(int state) {
        switch (state) {
        case Bundle.UNINSTALLED:
            return "UNINSTALLED";
        case Bundle.INSTALLED:
            return "INSTALLED";
        case Bundle.RESOLVED:
            return "RESOLVED";
        case Bundle.STARTING:
            return "STARTING";
        case Bundle.STOPPING:
            return "STOPPING";
        case Bundle.ACTIVE:
            return "ACTIVE";
        default:
            return null;
        }
    }

    private String findLerimpocApiExportPackage() {
        BundleHolder apiBundle = findBundle("lerimpoc-api");
        return apiBundle.getExportPackage();
    }

    private void startBundle(BundleContext bundleContext, BundleHolder bundleHolder) throws BundleException {
        URI file = bundleHolder.getJarFile()
                .toUri();
        LOG.debug("Starting bundle: {}", file);
        Bundle bundle = bundleContext.installBundle(file.toString());
        if (bundleHolder.getFragmentHost() == null) {
            bundle.start(Bundle.START_ACTIVATION_POLICY);
        }
    }

    private BundleHolder findBundle(String symbolicName) {
        return findBundle(bundles, symbolicName);
    }

    public static BundleHolder findBundle(Set<BundleHolder> allBundles, String symbolicName) {
        return allBundles.stream()
                .filter(b -> Objects.equals(symbolicName, b.getBundleSymbolicName()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Could not find bundle with symbolic name '" + symbolicName + "' in bundle holder list"));
    }
}
