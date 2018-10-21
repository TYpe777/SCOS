package es.source.code.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import es.source.code.model.MailInfo;

/**
 * @author taoye
 * @date 2018/10/18.
 * @classname MailSender.java
 * @description 简单邮件发送器，只能发送文本格式邮件
 */
public class MailSender {
    /**
     * 以文本格式发送邮件
     * @param mailInfo
     * @return
     */
    public boolean sendTextMail(MailInfo mailInfo){
        Authenticator authenticator = null;
        Properties properties = mailInfo.getProperties();
        if(mailInfo.isVaildate()){
            authenticator = new MailAuthenticator(mailInfo.getUserName(),mailInfo.getPassword());
        }

        Session sendMailSession = Session.getDefaultInstance(properties,authenticator);

        try{
            Message mailMsg = new MimeMessage(sendMailSession);
            Address from = new InternetAddress(mailInfo.getFromAddress());
            mailMsg.setFrom(from);
            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMsg.setRecipient(Message.RecipientType.TO,to);
            mailMsg.setSubject(mailInfo.getSubject());
            mailMsg.setSentDate(new Date());
            mailMsg.setText(mailInfo.getContent());
            Transport.send(mailMsg);
            return true;
        }catch(MessagingException e){
            e.printStackTrace();
        }
        return false;
    }
}
