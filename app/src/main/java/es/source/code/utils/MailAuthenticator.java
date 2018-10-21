package es.source.code.utils;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author taoye
 * @date 2018/10/18.
 * @classname MailAuthenticator.java
 * @description 登录邮箱时，进行身份认证
 */
public class MailAuthenticator extends Authenticator{
    String userName = null;// 邮箱登录名，即邮箱地址
    String password = null;// 邮箱密码，这里是授权码

    public MailAuthenticator(){}

    public MailAuthenticator(String userName,String password){
        this.userName = userName;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(userName,password);
    }
}
