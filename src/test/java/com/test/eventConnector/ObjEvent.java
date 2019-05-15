package com.test.eventConnector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.Scanner;
import org.apache.commons.lang.SystemUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.AssertJUnit;

import cucumber.api.Scenario;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.MobileBy.ByAndroidUIAutomator;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class ObjEvent {

	private Properties capabilitiesRepo = null;
	private DesiredCapabilities desiredCapabilities;
	public AndroidDriver<MobileElement> driver;

	private AppiumDriverLocalService service;
	private Scanner x;
	private WebDriverWait wait;

	public ObjEvent() throws IOException {
		capabilitiesRepo = new Properties();
		File properFile = new File(System.getProperty("user.dir"),
				"/src/test/java/com/test/config/Capabilities.properties");
		FileInputStream fsCapabilities = new FileInputStream(properFile);
		capabilitiesRepo.load(fsCapabilities);
	}

	public void open() throws IOException, InterruptedException {

		if (getDriver() == null) {

			if (SystemUtils.IS_OS_WINDOWS) {
				// TODO : WINDOWS
				System.setProperty(AppiumServiceBuilder.NODE_PATH, "C:\\Program Files\\nodejs\\node.exe");
				System.setProperty(AppiumServiceBuilder.APPIUM_PATH,
						System.getenv("USER_HOME") + "\\node_modules\\appium\\build\\lib\\main.js");
			} else {
				// TODO : MAC
				System.setProperty(AppiumServiceBuilder.NODE_PATH, "/usr/local/bin/node.sh");
				System.setProperty(AppiumServiceBuilder.APPIUM_PATH,
						System.getProperty("user.dir") + "/node_modules/appium/build/lib/main.js");
			}

			service = AppiumDriverLocalService
					.buildService(new AppiumServiceBuilder().usingAnyFreePort().withIPAddress("127.0.0.1"));
			service.start();
			desiredCapabilities = new DesiredCapabilities();
			desiredCapabilities.setCapability("deviceName", capabilitiesRepo.getProperty("deviceName"));
			desiredCapabilities.setCapability("platformVersion", capabilitiesRepo.getProperty("platformVersion"));
			desiredCapabilities.setCapability("platformName", "Android");
			desiredCapabilities.setCapability("appWaitActivity", capabilitiesRepo.getProperty("appActivity"));
			desiredCapabilities.setCapability("appPackage", capabilitiesRepo.getProperty("appPackage"));
			desiredCapabilities.setCapability("appActivity", capabilitiesRepo.getProperty("appActivity"));

			// Version Android to use UiAutomator2
			int Versi = Integer.parseInt(capabilitiesRepo.getProperty("platformVersion"));

			if (Versi >= 6) {
				// Automation for Android 7.0.0 ++
				desiredCapabilities.setCapability("automationName", "uiautomator2");
			} else {
				desiredCapabilities.setCapability("platformVersion", capabilitiesRepo.getProperty("platformVersion"));
			}

			setDriver(new AndroidDriver<MobileElement>(service.getUrl(), desiredCapabilities));

		} else {
			desiredCapabilities.setCapability("appWaitActivity", capabilitiesRepo.getProperty("appActivity"));
			desiredCapabilities.setCapability("appPackage", capabilitiesRepo.getProperty("appPackage"));
			desiredCapabilities.setCapability("appActivity", capabilitiesRepo.getProperty("appActivity"));
		}
	}

	public void install() throws IOException, InterruptedException {
		desiredCapabilities = new DesiredCapabilities();

		if (SystemUtils.IS_OS_WINDOWS) {
			// TODO : WINDOWS
			System.setProperty(AppiumServiceBuilder.NODE_PATH, "C:\\Program Files\\nodejs\\node.exe");
			System.setProperty(AppiumServiceBuilder.APPIUM_PATH,
					System.getenv("USER_HOME") + "\\node_modules\\appium\\build\\lib\\main.js");
		} else {
			// TODO : MAC
			System.setProperty(AppiumServiceBuilder.NODE_PATH, "/usr/local/bin/node.sh");
			System.setProperty(AppiumServiceBuilder.APPIUM_PATH,
					System.getProperty("user.dir") + "/node_modules/appium/build/lib/main.js");
		}

		service = AppiumDriverLocalService
				.buildService(new AppiumServiceBuilder().usingAnyFreePort().withIPAddress("127.0.0.1"));
		service.start();

		File sourceDir = new File(System.getProperty("user.dir"), capabilitiesRepo.getProperty("APK_PATH"));

		String[] apkFilenames = sourceDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".apk"))
					return true;
				else
					return false;
			}
		});

		// TODO: Get newest file for list of apkFilenames
		String apkFilepath = sourceDir.getAbsolutePath() + File.separator + apkFilenames[0];

		desiredCapabilities.setCapability("deviceName", capabilitiesRepo.getProperty("deviceName"));
		desiredCapabilities.setCapability("platformVersion", capabilitiesRepo.getProperty("platformVersion"));
		desiredCapabilities.setCapability("platformName", "Android");
		desiredCapabilities.setCapability(MobileCapabilityType.APP, apkFilepath);

		// Version Android to use UiAutomator2
		int Versi = Integer.parseInt(capabilitiesRepo.getProperty("platformVersion"));

		if (Versi >= 7) {
			// Automation for Android 7.0.0 ++
			desiredCapabilities.setCapability("automationName", "uiautomator2");
		} else {
			desiredCapabilities.setCapability("platformVersion", capabilitiesRepo.getProperty("platformVersion"));
		}

		setDriver(new AndroidDriver<MobileElement>(service.getUrl(), desiredCapabilities));
	}

	public void logon(String userID) {
		boolean found = false;

		String filePath = System.getProperty("user.dir") + "\\DataFile\\DataLogin.txt";
		String searchTerm = userID;
		String username = "";
		String password = "";

		try {
			x = new Scanner(new File(filePath));
			x.useDelimiter("[,\n]");

			while (x.hasNext() && !found) {
				username = x.next();
				password = x.next();

				if (username.equals(searchTerm)) {
					found = true;
				}
			}

			if (found) {
				getDriver().findElementByAndroidUIAutomator("UiSelector().description(\"item_inputtxt_mobilenumber\")")
						.sendKeys(username);

				getDriver().findElementByAndroidUIAutomator("UiSelector().description(\"item_inputtxt_pin\")")
						.sendKeys(password.trim());

			} else {
				System.out.println("Record Not Found: ");
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void tapByContentdescEvent(String objectName) throws InterruptedException {
		wait = new WebDriverWait(getDriver(), 20);
		wait.until(ExpectedConditions.elementToBeClickable(ByAndroidUIAutomator.AccessibilityId(objectName)));

		// TODO : Find Element by content-desc
		getDriver().findElementByAndroidUIAutomator("UiSelector().description(\"" + objectName + "\")").click();
	}

	public void tapByNameEvent(String objectName) {
		// TODO : Find Element by name
		getDriver().findElement(By.name(objectName)).click();
	}

	public void tapByTextEvent(String objectName) {
		// TODO : Find Element by Text
		getDriver().findElementByXPath("//*[@text = '" + objectName + "']").click();
	}

	public void tapByIdEvent(String objectName) throws InterruptedException {
		wait = new WebDriverWait(getDriver(), 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(objectName)));

		// TODO : Find Element by ID
		getDriver().findElement(By.id(objectName)).click();
	}

	public void tapByXpathEvent(String objectName) {
		// TODO : Find Element by Xpath for All Tab
		getDriver().findElementByXPath("//*[@class = 'android.widget.TextView' and @text = '" + objectName + "']")
				.click();
	}

	public void setTextbyContentdescEvent(String text, String objectName) throws InterruptedException {
		wait = new WebDriverWait(getDriver(), 20);
		wait.until(ExpectedConditions.elementToBeClickable(ByAndroidUIAutomator.AccessibilityId(objectName)));

		// TODO : Find Element by content-desc
		getDriver().findElementByAndroidUIAutomator("UiSelector().description(\"" + objectName + "\")").sendKeys(text);
	}

	public void setTextByIDEvent(String text, String objectName) {
		wait = new WebDriverWait(getDriver(), 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(objectName)));

		// TODO : Find Element by ID
		getDriver().findElement(By.id(objectName)).sendKeys(text);
	}

	public void setTextByXpathEvent(String text, String objectName) {
		// TODO : Find Element By Xpath
	}

	public void selectListItemByContentDescEvent(String text, String objectName) throws InterruptedException {
		wait = new WebDriverWait(getDriver(), 20);
		wait.until(ExpectedConditions.elementToBeClickable(ByAndroidUIAutomator.AccessibilityId(objectName)));

		// TODO: Find element by content-desc
		getDriver().findElementByAndroidUIAutomator("UiSelector().description(\"" + objectName + "\")").click();
		Thread.sleep(1000);
		getDriver().findElementByAndroidUIAutomator("UiSelector().text(\"" + text + "\")").click();
	}

	public void selectListItemByIdEvent(String text, String objectName) throws InterruptedException {
		wait = new WebDriverWait(getDriver(), 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(objectName)));

		// TODO: Find element by Id
		getDriver().findElement(By.id(objectName)).click();
		Thread.sleep(1000);
		getDriver().findElementByAndroidUIAutomator("UiSelector().text(\"" + text + "\")").click();
	}

	public void checkElementByContentDescEvent(String objectName) {
		wait = new WebDriverWait(getDriver(), 20);
		wait.until(ExpectedConditions.elementToBeClickable(ByAndroidUIAutomator.AccessibilityId(objectName)));

		// TODO : Find Element by content-desc
		getDriver().findElementByAndroidUIAutomator("UiSelector().description(\"" + objectName + "\")").click();
	}

	public void checkElementByIdEvent(String objectName) {
		wait = new WebDriverWait(getDriver(), 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(objectName)));

		// TODO : Find Element by ID
		getDriver().findElement(By.id(objectName)).click();
	}

	public void checkElementByXpathEvent(String objectName) {
		// TODO : Find Element by xpath
	}

	public void verifyEqualByContentDescEvent(String objectName, String text) {
		wait = new WebDriverWait(getDriver(), 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(ByAndroidUIAutomator.AccessibilityId(objectName)));

		String getText = getDriver().findElementByAndroidUIAutomator("UiSelector().description(\"" + objectName + "\")")
				.getText();
		AssertJUnit.assertEquals(text, getText);
	}

	public void verifyEqualByIdEvent(String objectName, String text) throws InterruptedException {
		wait = new WebDriverWait(getDriver(), 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(objectName)));

		// TODO : Find element by ID
		String getText = getDriver().findElement(By.id(objectName)).getText();
		AssertJUnit.assertEquals(text, getText);

	}

	public void verifyEqualByXpathEvent(String objectName, String text) {
		// TODO : Find element by Xpath
	}

	public void verifyElementExistByContentDescEvent(String objectName, Scenario myScenario) {
		wait = new WebDriverWait(getDriver(), 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(ByAndroidUIAutomator.AccessibilityId(objectName)));

		// TODO : Find Element by content-desc
		boolean content = getDriver()
				.findElementByAndroidUIAutomator("UiSelector().description(\"" + objectName + "\")").isDisplayed();
		AssertJUnit.assertEquals(objectName, content);
	}

	public void verifyElementExistByIdEvent(String objectName, Scenario myScenario) {
		wait = new WebDriverWait(getDriver(), 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(objectName)));

		// TODO : Find Element by ID
		boolean id = getDriver().findElement(By.id(objectName)).isDisplayed();
		AssertJUnit.assertEquals(objectName, id);
	}

	public void verifyElementExistByTextEvent(Scenario myScenario, String objectName) {
		boolean text = getDriver().findElementByAndroidUIAutomator("UiSelector().text(\"" + objectName + "\")")
				.isDisplayed();
		AssertJUnit.assertEquals(objectName, text);
	}

	public void verifyElementDisableByIdEvent(Scenario myScenario, String objectName, int timeLimitInSeconds)
			throws IOException {
		boolean button = getDriver().findElement(By.id(objectName)).isEnabled();
		AssertJUnit.assertEquals(false, button);
	}

	public void verifyElementExistByToastEvent(String objectName, Scenario myScenario) {
		// TODO : Verify element toast
		WebElement toastView = getDriver().findElement(By.xpath("//android.widget.Toast[1]"));
		AssertJUnit.assertEquals(objectName, toastView.getAttribute("name"));
	}

	public void verifyElementExistByXpathEvent(String objectName, Scenario myScenario) {
		// TODO : Find Element by Xpath
		boolean xpath = getDriver()
				.findElement(By.xpath("//*[@class='android.widget.*?' and @text='" + objectName + "']")).isDisplayed();
		AssertJUnit.assertEquals(objectName, xpath);
	}

	// Swipe Up
	@SuppressWarnings("rawtypes")
	public void swipeDown() {
		Dimension size;
		int start = 0;
		int end = 0;
		int anchor = 0;
		int timeduration = 2;

		size = getDriver().manage().window().getSize();
		end = (int) (size.height * 0.80);
		start = (int) (size.height * 0.20);
		anchor = size.width / 2;

		new TouchAction(getDriver()).press(PointOption.point(anchor, start))
				.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(timeduration)))
				.moveTo(PointOption.point(anchor, end)).release().perform();

	}

	// Swipe Down
	@SuppressWarnings("rawtypes")
	public void swipeUp() {
		Dimension size;
		int start = 0;
		int end = 0;
		int anchor = 0;
		int timeduration = 2;

		size = getDriver().manage().window().getSize();
		System.out.println(size);
		end = (int) (size.height * 0.20);
		start = (int) (size.height * 0.80);
		anchor = size.width / 2;

		new TouchAction(getDriver()).press(PointOption.point(anchor, start))
				.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(timeduration)))
				.moveTo(PointOption.point(anchor, end)).release().perform();
	}

	// Swipe Right
	@SuppressWarnings("rawtypes")
	public void swipeLeft() {
		Dimension size;
		int anchor = 0;
		int end = 0;
		int start = 0;
		int timeduration = 2;

		size = getDriver().manage().window().getSize();
		System.out.println(size);
		start = (int) (size.width * 0.80);
		end = (int) (size.width * 0.20);
		anchor = size.height / 2;

		new TouchAction(getDriver()).press(PointOption.point(start, anchor))
				.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(timeduration)))
				.moveTo(PointOption.point(end, anchor)).release().perform();
	}

	// Swipe Left
	@SuppressWarnings("rawtypes")
	public void swipeRight() {
		Dimension size;
		int anchor = 0;
		int end = 0;
		int start = 0;
		int timeduration = 2;

		size = getDriver().manage().window().getSize();
		System.out.println(size);
		start = (int) (size.width * 0.20);
		end = (int) (size.width * 0.80);
		anchor = size.height / 2;

		new TouchAction(getDriver()).press(PointOption.point(start, anchor))
				.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(timeduration)))
				.moveTo(PointOption.point(end, anchor)).release().perform();
	}

	// Wait for element for Id
	public boolean waitForId(int timeLimitInSeconds, String objectName, boolean isElementPresent) {
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), timeLimitInSeconds);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(objectName)));
			isElementPresent = getDriver().findElement(By.id(objectName)).isDisplayed();
			return isElementPresent;
		} catch (Exception e) {
			isElementPresent = false;
			System.out.println(e.getMessage());
			return isElementPresent;
		}

	}

	// Wait for element content-desc
	public boolean waitForContent(int timeLimitInSeconds, String objectName, boolean isElementPresent) {
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), timeLimitInSeconds);
			wait.until(ExpectedConditions.visibilityOfElementLocated(
					MobileBy.AndroidUIAutomator("UiSelector().description(\"" + objectName + "\"));")));
			isElementPresent = getDriver()
					.findElement(MobileBy.AndroidUIAutomator("UiSelector().description(\"" + objectName + "\"));"))
					.isDisplayed();
			return isElementPresent;
		} catch (Exception e) {
			isElementPresent = false;
			System.out.println(e.getMessage());
			return isElementPresent;
		}
	}

	// Wait for element by text
	public boolean waitForText(int timeLimitInSeconds, String objectName, boolean isElementPresent) {
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), timeLimitInSeconds);
			wait.until(ExpectedConditions.visibilityOfElementLocated(
					MobileBy.AndroidUIAutomator("UiSelector().text(\"" + objectName + "\");")));
			isElementPresent = getDriver()
					.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"" + objectName + "\"));"))
					.isDisplayed();
			return isElementPresent;
		} catch (Exception e) {
			isElementPresent = false;
			System.out.println(e.getMessage());
			return isElementPresent;
		}

	}

	// Wait for element by index
	public boolean waitForIndex(int timeLimitInSeconds, String objectName, boolean isElementPresent) {
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), timeLimitInSeconds);
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(MobileBy.xpath("//android.widget.*?[@index='" + objectName + "']")));
			isElementPresent = getDriver()
					.findElement(MobileBy.xpath("//android.widget.*?[@index='" + objectName + "']")).isDisplayed();
			return isElementPresent;
		} catch (Exception e) {
			isElementPresent = false;
			System.out.println(e.getMessage());
			return isElementPresent;
		}
	}

	public void screenShot(Scenario myScenario) {
		myScenario.embed(((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES), "image/png");
	}

	// Swipe to Element by Text
	public void swipeToEleTextEvent(String objectName) {

		getDriver().findElementByAndroidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView("
				+ "new UiSelector().text(\"" + objectName + "\"));").isDisplayed();
	}

	// Swipe to Element by Id
	public void swipeToEleIdEvent(String objectName) {

		getDriver().findElementByAndroidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView("
				+ "new UiSelector().resourceId(\".*?id/" + objectName + "\"));").isDisplayed();
	}

	public AndroidDriver<MobileElement> getDriver() {
		return driver;
	}

	public void setDriver(AndroidDriver<MobileElement> driver) {
		this.driver = driver;
	}
}