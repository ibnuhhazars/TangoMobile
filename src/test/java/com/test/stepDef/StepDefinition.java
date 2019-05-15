package com.test.stepDef;

import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.test.eventConnector.ObjEvent;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefinition extends ObjEvent {

	private Scenario myScenario;

	public StepDefinition() throws IOException {
		super();
	}

	@Before()
	public void definedScenario(Scenario scenario) {
		myScenario = scenario;
	}

	@Given("^.*?open.*?$")
	public void openApps() throws IOException, InterruptedException {
		open();
	}

	@Given("^.*?install.*?$")
	public void i_install_Selendroid_apps() throws Throwable {
		install();
	}

	@When(".*?loged.*? \"(.*?)\"$")
	public void loged(String userID) {
		logon(userID);
	}

	@When(".*?click button \"(.*?)\"$")
	public void tapByContentdesc(String objectName) throws InterruptedException {
		tapByContentdescEvent(objectName);
	}

	@When(".*?click button by id \"(.*?)\"$")
	public void tapById(String objectName) throws InterruptedException {
		tapByIdEvent(objectName);
	}

	@When(".*?click button by Xpath \"(.*?)\"$")
	public void tapByXpath(String objectName) {
		tapByXpathEvent(objectName);
	}

	@When(".*?click by text.*\"(.*?)\"$")
	public void tapByTEXT(String objectName) throws InterruptedException {
		tapByTextEvent(objectName);
	}

	@When(".*?enter.*? \"(.*?)\" in \"(.*?)\"$")
	public void setTextbyContentdesc(String text, String objectName) throws InterruptedException {
		setTextbyContentdescEvent(text, objectName);
	}

	@When(".*?enter.*? \"(.*?)\" in id \"(.*?)\"$")
	public void setTextByID(String text, String objectName) {
		setTextByIDEvent(text, objectName);
	}

	@When(".*?enter.*? \"(.*?)\" in xpath \"(.*?)\"$")
	public void setTextByXpath(String text, String objectName) {
		setTextByXpathEvent(text, objectName);
	}

	@When(".*?select.*? \"(.*?)\" in \"(.*?)\"$")
	public void selectListItemByContentDesc(String text, String objectName) throws InterruptedException {
		selectListItemByContentDescEvent(text, objectName);
	}

	@When(".*?select.*? \"(.*?)\" in id \"(.*?)\"$")
	public void selectListItemById(String text, String objectName) throws InterruptedException {
		selectListItemByIdEvent(text, objectName);
	}

	@When(".*?check checkbox \"(.*?)\"$")
	public void checkElementByContentDesc(String objectName) {
		checkElementByContentDescEvent(objectName);
	}

	@When(".*?check checkbox by id \"(.*?)\"$")
	public void checkElementById(String objectName) {
		checkElementByIdEvent(objectName);
	}

	@When(".*?check checkbox xpath \"(.*?)\"$")
	public void checkElementByXpath(String objectName) {
		checkElementByXpathEvent(objectName);
	}

	@Then(".*? \"([^\"]*)\" .*?display by content")
	public void verifyElementExistByContentdesc(String objectName) {
		verifyElementExistByContentDescEvent(objectName, myScenario);
	}

	@Then(".*? \"([^\"]*)\" .*?display by Id")
	public void verifyElementExistById(String objectName) {
		verifyElementExistByIdEvent(objectName, myScenario);
	}

	@Then(".*? \"([^\"]*)\" .*?display by text")
	public void verifyElementExistByTEXT(String objectName) {
		verifyElementExistByTextEvent(myScenario, objectName);
	}

	@Then(".*? \"([^\"]*)\" .*?display by xpath")
	public void verifyElementExistByXPATH(String objectName) {
		verifyElementExistByXpathEvent(objectName, myScenario);
	}

	@Then(".*? \"([^\"]*)\" .*?display by toast")
	public void verifyElementExistByTOAST(String objectName) {
		verifyElementExistByToastEvent(objectName, myScenario);
	}

	@When(".*?swipe up.*?")
	public void swipeup() {
		swipeUp();
	}

	@When(".*?swipe down.*?")
	public void swipedownElementExist() {
		swipeDown();
	}

	@When(".*?swipe right.*?")
	public void swiperightElementExist() {
		swipeRight();
	}

	@When(".*?swipe left.*?")
	public void swipeleftElementExist() {
		swipeLeft();
	}

	@Then(".*?verify, so that \"(.*?)\" is \"(.*?)\"$")
	public void verifyEqualByContentdesc(String objectName, String text) {
		verifyEqualByContentDescEvent(objectName, text);
	}

	@Then(".*?verify, so that id \"(.*?)\" is \"(.*?)\"$")
	public void verifyEqualById(String objectName, String text) throws InterruptedException {
		verifyEqualByIdEvent(objectName, text);
	}

	@Then(".*?verify, so that xpath \"(.*?)\" is \"(.*?)\"$")
	public void verifyEqualByXpath(String objectName, String text) {
		verifyEqualByXpathEvent(objectName, text);
	}

	@Then(".*? \"([^\"]*)\" .*?disable by id")
	public void verifyElementDisableByID(String objectName) throws IOException {
		verifyElementDisableByIdEvent(myScenario, objectName, 100);
	}

	@Then("^.*?screenshot.*?$")
	public void screenShot() {
		screenShot(myScenario);
	}

	@Given(".*?swipe to text \"(.*?)\"$")
	public void swipeToElementText(String objectName) throws InterruptedException {
		swipeToEleTextEvent(objectName);
	}

	@Given(".*?swipe to id \"(.*?)\"$")
	public void swipeToElementID(String objectName) throws InterruptedException {
		swipeToEleIdEvent(objectName);
	}

	@After
	public void afterTest() throws Exception {

		if (myScenario.isFailed()) {
			myScenario.embed(((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES), "image/png");
			myScenario.write("Scenario Fail");
		} else {
			myScenario.write("Scenario Pass");
		}

		Thread.sleep(3000);

		getDriver().quit();
	}
}
