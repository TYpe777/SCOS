package es.source.code.utils;

import android.content.Context;

import java.util.List;

import es.source.code.model.Food;

/**
 * @author taoye
 * @date 2018/10/29.
 * @classname SeafoodProvider.java
 * @description 海鲜菜单的提供者类，从Json文件中读取菜单
 */
public class SeafoodProvider extends FoodListProvider{
    private final static String LISTNAME = "Seafood"; // 菜单的名称

    public static List<Food> getList(Context mContext){
        return getFoodsFromJSON(LISTNAME, mContext);
    }
}
