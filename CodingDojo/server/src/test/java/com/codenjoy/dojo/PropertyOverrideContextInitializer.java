package com.codenjoy.dojo;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;

import java.util.Random;

public class PropertyOverrideContextInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        setup(context, "messages", "messages.db");
        setup(context, "log", "logs.db");
        setup(context, "payment", "payment.db");
        setup(context, "saves", "saves.db");
        setup(context, "users", "users.db");
        setup(context, "settings", "settings.db");

//            TestPropertySourceUtils.addPropertiesFilesToEnvironment(
//                    context, "context-override-application.properties");
    }

    public void setup(ConfigurableApplicationContext context, String db, String file) {
        String dbFile = "target/" + file + new Random().nextInt();
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                context, "database.files." + db + "=" + dbFile);
    }
}
