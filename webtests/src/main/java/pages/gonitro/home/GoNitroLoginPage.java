package pages.gonitro.home;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import ru.yandex.qatools.allure.annotations.Step;
import framework.automation.page.CriteriaPageFactory;
import framework.automation.page.web.WebPage;

public class GoNitroLoginPage extends WebPage<GoNitroLoginPage>{
	
	@FindBy (how = How.XPATH, using = "//input[@id='home-email']")
	public WebElement emailTextBox;

	@FindBy (how = How.XPATH, using = "//input[@id='home-password']")
	public WebElement passwordTextBox;
	
	@FindBy (how = How.XPATH, using = "//button[@id='login-cloud-redux']")
	public WebElement signInButton;
	
	
	@FindBy (how = How.XPATH, using = "//footer[contains(@class,'gonitro-footer')]")
	public WebElement footer;
	
	public GoNitroLoginPage(WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public ExpectedCondition<WebElement> loadedCriteria() {
		return ExpectedConditions.visibilityOf(footer);
	}
	
	@Step(value = "Login to GoNitro with email: \"{0}\" and password: \"{1}\"")
	public NitroCloudHomePage loginToNitroCloud(String email, String password){
		emailTextBox.clear();
		emailTextBox.sendKeys(email);
		passwordTextBox.clear();
		passwordTextBox.sendKeys(password);
		signInButton.click();
		return CriteriaPageFactory.criteriaInitWebElements(webDriver, NitroCloudHomePage.class);
	} 

}