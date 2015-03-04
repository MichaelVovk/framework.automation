package pages.gonitro.document.workflow;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.gonitro.signatures.NewSignaturePopUp;
import ru.yandex.qatools.allure.annotations.Step;
import framework.automation.page.CriteriaPageFactory;
import framework.automation.page.web.WebPage;

public class PrepareDocumentPage extends WebPage<PrepareDocumentPage>{
	
	@FindBy(how = How.XPATH, using = "//div[@id='step_prepare']//a[contains(@class,'button')][1]")
	public WebElement documentSignature;
	
	@FindBy(how = How.XPATH, using = "//div[@id='step_self_sign']//a[contains(@class,'activator-signatures')]")
	public WebElement mySignaturesButton;
	
	@FindBy(how = How.XPATH, using = "//div[contains(@class,'signature-list-acquire')]/a")
	public WebElement createNewSignatureButton;
	
	@FindBy(how = How.XPATH, using = "//menu[@id='menu_mysigs']//div[last()]//img")
	public List<WebElement> mySignaturesItems;
	
	@FindBy(how = How.XPATH, using = "//div[contains(@id,'workflow_sign')]//div[contains(@id,'page_')]")
	public WebElement documentWorkflowWindow;
	
	@FindBy(how = How.XPATH, using = "//div[@id='step_prepare']//a[contains(@class,'right-end')]")
	public WebElement continueButtonStepPrepare;
	
	@FindBy(how = How.XPATH, using = "//div[@id='step_self_sign']//a[contains(@class,'right-end')]")
	public WebElement continueButtonStepSelfSign;
	
	public PrepareDocumentPage(WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public ExpectedCondition<WebElement> loadedCriteria() {
		return ExpectedConditions.visibilityOf(documentWorkflowWindow);
	}
	
	@Step(value = "Set signature on document for others")
	public SendRequestPage setSignatureOnDocumentOthers() throws InterruptedException{
		documentSignature.click();
		dragToolsElementToDocument(documentSignature);
		continueButtonStepPrepare.click();
		return CriteriaPageFactory.criteriaInitWebElements(webDriver,
				SendRequestPage.class);
	}
	
	@Step(value = "Confirm set signature on document self sign")
	public SelfSignCompletedPage confirmSignatureOnDocumentSelfSign() throws InterruptedException{
		continueButtonStepSelfSign.click();
		return CriteriaPageFactory.criteriaInitWebElements(webDriver,
				SelfSignCompletedPage.class);
	}
	
	@Step(value = "Set my signature on document and get it's source")
	public void setMyCreatedSignatureOnDocument(int x, int y, int direction) throws AWTException, InterruptedException {
		clickNewSignature();
		if (!mySignaturesItems.isEmpty()) {
			addSignatureFromDropDownToDocument(mySignaturesItems.get(0), x,y,direction);
		} else {
			createAndAddSignatureToDocument("default new sign", x,y,direction);
		}
	}
	
	@Step(value = "Get all signatures from document")
	public List<WebElement> getSignaturesOnDocument(){
		List<WebElement> signaturesAdded = documentWorkflowWindow.
				findElements(By.xpath(".//div[contains(@class,'stamp-signature')]/img"));
		driverWaitUtil.forCondition(ExpectedConditions.visibilityOfAllElements(signaturesAdded));
		return signaturesAdded;
	}
	
	@Step(value = "Get source of last created signature")
	public String getLastCreatedSignatureSource(){
		return mySignaturesItems.get(0).getAttribute("data-signature-id");
	}
	
	@Step(value = "Get source of signature that was created in sigmatures menu page")
	public String getSourceOfDocumentSignatureCreatedInMenu(int index){
		return getSignaturesOnDocument().get(index).getAttribute("alt");
	}
	
	@Step(value = "Get source of: \"{0}\" signature that was created in prepare document page")
	public String getSourceOfDocumentSignatureCreatedInPrepare(int index){
		return getSignaturesOnDocument().get(index).getAttribute("data-signature-id");
	}
	
	@Step(value = "Click create new signature")
	public NewSignaturePopUp clickCreateMyNewSignature(){
		createNewSignatureButton.click();
		return CriteriaPageFactory.criteriaInitWebElements(webDriver,
				NewSignaturePopUp.class);
	}
	
	@Step(value = "Create and add new signature to document with text: \"{0}\"")
	public void createAndAddSignatureToDocument(String signatureText, int x, int y, int direction) throws AWTException, InterruptedException{
		createNewSignatureFromPrepare(signatureText);
		addSignatureFromDropDownToDocument(mySignaturesItems.get(0),x,y,direction);
	}
	
	@Step(value = "Create and add new signature to document on prepare page with text: \"{0}\"")
	public PrepareDocumentPage createNewSignatureFromPrepare(String signatureText){
		NewSignaturePopUp newSignPopUp = clickCreateMyNewSignature();
		PrepareDocumentPage preparePage = newSignPopUp.typeNewSignature(signatureText, PrepareDocumentPage.class);
		return preparePage;
	}
	
	@Step (value = "Click my signatures button")
	public void clickNewSignature(){
		mySignaturesButton.click();
	}
	
	@Step (value = "Add signatures from dropdown list to document")
	public void addSignatureFromDropDownToDocument(WebElement signature, int xCenter, int yCenter, int direction) throws AWTException, InterruptedException{
		(new Actions(webDriver)).click(signature).build().perform();
		Robot robot = new Robot();
		//wait for mouth to be prepared
		Thread.sleep(1000);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		if (direction < 0){
			xCenter = (-1) * xCenter;
			yCenter = (-1) * yCenter;
		}
	    int x = (int) (dimension.getWidth() / 2 - xCenter);
	    int y = (int) (dimension.getHeight() / 2 - yCenter);
		robot.mouseMove(x, y);
		robot.mousePress(InputEvent.BUTTON1_MASK ); 
		robot.mouseRelease(InputEvent.BUTTON1_MASK ); 
		Thread.sleep(50); 
		robot.mousePress(InputEvent.BUTTON1_MASK ); 
		robot.mouseRelease(InputEvent.BUTTON1_MASK );
	}
	
	@Step(value = "Drag Element to document")
	private void dragToolsElementToDocument(WebElement elementToDrag){
		(new Actions(webDriver)).dragAndDrop(elementToDrag, documentWorkflowWindow).build().perform();
	}

}
