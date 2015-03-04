package pages.gonitro.signatures;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import ru.yandex.qatools.allure.annotations.Step;

import com.google.common.base.Predicate;

import framework.automation.page.CriteriaPageFactory;
import framework.automation.page.web.WebPage;
import framework.automation.utils.wait.WaitUtil;

public class NewSignaturePopUp extends WebPage<NewSignaturePopUp>{

	@FindBy (how = How.XPATH, using = "//span[contains(@id,'SignatureCapture')]")
	public WebElement sinaturePopUpTitle;
	
	@FindBy (how = How.XPATH, using = "//nav[contains(@class,'signature-tools')]//span[contains(text(),'Type')]")
	public WebElement typeTab;
	
	@FindBy (how = How.XPATH, using = "//input[contains(@class,'capture-input')]")
	public WebElement inputSignatureTextBox;
	
	@FindBy (how = How.XPATH, using = "//div[contains(@class,'signature-capture')]//a[contains(@class,'button-okay') and not(contains(@class,'disabled'))]")
	public WebElement createSignatureButton;
	
	public NewSignaturePopUp(WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public ExpectedCondition<WebElement> loadedCriteria() {
		return ExpectedConditions.visibilityOf(sinaturePopUpTitle);
	}
	
	@Step (value = "Type new signature text: \"{0}\"")
	public <T extends WebPage<T>> T typeNewSignature(String signatureText, Class<T> pageObject){
		typeTab.click();
		driverWaitUtil.forCondition(ExpectedConditions.visibilityOf(inputSignatureTextBox));
		inputSignatureTextBox.clear();
		inputSignatureTextBox.sendKeys(signatureText);
		createSignatureButton.click();
		final boolean signaturePopUpVisible = sinaturePopUpTitle.isDisplayed();
		WaitUtil<Boolean> waitUtil = new WaitUtil<Boolean>(signaturePopUpVisible);
		waitUtil.forCondition(new Predicate<Boolean>() {
			@Override
			public boolean apply(Boolean input) {
				return !signaturePopUpVisible;
			}
		});
		Class<T> clazz = pageObject;
		return CriteriaPageFactory.criteriaInitWebElements(webDriver, clazz);
	}

}
