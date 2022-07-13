package fr.jmini.lerimpoc.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.osgi.service.component.annotations.Component;

import fr.jmini.lerimpoc.api.LerimpocInput;
import fr.jmini.lerimpoc.api.LerimpocService;

@Component(service = LerimpocService.class)
public class LerimpocComponent implements LerimpocService {

    @Override
    public void run(LerimpocInput input) {
        try {
            createOutputFile(input);
        } catch (Exception e) {
            throw new RuntimeException("Error while creating output file", e);
        }
    }

    private void createOutputFile(LerimpocInput input) throws IOException {
        Path file = input.getOutputDir()
                .resolve("output.txt");
        String content = "== " + input.getProjectName() + "\n";
        Files.write(file, content.getBytes(StandardCharsets.UTF_8));
    }

}
