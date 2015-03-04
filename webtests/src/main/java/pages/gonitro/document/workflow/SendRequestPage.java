package pages.gonitro.document.workflow;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import ru.yandex.qatools.allure.annotations.Step;
import framework.automation.page.CriteriaPageFactory;
import framework.automation.page.web.WebPage;

public class SendRequestPage extends WebPage<SendRequestPage> {

	@FindBy(how = How.XPATH, using = "//textarea[@id='request_signature_message']")
	public WebElement messageTextBoxButton;

	@FindBy(how = How.XPATH, using = "//div[@id='step_requestsignature']//input[@name='subject']")
	public WebElement requestSubject;

	@FindBy(how = How.XPATH, using = "//div[@id='step_requestsignature']//a[contains(@class,'green next')]")
	public WebElement sendRequestButton;

	public SendRequestPage(WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public ExpectedCondition<WebElement> loadedCriteria() {
		return ExpectedConditions.visibilityOf(messageTextBoxButton);
	}

	@Step(value = "Send request to user with subject: \"{0}\"")
	public PreapareCompletedPage sendRequest(String subject) {
		requestSubject.clear();
		requestSubject.sendKeys(subject);
		sendRequestButton.click();
		return CriteriaPageFactory.criteriaInitWebElements(webDriver,
				PreapareCompletedPage.class);
	}

}
