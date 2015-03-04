package pages.gonitro.notifications;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import framework.automation.page.web.WebPage;

public class NotificationsPage extends WebPage<NotificationsPage> {

	@FindBy(how = How.XPATH, using = "//div[contains(@class,'alternating-list')]")
	public List<WebElement> notsElementsList;
	
	/**
	 * -----------------------
	 * notification checkboxes
	 * -----------------------
	 */
	@FindBy(how = How.XPATH, using = "(//input[@class='notification-setting'])[1]")
	public WebElement receiveDocumentToSignCheckBox;

	public NotificationsPage(WebDriver webDriver) {
		super(webDriver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ExpectedCondition<List<WebElement>> loadedCriteria() {
		return ExpectedConditions.visibilityOfAllElements(notsElementsList);
	}

	/*
	 * @Step(value = "Click Manage All Notifications Button") public
	 * NotificationsPage manageNotsButton(){ manageButton.click(); return
	 * CriteriaPageFactory.criteriaInitWebElements(webDriver,
	 * NotificationsPage.class); }
	 */

	public static void updateNotificationsForCurrentAccount(WebDriver driver) {
		String udpateUrl = "https://cloud.gonitrostage.com/account/updatenotifications?"
				+ "notificationSettings%5B0%5D%5Bsetting%5D=1&notificationSettings%5B0%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B1%5D%5Bsetting%5D=64&notificationSettings%5B1%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B2%5D%5Bsetting%5D=256&notificationSettings%5B2%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B3%5D%5Bsetting%5D=4194304&notificationSettings%5B3%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B4%5D%5Bsetting%5D=8&notificationSettings%5B4%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B5%5D%5Bsetting%5D=2097152&notificationSettings%5B5%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B6%5D%5Bsetting%5D=128&notificationSettings%5B6%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B7%5D%5Bsetting%5D=8388608&notificationSettings%5B7%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B8%5D%5Bsetting%5D=134217728&notificationSettings%5B8%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B9%5D%5Bsetting%5D=536870912&notificationSettings%5B9%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B10%5D%5Bsetting%5D=-2147483648&notificationSettings%5B10%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B11%5D%5Bsetting%5D=268435456&notificationSettings%5B11%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B12%5D%5Bsetting%5D=1073741824&notificationSettings%5B12%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B13%5D%5Bsetting%5D=2&notificationSettings%5B13%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B14%5D%5Bsetting%5D=512&notificationSettings%5B14%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B15%5D%5Bsetting%5D=8192&notificationSettings%5B15%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B16%5D%5Bsetting%5D=16384&notificationSettings%5B16%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B17%5D%5Bsetting%5D=16&notificationSettings%5B17%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B18%5D%5Bsetting%5D=1024&notificationSettings%5B18%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B19%5D%5Bsetting%5D=2048&notificationSettings%5B19%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B20%5D%5Bsetting%5D=67108864&notificationSettings%5B20%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B21%5D%5Bsetting%5D=4&notificationSettings%5B21%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B22%5D%5Bsetting%5D=4096&notificationSettings%5B22%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B23%5D%5Bsetting%5D=32&notificationSettings%5B23%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B24%5D%5Bsetting%5D=32768&notificationSettings%5B24%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B25%5D%5Bsetting%5D=262144&notificationSettings%5B25%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B26%5D%5Bsetting%5D=524288&notificationSettings%5B26%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B27%5D%5Bsetting%5D=65536&notificationSettings%5B27%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B28%5D%5Bsetting%5D=16777216&notificationSettings%5B28%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B29%5D%5Bsetting%5D=131072&notificationSettings%5B29%5D%5Benabled%5D=true&"
				+ "notificationSettings%5B30%5D%5Bsetting%5D=1048576&notificationSettings%5B30%5D%5Benabled%5D=true";
		driver.get(udpateUrl);
		driver.navigate().back();
	}
	
}
