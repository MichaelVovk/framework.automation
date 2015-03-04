package pages.gonitro.notifications;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import ru.yandex.qatools.allure.annotations.Step;
import framework.automation.page.CriteriaPageFactory;
import framework.automation.page.web.WebPage;

public class UnsubscribeCompletedPage extends WebPage<UnsubscribeCompletedPage>{
	
	@FindBy(how = How.XPATH, using = "//h2[contains(text(),'Unsubscribe Successful!')]")
	public WebElement successText;
	
	@FindBy(how = How.XPATH, using = "//a[contains(text(),'Manage all notifications')]")
	public WebElement manageButton;

	public UnsubscribeCompletedPage(WebDriver webDriver) {
		super(webDriver);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ExpectedCondition<WebElement> loadedCriteria() {
		return ExpectedConditions.visibilityOf(successText);
	}

	@Step(value = "Click Manage All Notifications Button")
	public NotificationsPage clickManageNotsButton(){
		manageButton.click();
		return CriteriaPageFactory.criteriaInitWebElements(webDriver,
				NotificationsPage.class);
	}
	
}
