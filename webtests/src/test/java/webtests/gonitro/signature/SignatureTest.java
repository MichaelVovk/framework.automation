package webtests.gonitro.signature;

import java.awt.AWTException;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import pages.gonitro.document.workflow.DocumentWorkflowPage;
import pages.gonitro.document.workflow.PrepareDocumentPage;
import pages.gonitro.document.workflow.SelfSignCompletedPage;
import pages.gonitro.home.GoNitroLoginPage;
import pages.gonitro.home.NitroCloudHomePage;
import pages.gonitro.signatures.NewSignaturePopUp;
import pages.gonitro.signatures.SignatureMenuPage;
import webtests.testbase.WebTestBase;
import framework.automation.driver.web.WebBrowser;
import framework.automation.driver.web.WebDriverFactory;
import framework.automation.page.web.WebPage;
import framework.utils.log.FrameworkLogger;
import framework.utils.log.LogFactory;

public class SignatureTest extends WebTestBase {

	private static final FrameworkLogger LOG = LogFactory
			.getLogger(SignatureTest.class);

	protected String url = "https://cloud.gonitrostage.com/";
	protected WebDriver webDriver;
	protected GoNitroLoginPage nitroLoginPage;

	@Parameters({ "browserName", "proxyEnabled" })
	@BeforeMethod(dependsOnMethods = { "setupTestBaseMethod" })
	public void setup(String browserName, String proxyEnabled) throws Exception {
		LOG.info("Before Test WebDriver Demo precondtion");

		WebBrowser browser = WebBrowser.getInstance(browserName, url,
				proxyEnabled);
		webDriver = WebDriverFactory.getWebDriver(browser);

		WebPage<GoNitroLoginPage> page = new GoNitroLoginPage(webDriver);
		nitroLoginPage = page.navigateToPageUrl(url, GoNitroLoginPage.class);

	}

	@AfterMethod
	// (dependsOnMethods = { "tearDownTestBaseMethod" })
	public void tearDown() {
		
		LOG.info("After Test WebDriver Demo killing driver");
		if (webDriver != null) {
			WebDriverFactory.killDriverInstance();
		}
		
	}

	@Test
	public void signatureTest() throws AWTException, InterruptedException {
		String accountLogin = "gonitrotest1@gmail.com";
		String accountPassword = "gonitrotest";
		String signatureText1 = "taras1";
		String signatureText2 = "taras2";
		String testDocumentLocation = "D:\\reject-test.png";
		
		NitroCloudHomePage nitroCloudHomePage = nitroLoginPage.loginToNitroCloud(accountLogin, accountPassword);
		SignatureMenuPage signatureMenuPage = nitroCloudHomePage.openSignaturesMenu();
		NewSignaturePopUp signaturePopUp = signatureMenuPage.createNewSignature(); 
		
		int createdSignatures1 = signatureMenuPage.getCreatedSignatures().size();
		signatureMenuPage = signaturePopUp.typeNewSignature(signatureText1, SignatureMenuPage.class);
		int createdSignatures2 = signatureMenuPage.getCreatedSignatures().size();
		Assert.assertTrue(createdSignatures2 > createdSignatures1, "signature wasn't created");
		
		String signatureCreatedFromAccountSettingsSource = signatureMenuPage.getLastCreatedSignatureSource();
		
		DocumentWorkflowPage addNewDocumentPage = nitroCloudHomePage.reopenNitroCloudHomePage()
												.openAddNewDocumentWindow().addNewDocument(testDocumentLocation);
		PrepareDocumentPage prepareDocument = addNewDocumentPage.clickSignInButton().setSigningOptionToMe();
		prepareDocument.setMyCreatedSignatureOnDocument(50,50,1);
		String setCreatedSignatureOnPrepareDocumentSource = prepareDocument.getSourceOfDocumentSignatureCreatedInMenu(0);
		Assert.assertEquals(signatureCreatedFromAccountSettingsSource, 
				setCreatedSignatureOnPrepareDocumentSource, "signatures have different source images");
		
		prepareDocument.clickNewSignature();
		prepareDocument.createAndAddSignatureToDocument(signatureText2,50,50,-1);
		
		String lastCreatedSignature = prepareDocument.getLastCreatedSignatureSource();
		String signatureCreatedInPrepare = prepareDocument.getSourceOfDocumentSignatureCreatedInPrepare(1);
		
		Assert.assertEquals(lastCreatedSignature, signatureCreatedInPrepare, "last created signature and alst added to doc are not the same");
		
		SelfSignCompletedPage signCompletedPage = prepareDocument.confirmSignatureOnDocumentSelfSign();
		
		Assert.assertTrue(signCompletedPage.isRequestCompleted(), "request is not completed correctly");
		
	}

}
