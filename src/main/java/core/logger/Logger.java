package core.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;

public class Logger {

    public static void initateLog4JLogger() {
        org.apache.logging.log4j.core.LoggerContext context = (LoggerContext) LogManager.getContext(false);
        File file = new File("src/main/resources/log4j-properties/log4j2.xml");

        context.setConfigLocation(file.toURI());
    }

}
