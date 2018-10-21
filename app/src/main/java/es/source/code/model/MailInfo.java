package es.source.code.model;

import java.util.Properties;

/**
 * @author taoye
 * @date 2018/10/18.
 * @classname MailInfo/java
 * @description 简单邮件类，不包含附件
 */
public class MailInfo {
    private String mailServerHost; // 发送邮件的服务器的IP
    private String mailServerPost; // 发送邮件的服务器的端口
    private String fromAddress; // 邮件发送方的邮箱地址
    private String toAddress; // 邮件接收方的邮箱地址
    private String userName; // 登录邮件发送服务器的用户名
    private String password; // 登录邮件发送服务器的授权码
    private boolean vaildate = true; // 是否需要身份认证
    private String subject; // 邮件的主题
    private String content; //  邮件的内容

    // 获取邮件会话的属性
    public Properties getProperties(){
        Properties properties = new Properties();
        properties.put("mail.smtp.host",this.mailServerHost);
        properties.put("mail.smtp.port",this.mailServerPost);
        properties.put("mail.smtp.auth",vaildate?"true":"false"); // 不能写成properties.put("mail.smtp.auth",this.vaildate);否则会报错
        return properties;
    }

    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerPost() {
        return mailServerPost;
    }

    public void setMailServerPost(String mailServerPost) {
        this.mailServerPost = mailServerPost;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isVaildate() {
        return vaildate;
    }

    public void setVaildate(boolean vaildate) {
        this.vaildate = vaildate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
