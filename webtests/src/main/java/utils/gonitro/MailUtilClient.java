package utils.gonitro;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import ru.yandex.qatools.allure.annotations.Step;

import com.google.common.base.Predicate;

import framework.automation.utils.wait.WaitUtil;

public class MailUtilClient {

	private Store store;

	public MailUtilClient(String accountLogin, String accountPassword) {
		try {
			connectToGmailServer(accountLogin, accountPassword);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Step(value = "Connect to gmail mail server with login: \"{0}\"")
	private void connectToGmailServer(String accountLogin,
			String accountPassword) throws MessagingException {
		Properties mailServerProperties;
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.host", "gmail.com");
		mailServerProperties.put("mail.smtp.socketFactory.port", "465");
		mailServerProperties.put("mail.smtp.socketFactory.port", "465");
		mailServerProperties.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.port", "true");
		Session getMailSession = Session.getDefaultInstance(
				mailServerProperties, null);

		store = getMailSession.getStore("imaps");
		store.connect("smtp.gmail.com", accountLogin, accountPassword);
	}

	@Step(value = "Wait for mail with subject: \"{0}\"")
	public void waitForMailWithSubject(final String subject) {
		WaitUtil<String> waitUtil = new WaitUtil<String>(subject);
		waitUtil = waitUtil.withTimeout(300);
		waitUtil.forCondition(new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return searchEmailFromUnread(subject) != null;
			}
		});
	}	

	@Step(value = "Generate email theme with subject: \"{0}\"")
	public static String generateMailSubjectName(String subject) {
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss");
		return subject + "-" + sdf.format(cal.getTime());
	}
	
	// we are checking last 10 emails for account to be sure mail received with
	// unique subject
	@Step(value = "Search for email that contain subject: \"{0}\" from unread emails")
	public Message searchEmailFromUnread(String subject) {
		try {
			// opens inbox mail folder and search for mail
			Folder folderInbox = store.getFolder("Inbox");
			folderInbox.open(Folder.READ_WRITE);
			Flags seen = new Flags(Flags.Flag.SEEN);
			FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
			Message unreadMessages[] = folderInbox.search(unseenFlagTerm);
			for (int i = 0; i < unreadMessages.length; i++) {
				Message message = unreadMessages[i];
				if (message.getSubject().contains(subject)) {
					return message;
				}
			}

		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Step(value = "Search for email that contain subject: '\"{0}\"' from unread emails and read it")
	public Message searchEmailFromUnreadAndReadIt(String subject) {
		Message message = searchEmailFromUnread(subject);
		try {
			readMail(subject);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
	
	private void readMail(String subject) throws MessagingException {
		Message message = searchEmailFromUnread(subject);
		message.setFlag(Flags.Flag.SEEN, true);
	}
	
	@Step(value = "Read all unreaded emails")
	public void readAllUnreadMails() {
		try {
			Folder folderInbox = store.getFolder("Inbox");

			folderInbox.open(Folder.READ_WRITE);

			Flags seen = new Flags(Flags.Flag.SEEN);
			FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
			Message unreadMessages[] = folderInbox.search(unseenFlagTerm);
			for (int i = 0; i < unreadMessages.length; i++) {
				Message message = unreadMessages[i];
				String subject = message.getSubject();
				readMail(subject);
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	@Step(value = "Get received email html content for parsing")
	private String getMailHtmlContent(String subject) throws IOException,
			MessagingException {
		Message message = searchEmailFromUnreadAndReadIt(subject);
		if (message != null) {
			if (message instanceof MimeMessage) {
				MimeMessage mimeMessage = (MimeMessage) message;
				Object contentObject = mimeMessage.getContent();
				String mailHtmlContent = null;
				if (contentObject instanceof Multipart) {
					Multipart content = (Multipart) contentObject;
					int count = content.getCount();
					for (int multiPartIndex = 0; multiPartIndex < count; multiPartIndex++) {
						MimeMultipart mmpart = (MimeMultipart) content.getBodyPart(
								multiPartIndex).getContent();
						for (int multiPartBodyPartIndex = 0; multiPartBodyPartIndex < mmpart
								.getCount(); multiPartBodyPartIndex++) {
							if (mmpart.getBodyPart(multiPartBodyPartIndex)
									.getContentType().contains("HTML")) {
								mailHtmlContent = mmpart
										.getBodyPart(multiPartBodyPartIndex)
										.getContent().toString();
								break;
							}
						}
						return mailHtmlContent;
					}
				}
			}
		}
		return null;
	}
	
	@Step(value = "Get unsubcscribe url from mail with subject: \"{0}\"")
	public String getUnsubscribeURLFromMailHTMLContent(String subject) throws IOException, MessagingException{
		String html = getMailHtmlContent(subject);
		if (html != null) {
			Document doc = Jsoup.parse(html);
			String result = doc.getElementsContainingOwnText("Unsubscribe from these notifications").get(0).attr("href");
			return result;
		} else throw new RuntimeException("no html source found in requested mail with subject " + subject);
	}

	// get the gonitro Url from mail html body
	@Step(value = "Get Go Nitro sign in url from received email with subject: \"{0}\"")
	public String getSignUrlFromMailContent(String subject, String signUrlDomen) throws IOException, MessagingException {
		String html  = getMailHtmlContent(subject);
		if (html != null) {
			Document doc = Jsoup.parse(html);
			String result = doc.getElementsByAttributeValueContaining("href", signUrlDomen + "sign").get(0).attr("href");
			return result;
		} else throw new RuntimeException("no html source found in requested mail with subject " + subject);

	}

}
