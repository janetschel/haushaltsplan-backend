package de.janetschel.haushaltsplan.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(App.class);
        addInitHooks(springApplication);
        springApplication.run(args);
    }

    private static void addInitHooks(SpringApplication springApplication) {
        springApplication.addListeners((ApplicationListener<ApplicationEnvironmentPreparedEvent>) event -> {
            LOGGER.info(String.format("Running on java version %s", event.getEnvironment().getProperty("java.runtime.version")));
            LOGGER.info(String.format("Using authtoken: %s", event.getEnvironment().getProperty("authentication.user")));
        });
    }
}
