package core.utils;

import com.typesafe.config.ConfigException;
import core.domainentity.config.ConfigProvider;
import core.domainentity.entity.EntityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.logging.Level;

public class TestContextProvider {

    // Set slf4j logger
    private static final Logger SLF4J_LOGGER = LoggerFactory.getLogger(TestContextProvider.class);
    private static final java.util.logging.Logger JAVA_LOGGER = java.util.logging.Logger.getLogger(TestContextProvider.class.getName());

    public static String getElementDefinedInTestContext(final String key) {
        try {
            return ConfigProvider.getConfig(EntityBuilder.getTestContext()).getString(key);
        } catch (ConfigException configException) {
            SLF4J_LOGGER.error(configException.getLocalizedMessage());
            JAVA_LOGGER.log(Level.SEVERE, configException.getLocalizedMessage());
            throw new RuntimeException(configException);
        }
    }

    public static List<String> getListOfElementsDefinedInTestContext(final String key) {
        try {
            return ConfigProvider.getConfig(EntityBuilder.getTestContext()).getStringList(key);
        } catch (ConfigException configException) {
            SLF4J_LOGGER.error(configException.getLocalizedMessage());
            JAVA_LOGGER.log(Level.SEVERE, configException.getLocalizedMessage());
            throw new RuntimeException(configException);
        }
    }

    public static String getElementDefinedInConfigFile(final String testContext, final String key) {
        try {
            return ConfigProvider.getConfig(testContext).getString(key);
        } catch (ConfigException configException) {
            SLF4J_LOGGER.error(configException.getLocalizedMessage());
            JAVA_LOGGER.log(Level.SEVERE, configException.getLocalizedMessage());
            throw new RuntimeException(configException);
        }
    }

    public static List<String> getListOfElementsDefinedInConfigFile(final String testContext, final String key) {
        try {
            return ConfigProvider.getConfig(testContext).getStringList(key);
        } catch (ConfigException configException) {
            SLF4J_LOGGER.error(configException.getLocalizedMessage());
            JAVA_LOGGER.log(Level.SEVERE, configException.getLocalizedMessage());
            throw new RuntimeException(configException);
        }
    }

}
