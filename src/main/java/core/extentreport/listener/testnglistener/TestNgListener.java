package core.extentreport.listener.testnglistener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.logging.Level;

public class TestNgListener implements ITestListener {

    // Set java and slf4j logger
    private static final Logger SLF4J_LOGGER = LoggerFactory.getLogger(TestNgListener.class);
    private static final java.util.logging.Logger JAVA_LOGGER = java.util.logging.Logger.getLogger(TestNgListener.class.getName());

    @Override
    public void onStart(ITestContext context) {
        // No instructions
    }

    @Override
    public void onFinish(ITestContext context) {
        // No instructions
    }

    @Override
    public void onTestStart(ITestResult result) {
        // No instructions
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // No instructions
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // No instructions
    }

    @Override
    public void onTestFailure(ITestResult result) {
        SLF4J_LOGGER.error(result.getThrowable().toString());
        JAVA_LOGGER.log(Level.SEVERE, result.getThrowable().toString());
    }
}
