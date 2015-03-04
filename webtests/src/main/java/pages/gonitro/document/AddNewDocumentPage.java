package pages.gonitro.document;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.gonitro.document.workflow.DocumentWorkflowPage;
import ru.yandex.qatools.allure.annotations.Step;
import framework.automation.driver.web.WebBrowser;
import framework.automation.page.CriteriaPageFactory;
import framework.automation.page.web.WebPage;

public class AddNewDocumentPage extends WebPage<AddNewDocumentPage> {

	@FindBy(how = How.XPATH, using = "(//*[@id='html5_dragdrop-container']/a[contains(@class,'action-form')])[2]")
	public WebElement uploadFromPCButton;

	@FindBy(how = How.XPATH, using = "//div[@id='dialog_newdocumentchooser']")
	public WebElement documentAddPopUp;

	public AddNewDocumentPage(WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public ExpectedCondition<WebElement> loadedCriteria() {
		return ExpectedConditions.visibilityOf(documentAddPopUp);
	}

	@Step(value = "Add new document: \"{0}\"")
	public DocumentWorkflowPage addNewDocument(String filePath)
			throws AWTException, InterruptedException {
		uploadFromPCButton.click();
		typeInModal(filePath);
		return CriteriaPageFactory.criteriaInitWebElements(webDriver,
				DocumentWorkflowPage.class);
	}

	@Step(value = "Choose file from modal window by path: \"{0}\"")
	private void typeInModal(String characters) throws AWTException, InterruptedException {
		StringSelection stringSelection = new StringSelection(characters);
		Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(stringSelection, null);

		Robot robot = new Robot();
		//wait for modal opening
		Thread.sleep(1000);
		
		if (WebBrowser.getSetWebBrowser().getCurrentExecutionEnvironmentName().equals("chrome")){
			robot.mouseMove(100, 20);
			Thread.sleep(1000);
			robot.mousePress(InputEvent.BUTTON1_MASK);
			Thread.sleep(1000);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			//wait for cursor focus
			Thread.sleep(1000);
		}
		
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		
		//wait for paste operation
		Thread.sleep(1000);
		
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.mouseMove(200, 200);
	}

}
