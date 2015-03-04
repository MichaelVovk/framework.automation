package pages.gonitro.document.workflow;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import ru.yandex.qatools.allure.annotations.Step;
import framework.automation.page.web.WebPage;

public class SelfSignCompletedPage extends WebPage<SelfSignCompletedPage> {

	@FindBy(how = How.XPATH, using = "//div[@id='step_selfsign_complete']//div[contains(@class,'success-msg')]/h2")
	public WebElement successMessage;

	public SelfSignCompletedPage(WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public ExpectedCondition<WebElement> loadedCriteria() {
		return ExpectedConditions.visibilityOf(successMessage);
	}

	@Step (value = "Checking whether request is completed")
	public boolean isRequestCompleted() {
		return successMessage.getText().contains("Document Signed!");
	}

}
