package es.source.code.model;

import java.io.Serializable;

/**
 * @author taoye
 * @date 2018/10/4
 * @classname OrderItem.java
 * @description 订单列表的一条记录
 */
public class OrderItem implements Serializable{
    private Food food; // 订单项对应的菜品
    private boolean isOrdered; // 是否下单
    private int count; // 已点该种菜品数量

    public OrderItem(){}

    public OrderItem(Food food){
        this.food = food;
        isOrdered = false;
        count = 1;
    }

    public OrderItem(Food food,int count){
        this.food = food;
        this.count = count;
    }

    public OrderItem(Food food,boolean isOrdered,int count){
        this.food = food;
        this.isOrdered = isOrdered;
        this.count = count;
    }

    public Food getFood(){
        return food;
    }

    public boolean isOrdered(){
        return isOrdered;
    }

    public void order(){
        isOrdered = true;
    }

    public int getCount(){
        return count;
    }

    public void addCount(int num){
        count += num;
    }

    public void subCount(int num){
        if(count >= num){
            count -= num;
        }else {
            count = 0;
        }
    }

}
