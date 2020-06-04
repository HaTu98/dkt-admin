package vn.edu.vnu.uet.dktadmin.dto.service.sendMail;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.dao.admin.AdminDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Admin;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Service
public class ResetPassword extends Thread{
    private final int LENGTH_PASSWORD = 10;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void resetPassword(String email) throws MessagingException {
        Admin admin = adminDao.getByEmail(email);
        if (admin == null) return;
        String password = generateRandomPassword(LENGTH_PASSWORD);
        String passwordEncode = passwordEncoder.encode(password);
        admin.setPassword(passwordEncode);
        adminDao.save(admin);
        sendResetPassword(email, password,admin.getFullName());
    }

    public void sendResetPassword(String to, String password, String name) throws MessagingException {
        String subject = "Reset account dkt";
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        String content = getContent(password,name);
        message.setContent(content,"text/html");
        Thread job = new Thread(() -> emailSender.send(message));
        job.start();
    }

    private String getContent(String password, String name) {
        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\t<title>Đặt lại tài khoản hệ thống Đăng Ký Thi</title>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"+
                "</head>\n" +
                "<body>\n" +
                "\t<p>Dear <b>" + name +"</b>,</p>\n" +
                "<p>Mật khẩu của bạn đã được reset. Mật khẩu mới là:<b>" + password + "</b></p>" +
                "</body>\n" +
                "</html>";
        return content;
    }

    public String generateRandomPassword(int len) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        return RandomStringUtils.random(len, chars);
    }
}
