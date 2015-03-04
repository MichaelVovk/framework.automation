package webtests.gonitro.notifications;

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
import pages.gonitro.document.workflow.DocumentWorkflowPage;
import pages.gonitro.document.workflow.PreapareCompletedPage;
import pages.gonitro.document.workflow.PrepareDocumentPage;
import pages.gonitro.home.GoNitroLoginPage;
import pages.gonitro.home.NitroCloudHomePage;
import pages.gonitro.notifications.NotificationsPage;
import pages.gonitro.notifications.UnsubscribeCompletedPage;
import utils.gonitro.MailUtilClient;
import webtests.gonitro.rejectrequest.WorkflowTest;
import webtests.testbase.WebTestBase;
import framework.automation.driver.web.WebBrowser;
import framework.automation.driver.web.WebDriverFactory;
import framework.automation.page.web.WebPage;
import framework.utils.log.FrameworkLogger;
import framework.utils.log.LogFactory;

public class SigningNotifications extends WebTestBase {

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
		NotificationsPage.updateNotificationsForCurrentAccount(webDriver);
		LOG.info("After Test WebDriver Demo killing driver");
		if (webDriver != null) {
			WebDriverFactory.killDriverInstance();
		}
		
	}

	@Test
	public void signingNotificationTest() throws IOException, AWTException, InterruptedException, MessagingException {
		String accountLogin = "gonitrotest1@gmail.com";
		String accountPassword = "gonitrotest";
		String testDocumentLocation = "D:\\reject-test.png";
		
		NitroCloudHomePage nitroCloudHomePage = nitroLoginPage.loginToNitroCloud(accountLogin, accountPassword);
		NotificationsPage.updateNotificationsForCurrentAccount(webDriver);
		AddNewDocumentPage newDocPage = nitroCloudHomePage.openAddNewDocumentWindow();
		DocumentWorkflowPage docWorkFlowPage = newDocPage.addNewDocument(testDocumentLocation);
		
		String receiverAccountName = "Taras";
		String receiverAccountLogin = "gonitrotest2@gmail.com";
		String receiverAccountPassword = "gonitrotest";
		
		PrepareDocumentPage prepareDocument = docWorkFlowPage.clickSignInButton().setSigningOptionToOthers(receiverAccountName, receiverAccountLogin);
		
		String subject = "notifications-test1";
		String requestSubject = MailUtilClient.generateMailSubjectName(subject);
	
		PreapareCompletedPage completeRequest = prepareDocument.setSignatureOnDocumentOthers().sendRequest(requestSubject);
		
		Assert.assertTrue(completeRequest.isRequestCompleted(), "request was not completed");
		
		nitroCloudHomePage = completeRequest.closeCompletedPage().reLogin(url, receiverAccountLogin, receiverAccountPassword);
		NotificationsPage.updateNotificationsForCurrentAccount(webDriver);
		
		MailUtilClient gmailClient2 = new MailUtilClient(receiverAccountLogin, receiverAccountPassword);
		try {
			gmailClient2.waitForMailWithSubject(requestSubject);
			UnsubscribeCompletedPage unscscrPage = completeRequest.navigateToPageUrl(gmailClient2.getUnsubscribeURLFromMailHTMLContent(requestSubject), UnsubscribeCompletedPage.class);
			NotificationsPage notsPage = unscscrPage.clickManageNotsButton();
			
			Assert.assertFalse(notsPage.receiveDocumentToSignCheckBox.isSelected(), "receive document to sign checkbox is selected");
			
		} finally{
			gmailClient2.readAllUnreadMails();
		}
	}

}