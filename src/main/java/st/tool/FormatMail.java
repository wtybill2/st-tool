package st.tool;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class FormatMail {
    // foxmail
    // outlook

    private String[] to;   // 收件人电子邮箱
    private String   from; // 发件人电子邮箱
    private String   pass;
    // private String hostSend; // 指定发送邮件的主机 smtp.qq.com
    // private Session session; // 获取默认session对象
    private MimeMessage message; // 创建默认的 MimeMessage 对象

    public FormatMail(String[] to, String from, String pass, String hostSend, String portSend) {
        this.to = to;
        this.from = from;
        this.pass = pass;
        init(hostSend, portSend);
    }

    public void init(String hostSend, String portSend) {
        // 获取系统属性
        // Properties properties = System.getProperties();
        Properties properties = new Properties();
        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", hostSend);
        properties.setProperty("mail.smtp.port", portSend);
        properties.put("mail.smtp.auth", "true");
        // Session session = Session.getDefaultInstance(properties);
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass); // 发件人邮件用户名、密码
            }
        });
        // 创建默认的 MimeMessage 对象
        message = new MimeMessage(session);
    }

    public void send(String subject, String content) throws MessagingException {
        // Set From:
        message.setFrom(new InternetAddress(from));
        // Set To:
        for (String t : to) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(t));
        }
        // Set Subject:
        message.setSubject(subject);
        // 设置消息体
        message.setText(content);
        // 或者 发送 HTML 消息, 可以插入html标签
        // message.setContent("<h1>This is actual message</h1>",
        // "text/html;charset=utf-8" );

        // 发送消息
        Transport.send(message);
        System.out.println("Sent message successfully....");
    }

    public static void main(String[] args) throws MessagingException {
        String[] to = { "2530999359@qq.com" };// 收件人
        String from = "444677717@qq.com";// 发件人
        String pass = " ";
        String hostSend = "smtp.qq.com";
        String portSend = "587";
        FormatMail ms = new FormatMail(to, from, pass, hostSend, portSend);
        ms.send("AAA", "BBB");
    }
}
