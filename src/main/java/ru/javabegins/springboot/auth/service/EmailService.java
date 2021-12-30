package ru.javabegins.springboot.auth.service;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.Future;

@Service
@Log
public class EmailService {
    @Value("${client.url}")
    private String clientURL;

    @Value("${email.from}")
    private String emailFrom;

    private JavaMailSender sender;

    @Autowired
    public EmailService(JavaMailSender sender) {
        this.sender = sender;
    }

    @Async
    public Future<Boolean> sendActivationEmail(String email, String username, String uuid) {
        try {
            MimeMessage mimeMessage=sender.createMimeMessage();
            MimeMessageHelper message=new MimeMessageHelper(mimeMessage, "UTF-8");
            String url= clientURL +"activate-account/"+uuid;
            String htmlMsg=String.format(
                    "Добрий день!<br/><br/>" +
                            "Ви створили екааунт для веб-застосунку \"Планувальник справ\": %s <br/><br/>" +
                            "<a href='%s'>%s</a><br/><br/>",  username, url, "Для підтвердження регістрації натиснить на посилання");
            mimeMessage.setContent(htmlMsg, "text/html");

            message.setTo(email);
            message.setFrom(emailFrom);
            message.setSubject("Активація екаунту");
            message.setText(htmlMsg, true);

            sender.send(mimeMessage);
            return new AsyncResult<>(true);
        }
        catch (MessagingException e){
            e.printStackTrace();
        }
        System.out.println("NOT SENT===========================");
        return new AsyncResult<>(false);
    }

    @Async
    public Future<Boolean> sendResetPasswordEmail(String email, String token) {
        try {
            MimeMessage mimeMessage = sender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");

            String url = clientURL + "/update-password/" + token;
            String htmlMsg = String.format(
                    "Добрий день.<br/><br/>" +
                            "Хтось зробив запит зміни пароля для застосунку  \"Планувальник справ\".<br/><br/>" +
                            "Якщо це були не ви - не робіть нічого,можете просто видалити цей лист.<br/><br/> Для зміни пароля натиснить на посилання: <br/><br/> " +
                            "<a href='%s'>%s</a><br/><br/>", url, "Змінити пароль");
            mimeMessage.setContent(htmlMsg, "text/html");
            message.setTo(email);
            message.setSubject("Зміна пароля");
            message.setFrom(emailFrom);
            message.setText(htmlMsg, true);
            sender.send(mimeMessage);

            return new AsyncResult<>(true);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(false);
    }



}
