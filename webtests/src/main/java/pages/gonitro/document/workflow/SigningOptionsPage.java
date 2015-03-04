package pages.gonitro.document.workflow;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import ru.yandex.qatools.allure.annotations.Step;

import com.google.common.base.Function;

import framework.automation.page.CriteriaPageFactory;
import framework.automation.page.web.WebPage;

public class SigningOptionsPage extends WebPage<SigningOptionsPage>{

	@FindBy(how = How.XPATH, using = "//input[contains(@class,'i-sign resetable')]")
	public WebElement iSignInCheckBox;
	
	@FindBy(how = How.XPATH, using = "//input[contains(@class,'others-sign resetable')]")
	public WebElement otherSignInCheckBox;
	
	@FindBy(how = How.XPATH, using = "//div[contains(@class,'add-request-signer')]//input[@name='name']")
	public WebElement nameTextBox;
	
	@FindBy(how = How.XPATH, using = "//div[contains(@class,'add-request-signer')]//input[@name='email']")
	public WebElement emailTextBox;
	
	@FindBy(how = How.XPATH, using = "//div[@id='step_signing_options']//a[contains(@class,'next blue')]")
	public WebElement continueButton;
	
	public SigningOptionsPage(WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public ExpectedCondition<WebElement> loadedCriteria() {
		return ExpectedConditions.visibilityOf(otherSignInCheckBox);
	}
	
	@Step (value = "Set signing options with name: \"{0}\" and email: \"{1}\"")
	public PrepareDocumentPage setSigningOptionToOthers(String name, String email) {
		otherSignInCheckBox.click();
		driverWaitUtil.forCondition(new Function<WebDriver, Boolean>() {
			@Override
			public Boolean apply(WebDriver input) {
				return (nameTextBox.isDisplayed() && emailTextBox.isDisplayed());
			}
		});
		nameTextBox.sendKeys(name);
		emailTextBox.sendKeys(email);
		continueButton.click();
		return CriteriaPageFactory.criteriaInitWebElements(webDriver,
				PrepareDocumentPage.class);
	}
	
	@Step (value = "Set signing options to me")
	public PrepareDocumentPage setSigningOptionToMe() {
		iSignInCheckBox.click();
		continueButton.click();
		return CriteriaPageFactory.criteriaInitWebElements(webDriver,
				PrepareDocumentPage.class);
	}
	
}
