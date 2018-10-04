package es.source.code.model;

/**
 * @description 订单的一条记录
 * Created by taoye on 2018/10/4.
 */
public class OrderItem {

    private Food food;
    private boolean isOrdered;

    public OrderItem(){}

    public OrderItem(Food food){
        this.food = food;
        isOrdered = false;
    }

    public OrderItem(Food food,boolean isOrdered){
        this.food = food;
        this.isOrdered = isOrdered;
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

}
