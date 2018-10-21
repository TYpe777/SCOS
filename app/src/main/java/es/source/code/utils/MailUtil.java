package es.source.code.utils;

import android.util.Log;

import es.source.code.model.MailInfo;

/**
 * @author taoye
 * @date 2018/10/18.
 * @classname MailUtil.java
 * @description 发送求助邮件
 */
public class MailUtil {
    // 从163邮箱发送给QQ邮箱
    private static final String HOST = "smtp.163.com";
    private static final String PORT = "25";
    private static final String FROM_ADDRESS = "taoy97@163.com";
    private static final String FROM_PASSWORD = "pop3smtptaoy97";
    private static final String TO_ADDRESS = "970149848@qq.com";

//    // 从QQ邮箱发送给163邮箱
//    private static final String HOST = "smtp.qq.com";
//    private static final String PORT = "587";
//    private static final String FROM_ADDRESS = "970149848@qq.com";
//    private static final String FROM_PASSWORD = "zdgxkvqkfragbdac";
//    private static final String TO_ADDRESS = "taoy97@163.com";

    /**
     * 创建一个邮件实例
     * @param subject
     * @param content
     * @return
     */
    private static MailInfo createMail(String subject,String content){
        final MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost(HOST);
        mailInfo.setMailServerPost(PORT);
        mailInfo.setVaildate(true);
        mailInfo.setUserName(FROM_ADDRESS);
        mailInfo.setPassword(FROM_PASSWORD);
        mailInfo.setFromAddress(FROM_ADDRESS);
        mailInfo.setToAddress(TO_ADDRESS);
        mailInfo.setSubject(subject);
        mailInfo.setContent(content);
        return mailInfo;
    }

    /**
     * 发送文本格式的邮件
     * @param subject
     * @param content
     * @return
     */
    public static boolean autoSendMail(String subject,String content){
        try{
            final MailInfo mailInfo = createMail(subject,content);
            final MailSender mailSender = new MailSender(); // 邮件发送器
            boolean b = mailSender.sendTextMail(mailInfo); // 判断邮件是否发送成功
            Log.i("邮件状态","发送" + b);
            return b;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false; // 发送失败
    }
}
