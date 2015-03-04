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

public class DocumentWorkflowPage extends WebPage<DocumentWorkflowPage> {

	@FindBy(how = How.XPATH, using = "//span[contains(text(),'Sign') and contains(@class,'header')]")
	public WebElement signInButton;

	@FindBy(how = How.XPATH, using = "//div[contains(@class,'lightbox')]//span[contains(@class,'msg')]")
	public WebElement documentUploadedStatus;

	@FindBy(how = How.XPATH, using = "//span[contains(@class,'expand-icon collapsible')]")
	public WebElement documentPreview;
	

	public DocumentWorkflowPage(WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public ExpectedCondition<WebElement> loadedCriteria() {
		driverWaitUtil.forCondition(ExpectedConditions.textToBePresentInElement(documentUploadedStatus, "Document Uploaded"));
		return ExpectedConditions.visibilityOf(documentPreview);
	}

	@Step(value = "Click Sign In Document")
	public SigningOptionsPage clickSignInButton() {
		signInButton.click();
		return CriteriaPageFactory.criteriaInitWebElements(webDriver,
				SigningOptionsPage.class);
	}

}
