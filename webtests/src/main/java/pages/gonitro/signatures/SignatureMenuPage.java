package pages.gonitro.signatures;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import ru.yandex.qatools.allure.annotations.Step;
import framework.automation.page.CriteriaPageFactory;
import framework.automation.page.web.WebPage;

public class SignatureMenuPage extends WebPage<SignatureMenuPage> {
	
	@FindBy (how = How.XPATH, using = "//a[contains(@class,'button blue') and parent::*[following-sibling::*[contains(@class,'signatures')]]]")
	public WebElement createNewSignature;
	
	@FindBy (how = How.XPATH, using = "//div[contains(@class,'signatures content')]/ul[contains(@class,'entries')]")
	public WebElement signaturesEntries;
	
	public SignatureMenuPage(WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public ExpectedCondition<WebElement> loadedCriteria() {
		return ExpectedConditions.visibilityOf(createNewSignature);
	}
	
	@Step(value = "Create new signature")
	public NewSignaturePopUp createNewSignature(){
		createNewSignature.click();
		return CriteriaPageFactory.criteriaInitWebElements(webDriver, NewSignaturePopUp.class);
	}
	
	@Step(value = "Get created signatres")
	public List<WebElement> getCreatedSignatures(){
		return signaturesEntries.findElements(By.xpath(".//li"));
	}
	
	@Step(value = "Get last created signature source")
	public String getLastCreatedSignatureSource(){
		return getCreatedSignatures().get(0).findElement(By.xpath("//a[contains(@class,'display-user-input')]")).getText();
	}

}
