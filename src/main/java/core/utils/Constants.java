package core.utils;

public class Constants {

    /*
     *
     */
    public static final String WINDOWS_OS = "windows";
    public static final String MAC_OS = "mac";
    public static final String DRIVER_PATH = "src/test/resources/web-drivers/";
    public static final String SCREENSHOTS_DIRECTORY = "target/screenshots/";
    public static String BROWSER_NAME;
    public static String BROWSER_VERSION;

    /*
     * Extent report properties
     */
    public static final String TIME_STAMP_FORMAT = "yyyy-MM-dd-'T'-HH-mm-ss-'Z'";
    public static final String REPORT_BASE_PATH = "src/test/resources/reports/";

    // Extent-config.xml file
    public static final String EXTENT_CONFIG_SOURCE = "src/main/resources/extent-report-properties/";
    public static final String EXTENT_CONFIG_DESTINATION = "src/test/resources/extent-report-properties/";
    public static final String EXTENT_CONFIG_FILE_NAME = "extent-config.xml";
    public static final String EXTENT_CONFIG_SOURCE_FILE_PATH = EXTENT_CONFIG_SOURCE + EXTENT_CONFIG_FILE_NAME;
    public static final String EXTENT_CONFIG_FILE_PATH = EXTENT_CONFIG_DESTINATION + EXTENT_CONFIG_FILE_NAME;

    //extent.properties files
    public static final String EXTENT_PROPERTIES_DESTINATION = "src/test/resources/extent-report-properties/";
    public static final String EXTENT_PROPERTIES_FILE_NAME = "extent.properties";
    public static final String EXTENT_PROPERTIES_FILE_PATH = EXTENT_PROPERTIES_DESTINATION + EXTENT_PROPERTIES_FILE_NAME;

    /*
     * System Properties
     */
    public final static String PROJECT_PATH = System.getProperty("user.dir");
    public final static String OS = System.getProperty("os.name");
    public final static String USER_NAME = System.getProperty("user.name");
    public static final String BROWSER = System.getProperty("browser");
    public static final String ENV = System.getProperty("env");
    public static final String CONFIG_FILE = System.getProperty("config.file");
    public static final String TEST_REPORT = System.getProperty("preferred.report");
    public static final String SCREENSHOT_PADDING = System.getProperty("screenshot.padding");
}
