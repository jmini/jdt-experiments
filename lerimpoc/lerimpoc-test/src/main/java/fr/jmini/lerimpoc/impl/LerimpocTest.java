package fr.jmini.lerimpoc.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.junit5.service.ServiceExtension;

import fr.jmini.lerimpoc.api.LerimpocInput;
import fr.jmini.lerimpoc.api.LerimpocService;

@ExtendWith(ServiceExtension.class)
public class LerimpocTest {

    @InjectService
    LerimpocService service;

    @Test
    public void test() throws Exception {
        assertThat(service)
                .as("service is null. This test must be run inside an OSGi framework")
                .isNotNull();

        Path outputDir = Files.createTempDirectory("output");

        LerimpocInput input = new LerimpocInput();
        input.setProjectName("Example project");
        input.setOutputDir(outputDir);

        service.run(input);

        assertThat(outputDir).isDirectory();
        assertThat(outputDir.resolve("output.txt")).hasContent("== Example project\n"
                + "org.eclipse.osgi\n"
                + "junit-jupiter-api\n"
                + "junit-jupiter-engine\n"
                + "junit-platform-commons\n"
                + "junit-platform-engine\n"
                + "junit-platform-launcher\n"
                + "lerimpoc-api\n"
                + "lerimpoc-impl\n"
                + "lerimpoc-test\n"
                + "org.apache.felix.scr\n"
                + "org.assertj.core\n"
                + "org.opentest4j\n"
                + "org.osgi.test.common\n"
                + "org.osgi.test.junit5\n"
                + "org.osgi.util.function\n"
                + "org.osgi.util.promise\n"
                + "biz.aQute.tester.junit-platform\n"
                + "");

    }
}
