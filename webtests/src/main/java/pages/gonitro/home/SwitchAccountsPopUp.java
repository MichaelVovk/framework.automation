package pages.gonitro.home;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.gonitro.document.GoNitroViewDocumentRequestPage;
import ru.yandex.qatools.allure.annotations.Step;
import framework.automation.page.CriteriaPageFactory;
import framework.automation.page.web.WebPage;

public class SwitchAccountsPopUp extends WebPage<SwitchAccountsPopUp>{
	
	@FindBy(how = How.XPATH, using = "//div[contains(@class,'transport')]/a[contains(@class,'button-okay')]")
	public WebElement switchAccounts;

	public SwitchAccountsPopUp(WebDriver webDriver) {
		super(webDriver);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ExpectedCondition<WebElement> loadedCriteria() {
		return ExpectedConditions.visibilityOf(switchAccounts);
	}

	@Step(value = "Switch accounts for GoNitro")
	public GoNitroViewDocumentRequestPage confirmSwitchAccounts(){
		switchAccounts.click();
		return CriteriaPageFactory.criteriaInitWebElements(webDriver,
				GoNitroViewDocumentRequestPage.class);
	}
	
}
