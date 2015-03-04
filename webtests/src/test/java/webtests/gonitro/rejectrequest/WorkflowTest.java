package webtests.gonitro.rejectrequest;

import java.awt.AWTException;
import java.io.IOException;

import javax.mail.MessagingException;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import pages.gonitro.document.AddNewDocumentPage;
import pages.gonitro.document.GoNitroViewDocumentRequestPage;
import pages.gonitro.document.ReadOnlyVersionPage;
import pages.gonitro.document.workflow.PreapareCompletedPage;
import pages.gonitro.document.workflow.DocumentWorkflowPage;
import pages.gonitro.document.workflow.PrepareDocumentPage;
import pages.gonitro.home.GoNitroLoginPage;
import pages.gonitro.home.NitroCloudHomePage;
import pages.gonitro.home.SwitchAccountsPopUp;
import utils.gonitro.MailUtilClient;
import webtests.testbase.WebTestBase;
import framework.automation.driver.web.WebBrowser;
import framework.automation.driver.web.WebDriverFactory;
import framework.automation.page.web.WebPage;
import framework.utils.log.FrameworkLogger;
import framework.utils.log.LogFactory;

public class WorkflowTest extends WebTestBase {

	private static final FrameworkLogger LOG = LogFactory
			.getLogger(WorkflowTest.class);

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
	public void rejectRequestTest() throws IOException, AWTException, InterruptedException, MessagingException {
		String accountLogin = "gonitrotest1@gmail.com";
		String accountPassword = "gonitrotest";
		String accountMailPassword = "nitrotest";
		String testDocumentLocation = "D:\\reject-test.png";
		
		NitroCloudHomePage nitroCloudHomePage = nitroLoginPage.loginToNitroCloud(accountLogin, accountPassword);
		AddNewDocumentPage newDocPage = nitroCloudHomePage.openAddNewDocumentWindow();
		DocumentWorkflowPage docWorkFlowPage = newDocPage.addNewDocument(testDocumentLocation);
		
		String receiverAccountName = "Taras";
		String receiverAccountLogin = "gonitrotest2@gmail.com";
		String receiverAccountPassword = "gonitrotest";
		
		PrepareDocumentPage prepareDocument = docWorkFlowPage.clickSignInButton().setSigningOptionToOthers(receiverAccountName, receiverAccountLogin);
		
		String subject = "reject-test";
		String requestSubject = MailUtilClient.generateMailSubjectName(subject);
	
		PreapareCompletedPage completeRequest = prepareDocument.setSignatureOnDocumentOthers().sendRequest(requestSubject);
		
		Assert.assertTrue(completeRequest.isRequestCompleted(), "request was not completed");
		
		MailUtilClient gmailClient1 = new MailUtilClient(receiverAccountLogin, receiverAccountPassword);
		gmailClient1.waitForMailWithSubject(requestSubject);
			
		SwitchAccountsPopUp switchAccounts = completeRequest.navigateToPageUrl(gmailClient1.getSignUrlFromMailContent(requestSubject, url), SwitchAccountsPopUp.class);
		GoNitroViewDocumentRequestPage requestDocPage = switchAccounts.confirmSwitchAccounts();
		
		ReadOnlyVersionPage readOnlyPage = requestDocPage.rejectRquest().confirmRejectRequest();
		Assert.assertTrue(readOnlyPage.isReadOnlyPageDisplayed(), "Request page wasn't closed");
		
		MailUtilClient gmailClient2 = new MailUtilClient(accountLogin, accountMailPassword);
		
		try {
			
			gmailClient2.waitForMailWithSubject(subject);
			Assert.assertTrue(gmailClient2.searchEmailFromUnread("rejected your request to sign \"" + subject + "\"") != null);
			
		} finally{
			gmailClient2.readAllUnreadMails();
		}

	}

}
