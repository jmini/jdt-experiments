package fr.jmini.lerimpoc.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.junit5.context.BundleContextExtension;

@ExtendWith(BundleContextExtension.class)
public class BundleContextTest {

    @InjectBundleContext
    BundleContext bundleContext;

    @Test
    public void test() throws Exception {
        assertThat(bundleContext)
                .as("bundleContext is null. This test must be run inside an OSGi framework")
                .isNotNull();

        assertThat(bundleContext.getBundles())
                .hasSize(17)
                .extracting(Bundle::getSymbolicName)
                .containsExactly("org.eclipse.osgi",
                        "junit-jupiter-api",
                        "junit-jupiter-engine",
                        "junit-platform-commons",
                        "junit-platform-engine",
                        "junit-platform-launcher",
                        "lerimpoc-api",
                        "lerimpoc-impl",
                        "lerimpoc-test",
                        "org.apache.felix.scr",
                        "org.assertj.core",
                        "org.opentest4j",
                        "org.osgi.test.common",
                        "org.osgi.test.junit5",
                        "org.osgi.util.function",
                        "org.osgi.util.promise",
                        "biz.aQute.tester.junit-platform");
    }
}
