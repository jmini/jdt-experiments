package fr.jmini.lerimpoc.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import fr.jmini.lerimpoc.api.LerimpocInput;
import fr.jmini.lerimpoc.api.LerimpocService;

@Component(service = LerimpocService.class)
public class LerimpocComponent implements LerimpocService {

    private BundleContext bundleContext;

    @Activate
    public void activate(BundleContext bc) {
        this.bundleContext = bc;
    }

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
        StringBuilder sb = new StringBuilder();
        sb.append("== " + input.getProjectName() + "\n");
        if (bundleContext == null) {
            sb.append("!! Bundle context is null !!");
        } else {
            for (Bundle b : bundleContext.getBundles()) {
                sb.append(b.getSymbolicName() + "\n");
            }
        }
        String content = sb.toString();
        Files.write(file, content.getBytes(StandardCharsets.UTF_8));
    }

}
