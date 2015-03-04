package framework.utils.listeners;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import framework.automation.driver.web.WebBrowser;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.cropper.indent.BlurFilter;
import ru.yandex.qatools.ashot.cropper.indent.IndentCropper;
import framework.automation.utils.js.JSExecution;

/**
 * Utility class for different driver's listeners
 * @author Taras.Lytvyn
 *
 */
public class AutomationDriverListenerUtil {

	/**
	 * attaches web page element detailed description to report
	 * @param description with tag and text and absolute xpath
	 * @param driver	driver
	 * @param element	element
	 * @return	element's description
	 */
	@Attachment(value = "Element action was performed \"{0}\" description: ")
	static String getElementDescription(String description, WebDriver driver, WebElement element) {
		return "Element performed action was: " + description + "\n"
				/*+ "Element absolute xpath was: "
				+ getElementDescriptorXPATH(driver, element) + "\n" */
				+ "Element tag name: " + element.getTagName() + "\n"
				+ "Element text: " + "'" + element.getText() + "'";
	}

	/**
	 * attaches	native mobile element detailed description
	 * @param description	description
	 * @param driver	driver
	 * @param element	element
	 * @return	element's description
	 */
	@Attachment(value = "Element action was performed \"{0}\" description: ")
	static String getNativeMobileElementDescription(String description, WebDriver driver,
			WebElement element) {
		return "Element performed action was: " + description + "\n"
				+ "Element tag name: " + element.getTagName() + "\n"
				+ "Element text: " + "'" + element.getText() + "'";
	}

	/**
	 * attaches separate web element screenshot image
	 * @param driver	driver
	 * @param element	element
	 * @param screenShotStepFilter	screenshot step filter (redline or blur)
	 * @return	byte array of element's screenshot image
	 */
	@Attachment(value = "A step screenshot with highlighted element: ")
	static byte[] takeWebElementScreenshotImage(WebDriver driver,
			WebElement element, String screenShotStepFilter) {
		return TestListenerUtil
				.getByteArrayFromImage(
						takeWebElementScreenshot(driver, element,
								screenShotStepFilter), "png");
	}

	/**
	 * takes webbrowser screenshot after test step
	 * @param driver	driver
	 * @return	byte array of image
	 */
	@Attachment(value = "After step screenshot: ")
	static byte[] takeWebBrowserScreenshotImageAfterStep(WebDriver driver) {
		return TestListenerUtil.getByteArrayFromImage(
				takeWebBrowserScreenshot(driver), "png");
	}

	/**
	 * gets the script that was executed (just for allure attachment)
	 * @param script	script	
	 * @return	executed script
	 */

	@Attachment(value = "Script that was executed: ")
	static String getScriptExecuted(String script) {
		return script;
	}

	/**
	 * gets Exception description (just for allure attachment)
	 * @param throwable	exception
	 * @return	message of exception
	 */
	@Attachment(value = "Exception description: ")
	static String getExceptionDescription(Throwable throwable) {
		return throwable.getMessage();
	}

	/**
	 * gets the element xpath (full)
	 * @param driver	driver
	 * @param element	element
	 * @return	text of element's direct full xpath
	 */
	/*
	private static String getElementDescriptorXPATH(WebDriver driver,
			WebElement element) {
		JSExecution jsExecutor = new JSExecution(driver);
		return (String) jsExecutor.execScript("gPt = function(c) {"
				+ "if(c.id!=='') {" + "return'id(\"'+c.id+'\")' }"
				+ "if(c===document.body) {" + "return c.tagName }"
				+ "var a=0; var e=c.parentNode.childNodes;"
				+ "for (var b=0; b < e.length; b++) {" + "var d=e[b];"
				+ "if(d===c) { "
				+ "return gPt(c.parentNode)+ '/' + c.tagName + '['+(a+1)+']' }"
				+ "if(d.nodeType===1&&d.tagName===c.tagName){" + "a++ } } };"
				+ "return gPt(arguments[0]).toLowerCase();", element);
	}*/

	/**
	 * get's webdriver element simple description from element's toString()
	 * @param element	element
	 * @return	elemen't simple description
	 */
	static String getElementString(WebElement element) {
		return element.toString().substring(
				element.toString().indexOf("->") + 3,
				element.toString().length() - 1);
	}

