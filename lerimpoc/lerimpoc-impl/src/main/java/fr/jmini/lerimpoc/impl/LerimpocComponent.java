package fr.jmini.lerimpoc.impl;

import org.osgi.service.component.annotations.Component;

import fr.jmini.lerimpoc.api.LerimpocInput;
import fr.jmini.lerimpoc.api.LerimpocService;

@Component(service = LerimpocService.class)
public class LerimpocComponent implements LerimpocService {

    @Override
    public void run(LerimpocInput input) {
    }

}
