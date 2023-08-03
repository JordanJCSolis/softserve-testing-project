package core.extentreport;

import com.aventstack.extentreports.MediaEntityBuilder;
import core.extentreport.listener.cucumberlistener.CucumberListener;
import core.utils.Constants;
import core.utils.Params;
import core.web.driverfactory.WebDriverProvider;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ScreenShotProvider {


    enum LOCATION {
        TOP,
        LEFT,
        RIGHT,
        BOTTOM
    }

    private static By header = null;
    private static By footer = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenShotProvider.class);
    private static final int SCROLL_TIMEOUT_MILLI_SECS = 500;
    private static int screenshotCount = 0;

    /*
     * Private constructor to prevent class being instantiated
     */
    private ScreenShotProvider() {
        // No data
    }

    /*
     * Returns the WebDriver session used during the execution
     *
     * @return : WebDriver
     */
    private static WebDriver getDriver() {
        return WebDriverProvider.getDriver(Constants.BROWSER).getWebDriver();
    }

    /*
     * Capture screenshot and attach it to the extent report.
     *
     * public method that can be called in any UI project.
     */
    public static void takeScreenshot() {
        attachScreenshotAsBase64();
    }

    /*
     * Attach a screenshot in base64 format to extent report.
     */
    private static void attachScreenshotAsBase64() {
        Params.extentTestScenario
                .info(MediaEntityBuilder.createScreenCaptureFromBase64String(captureScreenshotAsBase64())
                        .build());
    }

    /*
     * Attach a screenshot in file Format to extent report.
     */
    private static void attachScreenshotAsFile() {
        Params.extentTestScenario
                .info(MediaEntityBuilder.createScreenCaptureFromPath("http://localhost:63342/" + captureScreenshotAsFile()).build());
    }

    /*
     * Capture Screenshot in base 64 format and saves a copy of it in "target/screenshot/" directory
     * as File format
     *
     * @returns : String : screenshot taken in base64 format
     */
    private static String captureScreenshotAsBase64() {
        TakesScreenshot ts = (TakesScreenshot) getDriver();
        String screenshot = ts.getScreenshotAs(OutputType.BASE64);
        byte[] decodedScreenshot = Base64.getDecoder().decode(screenshot.getBytes(StandardCharsets.UTF_8));
        try {
            FileUtils.writeByteArrayToFile(new File(setScreenshotName("png")), decodedScreenshot);
        } catch (IOException e) {
            ScreenShotProvider.LOGGER.error("Error converting byte stream into File");
            ScreenShotProvider.LOGGER.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return screenshot;
    }

    /*
     * Capture Screenshot in base File format and saves a copy of it in "target/screenshot/" directory
     * as File format
     *
     * @returns : String : the path for the current screenshot taken
     */
    private static String captureScreenshotAsFile() {
        TakesScreenshot ts = (TakesScreenshot) getDriver();
        File screenshot = ts.getScreenshotAs(OutputType.FILE);
        File destination = new File(setScreenshotName("jpeg"));
        try {
            FileUtils.copyFile(screenshot, destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchSessionException e) {
            throw new NoSuchSessionException(e.toString());
        }
        return destination.getPath();
    }

    /*
     * Capture Screenshot in base byre steam format and saves a copy of it in "target/screenshot/" directory
     * as File format
     *
     * @returns : String : the path for the current screenshot taken
     */
    private static String captureScreenshotAsBytes() {
        TakesScreenshot ts = (TakesScreenshot) getDriver();
        byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
        File destination = new File(setScreenshotName("jpeg"));
        try {
            FileUtils.writeByteArrayToFile(destination, screenshot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return destination.getPath();
    }

    /*
     * Generates the path for the current screenshot following next pattern:
     *
     * target/screenshots/{featureName]/{scenarioName}/{cucumberKeyword}-{cucumberStep}_screenshot{screenshotCount}.imageExtension
     *
     * @params : String : extension for the image
     *
     * @return : String : The path for the current screenshot
     */
    private static String setScreenshotName(String extension) {

        return Constants.SCREENSHOTS_DIRECTORY
                .concat(normalizeGherkinSteps(ExtentReportPath.featureReportName())).concat("/")
                .concat(normalizeGherkinSteps(CucumberListener.getCucumberFeatureName())).concat("/")
                .concat(normalizeGherkinSteps(CucumberListener.getCucumberScenarioName())).concat("/")
                .concat(CucumberListener.getCucumberKeyword()).concat("-")
                .concat(normalizeGherkinSteps(CucumberListener.getCucumberStep())).concat("_")
                .concat("screenshot").concat("_" + screenshotCount++)
                .concat(".").concat(extension)
                .replaceAll("\"", "").replaceAll("\\s+", "-");
    }

    /*
     * Captures a screenshot of the full page or one specific element and embeds it into the report
     *
     * @param : WebElement element : WebElement that needs to be captured. If NULL, then the whole page will be captured.
     *
     */
    public static void takeFullScreenshot(WebElement element) {
        WebDriver driver = ScreenShotProvider.getDriver();

        // Remove unwanted elements from reporting (header and footer)
        JavascriptExecutor js = null;
        if (driver instanceof JavascriptExecutor) {
            js = (JavascriptExecutor) driver;
            try {
                // Remove header if element is defined (not null)
                if (header != null) {
                    final WebElement pageHeader = driver.findElement(ScreenShotProvider.header);
                    js.executeScript("arguments[0].parentNode.removeChild(arguments[0])", pageHeader);
                }

                // Remove footer if element is defined (not null)
                if (footer != null) {
                    final WebElement pageFooter = driver.findElement(ScreenShotProvider.footer);
                    js.executeScript("arguments[0].parentNode.removeChild(arguments[0])", pageFooter);
                }
            } catch (NoSuchElementException noSuchElementException) {
                final String WARNING_MSG = "ERROR CROPPING HEADER AND FOOTER ELEMENTS IN SCREENSHOT";
                ScreenShotProvider.LOGGER.warn(WARNING_MSG, noSuchElementException.getLocalizedMessage());
            }
        }

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            if (element == null) {
                ImageIO.write(new AShot().shootingStrategy(ShootingStrategies.viewportPasting
                                (ScreenShotProvider.SCROLL_TIMEOUT_MILLI_SECS)).takeScreenshot(driver)
                        .getImage(), "PNG", baos);
            } else {
                ImageIO.write(takeScreenshotOverScrollableElement(element), "PNG", baos);
            }
            baos.flush();
            final byte[] imageInByte = baos.toByteArray();

            // Save file
            File destination = new File(setScreenshotName("png"));
            try {
                FileUtils.writeByteArrayToFile(destination, imageInByte);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String image = Base64.getEncoder().encodeToString(imageInByte);
            Params.extentTestScenario
                    .info(MediaEntityBuilder.createScreenCaptureFromBase64String(image)
                            .build());
            baos.close();
        } catch (IOException ioException) {
            ScreenShotProvider.LOGGER.error("Error while adding screenshot.\n", ioException);
        } catch (Exception exception) {
            ScreenShotProvider.LOGGER.error("Error converting image into byte stream.\n", exception);
        }
    }


    /*
     * Captures a screenshot of a WebElement which can be scrollable or non-scrollable.
     *
     * @params: WebElement element: WebElement to capture in screenshot.
     *
     */
    private static RenderedImage takeScreenshotOverScrollableElement(WebElement element) throws IOException {
        // Define WebDriver and JavaScript Executor
        WebDriver driver = getDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Get Window width and height.
        int windowWidth = driver.manage().window().getSize().width;
        int windowHeight = driver.manage().window().getSize().height;

        // Variables used to hold the element width and height
        int elementHeight = 0;
        int elementWidth = 0;
        // Variables used to set the coordinates of the element (A(m,n) and B(p,q))
        int elementXi = 0;
        int elementXf = 0;
        int elementYi = 0;
        int elementYf = 0;
        // WebElement to get the parent node of the current WebElement
        WebElement parentNode = null;
        // Variables used to hold the parentNode width and height
        int parentWidth = 0;
        int parentHeight = 0;
        // Variables used to set the coordinates of the parentNode (C(r,s) and D(t,u))
        int parentXi = 0;
        int parentXf = 0;
        int parentYi = 0;
        int parentYf = 0;

        if (Constants.SCREENSHOT_PADDING.equalsIgnoreCase("enable")) {
            // Get element width and height
            elementHeight = element.getSize().getHeight();
            elementWidth = element.getSize().getWidth();

            // Get element coordinates
            elementXi = element.getLocation().x;
            elementXf = elementXi + elementWidth;
            elementYi = element.getLocation().y;
            elementYf = elementYi + elementHeight;

            // Get element parent node
            parentNode = (elementXf > windowWidth || elementYf > windowHeight) ?
                    (WebElement) js.executeScript("return arguments[0].parentNode;", element)
                    : element;

            // Get parent node width and height
            parentWidth = parentNode.getRect().width;
            parentHeight = parentNode.getRect().height;

            // Parent parent coordinates (Visible area)
            parentXi = parentNode.getLocation().x;
            parentXf = parentNode.getLocation().x + parentWidth;
            parentYi = parentNode.getLocation().y;
            parentYf = parentNode.getLocation().y + parentHeight;

        } else {
            // Get element width and height
            elementHeight = getCssAttributeAsInteger(element, "height");
            elementWidth = getCssAttributeAsInteger(element, "width");

            // Get element coordinates (Without considering padding, border and margin)
            elementXi = element.getLocation().x + getElementPaddingBorderMargin(element, LOCATION.LEFT);
            elementXf = elementXi + elementWidth;
            elementYi = element.getLocation().y + getElementPaddingBorderMargin(element, LOCATION.TOP);
            elementYf = elementYi + elementHeight;

            // Get element parent node
            parentNode = (elementXf > windowWidth || elementYf > windowHeight) ?
                    (WebElement) js.executeScript("return arguments[0].parentNode;", element)
                    : element;

            // Get parent node width and height
            parentWidth = getCssAttributeAsInteger(parentNode, "width");
            parentHeight = getCssAttributeAsInteger(parentNode, "height");

            // Parent parent coordinates (Visible area)
            parentXi = parentNode.getLocation().x + getElementPaddingBorderMargin(parentNode, LOCATION.LEFT);
            parentXf = parentXi + parentWidth;
            parentYi = parentNode.getLocation().y + getElementPaddingBorderMargin(element, LOCATION.TOP);
            parentYf = parentYi + parentHeight;
        }

        parentYf = Math.min(elementYf, parentYf);

        // Create list of type "java.awt.image.BufferedImage" to hold every cropped image
        List<BufferedImage> shots = new ArrayList<>();

        // Constants used to set a limit in X and Y so system will create a "square" as delimit area to capture the screenshots.
        // This helps to avoid capturing "constant" that are hold across the scrollable elements such as
        // table headers or frozen first columns.
        final int INCREMENT_X_AXIS = 100;
        final int REDUCTION_X_AXIS = 100;
        final int INCREMENT_Y_AXIS = 50;
        final int REDUCTION_Y_AXIS = 50;

        // Variables to set the max and min width and height to capture the screenshot
        final int MAX_SCREENSHOT_WIDTH = 500;
        final int MAX_SCREENSHOT_HEIGHT = 500;
        final int MIN_SCREENSHOT_WIDTH = 50;
        final int MIN_SCREENSHOT_HEIGHT = 50;

        // Set the screenshot size for Y axis
        int auxScreenshotHeight = (parentHeight >= (INCREMENT_Y_AXIS + REDUCTION_Y_AXIS + MIN_SCREENSHOT_HEIGHT)) ?
                (parentHeight - (INCREMENT_Y_AXIS + REDUCTION_Y_AXIS)) : (parentHeight);
        final int SCREENSHOT_HEIGHT = Math.min(auxScreenshotHeight, MAX_SCREENSHOT_HEIGHT);

        // Set the screenshot size for X axis
        int auxScreenshotWidth = (parentWidth >= (INCREMENT_X_AXIS + REDUCTION_X_AXIS + MIN_SCREENSHOT_WIDTH)) ?
                (parentWidth - (INCREMENT_X_AXIS + REDUCTION_X_AXIS)) : (parentWidth);
        final int SCREENSHOT_WIDTH = Math.min(auxScreenshotWidth, MAX_SCREENSHOT_WIDTH);


        // Define "maxVisibleScreenX" and "maxVisibleScreenX".
        // maxVisibleScreen works as a limit for capturing the screenshot so system avoids capturing "constants"
        // multiple times when scrolling an element.
        //By reducing the size of the parent in (parentXf -REDUCTION_X_AXIS) and (parentYf - REDUCTION_Y_AXIS)
        // systems avoids those constants and just capture them one time at the end.
        int maxVisibleScreenX = (parentWidth >= (INCREMENT_X_AXIS + REDUCTION_X_AXIS + SCREENSHOT_WIDTH)) ?
                (parentXi + parentWidth) - REDUCTION_X_AXIS
                : ((parentXi + parentWidth));
        int maxVisibleScreenY = (parentHeight >= (INCREMENT_Y_AXIS + REDUCTION_Y_AXIS + SCREENSHOT_HEIGHT)) ?
                (parentYi + parentHeight) - REDUCTION_Y_AXIS
                : (parentYi + parentHeight);

        // Not needed since to compute for getting the SCREENSHOT_HEIGHT and SCREENSHOT_WIDTH considers these values
        int minVisibleScreenX = parentXi + INCREMENT_X_AXIS;
        int minVisibleScreenY = parentYi + INCREMENT_Y_AXIS;


        // Variable to hold the maximum scroll is permissible in Y axis
        int maxVerticalScroll = Math.max(elementHeight - parentHeight, 0);

        // Variable to record the height that is pending to capture
        int pendingElementHeightToCapture = elementHeight;

        // Initiate screenshot height
        int currentScreenShotHeight = SCREENSHOT_HEIGHT;

        // Initial and final coordinates of Y axis
        int Yi = elementYi;
        int Yf = 0;

        // Variable used to scroll over Y axis
        int scrollHeight = 0;

        // Number of times X and Y axis where loop
        int repetitionsY = 0;
        int repetitionsX = 0;


        while (maxVerticalScroll > 0 || pendingElementHeightToCapture > 0) {
            // Factor to scroll vertically
            int factorY = currentScreenShotHeight - (maxVisibleScreenY - Yf);
            scrollHeight = (factorY > 0) ? (scrollHeight += factorY) : (scrollHeight += 0);
            maxVerticalScroll -= scrollHeight;
            js.executeScript("arguments[0].scrollTop=" + scrollHeight, parentNode);

            // Get Yi and Yf
            if (pendingElementHeightToCapture < currentScreenShotHeight) {
                currentScreenShotHeight = pendingElementHeightToCapture;
                Yi = parentYf - currentScreenShotHeight;
            } else {
                if (repetitionsY != 0) {
                    Yi = (Yf == maxVisibleScreenY) ? (Yf - currentScreenShotHeight) : (Yf - scrollHeight);
                }
            }
            Yf = Yi + currentScreenShotHeight;

            // Initial and final coordinates of X axis
            int Xi = elementXi;
            int Xf = 0;
            // Variable to record the width that is pending to capture
            int pendingElementWidthToCapture = elementXf;
            // Variable to hold the maximum scroll is permissible in X axis
            int maxHorizontalScroll = Math.max(elementWidth - parentWidth, 0);
            // Initiate screenshot width
            int currentScreenshotWidth = SCREENSHOT_WIDTH;
            // Variable used to scroll over X axis
            int scrollWidth = 0;
            // Restart variable to 0
            repetitionsX = 0;

            while (maxHorizontalScroll > 0 || pendingElementWidthToCapture > 0) {
                // factor to scroll horizontally
                int factorX = currentScreenshotWidth - (maxVisibleScreenX - Xf);
                scrollWidth = (factorX > 0) ? (scrollWidth += factorX) : (scrollWidth += 0);
                maxHorizontalScroll -= scrollWidth;
                js.executeScript("arguments[0].scrollLeft=" + scrollWidth, parentNode);

                // Get Xi and Xf
                if (pendingElementWidthToCapture < currentScreenshotWidth) {
                    currentScreenshotWidth = pendingElementWidthToCapture;
                    Xi = parentXf - currentScreenshotWidth;
                } else {
                    if (repetitionsX != 0) {
                        Xi = (Xf == maxVisibleScreenX) ? (Xf - currentScreenshotWidth) : (Xf - scrollWidth);
                    }
                }
                Xf = Xi + currentScreenshotWidth;

                // Capture full screenshot
                File fileScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                // Convert screenshot captured to a Buffered image
                BufferedImage fullImage = ImageIO.read(fileScreenshot);
                // Crop image
                BufferedImage croppedImage = fullImage.getSubimage(
                        Xi, Yi, currentScreenshotWidth, currentScreenShotHeight);
                // Add cropped image to List
                shots.add(croppedImage);

                pendingElementWidthToCapture -= currentScreenshotWidth;
                repetitionsX++;
            }

            js.executeScript("arguments[0].scrollLeft=0", parentNode);
            pendingElementHeightToCapture -= currentScreenShotHeight;
            repetitionsY++;
        }

        BufferedImage joinedImage = null;
        Graphics g = null;

        int x = 0, y = 0, aux = 1;
        for (BufferedImage image : shots) {
            if (joinedImage == null) {
                joinedImage = new BufferedImage(elementWidth, elementHeight, BufferedImage.TYPE_INT_RGB);
                g = joinedImage.getGraphics();
            }

            g.drawImage(image, x, y, null);

            if (aux == repetitionsX) {
                x = 0;
                y += image.getHeight();
                aux = 0;
            } else {
                x += image.getWidth();
            }
            aux++;
        }

        return joinedImage;
    }

    /*
     * Get an element CSS attribute and return it as a Float
     *
     * @param: WebElement element : element where to extract CSS value
     * @param: String cssAttribute: CSS attribute to extract from element
     *
     * @return : Float : Value in float data type
     */
    private static Float getCssAttributeAsFloat(WebElement element, String cssAttribute) {
        return Float.parseFloat(element.getCssValue(cssAttribute).replaceAll("[^\\d.]", ""));
    }

    /*
     * Get an element CSS attribute and return it as Integer
     *
     * @param: WebElement element : element where to extract CSS value
     * @param: String cssAttribute: CSS attribute to extract from element
     *
     * @return : Float : Value in int data type
     */
    private static Integer getCssAttributeAsInteger(WebElement element, String cssAttribute) {
        return (int) Math.floor(getCssAttributeAsFloat(element, cssAttribute));
    }

    /*
     * Get the sum of the margin, border and padding and return the value
     *
     * @param : WebElement element : Element where to extract the box model
     * @param : Location location : Side of the box model where to extract the box model (top, bottom, left or right)
     *
     * @return : Integer : sum of the sides of the box model
     */
    private static Integer getElementPaddingBorderMargin(WebElement element, LOCATION position) {
        int elementPadding = getCssAttributeAsInteger(element, "padding-" + position.toString().toLowerCase());
        int elementBorder = getCssAttributeAsInteger(element, "border-" + position.toString().toLowerCase() + "-width");
        int elementMargin = getCssAttributeAsInteger(element, "margin-" + position.toString().toLowerCase());

        return elementPadding + elementBorder + elementMargin;
    }

    /*
     * Normalize the Gherkin instruction received by removing all the '.html', '.feature' and '/' characters.
     *
     * @params : String gherkinInstruction : Gherkin instruction to normalize (feature, scenario, step, etc).
     *
     * @return : String : returns a normalized string.
     */
    private static String normalizeGherkinSteps(String gherkinInstruction) {
        return gherkinInstruction
                .replace(".html", "")
                .replace(".feature", "")
                .replace("/", " ");
    }
}