	/**
	 * takes browser screenshot
	 * @param driver	driver
	 * @return	buffered image
	 */
	static BufferedImage takeWebBrowserScreenshot(WebDriver driver) {
		try {
			Thread.sleep(250);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Screenshot screenImage;
        AShot ashot = new AShot().coordsProvider(new WebDriverCoordsProvider());
        if (isRetinaScreen()) {
            screenImage = ashot.dpr(2).takeScreenshot(driver);
            return screenImage.getImage();
        }

        screenImage = ashot.takeScreenshot(driver);

		return screenImage.getImage();
	}

	/**
	 * takes web element separate screenshot
	 * @param driver	driver
	 * @param element	element
	 * @param screenShotStepFilter	screenshot filter
	 * @return
	 */
	private static BufferedImage takeWebElementScreenshot(WebDriver driver,
			WebElement element, String screenShotStepFilter) {
		BufferedImage elementScreenShotOnStep = null;

		if (screenShotStepFilter.equals("blur")) {
			elementScreenShotOnStep = blurElementOnScreenShot(driver, element);
		} else if (screenShotStepFilter.equals("redline")) {
			BufferedImage pageScreenShot = takeWebBrowserScreenshot(driver);
			elementScreenShotOnStep = cropElementOnScreenShotWithRectangle(
					pageScreenShot, element);
		} else {
			throw new RuntimeException(
					"Invalid parameter for screenshot filter is set");
		}
		return elementScreenShotOnStep;

	}

    //TODO need to be perfected
	/**
	 * align element with red line
	 * @param pageScreenShot	page screenshot
	 * @param element	element
	 * @return	image with aligned element
	 */
	private static BufferedImage cropElementOnScreenShotWithRectangle(
			BufferedImage pageScreenShot, WebElement element) {
		Point point = element.getLocation();
		int eleWidth = element.getSize().getWidth();
		int eleHeight = element.getSize().getHeight();

		int xLeftCropRectangleMove = point.getX() / 2;
		int xRightCropRectangleMove = (pageScreenShot.getWidth() - eleWidth - point
				.getX()) / 2;

		int yTopCropRectangleMove = point.getY() / 2;
		int yBottomCropRectangleMove = (pageScreenShot.getHeight() - eleHeight - point
				.getY()) / 2;

		// setting the aligned rectangle parameters
		Graphics2D graph = pageScreenShot.createGraphics();
		graph.setColor(Color.RED);
		graph.setStroke(new BasicStroke(2));

		// draw red rectangle
		graph.drawRect(point.getX() - 5, point.getY() - 5, eleWidth + 10,
				eleHeight + 10);
		graph.dispose();

		// crop
		BufferedImage elementScreenshot = pageScreenShot.getSubimage(
				point.getX() - xLeftCropRectangleMove, point.getY()
						- yTopCropRectangleMove, eleWidth
						+ xRightCropRectangleMove + xLeftCropRectangleMove,
				eleHeight + yBottomCropRectangleMove + yTopCropRectangleMove);

		return elementScreenshot;
	}

	/**
	 * blurs element's screenshot
	 * @param driver	driver
	 * @param element	element
	 * @return	buffered image with blurred element
	 */
	private static BufferedImage blurElementOnScreenShot(WebDriver driver,
			WebElement element) {
        Screenshot screenImage;
        AShot ashot = new AShot().coordsProvider(new WebDriverCoordsProvider()).imageCropper(new IndentCropper() // overwriting cropper
                .addIndentFilter(new BlurFilter()));
        if (isRetinaScreen()) {
            screenImage = ashot.dpr(2).takeScreenshot(driver, element);
            return screenImage.getImage();
        }

        screenImage = ashot.takeScreenshot(driver, element);

		return screenImage.getImage();
	}

    //TODO handle FF also in future, now should work for Chrome only,
    // because there are issues with Chrome Driver on Retinaб FF works fine fow now 04/03/15
    private static boolean isRetinaScreen(){
        if (WebBrowser.getSetWebBrowser().getCurrentExecutionEnvironmentName().equals("chrome")) {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            final GraphicsDevice device = env.getDefaultScreenDevice();

            try {
                Field field = device.getClass().getDeclaredField("scale");

                if (field != null) {
                    field.setAccessible(true);
                    Object scale = field.get(device);

                    if (scale instanceof Integer && ((Integer) scale).intValue() == 2) {
                        return true;
                    }
                }
            } catch (Exception ignore) {
            }

            return false;
        } else
            return false;
    }

}
