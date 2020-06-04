package vn.edu.vnu.uet.dktadmin.dto.service.sendMail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class ForgotPassword extends Thread{
    @Autowired
    private JavaMailSender emailSender;

    public void forgotPassword(String to, String link, String name) throws MessagingException {
        String subject = "Reset account dkt";
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        String content = getContent(link,name);
        message.setContent(content,"text/html");
        Thread job = new Thread(() -> emailSender.send(message));
        job.start();
    }

    private String getContent(String link, String name) {
        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\t<title>Forgot password email</title>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"+
                "</head>\n" +
                "<body>\n" +
                "\t<p>Dear <b>" + name +"</b>,</p> <br/>\n" +
                "\t<p><b>Để reset mật khẩu hệ thống Đăng Ký Thi bạn vui lòng click vào link:</b><p>\n" + link +
                "\t<p>Nếu yêu cầu reset mật khẩu hệ thống đăng ký thi không phải từ bạn. Bạn có thể bỏ qua email này </p>  \n" +
                "</body>\n" +
                "</html>";
        return content;
    }

}
