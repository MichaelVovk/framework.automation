package pages.gonitro.document;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import ru.yandex.qatools.allure.annotations.Step;
import framework.automation.page.web.WebPage;

public class ReadOnlyVersionPage extends WebPage<ReadOnlyVersionPage> {

	@FindBy(how = How.XPATH, using = "//div[contains(text(),'read-only version')]")
	public WebElement readOnlyLabel;

	public ReadOnlyVersionPage(WebDriver webDriver) {
		super(webDriver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ExpectedCondition<WebElement> loadedCriteria() {
		return ExpectedConditions.visibilityOf(readOnlyLabel);
	}
	
	@Step(value = "Check whether read only label displayed(Request Page is closed)")
	public boolean isReadOnlyPageDisplayed(){
		return readOnlyLabel.isDisplayed();
	}

}
