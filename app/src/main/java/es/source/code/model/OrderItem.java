package es.source.code.model;

import java.io.Serializable;

/**
 * @description 订单的一条记录
 * Created by taoye on 2018/10/4.
 */
public class OrderItem implements Serializable{

    private Food food;
    private boolean isOrdered;
    private int count;

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
