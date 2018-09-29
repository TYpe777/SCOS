package es.source.code.utils;

/**
 * 设置uid和pwd的正则表达式
 * Created by taoye on 2018/9/27.
 */
public class InputRegularExpr {
//    设置uid的正则表达式。不能为空，只能输入英文大小写和数字
    public static final String REGULAR_UID = "^[A-Za-z1-9_-]+$";
//    设置pwd的正则表达式。不能为空，只能输入英文大小写和数字
    public static final String REGULAR_PWD = "^[A-Za-z1-9_-]+$";
}
