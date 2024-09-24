package br.com.systems.fenix.API_Fenix.Service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import br.com.systems.fenix.API_Fenix.Service.EmailService;
import br.com.systems.fenix.API_Fenix.utils.EmailUtils;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final String UTF_8_ENCO = "UTF-8";

    private static final String NEW_USER_ACCOUNT_VERIFICATION = "Registration Confirmation!";

    private static final String NEW_USER_COUPON = "Coupon Registed!";

    private static final String EMAIL_TEMPLATE = "emailTemplate";

    private static final String TEXT_HTML = "text/html";

    @Value("spring.mail.username")
    private String fromEmail;

    private final String HOST = "http://localhost:8080/";

    private final JavaMailSender eMailSender;

    private final TemplateEngine templateEngine;

    @Override
    @Async
    public void sendSimpleMailMessage(String name, String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(EmailUtils.getEmailMessageResetPassword(name, HOST, token));
            eMailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Async
    public void sendSimpleCoupomMessage(String name, String to, String text, String promotionNameString) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_COUPON);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(EmailUtils.getEmailMessageCoupom(name, HOST, text, promotionNameString));
            eMailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Async
    public void sendSimpleMailMessageResetPassword(String name, String to, String token, String subject) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(subject);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(EmailUtils.getEmailMessageResetPassword(name, HOST, token));

            eMailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Async
    public void sendMineMessageWithAttachments(String name, String to, String token) {
        try {
            MimeMessage message = getMineMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCO);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(EmailUtils.getEmailMessage(name, HOST, token));

            // FileSystemResource fenix = new FileSystemResource(
            // new File(System.getProperty("user.home") + "/Pictures/Saved
            // Pictures/fenix_systems_logo.jpg"));

            // helper.addAttachment(fenix.getFilename(), fenix);

            helper.addInline("image",
                    new ClassPathResource("C:\\Users\\Fenix Systems\\Pictures\\Saved Pictures>fenix_systems_logo.jpg"));
            eMailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    @Async
    public void sendMineMessageWithEmbeddedFiles(String name, String to, String token) {
        try {
            MimeMessage message = getMineMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCO);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(EmailUtils.getEmailMessage(name, HOST, token));

            FileSystemResource fenix = new FileSystemResource(
                    new File(System.getProperty("user.home") + "/Pictures/Saved Pictures/fenix_systems_logo.jpg"));

            helper.addInline(getContentId(fenix.getFilename()), fenix);
            eMailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmail(String name, String to, String token) {
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("url", EmailUtils.getVerificationUrl(HOST, token));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);

            MimeMessage message = getMineMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCO);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);
            eMailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("url", EmailUtils.getVerificationUrl(HOST, token));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);

            MimeMessage message = getMineMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCO);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);

            MimeMultipart mimeMultipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text, TEXT_HTML);
            mimeMultipart.addBodyPart(messageBodyPart);

            BodyPart imageBodyPart = new MimeBodyPart();
            String imagePath = System.getProperty("user.home") + "/Pictures/Saved Pictures/fenix_systems_logo.jpg";
            DataSource dataSource = new FileDataSource(imagePath);
            imageBodyPart.setDataHandler(new DataHandler(dataSource));
            imageBodyPart.setHeader("Content-ID ", "<image>");
            mimeMultipart.addBodyPart(imageBodyPart);

            message.setContent(mimeMultipart);

            eMailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error sending email: " + e.getMessage());
        }
    }

    private MimeMessage getMineMessage() {
        return eMailSender.createMimeMessage();
    }

    private String getContentId(String fileName) {
        return "<" + fileName + ">";
    }

}
