package core.domainentity.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;

public class Entity {

    // Set slf4j logger
    private static final Logger SLF4J_LOGGER = LoggerFactory.getLogger(Entity.class);
    private static final java.util.logging.Logger JAVA_LOGGER = java.util.logging.Logger.getLogger(Entity.class.getName());

    // Browser name
    public static final String BROWSER = System.getProperty("browser");

    static {
        final String msg = "Browser for Web Application Under Test (WAUT) : " + BROWSER;
        SLF4J_LOGGER.info(msg);
        JAVA_LOGGER.log(Level.INFO, msg);
    }

    /*
     * Initialize entity with no values
     */
    public Entity() {
        String msg = "Inside entity...";
        SLF4J_LOGGER.info(msg);
        JAVA_LOGGER.log(Level.INFO, msg);
    }
}
