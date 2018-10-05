package es.source.code.utils;

import java.util.ArrayList;
import java.util.List;

import es.source.code.model.Food;

/**
 * @author taoye
 * @date 2018/10/5.
 * @classname FoodList.java
 * @description 菜单。在FoodView中使用
 */
public class FoodList {
    private  List<Food> HotDishes;
    private  List<Food> ColdDishes;
    private  List<Food> Seafood;
    private  List<Food> Drinks;

    public FoodList(){
        HotDishes = new ArrayList<Food>();
        HotDishes.add(new Food("梨汁肋排",45));
        HotDishes.add(new Food("麻婆豆腐",20));
        HotDishes.add(new Food("牛肉炖土豆",50));
        HotDishes.add(new Food("糯米团子",25));
        HotDishes.add(new Food("清蒸大闸蟹",80));
        HotDishes.add(new Food("肉螺炒牛蛙",45));
        HotDishes.add(new Food("土豆炖牛腩",50));
        HotDishes.add(new Food("香菇油菜",18));
        HotDishes.add(new Food("响油泥鳅",45));
        HotDishes.add(new Food("猪肉炖粉条",40));

        ColdDishes = new ArrayList<Food>();
        ColdDishes.add(new Food("鹌鹑蛋拌黄瓜",23));
        ColdDishes.add(new Food("姜汁莲藕",18));
        ColdDishes.add(new Food("酱香牛肉",35));
        ColdDishes.add(new Food("口水鸡",35));
        ColdDishes.add(new Food("凉拌鸡丝",35));
        ColdDishes.add(new Food("麻酱油麦菜",18));
        ColdDishes.add(new Food("蔬菜沙拉",23));
        ColdDishes.add(new Food("水晶猪皮冻", 35));
        ColdDishes.add(new Food("酸辣风爪",35));
        ColdDishes.add(new Food("香卤猪耳朵",35));

        Seafood = new ArrayList<Food>();
        Seafood.add(new Food("葱烧海参",60));
        Seafood.add(new Food("海参汤",80));
        Seafood.add(new Food("红烧带鱼",45));
        Seafood.add(new Food("浇汁鲍鱼",50));
        Seafood.add(new Food("青椒鱿鱼",40));
        Seafood.add(new Food("清蒸鲍鱼",50));
        Seafood.add(new Food("清蒸石斑鱼",55));
        Seafood.add(new Food("烧带鱼",42));
        Seafood.add(new Food("铁板鱿鱼",38));
        Seafood.add(new Food("香辣虾",55));

        Drinks = new ArrayList<Food>();
        Drinks.add(new Food("白酒",100));
        Drinks.add(new Food("板栗牛奶露",25));
        Drinks.add(new Food("红粉雪碧",18));
        Drinks.add(new Food("红酒雪碧",18));
        Drinks.add(new Food("牛奶",15));
        Drinks.add(new Food("啤酒",8));
        Drinks.add(new Food("水果茶",14));
        Drinks.add(new Food("酥梨汁",12));
        Drinks.add(new Food("鲜榨石榴汁",18));
        Drinks.add(new Food("椰香水果奶茶",25));
    }

    public List<Food> getHotDishes(){
        return HotDishes;
    }

    public List<Food> getColdDishes(){
        return ColdDishes;
    }

    public List<Food> getSeafood(){
        return Seafood;
    }

    public List<Food> getDrinks(){
        return Drinks;
    }
}
