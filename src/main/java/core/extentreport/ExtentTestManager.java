package core.extentreport;

import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {

    /*
     * private constructor
     */
    private ExtentTestManager() {
        // No data
    }

    // Assign author
    public static void assignAuthor(ExtentTest test) {
        test.assignAuthor(System.getProperty("user.name"));
    }

    // Assign device
    public static void assignDevice(ExtentTest test) {
        test.assignDevice(System.getProperty("os.name"));
    }

    //
    public static void assignCategory(ExtentTest test, String category) {
        test.assignCategory(category);
    }
}
