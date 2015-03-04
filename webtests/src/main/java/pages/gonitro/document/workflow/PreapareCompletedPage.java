package pages.gonitro.document.workflow;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.gonitro.home.NitroCloudHomePage;
import ru.yandex.qatools.allure.annotations.Step;
import framework.automation.page.CriteriaPageFactory;
import framework.automation.page.web.WebPage;

public class PreapareCompletedPage extends WebPage<PreapareCompletedPage> {

	@FindBy(how = How.XPATH, using = "//div[@id='step_sign_complete']//div[contains(@class,'success-msg')]/h2")
	public WebElement successMessage;
	
	@FindBy(how = How.XPATH, using = "//div[@id='step_sign_complete']//a[contains(@class,'button complete')]")
	public WebElement closeCompletedPageButton;

	public PreapareCompletedPage(WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public ExpectedCondition<WebElement> loadedCriteria() {
		return ExpectedConditions.visibilityOf(successMessage);
	}

	@Step (value = "Checking whether request is completed")
	public boolean isRequestCompleted() {
		return successMessage.getText().contains("Signature Request Sent!");
	}
	
	@Step (value = "Close completed page")
	public NitroCloudHomePage closeCompletedPage() {
		closeCompletedPageButton.click();
		return CriteriaPageFactory.criteriaInitWebElements(webDriver, NitroCloudHomePage.class);
	}

}
