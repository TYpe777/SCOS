package es.source.code.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.List;

/**
 * @author taoye
 * @date 2018/10/3.
 * @classname Food.java
 * @description 食物类
 */
public class Food implements Serializable{
    private Drawable image; // 食物的图片
    private String name; // 食物的名称
    private float price; // 食物的价格
    private String description; // 食物的描述

    public Food(){}

    public Food(String name,float price){
        this.name = name;
        this.price = price;
        this.description = null;
        this.image = null;
    }

    public Food(String name,float price,String description){
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Food(Drawable image,String name,float price,String description){
        this.image = image;
        this.name = name;
        this.price = price;
        this.description = description;
    }


    public Drawable getImage(){
        return image;
    }

    public String getName(){
        return name;
    }

    public float getPrice(){
        return price;
    }

    public String getDescription(){
        return description;
    }

    public void setImage(Drawable image){
        this.image = image;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPrice(float price){
        this.price = price;
    }

    public void setDescription(String description){
        this.description = description;
    }

    // 判断食物是否在参数传入的订单中
    public boolean inOrderList(List<OrderItem> orderList){
        if(orderList != null)
            for(int i=0;i<orderList.size();i++)
                if(orderList.get(i).getFood().getName().equals(this.name))
                    return true;
        return false;
    }
}
