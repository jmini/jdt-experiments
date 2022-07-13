package fr.jmini.lerimpoc.impl;

import static org.assertj.core.api.Assertions.assertThat;

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

        LerimpocInput input = new LerimpocInput();
        input.setProjectName("Example project");

        service.run(input);
    }
}