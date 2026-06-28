package ai.niksar.contract_wisor_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.model.Notification;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailService {
    @Value("${company.mail.noreply}")
    private String companyMailNoreply;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    private  Notification notification=new Notification();
    @Autowired
    private NotificationService notificationService;
    @Autowired
    @Lazy
    private ResetPasswordService resetPasswordService;
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String to, String subject, String body, UUID resetPassId) {
        try {
            if(to == null || to.equals("")){
                throw new ContractWisorException.E033();
            }
            notification=createNotification(to,subject,body);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(companyMailNoreply);
            mailSender.send(message);
            successNotificationSave(notification);
        }
        catch (Exception ex){
            resetPasswordService.deleteResetPass(resetPassId);
            failNotificationSave(notification);
            throw  new ContractWisorException.E034();
        }
    }

    @Async
    public void sendMailAsync(String to, String subject, String body) {
        try {
            if(to == null || to.equals("")){
                throw new ContractWisorException.E033();
            }
            notification=createNotification(to,subject,body);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(companyMailNoreply);
            mailSender.send(message);
            successNotificationSave(notification);
        }
        catch (Exception ex){
            failNotificationSave(notification);
            logger.error("An error occurred while sending the email: " +ex.getMessage());
        }
    }
    public Notification createNotification(String to, String subject, String body){
        Notification tempNotification=new Notification();
        tempNotification.setCreateDate(LocalDateTime.now());
        tempNotification.setType("mail");
        tempNotification.setStatus("0");
        tempNotification.setNotificationFrom(companyMailNoreply);
        tempNotification.setNotificationTo(to);
        tempNotification.setBody(body);
        tempNotification.setSubject(subject);
        return notificationService.saveNotification(tempNotification);
    }
    public void successNotificationSave(Notification notification){
        notification.setSendDate(LocalDateTime.now());
        notification.setStatus("2");
        notificationService.saveNotification(notification);
    }
    public void failNotificationSave(Notification notification){
        notification.setStatus("1");
        notificationService.saveNotification(notification);
    }
}
