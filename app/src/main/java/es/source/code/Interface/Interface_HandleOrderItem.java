package es.source.code.Interface;

import es.source.code.model.Food;

/**
 * @author taoye
 * @date 2018/10/4.
 * @classname Interface_HandleOrderItem.java
 * @description 接口。实现点击“点菜”将菜品加入订单和点击“退点”将菜品从订单中删除。订单保存在activity(FoodView)或(FoodOrderView)中
 */
public interface Interface_HandleOrderItem {
    void addOrderItem(Food food);

    void removeOrderItem(Food food);
}
