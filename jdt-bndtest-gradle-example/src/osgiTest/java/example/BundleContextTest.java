package example;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.junit5.context.BundleContextExtension;

@ExtendWith(BundleContextExtension.class)
public class BundleContextTest {

    @InjectBundleContext
    BundleContext bundleContext;

    @Test
    public void testBundleContextNotNull() throws Exception {
        assertNotNull(bundleContext, "bundleContext is null. This test must be run inside an OSGi framework");
    }
}
