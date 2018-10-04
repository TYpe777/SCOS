package es.source.code.model;

/**
 * @author taoye
 * @date 2018/10/3.
 * @classname Food.java
 * @description 食物类
 */
public class Food {
    private int image; // 食物的图片
    private String name; // 食物的名称
    private float price; // 食物的价格
    private String description; // 食物的描述

    public Food(){}

    public Food(int image,String name,float price,String description){
        this.image = image;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Food(String name,float price){
        this.name = name;
        this.price = price;
        this.description = null;
        this.image = 0;
    }

    public int getImage(){
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

    public void setImage(int image){
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
}
