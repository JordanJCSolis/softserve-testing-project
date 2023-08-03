package core.domainentity.entity;

import com.typesafe.config.Config;
import core.domainentity.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;

public class EntityBuilder {

    // Set slf4j logger
    private static final Logger SLF4j_LOGGER = LoggerFactory.getLogger(EntityBuilder.class);
    private static final java.util.logging.Logger JAVA_LOGGER = java.util.logging.Logger.getLogger(EntityBuilder.class.getName());
    //
    private static String testContext = null;

    static {
        final String configFile = System.getProperty("config.file");

        if (configFile == null || "".equals(configFile)) {
            String errorMsg = "Config file cannot be null or empty";
            SLF4j_LOGGER.error(errorMsg);
            JAVA_LOGGER.log(Level.SEVERE, errorMsg);
            throw new IllegalArgumentException();
        } else {
            final String msg = String.format("Config file: %s", configFile);
            SLF4j_LOGGER.info(msg);
            JAVA_LOGGER.log(Level.INFO, msg);

            // Load config
            ConfigProvider.config(configFile);
        }
    }

    // Constructor
    private EntityBuilder() {
        // private constructor
    }

    /*
     * Build entity with default configuration using entity provided in
     * Maven test params
     *
     * @return an instance of entity
     */
    public static Entity buildEntity() {
        setTestContext(null);
        return new Entity();
    }

    /*
     * Build entity with default configuration using entity provided in
     * Maven test params. Configuration specified to the context is merged
     * with default configuration
     *
     * @Param testContext: specific tag for account or profile or test case type
     *
     * @return an instance of entity
     */
    public static Entity buildEntity(String testContext) {
        setTestContext(testContext);

        if (testContext == null || "".equals(testContext)) {
            String errorMsg = "Test context cannot be NULL or empty";
            SLF4j_LOGGER.error(errorMsg);
            JAVA_LOGGER.log(Level.SEVERE, errorMsg);
            throw new IllegalArgumentException();
        } else {
            // Checks whether a value is present and non-null at the given path.
            if (ConfigProvider.getConfig().hasPath(testContext)) {
                final Config testContextConf = ConfigProvider.getConfig().getConfig(testContext);
                ConfigProvider.setConf(ConfigProvider.getConfig().withFallback(testContextConf).resolve());
            } else {
                final String errorMsg = String.format("Config for test context '%s' NOT FOUND", testContext);
                SLF4j_LOGGER.error(errorMsg);
                JAVA_LOGGER.log(Level.SEVERE, errorMsg);
                throw new IllegalArgumentException();
            }
        }
        return new Entity();
    }

    /*
     * @return
     * @param
     */
    public static String getTestContext() {
        return EntityBuilder.testContext;
    }

    /*
     * @Param: testContext: set testContext
     */
    public static void setTestContext(final String testContext) {
        EntityBuilder.testContext = testContext;
    }
}
