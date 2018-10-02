package es.source.code.utils;

/**
 * Author: taoye
 * Classname: Const.java
 * Description: 常量类
 * Date: 2018/9/29.
 */

public class Const {

    public static class IntentMsg {
        public static final String MESSAGE = "Message";
        // 从SCOSEntry启动MainScreen
        public static final String FROM_ENTRY = "FromEntry";
        // 登录成功，从LoginOrRegister返回MainScreen
        public static final String LOGIN_SUCC = "LoginSuccess";
        // 从LoginOrRegister的“返回键”返回MainScreen
        public static final String RETURN = "Return";
        // 从外部第三方APP跳转MainScreen
        public static final String FROM_THIRDPARTY = "FromThirdParty";
    }

    public static class InputRegularExpr {
        // 设置uid的正则表达式。不能为空，只能输入英文大小写和数字
        public static final String REGULAR_UID = "^[1-9a-zA-Z]+$";
        // 设置pwd的正则表达式。不能为空，只能输入英文大小写和数字
        public static final String REGULAR_PWD = "^[1-9a-zA-Z]+$";
    }

    public static class RequestCode {
        // MainScreen启动LoginOrRegister
        public static final int LOGINORPEGISTER = 0;
    }

    public static class RespondCode {
        // LoginOrRegister“返回键”返回MainScreen
        public static final int RETURN = 0;
        // 成功登录
        public static final int LOGINSUCC = 1;
    }

    public static class SetError {
        // 提示uid格式错误
        public static final String UID_FROMAT_ERROR = "输入内容不符合规则";
        // 提示uid超过字符数限制
        public static final String UID_NUMBER_ERROR = "登录名不得多于50位";
        // 提示pwd格式错误
        public static final String PWD_FROMAT_ERROR = "输入内容不符合规则";
        // 提示pwd未达到字符数要求
        public static final String PWD_NUMBER_ERROR = "密码不得少于6位";
    }
}