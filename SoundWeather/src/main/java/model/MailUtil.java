package model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class MailUtil {

	private static final String MAIL_USERNAME = "sounderweather.noreplay@gmail.com";
	private static final String MAIL_PASSWORD = "radoirumen";
	private static final String ENCRYPTON_KEY = "soundswe";


	private static Session initGmailSMTP() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(MAIL_USERNAME, MAIL_PASSWORD);
			}
		});
		return session;
	}

	public static void sendMail(String recipient, String subject, String text)
			throws AddressException, MessagingException {
		Message message = new MimeMessage(initGmailSMTP());

		message.setFrom(new InternetAddress(MAIL_USERNAME));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
		message.setSubject(subject);
		message.setText(text);
		Transport.send(message);

	}

	public static String encryptUsername(String username)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {

		byte[] input = username.getBytes("UTF-8");
		byte[] keyBytes = ENCRYPTON_KEY.getBytes("UTF-8");

		SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encrypted = cipher.doFinal(input);
		
		return URLEncoder.encode(Base64.encode(encrypted),"UTF-8");
	}
	public static String dencryptUsername(String username)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		
		
		byte[] input = Base64.decode(username);
		byte[] keyBytes = ENCRYPTON_KEY.getBytes("UTF-8");

		SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decrypted = cipher.doFinal(input);
		
		return URLDecoder.decode(new String(decrypted, Charset.forName("UTF-8")), "UTF-8");
	}
	
}
