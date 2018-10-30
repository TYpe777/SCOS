package es.source.code.utils;

/**
 * @author: taoye
 * @date: 2018/9/29.
 * @classname: Const.java
 * @description: 常量类
 */

public class Const {

    public static class IntentMsg {
        // 标记Intent携带的数据是信息，字符串类型
        public static final String MESSAGE = "Message";
        // 标记Intent携带的数据是用户对象，User类型
        public static final String USER = "User";
        // 标记Intent携带的数据是初始默认页，int类型
        public static final String DEFAULTPAGE = "DefaultPage";
        // 标记Intent携带的数据是菜单，即List<Food>
        public static final String FOODLIST = "Foodlist";
        // 标记Intent携带的数据是菜单列表的下标，用于菜品详细页，int类型
        public static final String INDEX = "Index";
        // 标记Intent携带的数据是菜品，即Food
        public static final String FOOD = "Food";

        // 初始默认页为“未下单菜”
        public static final int PAGE_UNORDERED = 0;
        // 初始默认页为“已下单菜”
        public static final int PAGE_ORDERED = 1;

        // loginState为0，未登录
        public static final String MSG_NOT_LOGIN = "NotLogin";
        // 从SCOSEntry启动MainScreen。loginState为1，已登录
        public static final String MSG_FROM_ENTRY = "FromEntry";
        // 登录成功，从LoginOrRegister返回MainScreen
        public static final String MSG_LOGIN_SUCC = "LoginSuccess";
        public static final String MSG_REGISTER_SUCC = "RegisterSuccess";
        // 从LoginOrRegister的“返回键”返回MainScreen
        public static final String MSG_RETURN = "Return";
    }

    public static class InputRegularExpr {
        // 设置uid的正则表达式。不能为空，只能输入英文大小写和数字
        public static final String REGULAR_UID = "^[1-9a-zA-Z]+$";
        // 设置pwd的正则表达式。不能为空，只能输入英文大小写和数字
        public static final String REGULAR_PWD = "^[1-9a-zA-Z]+$";
    }

    public static class RequestCode {
        // MainScreen
        public static final int MAINSCREEN = 0;
        // FoodView 的已点菜品
        public static final int FOODVIEW_ORDERED = 10;
        // FoodView 的查看订单
        public static final int FOODVIEW_VIEWORDER = 11;
        // FoodView点击菜品项，显示菜品详细信息
        public static final int FOODVIEW_FOODDETAILED=12;
    }

    public static class ResultCode {
        // LoginOrRegister返回MainScreen
        public static final int FROM_LOGINORREGISTER = 0;
        // FoodView返回MainScreen
        public static final int FROM_FOODVIEW = 1;
        // FoodOrderView返回MainScreen或FoodView
        public static final int FROM_FOODORDERVIEW = 2;
        // FoodDetailed返回FoodView
        public static final int FROM_FOODDETAILED = 3;
        // SCOSHelper返回MainScreen
        public static final int FROM_SCOSHELPER = 4;
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
