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

public class RejectDocumentPopUp extends WebPage<RejectDocumentPopUp> {

	@FindBy(how = How.XPATH, using = "//a[@id='reject_sign']")
	public WebElement rejectRequestButtonRed;
	
	@FindBy(how = How.XPATH, using = "//div[@id='dialog_rejectSignatureRequest']//a[contains(@class,'button-post blue')]")
	public WebElement confirmRejectOk;
	

	public RejectDocumentPopUp(WebDriver webDriver) {
		super(webDriver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ExpectedCondition<WebElement> loadedCriteria() {
		return ExpectedConditions.visibilityOf(rejectRequestButtonRed);
	}

	@Step(value = "Confirm reject confirmation")
	public ReadOnlyVersionPage confirmRejectRequest(){
		rejectRequestButtonRed.click();
		driverWaitUtil.forCondition(ExpectedConditions.visibilityOf(confirmRejectOk));
		confirmRejectOk.click();
		return CriteriaPageFactory.criteriaInitWebElements(webDriver,
				ReadOnlyVersionPage.class); 
	}
}
