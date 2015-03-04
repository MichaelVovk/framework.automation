package pages.gonitro.document;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import ru.yandex.qatools.allure.annotations.Step;
import framework.automation.page.CriteriaPageFactory;
import framework.automation.page.web.WebPage;

public class GoNitroViewDocumentRequestPage extends WebPage<GoNitroViewDocumentRequestPage>{
	
	@FindBy(how = How.XPATH, using = "//img[@id='page_0']")
	public WebElement document;

	@FindBy(how = How.XPATH, using = "//a[@id='btn_reject_signature_request']")
	public WebElement rejectRequestButton;

	public GoNitroViewDocumentRequestPage(WebDriver webDriver) {
		super(webDriver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ExpectedCondition<WebElement> loadedCriteria() {
		return ExpectedConditions.visibilityOf(document);
	}
	
	@Step(value = "Reject sign request")
	public RejectDocumentPopUp rejectRquest(){
		rejectRequestButton.click();
		return CriteriaPageFactory.criteriaInitWebElements(webDriver, RejectDocumentPopUp.class);
	}
	
	
}
