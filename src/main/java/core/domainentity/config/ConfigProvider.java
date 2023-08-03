package core.domainentity.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.logging.Level;

public class ConfigProvider {

    // slf4j logger definition
    private static final Logger SLF4J_LOGGER = LoggerFactory.getLogger(ConfigProvider.class);
    private static final java.util.logging.Logger JAVA_LOGGER = java.util.logging.Logger.getLogger(ConfigProvider.class.getName());

    // Constant used to indicate the base path where all config files must be created.
    public static final String CONFIG_BASE_PATH = "src/test/resources/config/";
    // Constant to hold the extension that config files must have
    public static final String CONFIG_FILE_EXTENSION = ".conf";
    // Constant to hold the name of the default config file
    public static final String DEFAULT_CONFIG_FILE_NAME = "default";
    // Constant to hold the path to the default config file
    public static final String DEFAULT_CONFIG_FILE_PATH =
            ConfigProvider.CONFIG_BASE_PATH + DEFAULT_CONFIG_FILE_NAME + CONFIG_FILE_EXTENSION;

    private static Config conf;
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    /*
     * Private constructor to prevent class being instantiated
     */
    private ConfigProvider() {
        // No instructions
    }

    /*
     * Creates a singleton instance of the type "com.typesafe.config.Config" by combining
     * two entities (both of type "com.typesafe.config.Config") and assigns the combined entity value
     * to the conf variable
     *
     * The entity received as parameter is combined with a default entity
     *
     * @Param entity : String : Name of the entity to be combined with the default entity
     *
     * @return : Cong : The config of type "com.typesafe.config.Config"
     * as a result of the entities merge.
     */
    public static synchronized Config config(final String entity) {

        synchronized (ConfigProvider.lock1) {
            if (ConfigProvider.conf != null) {
                return ConfigProvider.conf;
            }
        }

        Config defaultConfig = createConfigFile(DEFAULT_CONFIG_FILE_NAME);

        // Get desired config file path
        final String desiredConfigFileName =
                ConfigProvider.CONFIG_BASE_PATH + entity.toLowerCase(Locale.ENGLISH) + ConfigProvider.CONFIG_FILE_EXTENSION;

        // Log the file that is going to be loaded/used
        final String msg = String.format("Config file to be loaded: '%s'", desiredConfigFileName);
        SLF4J_LOGGER.info(msg);
        JAVA_LOGGER.log(Level.INFO, msg);


        // Create a Config variable of type "com.typesafe.config.Config" to hold the desired Config
        final Config desiredConfig = createConfigFile(entity);

        // Merge the default config file with the desired config file
        final Config combinedConfig = desiredConfig.withFallback(defaultConfig);

        // Extract the environment from the combinedConf to create a new config file
        final Config envConfig = combinedConfig.getObject(System.getProperty("env")).toConfig();

        // Merge the combinedConfig file with the environment config file and assign the value to 'conf' variable
        ConfigProvider.conf = ConfigFactory.load(combinedConfig.withFallback(envConfig).resolve());

        return ConfigProvider.conf;
    }

    private static Config joinDefaultConfigFiles(final String entity1, final String entity2) {
        // Create a Config variable of type "com.typesafe.config.Config"
        final Config entityConfig1 = createConfigFile(entity1);

        // Create a Config variable of type "com.typesafe.config.Config"
        final Config entityConfig2 = createConfigFile(entity2);

        // Merge both entities
        return entityConfig1.withFallback(entityConfig2);
    }

    private static Config joinDefaultConfigFiles(final Config entityConfig1, final String entity2) {
        // Create a Config variable of type "com.typesafe.config.Config"
        final Config entityConfig2 = createConfigFile(entity2);

        // Merge both entities
        return entityConfig1.withFallback(entityConfig2);
    }

    private static Config joinDefaultConfigFiles(final Config configEntity1, final Config configEntity2) {
        return configEntity1.withFallback(configEntity2);
    }

    private static Config createConfigFile(final String entity) {

        Path path = Path.of(ConfigProvider.CONFIG_BASE_PATH + entity + CONFIG_FILE_EXTENSION);
        if (Files.exists(path)) {
            // Create a variable of type "java.io.File" to hold the entity1
            final File entityFile1 = new File(ConfigProvider.CONFIG_BASE_PATH + entity + CONFIG_FILE_EXTENSION);

            // Parse file of type "java.io.File" to a Config variable of type "com.typesafe.config.Config"
            return ConfigFactory.parseFile(entityFile1);
        } else {
            System.out.println("Path for '" + path + "' does not exists.");
            return ConfigFactory.empty();
        }
    }

    /*
     * Used in combination with the getString() method [getConfig().getString(String key)],
     * returns the first element in a config file that matches with the key value
     *
     * @return: Config : The whole entity of type "com.typesafe.config.Config" created in
     * the config(final String entity) method
     */
    public static synchronized Config getConfig() {
        synchronized (ConfigProvider.lock2) {
            if (ConfigProvider.conf == null) {
                return null;
            }
            return ConfigProvider.conf;
        }
    }

    /*
     * Return a sub-config of type "com.typesafe.config.Config" from a main-config
     * of type "com.typesafe.config.Config"
     *
     * In other words, return one object of type "com.typesafe.config.Config"
     * that is contained in a config file of type "com.typesafe.config.Config"
     *
     * Used in combination with the getString() method [getConfig(final String subConfig).getString(String key)],
     * return the value of the key element within the subConfig
     *
     * @Param key : String : the name of the sub-config/object withing the
     * main-config
     *
     * @return : Config : The sub-config/object of type "com.typesafe.config.Config"
     */
    public static Config getConfig(final String subConfig) {
        if (subConfig == null || "".equals(subConfig)) {
            return ConfigProvider.conf;
        }
        return ConfigProvider.conf.getConfig(subConfig);
    }

    /*
     * Setter method for the conf variable
     */
    public static void setConf(final Config conf) {
        ConfigProvider.conf = ConfigFactory.load(conf);
    }
}
