package pages.gonitro.home;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.gonitro.document.AddNewDocumentPage;
import pages.gonitro.signatures.SignatureMenuPage;
import ru.yandex.qatools.allure.annotations.Step;
import framework.automation.page.CriteriaPageFactory;
import framework.automation.page.web.WebPage;

public class NitroCloudHomePage extends WebPage<NitroCloudHomePage>{
	
	@FindBy (how = How.XPATH, using ="//img[contains(@class,'logo')]")
	public WebElement pageLogo;
	
	@FindBy (how = How.XPATH, using = "//div[@id='new_document_container']/a[1]/span[contains(@class,'ico-upload')]")
	public WebElement addNewDocument;
	
	@FindBy (how = How.XPATH, using = "//span[contains(@class,'display-user-input')]")
	public WebElement displayUserInput;
	
	@FindBy (how = How.XPATH, using = "//menu[@id='menu_account']")
	public WebElement displayUserMenu;
	
	@FindBy (how = How.XPATH, using = "//footer[@class]")
	public WebElement footer;
	
	public SignatureMenuPage signPage;
	
	public NitroCloudHomePage(WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public ExpectedCondition<WebElement> loadedCriteria() {
		return ExpectedConditions.visibilityOf(footer);
	}
	
	@Step(value = "Open new document window")
	public AddNewDocumentPage openAddNewDocumentWindow(){
		addNewDocument.click();
		return CriteriaPageFactory.criteriaInitWebElements(webDriver, AddNewDocumentPage.class);
	}
	
	@Step(value = "Open signatures menu")
	public SignatureMenuPage openSignaturesMenu(){
		displayUserInput.click();
		driverWaitUtil.forElement(ExpectedConditions.visibilityOf(displayUserMenu));
		displayUserMenu.findElement(By.xpath("//a[3]")).click();
		return CriteriaPageFactory.criteriaInitWebElements(webDriver, SignatureMenuPage.class);
	}
	
	@Step(value = "Reopen Nitro Cloud Account's Home Page")
	public NitroCloudHomePage reopenNitroCloudHomePage(){
		pageLogo.click();
		return CriteriaPageFactory.criteriaInitWebElements(webDriver, NitroCloudHomePage.class);
	}
	
	@Step(value = "Logout from Nitro Cloud")
	public GoNitroLoginPage logout(){
		displayUserInput.click();
		driverWaitUtil.forElement(ExpectedConditions.visibilityOf(displayUserMenu));
		displayUserMenu.findElement(By.xpath("//a[contains(@href,'logout')]")).click();
		return CriteriaPageFactory.criteriaInitWebElements(webDriver, GoNitroLoginPage.class);
	}
	
	@Step(value = "ReLogin with another account: \"{1}\"")
	public NitroCloudHomePage reLogin(String url, String email, String password){
		GoNitroLoginPage loginPage = logout();
		//change Login page to cloud login page(they are not the same in DOM)
		loginPage = navigateToPageUrl(url, GoNitroLoginPage.class);
		NitroCloudHomePage homePage = loginPage.loginToNitroCloud(email, password);
		return homePage;
	}

}
	