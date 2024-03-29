package fr.jmini.lerimpoc.run;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import fr.jmini.lerimpoc.api.LerimpocInput;

class LerimpocRunnerTest {

    @Test
    void test() throws Exception {
        Set<BundleHolder> bundles = readResource("/osgi.txt").stream()
                .peek(f -> assertThat(f).isRegularFile())
                .map(LerimpocUtils::toBundleHolder)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toSet());

        Path buildDir = Files.createTempDirectory("test");
        Path outputDir = Files.createTempDirectory("output");

        LerimpocInput input = new LerimpocInput();
        input.setProjectName("Test project");
        input.setOutputDir(outputDir);

        new LerimpocRunner(buildDir, bundles, input).runEquinox();

        assertThat(outputDir).isDirectory();
        assertThat(outputDir.resolve("output.txt")).hasContent("== Test project\n"
                + "org.eclipse.osgi\n"
                + "org.osgi.util.function\n"
                + "org.osgi.util.promise\n"
                + "org.apache.felix.scr\n"
                + "lerimpoc-impl\n"
                + "");
    }

    private static Set<Path> readResource(String name) throws IOException {
        try (InputStream resource = LerimpocRunnerTest.class.getResourceAsStream(name)) {
            return new BufferedReader(new InputStreamReader(resource,
                    StandardCharsets.UTF_8)).lines()
                            .map(Paths::get)
                            .collect(Collectors.toSet());
        }
    }
}
